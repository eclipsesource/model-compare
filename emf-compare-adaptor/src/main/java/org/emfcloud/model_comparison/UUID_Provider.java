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