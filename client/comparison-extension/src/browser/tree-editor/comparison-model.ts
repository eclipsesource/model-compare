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
import URI from '@theia/core/lib/common/uri';
import { TreeEditor } from '../tree-widget/interfaces';

export namespace ComparisonModel {
    export namespace Type {
        export const DiagramLandscape = 'http://www.eclipsesource.com/modelserver/example/comparisonmodel#//DiagramLandscape';
        export const DiagramNode = 'http://www.eclipsesource.com/modelserver/example/comparisonmodel#//DiagramNode';
        export const DiagramNodeAttribute = 'http://www.eclipsesource.com/modelserver/example/comparisonmodel#//DiagramNodeAttribute';
        export const DiagramEdge = 'http://www.eclipsesource.com/modelserver/example/comparisonmodel#//DiagramEdge';
        export const DiagramEdgeMember = 'http://www.eclipsesource.com/modelserver/example/comparisonmodel#//DiagramEdgeMember';
        export const DiagramEdgeAttribute = 'http://www.eclipsesource.com/modelserver/example/comparisonmodel#//DiagramEdgeAttribute';
        
        export function name(type: string): string {
            return new URI(type).fragment.substring(2);
        }
    }

    const components = [
        Type.DiagramNode,
        Type.DiagramEdge
    ];

    const nodes_children = [
        Type.DiagramNodeAttribute,
        Type.DiagramNode,
        Type.DiagramEdge
    ];

    const edges_children = [
        Type.DiagramEdgeMember,
        Type.DiagramEdgeAttribute
    ];

    /** Maps types to their creatable children */
    export const childrenMapping: Map<string, TreeEditor.ChildrenDescriptor[]> = new Map([
        [
            Type.DiagramLandscape, [
                {
                    property: 'children',
                    children: components
                }
            ]
        ],
        [
            Type.DiagramNode, [
                {
                    property: 'children',
                    children: nodes_children
                }
            ]
        ],
        [
            Type.DiagramEdge, [
                {
                    property: 'children',
                    children: edges_children
                }
            ]
        ]
    ]);

}