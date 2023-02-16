/********************************************************************************
 * Copyright (c) 2021-2023 EclipseSource and others.
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
import { Message } from '@theia/core/lib/browser';
import { ReactDialog } from '@theia/core/lib/browser/dialogs/react-dialog';
import { injectable } from 'inversify';
import * as React from 'react';

export const DIALOG_TITLE = 'Choose Source and Target';
export const DIALOG_TITLE_BASE = 'Choose Source, Target and Base';

@injectable()
export class ComparisonOrderDialog extends ReactDialog<void> {
    protected source: string;
    protected target: string;
    protected base: string;
    protected showBase = true;

    constructor(source: string, target: string, base = '') {
        super({ title: base === 'undefined' || base.trim() === '' ? DIALOG_TITLE : DIALOG_TITLE_BASE });

        this.source = source;
        this.target = target;
        this.base = base;
        this.appendAcceptButton('Ok');

        if (this.base === 'undefined' || this.base.trim() === '') {
            this.showBase = false;
            this.base = '';
        }
    }

    swapTop(): void {
        const tmp: string = this.source;
        this.source = this.target;
        this.target = tmp;
        this.update();
    }

    swapBottom(): void {
        const tmp: string = this.target;
        this.target = this.base;
        this.base = tmp;
        this.update();
    }

    protected render(): React.ReactNode {
        let baseTags = <></>;
        if (this.showBase) {
            baseTags = (
                <>
                    <div className='dialog-section'>
                        <button className='theia-button' onClick={() => this.swapBottom()}>
                            <i className='codicon codicon-arrow-swap' /> Swap
                        </button>
                    </div>
                    <div className='dialog-section'>
                        <h4>base (common ancestor):</h4>
                        <input type='text' value={this.base} className={'theia-input'} readOnly={true} dir='rtl' />
                    </div>
                </>
            );
        }

        return (
            <div className='comparisonDialog'>
                <h3>{this.dialogHeader}</h3>
                <div className='dialog-section'>
                    <h4>source (old version):</h4>
                    <input type='text' value={this.source} className={'theia-input'} readOnly={true} dir='rtl' />
                </div>
                <div className='dialog-section'>
                    <button className='theia-button' onClick={() => this.swapTop()}>
                        <i className='codicon codicon-arrow-swap' /> Swap
                    </button>
                </div>
                <div className='dialog-section'>
                    <h4>target (new version):</h4>
                    <input type='text' value={this.target} className={'theia-input'} readOnly={true} dir='rtl' />
                </div>
                {baseTags}
            </div>
        );
    }

    protected get dialogHeader(): string {
        return this.showBase ? 'Select files for source, target and base' : 'Select files for source and target';
    }

    public getSource(): string {
        return this.source;
    }

    public getTarget(): string {
        return this.target;
    }

    public getBase(): string {
        return this.base;
    }

    protected onAfterAttach(msg: Message): void {
        super.onAfterAttach(msg);
        this.update();
    }

    get value(): undefined {
        return undefined;
    }
}
