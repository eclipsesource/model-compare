/********************************************************************************
 * Copyright (c) 2021 EclipseSource and others.
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
 ********************************************************************************/
import { CompositeTreeNode, DecoratedTreeNode, ExpandableTreeNode, SelectableTreeNode, TreeNode } from '@theia/core/lib/browser/tree';

export namespace TreeEditor {
    export type RootNode = CompositeTreeNode;

    export namespace RootNode {
        export function is(node: TreeNode | undefined): node is RootNode {
            return !!node;
        }
    }

    export interface Node extends CompositeTreeNode, ExpandableTreeNode, SelectableTreeNode, DecoratedTreeNode {
        editorId: string;
        // TODO type
        type: string;
        // TODO needed?
        data?: Record<string, unknown>;
    }

    export interface TreeData {
        error: boolean;
        data: any[];
    }
    export namespace Node {
        export function is(node: object | undefined): node is Node {
            return TreeNode.is(node) && 'type' in node && !!node['type'];
        }

        export function hasType(node: TreeNode | undefined, type: string): node is Node {
            return is(node) && node.type === type;
        }
    }

    /**
     * Descriptor stating creatable child types for one property in the corresponding parent data.
     */
    export interface ChildrenDescriptor {
        property: string;
        children: string[];
    }

    /**
     * Encapsulates logic to create the tree nodes from the tree's input data.
     */
    export const NodeFactory = Symbol('NodeFactory');
    export interface NodeFactory {
        /**
         * Recursively creates the tree's nodes from the given data.
         *
         * @param treeData The tree's data
         * @returns The tree's shown root nodes (not to confuse with the invisible RootNode)
         */
        mapDataToNodes(treeData: TreeData): Node[];

        /**
         * Creates the corresponding TreeNode for the given data.
         *
         * @param data The instance data to map to a tree node
         * @param parent The created node's parent node
         * @param property The JSON property which this node's data is contained in
         * @param indexOrKey If the data is inserted in an array property, this is the index it is inserted at.
         *                   If the data is inserted into an object, this is the key the data is associated with.
         */
        mapData(data: any, parent?: Node, property?: string, indexOrKey?: number | string): Node;
    }

    /**
     * Information to get the icon of an add command from an editor's label provider contribution.
     */
    export interface CommandIconInfo {
        _id: 'theia-tree-editor-command-icon-info';
        editorId: string;
        type: string;
    }

    export namespace CommandIconInfo {
        export function is(info: object | undefined): info is CommandIconInfo {
            return !!info && '_id' in info && 'theia-tree-editor-command-icon-info' === info['_id'];
        }
    }
}
