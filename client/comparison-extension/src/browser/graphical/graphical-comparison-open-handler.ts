/********************************************************************************
 * Copyright (c) 2022 EclipseSource and others.
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
import { MaybePromise } from '@theia/core';
import { WidgetOpenerOptions, WidgetOpenHandler } from '@theia/core/lib/browser';
import URI from '@theia/core/lib/common/uri';
import { GraphicalComparisonWidget, GraphicalComparisonWidgetOptions } from './graphical-comparison-widget';

export class GraphicalComparisonOpenHandler extends WidgetOpenHandler<GraphicalComparisonWidget> {
    id: string;
    canHandle(uri: URI, options?: WidgetOpenerOptions): MaybePromise<number> {
        throw new Error('Method not implemented.');
    }
    protected createWidgetOptions(uri: URI, options?: WidgetOpenerOptions): GraphicalComparisonWidgetOptions {
        throw new Error('Method not implemented.');
    }
}
