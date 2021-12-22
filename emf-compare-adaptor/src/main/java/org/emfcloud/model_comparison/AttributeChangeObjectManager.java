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

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.ecore.EObject;

public class AttributeChangeObjectManager extends ObjectManager<AttributeChangeDiffObject> {

	public AttributeChangeObjectManager(AttributeChange change) {
		EObject sourceObject = change.getMatch().getRight();
		EObject targetObject = change.getMatch().getLeft();
		EObject baseObject = change.getMatch().getOrigin();

		String uuid = "Random";
		String sourceUUID = null;
		String targetUUID = null;
		String baseUUID = null;

		String sourceValue = null;
		String targetValue = null;
		String baseValue = null;

		if (sourceObject != null) {
			sourceUUID = UUID_Provider.getUUID(sourceObject, change.getAttribute());
			sourceValue = sourceObject.eGet(change.getAttribute()).toString();
			uuid = sourceUUID;
		}
		if (targetObject != null) {
			targetUUID = UUID_Provider.getUUID(targetObject, change.getAttribute());
			targetValue = targetObject.eGet(change.getAttribute()).toString();
			if (uuid == "Random") {
				uuid = targetUUID;
			}
		}
		if (baseObject != null) {
			baseUUID = UUID_Provider.getUUID(baseObject, change.getAttribute());
			baseValue = baseObject.eGet(change.getAttribute()).toString();
			if (uuid == "Random") {
				uuid = baseUUID;
			}
		}

		this.uuid = uuid;
		this.source = new AttributeChangeDiffObject(sourceObject, sourceValue, sourceUUID);
		this.target = new AttributeChangeDiffObject(targetObject, targetValue, targetUUID);
		this.base = new AttributeChangeDiffObject(baseObject, baseValue, baseUUID);
	}

}
