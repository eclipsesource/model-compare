package org.emfcloud.model_comparison;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
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

public class JSONTreeCreator {
	
	/**
	 * builds the top overview tree which shows the differences and merge conflicts
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
		
	public static JSONTreeNode matchToJSONTree(Match match, EObjectChangeMapping changeMapping) {
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
			String uuidOrigin = null;
			String uuidLeft = null;
			String uuidRight = null;
			
			if (effectiveAncestor != null) {
				uuidOrigin = UUID_Provider.getUUID(effectiveAncestor);
			}
			if (effectiveLeft != null) {
				uuidLeft = UUID_Provider.getUUID(effectiveLeft);
			}
			if (effectiveRight != null) {
				uuidRight = UUID_Provider.getUUID(effectiveRight);
			}
			
			changeMapping.addMatch(effectiveLeft, effectiveRight, effectiveAncestor);
			
			if (uuidOrigin != null) {
				uuid = uuidOrigin;
				 
				if (uuidLeft != null) {
					changeMapping.addConnection(uuidOrigin, uuidLeft);
					if (uuidRight != null) {
						changeMapping.addConnection(uuidLeft, uuidRight);
					}
				} else {
					if (uuidRight != null) {
						changeMapping.addConnection(uuidOrigin, uuidRight);
					}
				}
			} else {
				if (effectiveLeft != null) {
					uuid = uuidLeft;
					if (uuidRight != null) {
						changeMapping.addConnection(uuidLeft, uuidRight);
					}
				} else {
					if (uuidRight != null) {
						uuid = uuidRight;
					} else {
						uuid = UUID.randomUUID().toString();
					}
				}
			}
			
			if (getLabelName(effectiveRight).equals("DataCenter DC-G")) {
				//System.out.println("da");
			}
			
			for (EObject eObject : new EObject[] {effectiveLeft, effectiveRight, effectiveAncestor}) {
				if (eObject != null) {
					JSONTreeNode node = new JSONTreeNode("match", getLabelName(eObject));
					node.setUuid(uuid);
					node.setIcon("fas fa-stream");
					node.setTypeMatch();
					return node;
				}
			}
		}
		return null;
	}
	
	public static JSONTreeNode diffToJSONTree(Diff diff, EObjectChangeMapping changeMapping) {
		if (diff instanceof ReferenceChange) {
			ReferenceChange change = (ReferenceChange) diff;
			
			EObject effectiveAncestor = change.getMatch().getOrigin();
			EObject effectiveLeft = change.getMatch().getLeft();
			EObject effectiveRight = change.getMatch().getRight();
			
			//check if it was a reference or a real node change
			String uuidLeft = null;
			String uuidLeftReference = null;
			String uuidRight = null;
			String uuidRightReference = null;
			String uuidOrigin = null;
			String uuidOriginReference = null;
			
			
			EObject value = null;
			EObject valueLeft = null;
			EObject valueRight = null;
			Map<String, EObject> matchMap = changeMapping.getMatch(change.getValue());
			if (matchMap != null) {
				valueLeft = matchMap.get("left");
				valueRight = matchMap.get("right");
				if (change.getSource().equals(DifferenceSource.LEFT)) {
					value = (change.getKind().equals(DifferenceKind.DELETE)) ? valueRight : valueLeft;
				} else if (change.getSource().equals(DifferenceSource.RIGHT)) {
					value = (change.getKind().equals(DifferenceKind.DELETE)) ? valueLeft : valueRight;
				}
			}

			if (value == null) {
				value = change.getValue();
			}
			
			
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
						
			String uuid = null;
			
			if (change.getKind().equals(DifferenceKind.MOVE)) {
				// always a containment?
				
				if (valueLeft != null) {
					uuidLeft = UUID_Provider.getUUID(valueLeft);
					uuidLeftReference = UUID_Provider.getUUID(valueLeft.eContainer(), change.getReference(), valueLeft);
				}
				if (valueRight != null) {
					uuidRight = UUID_Provider.getUUID(valueRight);
					uuidRightReference = UUID_Provider.getUUID(valueRight.eContainer(), change.getReference(), valueRight);
				}
				
				changeMapping.addDifference(uuidLeft, change.getKind());
				changeMapping.addDifference(uuidLeftReference, change.getKind());
				changeMapping.addDifference(uuidRight, change.getKind());
				changeMapping.addDifference(uuidRightReference, change.getKind());
				
				changeMapping.addConnection(uuidLeft, uuidRight);
				changeMapping.addConnection(uuidLeftReference, uuidRightReference);
				uuid = (uuidLeft != null) ? uuidLeft : uuidRight;
				if (uuid == null) {
					uuid = UUID.randomUUID().toString();
				}
			} else {
				if (effectiveLeft != null) {
					EObject leftValue = value;
					if (matchMap != null && matchMap.get("left") != null) {
						leftValue = matchMap.get("left");
					}
					
					if (effectiveLeft.eContents().contains(leftValue)) {
						uuidLeft = UUID_Provider.getUUID(leftValue);
						uuidLeftReference = UUID_Provider.getUUID(effectiveLeft, change.getReference(), leftValue);
					} else {
						if (!change.getKind().equals(DifferenceKind.CHANGE) && !change.getKind().equals(DifferenceKind.MOVE)) {
							
						}
						
						//uuidLeftReference = UUID_Provider.getUUID(value);
						uuidLeft = UUID_Provider.getUUID(effectiveLeft, change.getReference(), leftValue);
						
					}
				}
				if (effectiveRight != null) {
					EObject rightValue = value;
					if (matchMap != null && matchMap.get("right") != null) {
						rightValue = matchMap.get("right");
					}
					
					if (effectiveRight.eContents().contains(rightValue)) {
						uuidRight = UUID_Provider.getUUID(rightValue);
						uuidRightReference = UUID_Provider.getUUID(effectiveRight, change.getReference(), rightValue);
					} else {
						//uuidRightReference = UUID_Provider.getUUID(value);
						uuidRight = UUID_Provider.getUUID(effectiveRight, change.getReference(), rightValue);
					}
				}
				if (effectiveAncestor != null) {
					if (effectiveAncestor.eContents().contains(value)) {
						uuidOrigin = UUID_Provider.getUUID(value);
						uuidOriginReference = UUID_Provider.getUUID(effectiveAncestor, change.getReference(), value);
					} else {
						uuidOrigin = UUID_Provider.getUUID(effectiveAncestor, change.getReference(), value);
					}
				}
				
				if (uuidLeft != null && !uuidLeft.equals(uuidRight)) {
					changeMapping.addConnection(uuidLeft, uuidRight);
				} 
				
				if (uuidLeftReference != null && !uuidLeftReference.equals(uuidRightReference)) {
					changeMapping.addConnection(uuidLeftReference, uuidRightReference);
				}
				
				changeMapping.addDifference(uuidLeftReference, change.getKind());
				changeMapping.addDifference(uuidRightReference, change.getKind());
				changeMapping.addDifference(uuidLeft, change.getKind());
				changeMapping.addDifference(uuidRight, change.getKind());
				
				uuid = (uuidLeft != null) ? uuidLeft : uuidRight;
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
			

			
			

			
			
			JSONTreeNode node = new JSONTreeNode("diff", "<" + differenceSourceToString(diff.getSource()) + "> "
					+ getLabelName(change.getValue()) 
					+ " [" + change.getReference().getName() + " " + differenceKindToString(diff.getKind()) + "]");
			node.setUuid(uuid);
			node.setIcon(differenceKindToIcon(diff.getKind()));
			
			if (diff.getConflict() != null) {
				node.setTypeConflict();
			} else {
				node.setTypeDiff();
			}
			
			
			if (getLabelName(change.getValue()).equals("DataCenter DC-G")) {
				//System.out.println("da");
			}
			
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
				} else if (effectiveAncestor != null) {
					changeMapping.addConnection(uuidLeft, UUID_Provider.getUUID(effectiveAncestor, change.getAttribute()));
				}
			} else {
				if (effectiveRight != null) {
					uuid = UUID_Provider.getUUID(effectiveRight, change.getAttribute());
					if (effectiveAncestor != null) {
						changeMapping.addConnection(UUID_Provider.getUUID(effectiveAncestor, change.getAttribute()), uuid);
					}
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
			
			JSONTreeNode node = new JSONTreeNode("diff",  "<" + differenceSourceToString(diff.getSource()) + "> "
					+ change.getValue() 
					+ " [" + change.getAttribute().getName() + " " + differenceKindToString(diff.getKind()) + "]");
			node.setUuid(uuid);
			node.setIcon(differenceKindToIcon(diff.getKind()));
			
			if (diff.getConflict() != null) {
				node.setTypeConflict();
			} else {
				node.setTypeDiff();
			}
			
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
	
	private static String differenceSourceToString(DifferenceSource source) {
		if (source == DifferenceSource.LEFT) {
			return "left";
		} else if (source == DifferenceSource.RIGHT) {
			return "right";
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
