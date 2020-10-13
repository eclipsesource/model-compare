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
import * as React from 'react';
import { injectable, postConstruct } from 'inversify';
import { ReactWidget } from '@theia/core/lib/browser';

@injectable()
export class TextWidget extends ReactWidget {
    //readonly host: HTMLElement;
    protected text: string;
    
    constructor(text: string) {
        super();
        this.id = 'text-test-widget';
        this.addClass('small-widget-view');
        this.title.closable = true;
        this.text = text;

        /*
        this.scrollOptions = {
            suppressScrollX: true,
            minScrollbarLength: 35
        };
        this.node.tabIndex = 0;
        this.host = document.createElement('div');
        */
    }
    
    /*
    protected onUpdateRequest(msg: Message): void {
        super.onUpdateRequest(msg);
        ReactDOM.render(<React.Fragment>{this.render()}</React.Fragment>, this.host);
    }
    */

    protected render(): React.ReactNode {
        return <h3>{this.text}</h3>;
    }

    @postConstruct()
    protected init(): void {
        this.update();
    }
}