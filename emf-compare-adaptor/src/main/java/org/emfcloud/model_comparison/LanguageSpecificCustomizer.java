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
package org.emfcloud.model_comparison;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class LanguageSpecificCustomizer {

	public static JSONTreeNode customizeOverview(JSONTreeNode node) {
		return node;
	}

	public static JSONTreeNode customizeMatchNode(JSONTreeNode node, Match match) {

		return node;
	}

	public static JSONTreeNode customizeDiffNode(JSONTreeNode node, Diff diff) {
		return node;
	}

	public static JSONTreeNode customizeModel(JSONTreeNode node) {

		return node;
	}

	public static JSONTreeNode customizeNode(JSONTreeNode node, EObject eObject) {

		return node;
	}

	public static JSONTreeNode customizeAttribute(JSONTreeNode node, EObject eObject, EAttribute attribute) {

		return node;
	}

	public static JSONTreeNode customizeReferenceContainer(JSONTreeNode node, EObject source, EReference reference) {
		return node;
	}

	public static JSONTreeNode customizeReference(JSONTreeNode node, EObject source, EObject target,
			EReference reference) {

		return node;
	}
}
