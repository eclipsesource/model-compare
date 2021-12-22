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

import java.util.Collection;

import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;

public class ReferenceChangeDiffObject extends DiffObject {
	private EObject value;
	private String referenceUUID;
	private String referenceGraphicalID;

	public ReferenceChangeDiffObject(EObject object, EObject value, String uuid, ReferenceChange change) {
		super();
		this.object = object;
		this.value = value;
		this.uuid = uuid;
		this.graphicalId = UUID_Provider.getGraphicalUUID(object);
		this.referenceUUID = UUID_Provider.getUUID(object, change.getReference(), value);
		// Only add reference Ids if it is not a single Reference (e.g. in edges)
		if (object != null && object.eGet(change.getReference()) instanceof Collection<?>) {
			this.referenceGraphicalID = UUID_Provider.getGraphicalUUID(value);
		} else {
			this.referenceGraphicalID = "notNeeded";
		}
	}

	public EObject getValue() {
		return value;
	}

	public void setValue(EObject value) {
		this.value = value;
	}

	public String getReferenceUUID() {
		return referenceUUID;
	}

	public void setReferenceUUID(String referenceUUID) {
		this.referenceUUID = referenceUUID;
	}

	public String getReferenceGraphicalID() {
		return referenceGraphicalID;
	}

	public void setReferenceGraphicalID(String referenceGraphicalID) {
		this.referenceGraphicalID = referenceGraphicalID;
	}
}
