import { WidgetManager } from '@theia/core/lib/browser';
import URI from '@theia/core/lib/common/uri';
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
import { injectable } from 'inversify';

@injectable()
export class GraphicalComparisonOpener {
    showWidgets(widgetManager: WidgetManager, left: URI, right: URI) {
      throw Error("Needs to be implemented in the specific editors");

      /*
      super({
        widgetId: ComparisonTreeEditorWidget.WIDGET_ID,
        widgetName: ComparisonTreeEditorWidget.WIDGET_LABEL,
        defaultWidgetOptions: {
            area: 'main',
            mode: 'open-to-left' //open-to-right   ? -> split-left
        }
     });
    */
    }
}
