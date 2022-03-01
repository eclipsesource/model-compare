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
import { ILogger } from '@theia/core';
import { RawProcess, RawProcessFactory } from '@theia/process/lib/node/raw-process';
import { inject, injectable } from 'inversify';
import { ComparisonExtensionConfiguration } from '../browser/comparison-extension-configuration';

@injectable()
export class ComparisonServerExtensionConnection {
    constructor(
        @inject(RawProcessFactory) protected readonly processFactory: RawProcessFactory,
        @inject(ComparisonExtensionConfiguration) protected readonly config: ComparisonExtensionConfiguration,
        @inject(ILogger) private readonly logger: ILogger
    ) {}

    public compare(source: string, target: string, base: string, merges: string): Promise<string> {
        const jarPath = this.config.getComparisonJarPath();
        const modelJarPath = this.config.getModelJarPath();
        const packageName = this.config.getModelPackageName();
        if (jarPath.length === 0) {
            throw new Error('model-comparison jar not found');
        }

        const command = 'java';
        const args: string[] = [];

        args.push(
            '-jar',
            this.addQuotes(jarPath),
            '-model',
            this.addQuotes(modelJarPath),
            '-package',
            this.addQuotes(packageName),
            '-operation',
            'comparison',
            '-source',
            this.addQuotes(source),
            '-target',
            this.addQuotes(target),
            '-base',
            this.addQuotes(base),
            '-merges',
            this.addQuotes(merges)
        );

        return new Promise((resolve, reject) => {
            const process = this.spawnProcess(command, args);
            if (process === undefined || process.process === undefined || process === undefined || process.process === undefined) {
                reject('Process not spawned');
                return;
            }

            let out = '';
            const stdout = process.process.stdout;
            if (stdout) {
                stdout.on('data', data => {
                    out += data;
                });
            }

            process.process.on('exit', (code: any) => {
                const shouldResolve = this.handleExit(code, out);
                if (shouldResolve) {
                    resolve(out);
                } else {
                    reject();
                }
            });
        });
    }

    public highlight(source: string, target: string, base: string): Promise<string> {
        const jarPath = this.config.getComparisonJarPath();
        const modelJarPath = this.config.getModelJarPath();
        const packageName = this.config.getModelPackageName();
        if (jarPath.length === 0) {
            throw new Error('model-comparison jar not found');
        }

        const command = 'java';
        const args: string[] = [];

        args.push(
            '-jar',
            this.addQuotes(jarPath),
            '-model',
            this.addQuotes(modelJarPath),
            '-package',
            this.addQuotes(packageName),
            '-operation',
            'highlight',
            '-source',
            this.addQuotes(source),
            '-target',
            this.addQuotes(target),
            '-base',
            this.addQuotes(base)
        );

        return new Promise((resolve, reject) => {
            const process = this.spawnProcess(command, args);
            if (process === undefined || process.process === undefined || process === undefined || process.process === undefined) {
                reject('Process not spawned');
                return;
            }

            let out = '';
            const stdout = process.process.stdout;
            if (stdout) {
                stdout.on('data', data => {
                    out += data;
                });
            }

            process.process.on('exit', (code: any) => {
                const shouldResolve = this.handleExit(code, out);
                if (shouldResolve) {
                    resolve(out);
                } else {
                    reject();
                }
            });
        });
    }

    public merge(source: string, target: string, base: string, merges: string, mergeConflicts: string): Promise<string> {
        const jarPath = this.config.getComparisonJarPath();
        const modelJarPath = this.config.getModelJarPath();
        const packageName = this.config.getModelPackageName();
        if (jarPath.length === 0) {
            throw new Error('model-comparison jar not found');
        }

        const command = 'java';
        const args: string[] = [];

        args.push(
            '-jar',
            this.addQuotes(jarPath),
            '-model',
            this.addQuotes(modelJarPath),
            '-package',
            this.addQuotes(packageName),
            '-operation',
            'merge',
            '-source',
            this.addQuotes(source),
            '-target',
            this.addQuotes(target),
            '-base',
            this.addQuotes(base),
            '-merges',
            this.addQuotes(merges)
        );

        return new Promise((resolve, reject) => {
            const process = this.spawnProcess(command, args);
            if (process === undefined || process.process === undefined || process === undefined || process.process === undefined) {
                reject('Process not spawned');
                return;
            }

            let out = '';
            const stdout = process.process.stdout;
            if (stdout) {
                stdout.on('data', data => {
                    out += data;
                });
            }

            process.process.on('exit', (code: any) => {
                const shouldResolve = this.handleExit(code, out);
                if (shouldResolve) {
                    resolve(out);
                } else {
                    reject();
                }
            });
        });
    }

    protected handleExit(code: number, output: string): boolean {
        switch (code) {
            case 0:
                return true;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                console.error(`EMFCompare Framework: ${output}`);
                return false;
            default:
                console.error('UNKNOWN ERROR');
                return false;
        }
    }

    private spawnProcess(command: string, args?: string[]): RawProcess | undefined {
        const rawProcess = this.processFactory({ command: command, args: args, options: { shell: true } });
        if (rawProcess.process === undefined) {
            return undefined;
        }
        rawProcess.process.on('error', this.onDidFailSpawnProcess.bind(this));
        const stderr = rawProcess.process.stderr;
        if (stderr) {
            stderr.on('data', this.logError.bind(this));
        }
        return rawProcess;
    }

    private onDidFailSpawnProcess(error: Error): void {
        this.logger.error(error);
    }

    private logError(data: string): void {
        if (data) {
            this.logger.error(`Framework connection: ${data}`);
        }
    }

    private addQuotes(text: string): string {
        return '"' + text + '"';
    }
}
