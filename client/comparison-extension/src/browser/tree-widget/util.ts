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
import { createTreeContainer, TreeWidget as TheiaTreeWidget } from '@theia/core/lib/browser/tree';
import { interfaces } from 'inversify';
import { TreeEditor } from './interfaces';
import { MasterTreeWidget } from './master-tree-widget';
import { BaseTreeEditorWidget } from './tree-editor-widget';

function createTreeWidget(parent: interfaces.Container): MasterTreeWidget {
    const treeContainer = createTreeContainer(parent);

    treeContainer.unbind(TheiaTreeWidget);
    treeContainer.bind(MasterTreeWidget).toSelf();
    return treeContainer.get(MasterTreeWidget);
}

/**
 * Creates a new inversify container to create tree editor widgets using the given customizations.
 * If further services are needed than the given ones, these must either be bound in the parent container
 * or to the returned container before a tree editor widget is requested.
 *
 * Note that this method does not create a singletion tree editor but returns a new instance whenever an instace is requested.
 *
 * @param parent The parent inversify container
 * @param treeEditorWidget The concrete tree editor widget to create
 * @param modelService The tree editor's model service
 * @param nodeFactory The tree editor's node factory
 */
export function createBasicTreeContainter(
    parent: interfaces.Container,
    treeEditorWidget: interfaces.Newable<BaseTreeEditorWidget>,
    // modelService: interfaces.Newable<TreeEditor.ModelService>,
    nodeFactory: interfaces.Newable<TreeEditor.NodeFactory>
): interfaces.Container {
    const container = parent.createChild();
    // container.bind(TreeEditor.ModelService).to(modelService);
    container.bind(TreeEditor.NodeFactory).to(nodeFactory);
    container.bind(MasterTreeWidget).toDynamicValue(context => createTreeWidget(context.container));
    container.bind(treeEditorWidget).toSelf();

    return container;
}
