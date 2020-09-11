/********************************************************************************
 * Copyright (C) 2018 TypeFox and others.
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

import { CommandRegistry, Command, MenuModelRegistry, SelectionService } from '@theia/core/lib/common';
import { AbstractViewContribution } from '@theia/core/lib/browser';
import { injectable, inject } from 'inversify';
import { NavigatorContextMenu, FileNavigatorContribution } from '@theia/navigator/lib/browser/navigator-contribution';
import { UriCommandHandler, UriAwareCommandHandler } from '@theia/core/lib/common/uri-command-handler';
import URI from '@theia/core/lib/common/uri';
import { WorkspaceService } from '@theia/workspace/lib/browser';
import { TabBarToolbarContribution, TabBarToolbarRegistry } from '@theia/core/lib/browser/shell/tab-bar-toolbar';
import { ComparisonTreeEditorWidget, ComparisonTreeEditorWidgetOptions } from './tree-editor/ComparisonTreeEditorWidget';
import { ComparisonOrderDialog } from './comparison-order-dialog';
import { TreeComparisonConfiguration } from './tree-comparison-configuration';
import { GraphicalComparisonOpener } from './graphical-comparison-opener';

export namespace ComparisonCommands {
    export const FILE_COMPARE_TREE: Command = {
        id: 'file.compare.tree',
        category: "Comparison",
        label: 'Compare with tree view'
    };
    export const FILE_COMPARE_GRAPHICALLY: Command = {
        id: 'file.compare.graphical',
        category: "Comparison",
        label: 'Compare graphically'
    };
}

export namespace ScmNavigatorMoreToolbarGroups {
    export const SCM = '3_navigator_scm';
}

@injectable()
export class ComparisonContribution extends AbstractViewContribution<ComparisonTreeEditorWidget> implements TabBarToolbarContribution {

    @inject(CommandRegistry)
    protected readonly commandRegistry: CommandRegistry;

    @inject(FileNavigatorContribution)
    protected readonly fileNavigatorContribution: FileNavigatorContribution;

    @inject(WorkspaceService)
    protected readonly workspaceService: WorkspaceService;

    constructor(
        @inject(SelectionService) protected readonly selectionService: SelectionService,
        @inject(GraphicalComparisonOpener) protected readonly graphicalOpener: GraphicalComparisonOpener,
        @inject(TreeComparisonConfiguration) protected readonly config: TreeComparisonConfiguration) {
            
        super({
            widgetId: ComparisonTreeEditorWidget.WIDGET_ID,
            widgetName: ComparisonTreeEditorWidget.WIDGET_LABEL,
            defaultWidgetOptions: {
                area: 'main'
            }
        });
    }

    registerMenus(menus: MenuModelRegistry): void {
        menus.registerMenuAction(NavigatorContextMenu.COMPARE, {
            commandId: ComparisonCommands.FILE_COMPARE_TREE.id
        });
        menus.registerMenuAction(NavigatorContextMenu.COMPARE, {
            commandId: ComparisonCommands.FILE_COMPARE_GRAPHICALLY.id
        });
    }

    registerCommands(commands: CommandRegistry): void {
        commands.registerCommand(ComparisonCommands.FILE_COMPARE_TREE, this.newMultiUriAwareCommandHandler({
            isVisible: uris => this.showTreeCommand(uris),
            isEnabled: uris => this.showTreeCommand(uris),
            execute: async uris => {
                const [left, right, origin] = uris;
               
                const dialog: ComparisonOrderDialog = new ComparisonOrderDialog(String(left), String(right), String(origin));
                dialog.open().then(() => {
                    const options: ComparisonTreeEditorWidgetOptions = {
                        left: dialog.getLeft(),
                        right: dialog.getRight(),
                        origin: dialog.getOrigin()
                    }
                    this.showTreeWidget(options);
                });
            }
        }));
        commands.registerCommand(ComparisonCommands.FILE_COMPARE_GRAPHICALLY, this.newMultiUriAwareCommandHandler({
            isVisible: uris => this.showGraphicallyCommand(uris),
            isEnabled: uris => this.showGraphicallyCommand(uris),
            execute: async uris => {
                const [left, right] = uris;
                const dialog: ComparisonOrderDialog = new ComparisonOrderDialog(String(left), String(right));
                dialog.open().then(() => {
                    this.graphicalOpener.showWidgets(this.widgetManager, new URI(dialog.getLeft()), new URI(dialog.getRight()));
                });
            }
        }));
    }

    showTreeCommand(uris: URI[]): boolean{
        if (uris.length < 2 || uris.length > 3) return false;
        for(let i=0; i<uris.length; i++) {
            if (!this.config.canHandle(uris[i])) {
                return false;
            }
        }
        return true;
    }

    showGraphicallyCommand(uris: URI[]): boolean {
        if (this.config.supportGraphicalComparison()) {
            if (uris.length !== 2) return false;
            return this.config.canHandle(uris[0]) && this.config.canHandle(uris[1]);
        }
        return false;
    }

    registerToolbarItems(registry: TabBarToolbarRegistry): void {
        this.fileNavigatorContribution.registerMoreToolbarItem({
            id: ComparisonCommands.FILE_COMPARE_TREE.id,
            command: ComparisonCommands.FILE_COMPARE_TREE.id,
            tooltip: ComparisonCommands.FILE_COMPARE_TREE.label,
            group: ScmNavigatorMoreToolbarGroups.SCM,
        });
        this.fileNavigatorContribution.registerMoreToolbarItem({
            id: ComparisonCommands.FILE_COMPARE_GRAPHICALLY.id,
            command: ComparisonCommands.FILE_COMPARE_GRAPHICALLY.id,
            tooltip: ComparisonCommands.FILE_COMPARE_GRAPHICALLY.label,
            group: ScmNavigatorMoreToolbarGroups.SCM,
        });
    }

    async showTreeWidget(options: ComparisonTreeEditorWidgetOptions): Promise<ComparisonTreeEditorWidget> {
        const widget = await this.widget;
        widget.setContent(options);
        return this.openView({
            activate: true
        });
    }
    
    protected newMultiUriAwareCommandHandler(handler: UriCommandHandler<URI[]>): UriAwareCommandHandler<URI[]> {
        return new UriAwareCommandHandler(this.selectionService, handler, { multi: true });
    }
}
