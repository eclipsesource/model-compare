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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class UUID_Provider {
	
	public static String getUUID(EObject obj) {
		return UUIDType5.nameUUIDFromNamespaceAndString(UUIDType5.NAMESPACE_URL, normalize(EcoreUtil.getURI(obj).toString())).toString();
	}
	
	public static String getUUID(EObject owner, EAttribute attribute) {
		return UUIDType5.nameUUIDFromNamespaceAndString(UUIDType5.NAMESPACE_URL, normalize(EcoreUtil.getURI(owner).toString()) + normalize(EcoreUtil.getURI(attribute).toString())).toString();
	}
	
	public static String getUUID(EObject owner, EReference reference, EObject ref) {
		return UUIDType5.nameUUIDFromNamespaceAndString(UUIDType5.NAMESPACE_URL, normalize(EcoreUtil.getURI(owner).toString()) + normalize(EcoreUtil.getURI(reference).toString()) + normalize(EcoreUtil.getURI(ref).toString())).toString();
	}
	
	private static String normalize(String uri) {
		if (uri.startsWith("file:///")) {
			return uri.replaceFirst("file:///", "file:/");
		}
		return uri;
	}
}
