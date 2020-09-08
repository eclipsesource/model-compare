package org.eclipse.emfcloud.EMF_Compare_Ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class EObjectChangeMapping {
	private Map<EObject, ChangeRepresenation> eObjectMap = new HashMap<>();
	private Map<EAttribute, List<ChangeRepresenation>> eAttributeMap = new HashMap<>();
	
	public void add(EObject eObject, String uuid, DifferenceKind kind) {
		if (eObject != null && (eObjectMap.get(eObject) == null || eObjectMap.get(eObject).kind == null)) {
			eObjectMap.put(eObject, new ChangeRepresenation(eObject, uuid, kind));
		}
	}
	
	public void add(EAttribute eAttribute, EObject owner, String uuid, DifferenceKind kind) {
		if (owner != null) {
			List<ChangeRepresenation> repList = eAttributeMap.get(eAttribute);
			if (repList == null) {
				repList = new ArrayList<>();
				eAttributeMap.put(eAttribute, repList);
			}
			repList.add(new ChangeRepresenation(owner, uuid, kind));
		}
	}
	
	/**
	 * Returns the ChangeRepresenation for an EObject
	 */
	public ChangeRepresenation getChangeRep(EObject eObject) {
		ChangeRepresenation rep = eObjectMap.get(eObject);
		return rep;
	}
	
	/**
	 * Returns the ChangeRepresenation for an attribute
	 */
	public ChangeRepresenation getChangeRep(EAttribute eAttribute, EObject owner) {
		List<ChangeRepresenation> repList = eAttributeMap.get(eAttribute);
		if (repList == null) {
			return null;
		}
		for (ChangeRepresenation rep : repList) {
			if (owner.equals(rep.getOwner())) {
				return rep;
			}
		}
		
		return null;
	}
	
	
	
	public Map<EObject, ChangeRepresenation> getEObjectMap() {
		return eObjectMap;
	}

	public Map<EAttribute, List<ChangeRepresenation>> getEAttributeMap() {
		return eAttributeMap;
	}
	
	public static String getUUID(ChangeRepresenation rep, boolean createIfNotFound) {
		return EObjectChangeMapping.getUUID(rep, null, null, createIfNotFound);
	}

	public static String getUUID(ChangeRepresenation rep1, ChangeRepresenation rep2, ChangeRepresenation rep3, boolean createIfNotFound) {
		if (rep1 != null) {
			return rep1.getUuid();
		}
		if (rep2 != null) {
			return rep2.getUuid();
		}
		if (rep3 != null) {
			return rep3.getUuid();
		}
		
		if (createIfNotFound) {
			return UUID.randomUUID().toString();
		}
		return null;
	}


	class ChangeRepresenation {
		private EObject owner;
		private String uuid;
		private DifferenceKind kind;
		
		public ChangeRepresenation(EObject owner, String uuid, DifferenceKind kind) {
			super();
			this.owner = owner;
			this.uuid = uuid;
			this.kind = kind;
		}
		
		public EObject getOwner() {
			return owner;
		}
		public void setOwner(EObject owner) {
			this.owner = owner;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public DifferenceKind getKind() {
			return kind;
		}
		public void setKind(DifferenceKind kind) {
			this.kind = kind;
		}
		
	}
}
