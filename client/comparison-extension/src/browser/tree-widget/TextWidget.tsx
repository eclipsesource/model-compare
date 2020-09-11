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
import { ReactWidget } from '@theia/core/lib/browser/widgets/react-widget';
import * as React from 'react';
import { injectable, postConstruct } from 'inversify';
import { TREE_CLASS } from '@theia/core/lib/browser';

@injectable()
export class TextWidget extends ReactWidget {
    titleString: string;
    
    constructor(text: string) {
        super();
        this.id = 'text-widget';
        this.title.label = text;
        this.titleString = text;
        this.title.closable = true;
        //this.addClass('hello-world-widget');

        this.scrollOptions = {
            suppressScrollX: true,
            minScrollbarLength: 35
        };
        this.addClass(TREE_CLASS);
        this.node.tabIndex = 0;
    }

    protected render(): React.ReactNode {
        return <h3>{this.titleString}</h3>;
    }

    @postConstruct()
    protected init(): void {
        this.update();
    }
}