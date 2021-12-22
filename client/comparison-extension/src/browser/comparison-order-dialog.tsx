/********************************************************************************
 * Copyright (c) 2021 EclipseSource and others.
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

export const DIALOG_TITLE = 'Choose Source, Target, Base';
export const DIALOG_CLASS = 'comparisonDialog';
export const DIALOG_LABEL_CLASS = 'comparisonDialogInput';

@injectable()
export class ComparisonOrderDialog extends ReactDialog<void> {
    protected source: string;
    protected target: string;
    protected base: string;
    protected showBase = true;

    constructor(source: string, target: string, base = '') {
        super({ title: DIALOG_TITLE });

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
                    <button onClick={() => this.swapBottom()}>Swap</button>
                    <br />
                    <br />
                    <b>base (common ancenstor):</b>
                    <br />
                    <input type='text' value={this.base} className={DIALOG_LABEL_CLASS} readOnly={true} dir='rtl' />
                </>
            );
        }

        return (
            <div className={DIALOG_CLASS}>
                <h3>Select which file is source, target and base</h3>
                <b>source (old version):</b>
                <br />
                <input type='text' value={this.source} className={DIALOG_LABEL_CLASS} readOnly={true} dir='rtl' />
                <br />
                <br />
                <button onClick={() => this.swapTop()}>Swap</button>
                <br />
                <br />
                <b>target (new version):</b>
                <br />
                <input type='text' value={this.target} className={DIALOG_LABEL_CLASS} readOnly={true} dir='rtl' />
                <br />
                <br />
                {baseTags}
            </div>
        );
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
