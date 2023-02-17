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
import { ReactWidget } from '@theia/core/lib/browser';
import { injectable, postConstruct } from 'inversify';
import * as React from 'react';
import { BaseTreeEditorWidget } from './tree-editor-widget';

@injectable()
export class TreeActionWidget extends ReactWidget {
    protected parentView: BaseTreeEditorWidget;
    protected activateMerge = false;
    protected activateConflict = false;
    protected showGraphicalComparison = false;

    constructor(parent: BaseTreeEditorWidget) {
        super();
        this.parentView = parent;
        this.id = 'tree-settings-widget';
        this.addClass('widget-view');
        this.title.closable = true;
    }

    protected render(): React.ReactNode {
        let graphicalComparison = <></>;
        if (this.showGraphicalComparison) {
            graphicalComparison = (
                <div className='action-section'>
                    <h4>Graphical Comparison</h4>
                    <button className='theia-button' onClick={() => this.parentView.showGraphicalComparison()}>
                        <i className='codicon codicon-type-hierarchy-sub' /> Show graphical comparison
                    </button>
                </div>
            );
        }

        return (
            <div className='tree-actions'>
                <h3>{'Actions:'}</h3>
                <div className='action-section'>
                    <h4>Merge</h4>
                    <button
                        className='theia-button'
                        onClick={() => this.parentView.merge(true, false, false)}
                        disabled={!this.activateMerge}
                    >
                        <i className='codicon codicon-arrow-right' /> Accept Change (Merge To Source)
                    </button>
                    <button
                        className='theia-button'
                        onClick={() => this.parentView.merge(false, false, false)}
                        disabled={!this.activateMerge}
                    >
                        <i className='codicon codicon-close' /> Discard Change (Merge to Target)
                    </button>
                    <button className='theia-button' onClick={() => this.parentView.merge(true, true, false)}>
                        <i className='codicon codicon-arrow-right' /> Accept All (Merge to Source)
                    </button>
                </div>
                <div className='action-section'>
                    <h4>Resolve conflict</h4>
                    <button
                        className='theia-button'
                        onClick={() => this.parentView.merge(true, false, true)}
                        disabled={!this.activateConflict}
                    >
                        <i className='codicon codicon-arrow-left' /> Keep Source (Current)
                    </button>
                    <button
                        className='theia-button'
                        onClick={() => this.parentView.merge(false, false, true)}
                        disabled={!this.activateConflict}
                    >
                        <i className='codicon codicon-arrow-right' /> Keep Target (Incoming)
                    </button>
                </div>
                <div className='action-section'>
                    <h4>Undo</h4>
                    <button className='theia-button' onClick={() => this.parentView.undoMerge()} disabled={!this.parentView.dirty}>
                        <i className='codicon codicon-discard' /> Undo last action
                    </button>
                </div>
                {graphicalComparison}
            </div>
        );
    }

    public updateActivation(type: string): void {
        if (type === 'diff') {
            this.activateMerge = true;
            this.activateConflict = false;
        } else if (type === 'conflict') {
            this.activateMerge = false;
            this.activateConflict = true;
        } else {
            this.activateMerge = false;
            this.activateConflict = false;
        }
        this.update();
    }

    public setGraphicalComparisonVisibility(visible: boolean): void {
        this.showGraphicalComparison = visible;
        this.update();
    }

    @postConstruct()
    protected init(): void {
        this.update();
    }
}
