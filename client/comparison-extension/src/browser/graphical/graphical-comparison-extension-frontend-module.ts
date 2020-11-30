import '../../../style/index.css';
import '../../../style/elements.css';

import { ContainerModule } from 'inversify';
import { bindViewContribution, WidgetFactory } from '@theia/core/lib/browser';
import { GraphicalComparisonWidget, GraphicalComparisonWidgetOptions } from './graphical-comparison-widget';
import { GraphicalComparisonContribution } from './graphical-comparison-contribution';
import { TabBarToolbarContribution } from '@theia/core/lib/browser/shell/tab-bar-toolbar';

export default new ContainerModule(bind => {
    bindViewContribution(bind, GraphicalComparisonContribution);
    bind(TabBarToolbarContribution).toService(GraphicalComparisonContribution);
    
    bind<WidgetFactory>(WidgetFactory).toDynamicValue(context => ({
        id: GraphicalComparisonWidget.WIDGET_ID,
        createWidget: (options: GraphicalComparisonWidgetOptions) => {
            const container = context.container.createChild();
            container.bind(GraphicalComparisonWidgetOptions).toConstantValue(options);
            container.bind(GraphicalComparisonWidget).toSelf();
            return container.get(GraphicalComparisonWidget);
        }
    }));
});
