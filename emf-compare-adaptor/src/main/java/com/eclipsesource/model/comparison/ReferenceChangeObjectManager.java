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
package com.eclipsesource.model.comparison;

import java.util.Collection;

import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;

public class ReferenceChangeObjectManager extends ObjectManager<ReferenceChangeDiffObject> {

	public ReferenceChangeObjectManager(ReferenceChange change) {
		EObject sourceObject = change.getMatch().getRight();
		EObject targetObject = change.getMatch().getLeft();
		EObject baseObject = change.getMatch().getOrigin();

		String uuid = "Random";
		String sourceUUID = null;
		String targetUUID = null;
		String baseUUID = null;

		EObject sourceValue = null;
		EObject targetValue = null;
		EObject baseValue = null;

		if (sourceObject != null) {
			// handle reference lists
			if (sourceObject.eGet(change.getReference()) instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				EObjectContainmentEList<EObject> sourceValues = (EObjectContainmentEList<EObject>) sourceObject
						.eGet(change.getReference());
				int index = sourceValues.indexOf(change.getValue());
				if (index > -1) {
					sourceValue = (EObject) sourceValues.basicGet(index);
					if (sourceObject.eContents().contains(sourceValue)) {
						sourceUUID = UUID_Provider.getUUID(sourceValue);
					} else {
						sourceUUID = UUID_Provider.getUUID(sourceObject, change.getReference(), sourceValue);
					}
				}
			} else {
				// handle changes of reference attributes
				sourceValue = (EObject) sourceObject.eGet(change.getReference());

				if (sourceObject.eContents().contains(sourceValue)) {
					sourceUUID = UUID_Provider.getUUID(sourceValue);
				} else {
					sourceUUID = UUID_Provider.getUUID(sourceObject, change.getReference(), sourceValue);
				}
			}
			if (uuid == "Random" && sourceUUID != null) {
				uuid = sourceUUID;
			}
		}
		if (targetObject != null) {
			// handle reference lists
			if (targetObject.eGet(change.getReference()) instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				EObjectContainmentEList<EObject> targetValues = (EObjectContainmentEList<EObject>) targetObject
						.eGet(change.getReference());
				int index = targetValues.indexOf(change.getValue());
				if (index > -1) {
					targetValue = (EObject) targetValues.basicGet(index);
					if (targetObject.eContents().contains(targetValue)) {
						targetUUID = UUID_Provider.getUUID(targetValue);
					} else {
						targetUUID = UUID_Provider.getUUID(targetObject, change.getReference(), targetValue);
					}
				}
			} else {
				// handle changes of reference attributes
				targetValue = (EObject) targetObject.eGet(change.getReference());

				if (targetObject.eContents().contains(targetValue)) {
					targetUUID = UUID_Provider.getUUID(targetValue);
				} else {
					targetUUID = UUID_Provider.getUUID(targetObject, change.getReference(), targetValue);
				}
			}
			if (uuid == "Random" && targetUUID != null) {
				uuid = targetUUID;
			}
		}
		if (baseObject != null && uuid == "Random") {
			uuid = UUID_Provider.getUUID(baseObject);
		}

		this.uuid = uuid;
		this.source = new ReferenceChangeDiffObject(sourceObject, sourceValue, sourceUUID, change);
		this.target = new ReferenceChangeDiffObject(targetObject, targetValue, targetUUID, change);
		this.base = new ReferenceChangeDiffObject(baseObject, baseValue, baseUUID, change);
	}
}
