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
import { Emitter } from '@theia/core';
import { ExpandableTreeNode, TreeModel } from '@theia/core/lib/browser';
import { ContextMenuRenderer } from '@theia/core/lib/browser/context-menu-renderer';
import { CompositeTreeNode, TreeNode } from '@theia/core/lib/browser/tree/tree';
import { NodeProps, TreeProps } from '@theia/core/lib/browser/tree/tree-widget';
import { inject, injectable, postConstruct } from 'inversify';
import * as React from 'react';
import { TreeEditor } from './interfaces';
import { TreeWidgetWithTitle } from './tree-widget-with-title';

@injectable()
export class MasterTreeWidget extends TreeWidgetWithTitle {
    protected onTreeWidgetSelectionEmitter = new Emitter<readonly Readonly<TreeEditor.Node>[]>();
    protected data: TreeEditor.TreeData;

    constructor(
        @inject(TreeProps) readonly props: TreeProps,
        @inject(TreeModel) readonly model: TreeModel,
        @inject(ContextMenuRenderer) readonly contextMenuRenderer: ContextMenuRenderer,
        @inject(TreeEditor.NodeFactory) protected readonly nodeFactory: TreeEditor.NodeFactory
    ) {
        super(props, model, contextMenuRenderer);
        this.id = MasterTreeWidget.WIDGET_ID;
        this.title.label = MasterTreeWidget.WIDGET_LABEL;
        this.title.caption = MasterTreeWidget.WIDGET_LABEL;

        model.root = {
            id: MasterTreeWidget.WIDGET_ID,
            name: MasterTreeWidget.WIDGET_LABEL,
            parent: undefined,
            visible: false,
            children: []
        } as TreeEditor.RootNode;
    }

    @postConstruct()
    protected init(): void {
        super.init();

        this.toDispose.push(this.onTreeWidgetSelectionEmitter);
        this.toDispose.push(
            this.model.onSelectionChanged(e => {
                this.onTreeWidgetSelectionEmitter.fire(e as readonly Readonly<TreeEditor.Node>[]);
            })
        );
    }

    /** Overrides method in TreeWidget */
    protected handleClickEvent(node: TreeNode | undefined, event: React.MouseEvent<HTMLElement>): void {
        const x = event.target as HTMLElement;
        if (x.classList.contains('node-button')) {
            // Don't do anything because the event is handled in the button's handler
            return;
        }
        super.handleClickEvent(node, event);
    }

    /*
     * Overrides TreeWidget.renderTailDecorations
     * Add a add child and a remove button.
     */
    protected renderTailDecorations(node: TreeNode, props: NodeProps): React.ReactNode {
        const deco = super.renderTailDecorations(node, props);
        if (!TreeEditor.Node.is(node)) {
            return deco;
        }

        return <React.Fragment>{deco}</React.Fragment>;
    }

    public async setData(data: TreeEditor.TreeData): Promise<void> {
        this.data = data;
        await this.refreshModelChildren();
    }

    public selectFirst(): void {
        if (
            this.model.root &&
            TreeEditor.RootNode.is(this.model.root) &&
            this.model.root.children.length > 0 &&
            TreeEditor.Node.is(this.model.root.children[0])
        ) {
            this.model.selectNode(this.model.root.children[0] as TreeEditor.Node);
            this.model.refresh();
        }
    }

    public select(paths: string[]): void {
        if (paths.length === 0) {
            return;
        }
        const rootNode = this.model.root as TreeEditor.Node;
        const toSelect = paths.reduceRight(
            (node, path) => node.children.find(value => this.labelProvider.getName(value) === path),
            rootNode
        ) as TreeEditor.Node;
        this.model.selectNode(toSelect);
        this.model.refresh();
    }

    get onSelectionChange(): import('@theia/core').Event<readonly Readonly<TreeEditor.Node>[]> {
        return this.onTreeWidgetSelectionEmitter.event;
    }

    protected async refreshModelChildren(): Promise<void> {
        if (this.model.root && TreeEditor.RootNode.is(this.model.root)) {
            const newTree = !this.data || this.data.error ? [] : await this.nodeFactory.mapDataToNodes(this.data);
            this.model.root.children = newTree;
            this.model.refresh();
        }
    }

    protected isExpandable(node: CompositeTreeNode): node is ExpandableTreeNode {
        return node.children.length > 0;
    }

    protected renderIcon(node: TreeNode): React.ReactNode {
        return (
            <div className='tree-icon-container'>
                <div className={this.labelProvider.getIcon(node)} />
            </div>
        );
    }
}

// eslint-disable-next-line no-redeclare
export namespace MasterTreeWidget {
    export const WIDGET_ID = 'theia-tree-editor-tree';
    export const WIDGET_LABEL = 'Theia Tree Editor - Tree';
}
