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
import URI from '@theia/core/lib/common/uri';
import { injectable } from 'inversify';
import * as path from 'path';

@injectable()
export abstract class ComparisonExtensionConfiguration {
    abstract fileExtensions: string[];

    canHandle(uri: URI): boolean {
        if (this.fileExtensions.includes(uri.path.ext)) {
            return true;
        }
        return false;
    }

    supportGraphicalComparison(): boolean {
        return false;
    }

    getComparisonJarPath(): string {
        // eslint-disable-next-line no-undef
        return path.resolve(__dirname, '..', '..', 'server', 'server.jar');
    }

    getDiffViewIcon(): string {
        return 'far fa-copy';
    }

    getGraphicalDiffViewIcon(): string {
        return 'fas fa-project-diagram';
    }

    abstract getModelJarPath(): string;
    abstract getModelPackageName(): string;
}
