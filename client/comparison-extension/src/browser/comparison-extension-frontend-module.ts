import '../../style/forms.css';
import '../../style/index.css';
import '../../style/elements.css';
import '@fortawesome/fontawesome-free/js/all.js';

import { ContainerModule, injectable} from 'inversify';
import { WidgetFactory, LabelProviderContribution, WebSocketConnectionProvider } from '@theia/core/lib/browser';

import { ComparisonTreeLabelProvider } from './tree-editor/ComparisonLabelProviderContribution';
import { createBasicTreeContainter } from './tree-widget/util';
//import { NavigatableTreeEditorOptions } from './tree-widget/navigatable-tree-editor-widget';
//import URI from '@theia/core/lib/common/uri';
import { ComparisonTreeEditorWidget, ComparisonTreeEditorWidgetOptions } from './tree-editor/ComparisonTreeEditorWidget';
import { ComparisonModelService } from './tree-editor/comparison-model-service';
import { ComparisonTreeNodeFactory } from './tree-editor/comparison-node-factory';
//import { ComparisonTreeEditorContribution } from './comparison-editor-tree-contribution';
//import { MenuContribution, CommandContribution } from '@theia/core';
//import { CoffeeLabelProviderContribution } from './ComparisonLabelProvider';
import { bindViewContribution } from '@theia/core/lib/browser';
import { TabBarToolbarContribution } from '@theia/core/lib/browser/shell/tab-bar-toolbar';
import { ComparisonTreeContribution } from './comparison-contribution';
import { BackendClient, ComparisonBackendService, COMPARISON_BACKEND_PATH } from '../common/protocol';


export default new ContainerModule(bind => {
    console.log("starting frontend");

    // compare option in file system
    bindViewContribution(bind, ComparisonTreeContribution);
    bind(TabBarToolbarContribution).toService(ComparisonTreeContribution);

     // Bind Theia IDE contributions
    //(LabelProviderContribution).to(CoffeeLabelProviderContribution);
    //bind(OpenHandler).to(ComparisonTreeEditorContribution);
    //bind(MenuContribution).to(ComparisonTreeEditorContribution);
    //bind(CommandContribution).to(ComparisonTreeEditorContribution);
    bind(LabelProviderContribution).to(ComparisonTreeLabelProvider);

    // bind to themselves because we use it outside of the editor widget, too.
    bind(ComparisonModelService).toSelf().inSingletonScope();
    bind(ComparisonTreeLabelProvider).toSelf().inSingletonScope();
    
    bind<WidgetFactory>(WidgetFactory).toDynamicValue(context => ({
        id: ComparisonTreeEditorWidget.WIDGET_ID,
        createWidget: (options: ComparisonTreeEditorWidgetOptions) => {

        // This creates a new inversify Container with all the basic services needed for a theia tree editor.
        const treeContainer = createBasicTreeContainter(
            context.container,
            ComparisonTreeEditorWidget,
            ComparisonModelService,
            ComparisonTreeNodeFactory
        );

        console.log("options: " + options);

        // Our example tree editor needs additional options. So, we bind them in the container created before
        //const uri = new URI(".");
        treeContainer.bind(ComparisonTreeEditorWidgetOptions).toConstantValue(options);

        // Finally, we create a new editor by telling the container to retrieve an instance of our editor implementation
        return treeContainer.get(ComparisonTreeEditorWidget);
        }
    }));

    /*
    bind(ComparisonTreeEditorWidget).toSelf();
    bind(WidgetFactory).toDynamicValue(ctx => ({
        id: ComparisonTreeEditorWidget.WIDGET_ID,
        createWidget: () => ctx.container.get<ComparisonTreeEditorWidget>(ComparisonTreeEditorWidget)
    }));
    */

    // frontend
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
