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
import { CommandRegistry, MenuModelRegistry } from '@theia/core';
import { ApplicationShell, NavigatableWidgetOptions, OpenerService, WidgetOpenerOptions } from '@theia/core/lib/browser';
import URI from '@theia/core/lib/common/uri';
import { inject, injectable } from 'inversify';
import { TreeEditor } from './tree-widget/interfaces';
import { BaseTreeEditorContribution } from './tree-widget/tree-editor-contribution';
import { BaseTreeEditorWidget } from './tree-widget/tree-editor-widget';
import { TreeContextMenu } from './tree-widget/master-tree-widget';
import { ComparisonModelService } from './tree-editor/comparison-model-service';
import { ComparisonTreeEditorWidget } from './tree-editor/ComparisonTreeEditorWidget';
import { ComparisonTreeLabelProvider } from './tree-editor/ComparisonLabelProviderContribution';
import { ComparisonTreeCommands, OpenComparisonDiagramCommandHandler } from './tree-editor/comparison-tree-container';


@injectable()
export class ComparisonTreeEditorContribution extends BaseTreeEditorContribution {
  @inject(ApplicationShell) protected shell: ApplicationShell;
  @inject(OpenerService) protected opener: OpenerService;

  constructor(
    @inject(ComparisonModelService) modelService: TreeEditor.ModelService,
    @inject(ComparisonTreeLabelProvider) labelProvider: ComparisonTreeLabelProvider
  ) {
    super(ComparisonTreeEditorWidget.EDITOR_ID, modelService, labelProvider);
  }

  readonly id = ComparisonTreeEditorWidget.WIDGET_ID;
  readonly label = BaseTreeEditorWidget.WIDGET_LABEL;

  canHandle(uri: URI): number {
    if (
      uri.path.ext === '.json'
    ) {
      return 1000;
    }
    return 0;
  }

  registerCommands(commands: CommandRegistry): void {
    commands.registerCommand(
      ComparisonTreeCommands.OPEN_COMPARISON_DIAGRAM,
      new OpenComparisonDiagramCommandHandler(this.shell, this.opener));

    super.registerCommands(commands);
  }

  registerMenus(menus: MenuModelRegistry): void {
    menus.registerMenuAction(TreeContextMenu.CONTEXT_MENU, {
      commandId: ComparisonTreeCommands.OPEN_COMPARISON_DIAGRAM.id,
      label: ComparisonTreeCommands.OPEN_COMPARISON_DIAGRAM.label
    });

    super.registerMenus(menus);
  }

  protected createWidgetOptions(uri: URI, options?: WidgetOpenerOptions): NavigatableWidgetOptions {
    return {
      kind: 'navigatable',
      uri: this.serializeUri(uri)
    };
  }

  protected serializeUri(uri: URI): string {
    return uri.withoutFragment().toString();
  }

}
