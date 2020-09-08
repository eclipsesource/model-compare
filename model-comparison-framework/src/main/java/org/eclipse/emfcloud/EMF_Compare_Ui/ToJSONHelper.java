package org.eclipse.emfcloud.EMF_Compare_Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.FeatureMap;

public class ToJSONHelper {
	
	/**
	 * builds the top overview tree which shows the differences and merge conflicts (TODO)
	 */
	public static JSONTreeNode getOverview(EObject eObject, EObjectChangeMapping changeMapping) {
		if (eObject instanceof Match) {
			Match match = (Match)eObject;
			return matchToJSONTree(match, changeMapping);
			
		} else if (eObject instanceof Diff) {
			Diff diff = (Diff)eObject;
			return diffToJSONTree(diff, changeMapping);
			
		} else if (eObject instanceof MatchResource) {
			System.out.println("MatchResource");
			throw new RuntimeException("not implemented");
		} else if (eObject instanceof Equivalence) {
			System.out.println("Equivalence");
			throw new RuntimeException("not implemented");
		} else if (eObject instanceof Conflict) {
			System.out.println("Conflict");
			throw new RuntimeException("not implemented");
		}
		
		return null;
	}
	
	private static JSONTreeNode matchToJSONTree(Match match, EObjectChangeMapping changeMapping) {
		EObject effectiveAncestor = match.getOrigin();
		EObject effectiveLeft = match.getLeft();
		EObject effectiveRight = match.getRight();
		
		if (effectiveAncestor == null && effectiveLeft == null && effectiveRight == null) {
			EObject eContainer = match.eContainer();
			if (eContainer instanceof Match) {
				return getOverview(eContainer, changeMapping);
			}
			
		} else {
			
			EObjectChangeMapping.ChangeRepresenation repA = changeMapping.getChangeRep(effectiveAncestor);
			EObjectChangeMapping.ChangeRepresenation repL = changeMapping.getChangeRep(effectiveAncestor);
			EObjectChangeMapping.ChangeRepresenation repR = changeMapping.getChangeRep(effectiveAncestor);
			String uuid = EObjectChangeMapping.getUUID(repA, repL, repR, true);
			
			changeMapping.add(effectiveAncestor, uuid, null);
			changeMapping.add(effectiveLeft, uuid, null);
			changeMapping.add(effectiveRight, uuid, null);
			
			if (effectiveAncestor != null) {
				JSONTreeNode node = new JSONTreeNode("match", "[" + effectiveAncestor.eClass().getInstanceClass().getSimpleName()+ "] " + getLabelName(effectiveAncestor));
				node.setUuid(uuid);
				node.setIcon("fas fa-stream");
				return node;
			}
			if (effectiveLeft != null) {
				JSONTreeNode node = new JSONTreeNode("match", "[" + effectiveLeft.eClass().getInstanceClass().getSimpleName()+ "] " + getLabelName(effectiveLeft));
				changeMapping.add(effectiveAncestor, uuid, null);
				node.setUuid(uuid);
				node.setIcon("fas fa-stream");
				return node;
			}
			if (effectiveRight != null) {
				JSONTreeNode node = new JSONTreeNode("match", "[" + effectiveRight.eClass().getInstanceClass().getSimpleName()+ "] " + getLabelName(effectiveRight));
				changeMapping.add(effectiveAncestor, uuid, null);
				node.setUuid(uuid);
				node.setIcon("fas fa-stream");
				return node;
			}
		}
		return null;
	}
	
	
	private static JSONTreeNode diffToJSONTree(Diff diff, EObjectChangeMapping changeMapping) {
		if (diff instanceof ReferenceChange) {	
			ReferenceChange change = (ReferenceChange) diff;
			
			EObjectChangeMapping.ChangeRepresenation rep = changeMapping.getChangeRep(change.getValue());
			String uuid = EObjectChangeMapping.getUUID(rep, true);
			changeMapping.add(change.getValue(), uuid, diff.getKind());

			JSONTreeNode node = new JSONTreeNode("diff", "[" + change.getValue().eClass().getInstanceClass().getSimpleName() + "] "
					+ getLabelName(change.getValue()) + " [" + change.getReference().getName() + " " + differenceKindToString(diff.getKind()) + "]");
			node.setUuid(uuid);
			node.setIcon(differenceKindToIcon(diff.getKind()));
			return node;
			
		} else if (diff instanceof AttributeChange) {
			AttributeChange change = (AttributeChange) diff;
			
			EObject effectiveAncestor = diff.getMatch().getOrigin();
			EObject effectiveLeft = diff.getMatch().getLeft();
			EObject effectiveRight = diff.getMatch().getRight();
			
			EObjectChangeMapping.ChangeRepresenation repA = changeMapping.getChangeRep(effectiveAncestor);
			EObjectChangeMapping.ChangeRepresenation repL = changeMapping.getChangeRep(effectiveAncestor);
			EObjectChangeMapping.ChangeRepresenation repR = changeMapping.getChangeRep(effectiveAncestor);
			String uuid = EObjectChangeMapping.getUUID(repA, repL, repR, true);
			
			changeMapping.add(change.getAttribute(), effectiveAncestor, uuid, diff.getKind());
			changeMapping.add(change.getAttribute(), effectiveLeft, uuid, diff.getKind());
			changeMapping.add(change.getAttribute(), effectiveRight, uuid, diff.getKind());

			JSONTreeNode node = new JSONTreeNode("diff", change.getValue() + " [" + change.getAttribute().getName() + " " + differenceKindToString(diff.getKind()) + "]");
			node.setUuid(uuid);
			node.setIcon(differenceKindToIcon(diff.getKind()));
			return node;
			
		} else if (diff instanceof FeatureMapChange) {
			FeatureMapChange change = (FeatureMapChange) diff;
			System.err.println("FeatureMapChange");
			throw new RuntimeException("not implemented");

			//return new JSONTreeNode("diff", " [" + change.getAttribute().getName() + " " + differenceKindToString(diff.getKind()) + "]");
		}
		return null;
	}
	
	
	/**
	 * Converts an EObject into a tree containing all children and attributes
	 * @param eObject: the root object
	 * @return JSONTreeNode: a JSON representation of the tree structure
	 */
	public static JSONTreeNode objectToJSONTree(EObject eObject, EObjectChangeMapping changeMapping) {
		JSONTreeNode treeNode = new JSONTreeNode("node", getLabelName(eObject));
		EObjectChangeMapping.ChangeRepresenation rep = changeMapping.getChangeRep(eObject);
		treeNode.setIcon("far fa-circle gray");
		if (rep != null) {
			treeNode.setUuid(rep.getUuid());
			treeNode.setColor(differenceKindToColor(rep.getKind()));
		}

		// attributes
		EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();
		for (EAttribute eAttribute : eAllAttributes) {
			EObjectChangeMapping.ChangeRepresenation attributeRep = changeMapping.getChangeRep(eAttribute, eObject);
			
			String value = "";
			EStructuralFeature feature = eObject.eClass().getEStructuralFeature(eAttribute.getName());
			if (feature != null) {
				Object valueObject = eObject.eGet(feature);
				value = String.valueOf(valueObject);
			}
			JSONTreeNode attribute = new JSONTreeNode("attribute", eAttribute.getName() + " = " + value);
			attribute.setIcon("fas fa-tag gray");
			if (attributeRep != null) {
				attribute.setUuid(attributeRep.getUuid());
				attribute.setColor(differenceKindToColor(attributeRep.getKind()));
			}
			treeNode.addChild(attribute);
		}
		
		// children
		List<EObject> children = eObject.eContents();
		for (EObject child : children) {
			treeNode.addChild(objectToJSONTree(child, changeMapping));
		}
		
		// connections
		// icon fas fa-arrows-alt-h gray
		
		return treeNode;
	}
	
	
	private static String differenceKindToString(DifferenceKind kind) {
		if (kind == DifferenceKind.ADD) {
			return "added";
		} else if (kind == DifferenceKind.DELETE) {
			return "deleted";
		} else if (kind == DifferenceKind.CHANGE) {
			return "changed";
		} else if (kind == DifferenceKind.MOVE) {
			return "moved";
		}
		return "null";
	}
	
	private static String differenceKindToColor(DifferenceKind kind) {
		if (kind == DifferenceKind.ADD) {
			return "green";
		} else if (kind == DifferenceKind.DELETE) {
			return "red";
		} else if (kind == DifferenceKind.CHANGE) {
			return "yellow";
		} else if (kind == DifferenceKind.MOVE) {
			return "yellow";
		}
		return "";
	}
	
	private static String differenceKindToIcon(DifferenceKind kind) {
		if (kind == DifferenceKind.ADD) {
			return "fas fa-plus-circle green";
		} else if (kind == DifferenceKind.DELETE) {
			return "fas fa-minus-circle red";
		} else if (kind == DifferenceKind.CHANGE) {
			return "fas fa-pen yellow";
		} else if (kind == DifferenceKind.MOVE) {
			return "fas fa-exchange-alt yellow";
		}
		return "";
	}
	
	public static String getLabelName(EObject object) {
		String ret = null;
		if (object == null) {
			ret = "<null>"; //$NON-NLS-1$
		} else {
			EObject eObject = object;
			EClass eClass = eObject.eClass();
			ret = eClass.getName();

			EStructuralFeature feature = getLabelFeature(eClass);
			if (feature != null) {
				Object value = eObject.eGet(feature);
				if (value != null) {
					ret += " " + value.toString(); //$NON-NLS-1$
				}
			}
		}
		return ret;
	}
	
	private static EStructuralFeature getLabelFeature(EClass eClass) {
		if (eClass == EcorePackage.Literals.ENAMED_ELEMENT) {
			return EcorePackage.Literals.ENAMED_ELEMENT__NAME;
		}

		EAttribute result = null;
		for (EAttribute eAttribute : eClass.getEAllAttributes()) {
			if (!eAttribute.isMany() && eAttribute.getEType().getInstanceClass() != FeatureMap.Entry.class) {
				if ("name".equalsIgnoreCase(eAttribute.getName())) { //$NON-NLS-1$
					result = eAttribute;
					break;
				} else if (result == null) {
					result = eAttribute;
				} else if (eAttribute.getEAttributeType().getInstanceClass() == String.class
						&& result.getEAttributeType().getInstanceClass() != String.class) {
					result = eAttribute;
				}
			}
		}
		return result;
	}
}
