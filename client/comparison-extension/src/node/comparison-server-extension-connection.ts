/*!
 * Copyright (C) 2019 EclipseSource and others.
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
 */
import * as path from "path";
import { RawProcess, RawProcessFactory } from "@theia/process/lib/node/raw-process";
import { inject, injectable } from "inversify";
import { ILogger } from "@theia/core";

//const os = require('os');

@injectable()
export class ComparisonServerExtensionConnection {

  constructor(
    @inject(RawProcessFactory) protected readonly processFactory: RawProcessFactory,
    @inject(ILogger) private readonly logger: ILogger) { }

  public compare(left: string, right: string, origin: string): Promise<string> {
    const jarPath = path.resolve(__dirname, '..', '..',
        'server', 'model-comparison-1.0.jar');
    if (jarPath.length === 0) {
        throw new Error('model-comparison-1.0.jar not found');
    }

    const command = 'java';
    const args: string[] = [];
    /*
    let platformWorkspacePath = workspacePath;
    if (os.platform() === 'win32') {
        platformWorkspacePath = workspacePath.substr(1);
    }
    */

    args.push(
        '-jar', jarPath,
        '-operation', 'comparison',
        '-left', left,
        '-right', right //, '-origin', platformWorkspacePath
    );

    return new Promise(resolve => {
        const process = this.spawnProcess(command, args);
        if (process === undefined || process.process === undefined || process === null || process.process === null) {
            resolve('Process not spawned');
            return;
        }

        let out = "";
        const stdout = process.process.stdout;
        if (stdout) {
          stdout.on('data', data => {
            out += data;
          });
        }

        process.process.on('exit', (code: any) => {
          switch (code) {
              case 0: resolve(out); break;
              case -10: resolve('Custom ERROR (TODO)'); break;
              default: resolve('UNKNOWN ERROR'); break;
          }
        });
    });
  }

  private spawnProcess(command: string, args?: string[]): RawProcess | undefined {
    const rawProcess = this.processFactory({ command, args });
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

  private logError(data: string | Buffer) {
    if (data) {
        this.logger.error(`Ecore Gen: ${data}`);
    }
  }

}
