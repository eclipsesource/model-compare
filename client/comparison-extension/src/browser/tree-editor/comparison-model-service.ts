/*!
 * Copyright (C) 2019 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

import { injectable, inject } from 'inversify';
import { JsonSchema, UISchemaElement } from '@jsonforms/core';
import {
    CompositeTreeNode,
    DecoratedTreeNode,
    ExpandableTreeNode,
    SelectableTreeNode,
    TreeNode
} from '@theia/core/lib/browser/tree';
import { TreeEditor } from '../tree-widget/interfaces';
import { ILogger } from '@theia/core';
import { ComparisonModel } from './comparison-model';
import { comparisonSchema } from './comparison-schemas';


export type RootNode = CompositeTreeNode;

export namespace RootNode {
    export function is(node: TreeNode | undefined): node is RootNode {
        return !!node;
    }
}

export interface Node
    extends CompositeTreeNode,
    ExpandableTreeNode,
    SelectableTreeNode,
    DecoratedTreeNode {
    editorId: string;
    children: TreeNode[];
    name: string;
    jsonforms: {
        type: string;
        property: string;
        index?: string;
        data: any;
    };
}

export interface TreeData {
    error: boolean;
    data?: any;
}

export namespace Node {
    export function is(node: object | undefined): node is Node {
        return TreeNode.is(node) && 'jsonforms' in node && !!node['jsonforms'];
    }

    export function hasType(node: TreeNode | undefined, type: string): node is Node {
        return is(node) && node.jsonforms.type === type;
    }
}

/**
 * Descriptor stating creatable child types for one property in the corresponding parent data.
 */
export interface ChildrenDescriptor {
    property: string;
    children: string[];
}

export const ModelService = Symbol('JsonFormsModelService');
export interface ModelService {
    /**
     * Returns the data associated with the given node.
     * This is the data which is usually rendered by the editor's detail view when its node is selected.
     *
     * @param node The tree node
     * @returns The data associated with the node
     */
    getDataForNode(node: Node): any;

    /**
     * Returns the JsonSchema describing how the node's data should be rendered.
     * Alternatively, return undefined to generate a schema in the detail view.
     * @param node The tree node
     * @returns the JsonSchema describing the node's data or undefined to generate a schema.
     */
    getSchemaForNode(node: Node): JsonSchema | undefined;

    /**
     * Returns the ui schema describing how the node's data should be rendered.
     * Might return undefined to automatically generate a ui schema in the detail view based on the node's json schema.
     *
     * @param node The tree node
     * @returns The ui schema for the node's data or undefined to generate a ui schema.
     */
    getUiSchemaForNode(node: Node): UISchemaElement | undefined;

    /**
     * This mapping describes which child nodes can be created for a given type.
     * Thereby, the map's keys are the types and the values the children descriptors.
     * There is one children descriptor for every property that can contain children.
     */
    getChildrenMapping(): Map<string, ChildrenDescriptor[]>;

    /**
     * Returns the name of the given type. This could also be the type itself if it is already properly readable by a user.
     * @param type The type to calculate the name for
     */
    getNameForType(type: string): string;
}

@injectable()
export class ComparisonModelService implements TreeEditor.ModelService {

    constructor(@inject(ILogger) private readonly logger: ILogger) { }

    getDataForNode(node: TreeEditor.Node) {
        return node.jsonforms.data;
    }
    getSchemaForNode(node: TreeEditor.Node): import("@jsonforms/core").JsonSchema4 | import("@jsonforms/core").JsonSchema7 | undefined {
        return {
            definitions: comparisonSchema.definitions,
            ...this.getSubSchemaForNode(node)
        };
    }

    private getSubSchemaForNode(node: TreeEditor.Node) {
        const schema = this.getSchemaForType(node.jsonforms.type);
        if (schema) {
            return schema;
        }
        // there is no type, try to guess
        if (node.jsonforms.data.nodes) {
            return comparisonSchema.definitions.node;
        }
        return undefined;
    }

    private getSchemaForType(type: string) {
        if (!type) {
            return undefined;
        }
        const schema = Object.entries(comparisonSchema.definitions)
            .map(entry => entry[1])
            .find(
                definition =>
                    definition.properties && definition.properties.eClass.const === type
            );
        if (!schema) {
            this.logger.warn("Can't find definition schema for type " + type);
        }
        return schema;
    }
    
    getUiSchemaForNode(node: TreeEditor.Node): UISchemaElement | undefined {
        /*
        const schema = this.getUiSchemaForType(node.jsonforms.type);
        if (schema) {
            return schema;
        }
        // there is no type, try to guess
        if (node.jsonforms.data.nodes) {
            return workflowView;
        }
        */
        return undefined;
    }

    /*
    private getUiSchemaForType(type: string) {
        if (!type) {
            return undefined;
        }
        switch (type) {
            case CoffeeModel.Type.Machine:
                return machineView;
            case CoffeeModel.Type.ControlUnit:
                return controlUnitView;
            case CoffeeModel.Type.BrewingUnit:
                return brewingView;
            case CoffeeModel.Type.AutomaticTask:
                return automaticTaskView;
            case CoffeeModel.Type.ManualTask:
                return manualTaskView;
            case CoffeeModel.Type.DipTray:
                return dipTrayView;
            case CoffeeModel.Type.WaterTank:
                return waterTankView;
            case CoffeeModel.Type.Flow:
                return flowView;
            case CoffeeModel.Type.WeightedFlow:
                return weightedFlowView;
            case CoffeeModel.Type.Decision:
                return decisionView;
            case CoffeeModel.Type.Merge:
                return mergeView;
            default:
                this.logger.warn("Can't find registered ui schema for type " + type);
                return undefined;
        }
    }
    */

    getChildrenMapping(): Map<string, TreeEditor.ChildrenDescriptor[]> {
        return ComparisonModel.childrenMapping;
    }
    getNameForType(type: string): string {
        return ComparisonModel.Type.name(type);
    }
}
