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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * Creates the overview or model JSON trees for the tree comparison view.
 */
public class JSONTreeCreator {
	
	protected EObjectChangeMapping changeMapping;
	
	public JSONTreeCreator() {
		this(new EObjectChangeMapping());
	}
	
	public JSONTreeCreator(EObjectChangeMapping changeMapping) {
		super();
		this.changeMapping = changeMapping;
	}
	
	public JSONTreeNode createOverview(TreeNode treeNode) {
		EObject data = treeNode.getData();
		
		JSONTreeNode jsonNode = getOverview(data);

		for (TreeNode node : treeNode.getChildren()) {
			jsonNode.addChild(createOverview(node));
		}
		
		return LanguageSpecificCustomizer.customizeOverview(jsonNode);
	}
	
	protected JSONTreeNode getOverview(EObject eObject) {
		if (eObject instanceof Match) {
			Match match = (Match)eObject;
			return matchToJSONTree(match);
			
		} else if (eObject instanceof Diff) {
			Diff diff = (Diff)eObject;
			return diffToJSONTree(diff);
			
		} else if (eObject instanceof MatchResource) {
			System.out.println("MatchResource");
			throw new RuntimeException("MatchResource not implemented");
		} else if (eObject instanceof Equivalence) {
			throw new RuntimeException("Equivalence not implemented");
		} else if (eObject instanceof Conflict) {
			throw new RuntimeException("Conflict not implemented");
		}
		
		return null;
	}
		
	protected JSONTreeNode matchToJSONTree(Match match) {
		EObject effectiveAncestor = match.getOrigin();
		EObject effectiveLeft = match.getLeft();
		EObject effectiveRight = match.getRight();
		
		if (effectiveAncestor == null && effectiveLeft == null && effectiveRight == null) {
			EObject eContainer = match.eContainer();
			if (eContainer instanceof Match) {
				return getOverview(eContainer);
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
			
			this.changeMapping.addMatch(effectiveLeft, effectiveRight, effectiveAncestor);
			
			// determine UUID of tree node
			if (uuidOrigin != null) {
				uuid = uuidOrigin;
				 
				if (uuidLeft != null) {
					this.changeMapping.addConnection(uuidOrigin, uuidLeft);
					if (uuidRight != null) {
						this.changeMapping.addConnection(uuidLeft, uuidRight);
					}
				} else {
					if (uuidRight != null) {
						this.changeMapping.addConnection(uuidOrigin, uuidRight);
					}
				}
			} else {
				if (effectiveLeft != null) {
					uuid = uuidLeft;
					if (uuidRight != null) {
						this.changeMapping.addConnection(uuidLeft, uuidRight);
					}
				} else {
					if (uuidRight != null) {
						uuid = uuidRight;
					} else {
						uuid = UUID.randomUUID().toString();
					}
				}
			}
			
			// create tree node
			for (EObject eObject : new EObject[] {effectiveLeft, effectiveRight, effectiveAncestor}) {
				if (eObject != null) {
					JSONTreeNode node = new JSONTreeNode("match", getLabelName(eObject));
					node.setUuid(uuid);
					node.setIcon("fas fa-stream");
					node.setTypeMatch();
					return LanguageSpecificCustomizer.customizeMatchNode(node, match);
				}
			}
		}
		return null;
	}
	
	public JSONTreeNode diffToJSONTree(Diff diff) {
		if (diff instanceof ReferenceChange) {
			ReferenceChange change = (ReferenceChange) diff;
			return referenceChangeToJSONTree(change);
			
		} else if (diff instanceof AttributeChange) {
			AttributeChange change = (AttributeChange) diff;
			return attributeChangeToJSONTree(change);
			
		} else if (diff instanceof FeatureMapChange) {
			throw new RuntimeException("FeatureMapChange not implemented");
		}
		return null;
	}

	protected JSONTreeNode referenceChangeToJSONTree(ReferenceChange change) {
		EObject effectiveLeft = change.getMatch().getLeft();
		EObject effectiveRight = change.getMatch().getRight();
		
		String uuid = null;
		String uuidLeft = null;
		String uuidLeftReference = null;
		String uuidRight = null;
		String uuidRightReference = null;
		
		EObject value = null;
		EObject valueLeft = null;
		EObject valueRight = null;
		
		// get target EObject (value) using recorded matches
		Map<String, EObject> matchMap = this.changeMapping.getMatch(change.getValue());
		if (matchMap != null) {
			valueLeft = matchMap.get(EObjectChangeMapping.SOURCE_LEFT);
			valueRight = matchMap.get(EObjectChangeMapping.SOURCE_RIGHT);
			if (change.getSource().equals(DifferenceSource.LEFT)) {
				value = (change.getKind().equals(DifferenceKind.DELETE)) ? valueRight : valueLeft;
			} else if (change.getSource().equals(DifferenceSource.RIGHT)) {
				value = (change.getKind().equals(DifferenceKind.DELETE)) ? valueLeft : valueRight;
			}
		}
		if (value == null) {
			value = change.getValue();
		}		
		
		// determine UUID and save differences and connections
		if (change.getKind().equals(DifferenceKind.MOVE)) {
			// handling moves with containments
			
			if (valueLeft != null) {
				uuidLeft = UUID_Provider.getUUID(valueLeft);
				uuidLeftReference = UUID_Provider.getUUID(valueLeft.eContainer(), change.getReference(), valueLeft);
			}
			if (valueRight != null) {
				uuidRight = UUID_Provider.getUUID(valueRight);
				uuidRightReference = UUID_Provider.getUUID(valueRight.eContainer(), change.getReference(), valueRight);
			}
			
			this.changeMapping.addDifference(uuidLeft, change.getKind());
			this.changeMapping.addDifference(uuidLeftReference, change.getKind());
			this.changeMapping.addDifference(uuidRight, change.getKind());
			this.changeMapping.addDifference(uuidRightReference, change.getKind());
			
			this.changeMapping.addConnection(uuidLeft, uuidRight);
			this.changeMapping.addConnection(uuidLeftReference, uuidRightReference);
			uuid = (uuidLeft != null) ? uuidLeft : uuidRight;
			if (uuid == null) {
				uuid = UUID.randomUUID().toString();
			}
			
		} else {
			// handling all other reference changes
			
			if (effectiveLeft != null) {
				EObject leftValue = value;
				if (matchMap != null && matchMap.get(EObjectChangeMapping.SOURCE_LEFT) != null) {
					leftValue = matchMap.get(EObjectChangeMapping.SOURCE_LEFT);
				}
				
				if (effectiveLeft.eContents().contains(leftValue)) {
					uuidLeft = UUID_Provider.getUUID(leftValue);
					uuidLeftReference = UUID_Provider.getUUID(effectiveLeft, change.getReference(), leftValue);
				} else {
					uuidLeft = UUID_Provider.getUUID(effectiveLeft, change.getReference(), leftValue);
				}
			}
			if (effectiveRight != null) {
				EObject rightValue = value;
				if (matchMap != null && matchMap.get(EObjectChangeMapping.SOURCE_RIGHT) != null) {
					rightValue = matchMap.get(EObjectChangeMapping.SOURCE_RIGHT);
				}
				
				if (effectiveRight.eContents().contains(rightValue)) {
					uuidRight = UUID_Provider.getUUID(rightValue);
					uuidRightReference = UUID_Provider.getUUID(effectiveRight, change.getReference(), rightValue);
				} else {
					uuidRight = UUID_Provider.getUUID(effectiveRight, change.getReference(), rightValue);
				}
			}
			
			if (uuidLeft != null && !uuidLeft.equals(uuidRight)) {
				this.changeMapping.addConnection(uuidLeft, uuidRight);
			} 
			
			if (uuidLeftReference != null && !uuidLeftReference.equals(uuidRightReference)) {
				this.changeMapping.addConnection(uuidLeftReference, uuidRightReference);
			}
			
			this.changeMapping.addDifference(uuidLeftReference, change.getKind());
			this.changeMapping.addDifference(uuidRightReference, change.getKind());
			this.changeMapping.addDifference(uuidLeft, change.getKind());
			this.changeMapping.addDifference(uuidRight, change.getKind());
			
			uuid = (uuidLeft != null) ? uuidLeft : uuidRight;
		}
		
		// create tree node
		JSONTreeNode node = new JSONTreeNode("diff", "<" + differenceSourceToString(change.getSource()) + "> "
				+ getLabelName(change.getValue()) 
				+ " [" + change.getReference().getName() + " " + differenceKindToString(change.getKind()) + "]");
		node.setUuid(uuid);
		node.setIcon(differenceKindToIcon(change.getKind()));
		
		if (change.getConflict() != null) {
			node.setTypeConflict();
		} else {
			node.setTypeDiff();
		}
		
		return LanguageSpecificCustomizer.customizeDiffNode(node, change);
	}
	
	protected JSONTreeNode attributeChangeToJSONTree(AttributeChange change) {
		EObject effectiveAncestor = change.getMatch().getOrigin();
		EObject effectiveLeft = change.getMatch().getLeft();
		EObject effectiveRight = change.getMatch().getRight();
		
		String uuid;
		String uuidLeft = null;
		String uuidRight = null;
		
		// determine UUID and save connections
		if (effectiveLeft != null) {
			uuid = UUID_Provider.getUUID(effectiveLeft, change.getAttribute());
			uuidLeft = uuid;
			if (effectiveRight != null) {
				uuidRight = UUID_Provider.getUUID(effectiveRight, change.getAttribute());
				this.changeMapping.addConnection(uuidLeft, uuidRight);
			} else if (effectiveAncestor != null) {
				this.changeMapping.addConnection(uuidLeft, UUID_Provider.getUUID(effectiveAncestor, change.getAttribute()));
			}
		} else {
			if (effectiveRight != null) {
				uuid = UUID_Provider.getUUID(effectiveRight, change.getAttribute());
				uuidRight = uuid;
				if (effectiveAncestor != null) {
					this.changeMapping.addConnection(UUID_Provider.getUUID(effectiveAncestor, change.getAttribute()), uuid);
				}
			} else {
				if (effectiveAncestor != null) {
					uuid = UUID_Provider.getUUID(effectiveAncestor, change.getAttribute());
				} else {
					uuid = UUID.randomUUID().toString();
				}
			}
		}
		
		this.changeMapping.addDifference(uuidLeft, change.getKind());
		this.changeMapping.addDifference(uuidRight, change.getKind());
		
		// create tree node
		JSONTreeNode node = new JSONTreeNode("diff",  "<" + differenceSourceToString(change.getSource()) + "> "
				+ change.getValue() 
				+ " [" + change.getAttribute().getName() + " " + differenceKindToString(change.getKind()) + "]");
		node.setUuid(uuid);
		node.setIcon(differenceKindToIcon(change.getKind()));
		
		if (change.getConflict() != null) {
			node.setTypeConflict();
		} else {
			node.setTypeDiff();
		}
		
		return LanguageSpecificCustomizer.customizeDiffNode(node, change);
	}
	
	public TreeNodeCollection createModelTree(ResourceSet resourceSet) {
		EList<Resource> resources = resourceSet.getResources();
		TreeNodeCollection tree = new TreeNodeCollection();
		
		for (Resource resource : resources) {
			EList<EObject> eObjects = resource.getContents();
			
			for (EObject eObject : eObjects) {
				JSONTreeNode node = objectToJSONTree(eObject);
				tree.addChild(LanguageSpecificCustomizer.customizeModel(node));
			}
		}
		
		return tree;
	}
	
	protected JSONTreeNode objectToJSONTree(EObject eObject) {
		JSONTreeNode treeNode = new JSONTreeNode("node", getLabelName(eObject));
		treeNode.setIcon("far fa-circle gray");
		treeNode.setUuid(UUID_Provider.getUUID(eObject));
		DifferenceKind kind = this.changeMapping.getDifferenceKind(treeNode.getUuid());
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
			DifferenceKind kindAttribute = this.changeMapping.getDifferenceKind(attribute.getUuid());
			if (kindAttribute != null) {
				attribute.setColor(differenceKindToColor(kindAttribute));
			}
			attribute = LanguageSpecificCustomizer.customizeAttribute(attribute, eObject, eAttribute);
			treeNode.addChild(attribute);
		}
		
		// children
		List<EObject> children = eObject.eContents();
		for (EObject child : children) {
			treeNode.addChild(objectToJSONTree(child));
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
					List<EObject> list = new ArrayList<>();
					list.add((EObject) value);
					value = list;
				}
				
				if (value instanceof Collection<?>) {
					for (Object obj : (Collection<?>) value) {
						if (obj instanceof EObject) {
							EObject ref = (EObject) obj;
								if (!children.contains(ref)) {
								JSONTreeNode reference_child = new JSONTreeNode("reference", getLabelName(ref));
								reference_child.setIcon("fas fa-long-arrow-alt-right gray");
								reference_child.setUuid(UUID_Provider.getUUID(eObject, reference, ref));
								DifferenceKind kindRef = this.changeMapping.getDifferenceKind(reference_child.getUuid());
								if (kindRef != null) {
									reference_child.setColor(differenceKindToColor(kindRef));
								}
								reference_child = LanguageSpecificCustomizer.customizeReference(reference_child, eObject, ref, reference);
								referenceContainer.addChild(reference_child);
							}
						}
					}
				} else {
					System.err.print("Unrecognized reference feature: " + eObject.eGet(feature));
				}
			}
			
			// add non empty containers
			if (referenceContainer.getChildrenCount() > 0) {
				treeNode.addChild(LanguageSpecificCustomizer.customizeReferenceContainer(referenceContainer, eObject, reference));
			}
		}
		
		return LanguageSpecificCustomizer.customizeNode(treeNode, eObject);
	}
	
	protected String differenceKindToString(DifferenceKind kind) {
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
	
	protected String differenceKindToIcon(DifferenceKind kind) {
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
	
	protected String differenceSourceToString(DifferenceSource source) {
		if (source == DifferenceSource.LEFT) {
			return EObjectChangeMapping.SOURCE_LEFT;
		} else if (source == DifferenceSource.RIGHT) {
			return EObjectChangeMapping.SOURCE_RIGHT;
		} 
		return "";
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
	
	public EObjectChangeMapping getChangeMapping() {
		return this.changeMapping;
	}
	
	public static String getLabelName(EObject object) {
		String ret = null;
		if (object == null) {
			ret = "<null>";
		} else {
			EObject eObject = object;
			EClass eClass = eObject.eClass();
			ret = eClass.getName();

			EStructuralFeature feature = getLabelFeature(eClass);
			if (feature != null) {
				Object value = eObject.eGet(feature);
				if (value != null) {
					ret += " " + value.toString();
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
				if ("name".equalsIgnoreCase(eAttribute.getName())) {
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
