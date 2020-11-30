import '../../style/index.css';
import '../../style/elements.css';

import { ContainerModule, injectable} from 'inversify';
import { WidgetFactory, LabelProviderContribution, WebSocketConnectionProvider } from '@theia/core/lib/browser';
import { ComparisonTreeLabelProvider } from './tree-editor/ComparisonLabelProviderContribution';
import { createBasicTreeContainter } from './tree-widget/util';
import { ComparisonTreeEditorWidget, ComparisonTreeEditorWidgetOptions } from './tree-editor/ComparisonTreeEditorWidget';
import { ComparisonModelService } from './tree-editor/comparison-model-service';
import { ComparisonTreeNodeFactory } from './tree-editor/comparison-node-factory';
import { bindViewContribution } from '@theia/core/lib/browser';
import { TabBarToolbarContribution } from '@theia/core/lib/browser/shell/tab-bar-toolbar';
import { TreeComparisonContribution } from './tree-comparison-contribution';
import { BackendClient, ComparisonBackendService, COMPARISON_BACKEND_PATH } from '../common/protocol';
import { ComparisonExtensionConfiguration } from './comparison-extension-configuration';
import { GraphicalComparisonOpener } from './graphical/graphical-comparison-opener';

export default new ContainerModule(bind => {

    // compare option in file system
    bind(GraphicalComparisonOpener).toSelf().inSingletonScope();
    bind(ComparisonExtensionConfiguration).toSelf().inSingletonScope();
    bindViewContribution(bind, TreeComparisonContribution);
    bind(TabBarToolbarContribution).toService(TreeComparisonContribution);
    
    // bind Theia IDE contributions
    bind(LabelProviderContribution).to(ComparisonTreeLabelProvider);

    // bind to themselves because we use it outside of the editor widget too
    bind(ComparisonModelService).toSelf().inSingletonScope();
    bind(ComparisonTreeLabelProvider).toSelf().inSingletonScope();
    
    bind<WidgetFactory>(WidgetFactory).toDynamicValue(context => ({
        id: ComparisonTreeEditorWidget.WIDGET_ID,
        createWidget: (options: ComparisonTreeEditorWidgetOptions) => {

        // This creates a new inversify container with all the basic services needed for a theia tree editor
        const treeContainer = createBasicTreeContainter(
            context.container,
            ComparisonTreeEditorWidget,
            ComparisonModelService,
            ComparisonTreeNodeFactory
        );

        // create a new container
        return treeContainer.get(ComparisonTreeEditorWidget);
        }
    }));

    // for backend
    bind(BackendClient).to(BackendClientImpl).inSingletonScope();
    bind(ComparisonBackendService).toDynamicValue(ctx => {
        const connection = ctx.container.get(WebSocketConnectionProvider);
        const backendClient: BackendClient = ctx.container.get(BackendClient);
        return connection.createProxy<ComparisonBackendService>(COMPARISON_BACKEND_PATH, backendClient);
    }).inSingletonScope();
});


@injectable()
class BackendClientImpl implements BackendClient {
    getName(): Promise<string> {
        return new Promise(resolve => resolve('Client'));
    }
}
