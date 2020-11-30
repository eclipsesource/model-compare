/********************************************************************************
 * Copyright (c) 2019-2020 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the MIT License which is
 * available at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: EPL-2.0 OR MIT
 ********************************************************************************/

import { injectable } from 'inversify';
import * as React from 'react';
import { TreeWidget } from "@theia/core/lib/browser";

@injectable()
export class TreeWidgetWithTitle extends TreeWidget {

    treeTitle: string = "Title";

    protected render(): React.ReactNode {
        return <div><h3>{this.treeTitle}</h3>
            {super.render()}
        </div>
    }
}