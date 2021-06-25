/********************************************************************************
 * Copyright (c) 2020 EclipseSource and others.
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

import java.util.Iterator;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import EAM_Metamodel.EAM_Application;

public class LanguageSpecificCustomizer {
	
	public static JSONTreeNode customizeOverview(JSONTreeNode node) {
		if (node == null) {
			return null;
		}
		
		Iterator<JSONTreeNode> iterator = node.getChildren().iterator();
		while (iterator.hasNext()) {
			JSONTreeNode child = iterator.next();
			customizeOverview(child);
			
			// because we removed some nodes we need to check for unused matches
			if (child.getChildrenCount() == 0 && child.getType().equals("match")) {
				iterator.remove();
			}
		}
		return node;
	}
	
	public static JSONTreeNode customizeMatchNode(JSONTreeNode node, Match match) {
		
		return node;
	}
	
	public static JSONTreeNode customizeDiffNode(JSONTreeNode node, Diff diff) {
		if (node == null) {
			return null;
		}
		
		if (diff instanceof ReferenceChange) {
			ReferenceChange change = (ReferenceChange) diff;
			if (change.getReference().getName().contains("Opposite")) {
				return null;
			}
			
			if (!change.getReference().getName().equals("node")) {
				String name = node.getName();
				int insertPos = name.lastIndexOf('[');
				
				String connecton = "Association";
				if (change.getValue() instanceof EAM_Application) {
					if ((change.getMatch().getLeft() != null && change.getMatch().getLeft() instanceof EAM_Application) ||
							(change.getMatch().getRight() != null && change.getMatch().getRight() instanceof EAM_Application))
					connecton = "Communication";
				}
				
				node.setName(name.substring(0, insertPos+1) + connecton + " to " + name.substring(insertPos+1, name.length()));
			}
				
		}
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
		if (reference.getName().contains("Opposite")) {
			return node;
		}
		
		if (source instanceof EAM_Application && (reference.getName().equals("application") || reference.getName().equals("applicationOpposite"))) {
			node.setName("Communications to applications");
		} else if (!reference.getName().equals("node")) {
			if (reference.getName().contains("Opposite")) {
				node.setName("Associations to " + reference.getName().substring(0, reference.getName().indexOf("Opposite")) + "s");
			} else {
				node.setName("Associations to " + reference.getName() + "s");
			}
		}
		
		return node;
	}
	
	public static JSONTreeNode customizeReference(JSONTreeNode node, EObject source, EObject target, EReference reference) {
		
		return node;
	}
}
