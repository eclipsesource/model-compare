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

@injectable()
export class TextWidget extends ReactWidget {
    
    constructor(text: string) {
        super();
        this.id = 'text-widget';
        this.title.label = text;
        this.title.closable = true;
        //this.addClass('hello-world-widget');
    }

    protected render(): React.ReactNode {
        let styles = {
            backgroundColor: 'yellow',
          };
          //className="hello-world-widget"
        return ( 
            <React.Fragment>
                <div style={styles}>
                    {this.title.label}
                </div>
            </React.Fragment>
        );
    }

    @postConstruct()
    protected init(): void {
        this.update();
    }
}