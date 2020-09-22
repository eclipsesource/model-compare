package org.emfcloud.compare.EMF_Compare_Ui;

import java.util.ArrayList;
import java.util.Collection;
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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.emfcloud.compare.model_comparison.UUID_Provider;

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
			throw new RuntimeException("MatchResource not implemented");
		} else if (eObject instanceof Equivalence) {
			// probably not important
			//System.out.println("Equivalence");
			//throw new RuntimeException("not implemented");
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
			
			String uuid;
			String uuidLeft = null;
			String uuidRight = null;
			if (effectiveLeft != null) {
				uuid = UUID_Provider.getUUID(effectiveLeft);
				uuidLeft = uuid;
				if (effectiveRight != null) {
					uuidRight = UUID_Provider.getUUID(effectiveRight);
					changeMapping.addConnection(uuidLeft, uuidRight);
				}
			} else {
				if (effectiveRight != null) {
					uuid = UUID_Provider.getUUID(effectiveRight);
				} else {
					if (effectiveAncestor != null) {
						uuid = UUID_Provider.getUUID(effectiveAncestor);
					} else {
						uuid = UUID.randomUUID().toString();
					}
				}
			}
			
			for (EObject eObject : new EObject[] {effectiveLeft, effectiveRight, effectiveAncestor}) {
				if (eObject != null) {
					JSONTreeNode node = new JSONTreeNode("match", getLabelName(eObject));
					node.setUuid(uuid);
					node.setIcon("fas fa-stream");
					return node;
				}
			}
		}
		return null;
	}
	
	private static JSONTreeNode diffToJSONTree(Diff diff, EObjectChangeMapping changeMapping) {
		if (diff instanceof ReferenceChange) {	
			ReferenceChange change = (ReferenceChange) diff;
			
			//EObject effectiveAncestor = change.getMatch().getOrigin();
			EObject effectiveLeft = change.getMatch().getLeft();
			EObject effectiveRight = change.getMatch().getRight();
			
			//check if it was a reference or a real node change TODO: has 'node' always featureId = 0 ?
			String uuidLeft = null;
			String uuidRight = null;
			
			
			/*
			String uuidLeft = null;
			String uuidRight = null;
			if (change.getMatch() instanceof MatchSpec) {
				MatchSpec spec = (MatchSpec) change.getMatch();
				for (Diff difference : spec.getDifferences()) {
					if (difference instanceof ReferenceChange) {
						if (difference.getSource().equals(DifferenceSource.LEFT)) {
							ReferenceChange leftChange = (ReferenceChange) difference;
							if (effectiveLeft.eContents().contains(leftChange.getValue())) {
								uuidLeft = UUID_Provider.getUUID(leftChange.getValue());
							} else {
								uuidLeft = UUID_Provider.getUUID(effectiveLeft, leftChange.getReference(), leftChange.getValue());
							}
						} else if (difference.getSource().equals(DifferenceSource.RIGHT)) {
							ReferenceChange righChange = (ReferenceChange) difference;
							if (effectiveLeft.eContents().contains(righChange.getValue())) {
								uuidRight= UUID_Provider.getUUID(righChange.getValue());
							} else {
								uuidRight = UUID_Provider.getUUID(effectiveRight, righChange.getReference(), righChange.getValue());
							}
						}
					}
				}
			}
			*/
			
			if (effectiveLeft != null) {
				if (effectiveLeft.eContents().contains(change.getValue())) {
					uuidLeft = UUID_Provider.getUUID(change.getValue());
				} else {
					uuidLeft = UUID_Provider.getUUID(effectiveLeft, change.getReference(), change.getValue());
				}
			}
			if (effectiveRight != null) {
				if (effectiveRight.eContents().contains(change.getValue())) {
					uuidRight = UUID_Provider.getUUID(change.getValue());
				} else {
					uuidRight = UUID_Provider.getUUID(effectiveRight, change.getReference(), change.getValue());
				}
			}
						
			/*
			if (change.getReference().getFeatureID() == 0) {
				uuid = UUID_Provider.getUUID(change.getValue());
			} else {
				if (effectiveLeft != null) {
					uuid = UUID_Provider.getUUID(effectiveLeft, change.getReference(), change.getValue());
					uuidLeft = uuid;
					if (effectiveRight != null) {
						// TODO: what happend if move was from getFeatureID() == 0 to getFeatureID() != 0
						uuidRight = UUID_Provider.getUUID(effectiveRight, change.getReference(), change.getValue());
						changeMapping.addConnection(uuidLeft, uuidRight);
					}
				} else {
					if (effectiveRight != null) {
						uuid = UUID_Provider.getUUID(effectiveRight, change.getReference(), change.getValue());
						uuidRight = uuid;
					} else {
						if (effectiveAncestor != null) {
							uuid = UUID_Provider.getUUID(effectiveAncestor, change.getReference(), change.getValue());
						} else {
							uuid = UUID.randomUUID().toString();
						}
					}
				}
			}
			*/
			
			if (uuidLeft != null && !uuidLeft.equals(uuidRight)) {
				changeMapping.addConnection(uuidLeft, uuidRight);
			}
			changeMapping.addDifference(uuidLeft, change.getKind());
			changeMapping.addDifference(uuidRight, change.getKind());

			String uuid = (uuidLeft != null) ? uuidLeft : uuidRight;
			
			JSONTreeNode node = new JSONTreeNode("diff", getLabelName(change.getValue()) + " [" + change.getReference().getName() + " " + differenceKindToString(diff.getKind()) + "]");
			node.setUuid(uuid);
			node.setIcon(differenceKindToIcon(diff.getKind()));

			return node;
			
		} else if (diff instanceof AttributeChange) {
			AttributeChange change = (AttributeChange) diff;
			
			EObject effectiveAncestor = diff.getMatch().getOrigin();
			EObject effectiveLeft = diff.getMatch().getLeft();
			EObject effectiveRight = diff.getMatch().getRight();
			
			String uuid;
			String uuidLeft = null;
			String uuidRight = null;
			if (effectiveLeft != null) {
				uuid = UUID_Provider.getUUID(effectiveLeft, change.getAttribute());
				uuidLeft = uuid;
				if (effectiveRight != null) {
					uuidRight = UUID_Provider.getUUID(effectiveRight, change.getAttribute());
					changeMapping.addConnection(uuidLeft, uuidRight);
				}
			} else {
				if (effectiveRight != null) {
					uuid = UUID_Provider.getUUID(effectiveRight, change.getAttribute());
				} else {
					if (effectiveAncestor != null) {
						uuid = UUID_Provider.getUUID(effectiveAncestor, change.getAttribute());
					} else {
						uuid = UUID.randomUUID().toString();
					}
				}
			}
			
			changeMapping.addDifference(uuidLeft, change.getKind());
			changeMapping.addDifference(uuidRight, change.getKind());
			
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
		treeNode.setIcon("far fa-circle gray");
		treeNode.setUuid(UUID_Provider.getUUID(eObject));
		DifferenceKind kind = changeMapping.getDifferenceKind(treeNode.getUuid());
		if (kind != null) {
			treeNode.setColor(differenceKindToColor(kind));
		}

		// attributes
		EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();
		for (EAttribute eAttribute : eAllAttributes) {			
			String value = "";
			EStructuralFeature feature = eObject.eClass().getEStructuralFeature(eAttribute.getName());
			if (feature != null) {
				Object valueObject = eObject.eGet(feature);
				value = String.valueOf(valueObject);
			}
			JSONTreeNode attribute = new JSONTreeNode("attribute", eAttribute.getName() + " = " + value);
			attribute.setIcon("fas fa-tag gray");
			attribute.setUuid(UUID_Provider.getUUID(eObject, eAttribute));
			DifferenceKind kindAttribute = changeMapping.getDifferenceKind(attribute.getUuid());
			if (kindAttribute != null) {
				attribute.setColor(differenceKindToColor(kindAttribute));
			}
			treeNode.addChild(attribute);
		}
		
		// children
		List<EObject> children = eObject.eContents();
		for (EObject child : children) {
			treeNode.addChild(objectToJSONTree(child, changeMapping));
		}
		
		// references
		List<EReference> references = eObject.eClass().getEReferences();
		for (EReference reference : references) {			
			JSONTreeNode referenceContainer = new JSONTreeNode("reference", reference.getName());
			referenceContainer.setIcon("fas fa-arrows-alt-h gray");
			
			EStructuralFeature feature = eObject.eClass().getEStructuralFeature(reference.getName());
			if (feature != null) {
				Object value = eObject.eGet(feature);
				
				if (value instanceof EObject) {
					value = List.of(value);
				}
				
				if (value instanceof Collection<?>) {
					for (Object obj : (Collection<?>) value) {
						if (obj instanceof EObject) {
							EObject ref = (EObject) obj;
							if (!children.contains(ref)) {
								JSONTreeNode reference_child = new JSONTreeNode("reference", getLabelName(ref));
								reference_child.setIcon("fas fa-long-arrow-alt-right gray");
								reference_child.setUuid(UUID_Provider.getUUID(eObject, reference, ref));
								DifferenceKind kindRef = changeMapping.getDifferenceKind(reference_child.getUuid());
								if (kindRef != null) {
									reference_child.setColor(differenceKindToColor(kindRef));
								}								
								referenceContainer.addChild(reference_child);
							}
						}
					}
				} else {
					System.err.print("Unrecognized reference feature: " + eObject.eGet(feature));
				}
			}
			if (referenceContainer.getChildrenCount() > 0) {
				treeNode.addChild(referenceContainer);
			}
		}
		// connections
		// icon 
		
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
	
	public static String differenceKindToColor(DifferenceKind kind) {
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
