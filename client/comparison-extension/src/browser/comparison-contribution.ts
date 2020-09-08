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

import { CommandRegistry, Command, MenuModelRegistry, SelectionService, MessageService } from '@theia/core/lib/common';
import { FrontendApplication, AbstractViewContribution } from '@theia/core/lib/browser';
import { WidgetManager } from '@theia/core/lib/browser/widget-manager';
import { injectable, inject } from 'inversify';
import { OpenerService } from '@theia/core/lib/browser'; //open
import { NavigatorContextMenu, FileNavigatorContribution } from '@theia/navigator/lib/browser/navigator-contribution';
import { UriCommandHandler, UriAwareCommandHandler } from '@theia/core/lib/common/uri-command-handler';
//import { DiffUris } from '@theia/core/lib/browser/diff-uris';
import URI from '@theia/core/lib/common/uri';
//import { WorkspaceRootUriAwareCommandHandler } from '@theia/workspace/lib/browser/workspace-commands';
import { WorkspaceService } from '@theia/workspace/lib/browser';
import { TabBarToolbarContribution, TabBarToolbarRegistry } from '@theia/core/lib/browser/shell/tab-bar-toolbar';
import { ComparisonTreeEditorWidget, ComparisonTreeEditorWidgetOptions } from './tree-editor/ComparisonTreeEditorWidget';

export namespace ComparisonCommands {
    export const FILE_COMPARE_TREE: Command = {
        id: 'file.compare.tree',
        category: "Comparison",
        label: 'Compare with tree view'
    };
}

export namespace ScmNavigatorMoreToolbarGroups {
    export const SCM = '3_navigator_scm';
}

@injectable()
export class ComparisonTreeContribution extends AbstractViewContribution<ComparisonTreeEditorWidget> implements TabBarToolbarContribution {

    @inject(CommandRegistry)
    protected readonly commandRegistry: CommandRegistry;

    @inject(FileNavigatorContribution)
    protected readonly fileNavigatorContribution: FileNavigatorContribution;

    @inject(WorkspaceService)
    protected readonly workspaceService: WorkspaceService;

    constructor(
        @inject(SelectionService) protected readonly selectionService: SelectionService,
        @inject(WidgetManager) protected readonly widgetManager: WidgetManager,
        @inject(FrontendApplication) protected readonly app: FrontendApplication,
        //@inject(GitQuickOpenService) protected readonly quickOpenService: GitQuickOpenService,
        //@inject(FileService) protected readonly fileService: FileService,
        @inject(OpenerService) protected openerService: OpenerService,
        @inject(MessageService) protected readonly notifications: MessageService
        //@inject(ScmService) protected readonly scmService: ScmService
    ) {
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
    }

    registerCommands(commands: CommandRegistry): void {
        commands.registerCommand(ComparisonCommands.FILE_COMPARE_TREE, this.newMultiUriAwareCommandHandler({
            //isVisible: uri => !!this.findGitRepository(uri),
            //isEnabled: uri => !!this.findGitRepository(uri),
            execute: async uris => {
                const [file1, file2, file3] = uris;
                // TODO: add dialog https://github.com/eclipse-theia/theia/issues/2959
                const options: ComparisonTreeEditorWidgetOptions = {
                    left: file1,
                    right: file2,
                    origin: file3
                }
                this.showWidget(options);
            }
            /*
            execute: async fileUri => {
                console.log(fileUri);
                
                await this.quickOpenService.chooseTagsAndBranches(
                    async (fromRevision, toRevision) => {
                        const uri = fileUri.toString();
                        const fileStat = await this.fileService.resolve(fileUri);
                        const options: Git.Options.Diff = {
                            uri,
                            range: {
                                fromRevision
                            }
                        };
                        if (fileStat.isDirectory) {
                            this.showWidget(options);
                        } else {
                            const fromURI = fileUri.withScheme(GIT_RESOURCE_SCHEME).withQuery(fromRevision);
                            const toURI = fileUri;
                            const diffUri = DiffUris.encode(fromURI, toURI);
                            if (diffUri) {
                                open(this.openerService, diffUri).catch(e => {
                                    this.notifications.error(e.message);
                                });
                            }
                        }
                    }, this.findGitRepository(fileUri));
                    
            }
            */
        }));
    }

    registerToolbarItems(registry: TabBarToolbarRegistry): void {
        this.fileNavigatorContribution.registerMoreToolbarItem({
            id: ComparisonCommands.FILE_COMPARE_TREE.id,
            command: ComparisonCommands.FILE_COMPARE_TREE.id,
            tooltip: ComparisonCommands.FILE_COMPARE_TREE.label,
            group: ScmNavigatorMoreToolbarGroups.SCM,
        });
    }

    /*
    protected findGitRepository(uri: URI): Repository | undefined {
        const repo = this.scmService.findRepository(uri);
        if (repo && repo.provider.id === 'git') {
            return { localUri: repo.provider.rootUri };
        }
        return undefined;
    }
    */

    async showWidget(options: ComparisonTreeEditorWidgetOptions): Promise<ComparisonTreeEditorWidget> {
        const widget = await this.widget;
        widget.setContent(options);
        return this.openView({
            activate: true
        });
    }
    

    protected newMultiUriAwareCommandHandler(handler: UriCommandHandler<URI[]>): UriAwareCommandHandler<URI[]> {
        return new UriAwareCommandHandler(this.selectionService, handler, { multi: true });
    }

    /*
    protected newWorkspaceRootUriAwareCommandHandler(handler: UriCommandHandler<URI>): WorkspaceRootUriAwareCommandHandler {
        return new WorkspaceRootUriAwareCommandHandler(this.workspaceService, this.selectionService, handler);
    }
    */
}
