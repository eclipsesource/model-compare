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
/* eslint-disable no-useless-escape */
import { ILogger } from '@theia/core';
import {
    ApplicationShell,
    CompositeTreeNode,
    OpenViewArguments,
    Saveable,
    StatefulWidget,
    Title,
    TreeNode,
    Widget,
    WidgetManager
} from '@theia/core/lib/browser';
import URI from '@theia/core/lib/common/uri';
import { WorkspaceService } from '@theia/workspace/lib/browser/workspace-service';
import { inject, injectable } from 'inversify';
import { ComparisonBackendService } from '../../common/protocol';
import { JSONCompareResponse } from '../../node/json-compare-response';
import { ComparisonExtensionConfiguration } from '../comparison-extension-configuration';
import { GraphicalComparisonOpener } from '../graphical/graphical-comparison-opener';
import { GraphicalComparisonWidget, GraphicalComparisonWidgetOptions } from '../graphical/graphical-comparison-widget';
import { TreeEditor } from '../tree-widget/interfaces';
import { AddCommandProperty, MasterTreeWidget } from '../tree-widget/master-tree-widget';
import { BaseTreeEditorWidget } from '../tree-widget/tree-editor-widget';

export const ComparisonTreeEditorWidgetOptions = Symbol('ComparisonTreeEditorWidgetOptions');
export interface ComparisonTreeEditorWidgetOptions {
    source: string;
    target: string;
    base: string;
    merges: Array<MergeInstruction>;
    conflicts: Array<string>;
}

export interface MergeInstruction {
    type: string;
    target: string;
    direction: string;
}

@injectable()
export class ComparisonTreeEditorWidget extends BaseTreeEditorWidget implements Saveable, StatefulWidget {
    protected options: ComparisonTreeEditorWidgetOptions;
    protected comparisonResponse: JSONCompareResponse;

    constructor(
        @inject(ComparisonBackendService) readonly comparisonBackendService: ComparisonBackendService,
        @inject(ComparisonExtensionConfiguration) readonly config: ComparisonExtensionConfiguration,
        @inject(GraphicalComparisonOpener) protected readonly graphicalOpener: GraphicalComparisonOpener,
        @inject(WidgetManager) protected readonly widgetManager: WidgetManager,
        @inject(ApplicationShell) protected readonly shell: ApplicationShell,
        @inject(MasterTreeWidget)
        readonly myTreeWidgetOverview: MasterTreeWidget,
        @inject(MasterTreeWidget)
        readonly myTreeWidgetModel1: MasterTreeWidget,
        @inject(MasterTreeWidget)
        readonly myTreeWidgetModel2: MasterTreeWidget,
        @inject(WorkspaceService)
        readonly workspaceService: WorkspaceService,
        @inject(ILogger) readonly logger: ILogger
    ) {
        super(myTreeWidgetOverview, myTreeWidgetModel1, myTreeWidgetModel2, workspaceService, logger, ComparisonTreeEditorWidget.WIDGET_ID);

        window.onbeforeunload = () => this.dispose();
    }

    public setContent(options: ComparisonTreeEditorWidgetOptions): void {
        this.options = options;
        this.comparisonBackendService.getNewComparison(options.source, options.target, this.mergesToString(), options.base).then(r => {
            const response: JSONCompareResponse = JSON.parse(r);
            this.comparisonResponse = response;

            if (response.error.trim() !== '') {
                this.showInformation(response.error, 'red', 'fas fa-exclamation-triangle red');
                return;
            }
            this.myTreeWidgetModel1.treeTitle = 'Source: ' + this.options.source.replace(/^.*[\\\/]/, '');
            this.myTreeWidgetModel2.treeTitle = 'Target: ' + this.options.target.replace(/^.*[\\\/]/, '');

            this.myTreeWidgetOverview.treeTitle = 'Differences overview:';
            this.myTreeWidgetOverview.setData({ error: false, data: response.changesTree }).then(() => {
                this.myTreeWidgetOverview.selectFirst();
            });

            this.myTreeWidgetModel1.setData({ error: false, data: response.sourceTree }).then(() => {
                this.myTreeWidgetModel1.selectFirst();
            });
            this.myTreeWidgetModel2.setData({ error: false, data: response.targetTree }).then(() => {
                this.myTreeWidgetModel2.selectFirst();
            });

            this.myTreeWidgetOverview.model.refresh();

            this.actionWidget.setGraphicalComparisonVisibility(this.config.supportGraphicalComparison());
        });
    }

    protected treeSelectionChanged(treeWidget: MasterTreeWidget, selectedNodes: readonly Readonly<TreeEditor.Node>[]): void {
        if (treeWidget === this.treeWidgetOverview) {
            if (selectedNodes.length !== 0) {
                this.selectedNode = selectedNodes[0];
                this.navigateToSelection(this.treeWidgetModel1, this.selectedNode.jsonforms.data.uuid);
                this.navigateToSelection(this.treeWidgetModel2, this.selectedNode.jsonforms.data.uuid);
                this.actionWidget.updateActivation(this.selectedNode.jsonforms.data.type);
            }
        }
        this.update();
    }

    private navigateToSelection(treeWidget: MasterTreeWidget, uuid: string): void {
        if (!this.selectedNode) {
            return;
        }

        const rootNode = treeWidget.model.root as TreeEditor.RootNode;
        const nodes: Array<TreeNode> = [...rootNode.children];
        treeWidget.model.collapseAll(rootNode).then(() => {
            while (nodes.length !== 0) {
                const node: TreeNode = nodes.pop();
                if (TreeEditor.Node.is(node)) {
                    if (node.jsonforms.data.uuid === uuid) {
                        let parent: CompositeTreeNode = node.parent;
                        while (parent && parent !== rootNode) {
                            if (TreeEditor.Node.is(parent)) {
                                treeWidget.model.expandNode(parent);
                            }
                            parent = parent.parent;
                        }
                        treeWidget.model.selectNode(node);
                        return;
                    }
                    node.children.forEach(element => {
                        nodes.push(element);
                    });
                }
            }

            // try if there are connectedd uuids
            const relatedUuid = this.comparisonResponse.uuidConnection[uuid];
            if (relatedUuid) {
                this.navigateToSelection(treeWidget, relatedUuid);
            }
        });
    }

    protected deleteNode(node: TreeEditor.Node): void {
        throw new Error('Method not implemented.');
    }
    protected addNode({ node, type, property }: AddCommandProperty): void {
        throw new Error('Method not implemented.');
    }
    protected handleFormUpdate(data: any, node: TreeEditor.Node): void {
        throw new Error('Method not implemented.');
    }

    public save(): void {
        if (this.options) {
            this.comparisonBackendService
                .merge(this.options.source, this.options.target, this.options.base, this.mergesToString(), '')
                .then(response => {
                    this.options.merges = [];
                    this.setDirty(false);
                    this.setContent(this.options);
                });
        }
    }

    storeState(): object {
        return this.options;
    }

    restoreState(oldState: object): void {
        this.setContent(oldState as ComparisonTreeEditorWidgetOptions);
    }

    show(): void {
        super.show();
    }

    private showInformation(text: string, color: string, icon = ''): void {
        this.instanceData = {
            eClass: 'information',
            name: text,
            icon: icon,
            color: color,
            uuid: '',
            children: []
        };

        this.myTreeWidgetOverview.setData({ error: false, data: this.instanceData }).then(() => this.myTreeWidgetOverview.selectFirst());
    }

    protected configureTitle(title: Title<Widget>): void {
        title.label = 'Comparison Tree View';
        title.caption = ComparisonTreeEditorWidget.WIDGET_LABEL;
        title.closable = true;
        title.iconClass = `${this.config.getDiffViewIcon()} file-icon`;
    }

    private addMerge(type: string, target: string, direction: string): void {
        const merge: MergeInstruction = { type: type, target: target, direction: direction };
        this.options.merges.push(merge);
    }

    private mergesToString(): string {
        const merges = [];
        this.options.merges.forEach(merge => merges.push(merge.type + ';' + merge.target + ';' + merge.direction));
        const merg = merges.join();
        return merg;
    }

    public merge(toSource: boolean, all: boolean, conflict: boolean): void {
        this.setDirty(true);
        const type = conflict ? 'conflict' : 'diff';
        // Map because EMF Compare uses left & right
        const direction = toSource ? 'left' : 'right';
        let target;
        if (all) {
            target = 'all';
        } else {
            if (this.selectedNode) {
                target = this.selectedNode.jsonforms.data.uuid;
            }
        }

        this.addMerge(type, target, direction);
        this.setContent(this.options);
    }

    public undoMerge(): void {
        this.options.merges.pop();
        if (this.options.merges.length === 0) {
            this.setDirty(false);
        }
        this.setContent(this.options);
    }

    public showGraphicalComparison(): void {
        if (this.dirty) {
            if (confirm('There are unsaved changes, do you want to save and continue?')) {
                this.save();
            } else {
                return;
            }
        }

        this.graphicalOpener.getHighlights(this.options.source, this.options.target).then(async (highlights: any) => {
            const sourceWidget = await this.graphicalOpener.getLeftDiagram(new URI(this.options.source), highlights);
            const targetWidget = await this.graphicalOpener.getRightDiagram(new URI(this.options.target), highlights);
            const options: GraphicalComparisonWidgetOptions = {
                left: sourceWidget,
                right: targetWidget
            };
            this.widgetManager.getOrCreateWidget(GraphicalComparisonWidget.WIDGET_ID).then(widget => {
                (widget as GraphicalComparisonWidget).setContent(options);
                this.openView(widget, { activate: true });
            });
        });
    }

    async openView(widget: Widget, args: Partial<OpenViewArguments> = {}): Promise<Widget> {
        // TODO: We might be able to reuse some function so we do not need to implement it here
        const shell = this.shell;
        const tabBar = shell.getTabBarFor(widget);
        const area = shell.getAreaFor(widget);
        if (!tabBar) {
            const widgetArgs: OpenViewArguments = {
                reveal: true,
                ...args
            };
            await shell.addWidget(widget, widgetArgs);
        } else if (args.toggle && area && shell.isExpanded(area) && tabBar.currentTitle === widget.title) {
            switch (area) {
                case 'left':
                case 'right':
                    await shell.collapsePanel(area);
                    break;
                case 'bottom':
                    if (shell.bottomAreaTabBars.length === 1) {
                        await shell.collapsePanel('bottom');
                    }
                    break;
                default:
                    await this.shell.closeWidget(GraphicalComparisonWidget.WIDGET_ID);
            }
            return widget;
        }
        if (widget.isAttached && args.activate) {
            await shell.activateWidget(GraphicalComparisonWidget.WIDGET_ID);
        } else if (widget.isAttached && args.reveal) {
            await shell.revealWidget(GraphicalComparisonWidget.WIDGET_ID);
        }
        return widget;
    }
}

export namespace ComparisonTreeEditorWidget {
    export const WIDGET_ID = 'json-forms-comparison-tree-editor';
    export const EDITOR_ID = 'com.eclipsesource.comparison.editor';
}
