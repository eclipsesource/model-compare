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
import { bindViewContribution, LabelProviderContribution, WebSocketConnectionProvider, WidgetFactory } from '@theia/core/lib/browser';
import { TabBarToolbarContribution } from '@theia/core/lib/browser/shell/tab-bar-toolbar';
import { ContainerModule, injectable } from 'inversify';
import '../../style/elements.css';
import '../../style/index.css';
import { BackendClient, ComparisonBackendService, COMPARISON_BACKEND_PATH } from '../common/protocol';
import { GraphicalComparisonOpenHandler } from './graphical/graphical-comparison-open-handler';
import { GraphicalComparisonOpener } from './graphical/graphical-comparison-opener';
import { TreeComparisonContribution } from './tree-comparison-contribution';
import { ComparisonTreeNodeFactory } from './tree-editor/comparison-node-factory';
import { ComparisonTreeLabelProvider } from './tree-editor/ComparisonLabelProviderContribution';
import { ComparisonTreeEditorWidget, ComparisonTreeEditorWidgetOptions } from './tree-editor/ComparisonTreeEditorWidget';
import { createBasicTreeContainter } from './tree-widget/util';

export default new ContainerModule(bind => {
    // compare option in file system
    bind(GraphicalComparisonOpener).toSelf().inSingletonScope();
    bindViewContribution(bind, TreeComparisonContribution);
    bind(TabBarToolbarContribution).toService(TreeComparisonContribution);

    // bind Theia IDE contributions
    bind(LabelProviderContribution).to(ComparisonTreeLabelProvider);

    // bind to themselves because we use it outside of the editor widget too
    bind(ComparisonTreeLabelProvider).toSelf().inSingletonScope();
    bind(GraphicalComparisonOpenHandler).toSelf().inSingletonScope();

    bind<WidgetFactory>(WidgetFactory).toDynamicValue(context => ({
        id: ComparisonTreeEditorWidget.WIDGET_ID,
        createWidget: (options: ComparisonTreeEditorWidgetOptions) => {
            // This creates a new inversify container with all the basic services needed for a theia tree editor
            const treeContainer = createBasicTreeContainter(context.container, ComparisonTreeEditorWidget, ComparisonTreeNodeFactory);

            // create a new container
            return treeContainer.get(ComparisonTreeEditorWidget);
        }
    }));

    // for backend
    bind(BackendClient).to(BackendClientImpl).inSingletonScope();
    bind(ComparisonBackendService)
        .toDynamicValue(ctx => {
            const connection = ctx.container.get(WebSocketConnectionProvider);
            const backendClient: BackendClient = ctx.container.get(BackendClient);
            return connection.createProxy<ComparisonBackendService>(COMPARISON_BACKEND_PATH, backendClient);
        })
        .inSingletonScope();
});

@injectable()
class BackendClientImpl implements BackendClient {
    getName(): Promise<string> {
        return new Promise(resolve => resolve('Client'));
    }
}
