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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

		return jsonNode;
	}

	protected JSONTreeNode getOverview(EObject eObject) {
		if (eObject instanceof Match) {
			Match match = (Match) eObject;
			return matchToJSONTree(match);

		} else if (eObject instanceof Diff) {
			Diff diff = (Diff) eObject;
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
		EObject baseObject = match.getOrigin();
		EObject sourceObject = match.getLeft();
		EObject targetObject = match.getRight();

		if (baseObject == null && sourceObject == null && targetObject == null) {
			EObject eContainer = match.eContainer();
			if (eContainer instanceof Match) {
				return getOverview(eContainer);
			}

		} else {
			String uuid;
			String baseUUID = null;
			String sourceUUID = null;
			String targetUUID = null;

			if (baseObject != null) {
				baseUUID = UUID_Provider.getUUID(baseObject);
			}
			if (sourceObject != null) {
				sourceUUID = UUID_Provider.getUUID(sourceObject);
			}
			if (targetObject != null) {
				targetUUID = UUID_Provider.getUUID(targetObject);
			}

			this.changeMapping.addMatch(sourceObject, targetObject, baseObject);

			// determine UUID of tree node
			if (baseUUID != null) {
				uuid = baseUUID;

				if (sourceUUID != null) {
					this.changeMapping.addConnection(baseUUID, sourceUUID);
					if (targetUUID != null) {
						this.changeMapping.addConnection(sourceUUID, targetUUID);
					}
				} else {
					if (targetUUID != null) {
						this.changeMapping.addConnection(baseUUID, targetUUID);
					}
				}
			} else {
				if (sourceObject != null) {
					uuid = sourceUUID;
					if (targetUUID != null) {
						this.changeMapping.addConnection(sourceUUID, targetUUID);
					}
				} else {
					if (targetUUID != null) {
						uuid = targetUUID;
					} else {
						uuid = "Random";
					}
				}
			}

			// create tree node
			for (EObject eObject : new EObject[] { baseObject, sourceObject, targetObject }) {
				if (eObject != null) {
					JSONTreeNode node = new JSONTreeNode("match", getLabelName(eObject));
					node.setUuid(uuid);
					node.setIcon(IconProvider.getMatchIcon());
					node.setTypeMatch();
					return node;
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
		ReferenceChangeObjectManager manager = new ReferenceChangeObjectManager(change);

		updateChangeMapping(manager);

		this.changeMapping.addDifference(manager.getSource().getReferenceUUID(), change.getKind());
		this.changeMapping.addDifference(manager.getTarget().getReferenceUUID(), change.getKind());
		this.changeMapping.addDifference(manager.getSource().getUuid(), change.getKind());
		this.changeMapping.addDifference(manager.getTarget().getUuid(), change.getKind());

		this.changeMapping.addGraphicalDifference(manager.getSource().getReferenceGraphicalID(), change.getKind());
		this.changeMapping.addGraphicalDifference(manager.getTarget().getReferenceGraphicalID(), change.getKind());
		this.changeMapping.addGraphicalDifference(manager.getSource().getGraphicalId(), change.getKind());
		this.changeMapping.addGraphicalDifference(manager.getTarget().getGraphicalId(), change.getKind());

		boolean isConflict = change.getConflict() != null;

		// create tree node
		JSONTreeNode node = new JSONTreeNode("diff",
				getReferenceDiffString(change, "", getLabelName(manager.getSource().getValue()),
						getLabelName(manager.getTarget().getValue()), isConflict));
		node.setUuid(manager.getUuid());
		node.setIcon(differenceKindToIcon(change.getKind()));

		if (isConflict) {
			node.setTypeConflict();
		} else {
			node.setTypeDiff();
		}

		return node;
	}

	private String getReferenceDiffString(ReferenceChange change, String valueBase, String valueSource,
			String valueTarget, boolean isConflict) {
		if (isConflict) {
			String value = change.getSource() == DifferenceSource.RIGHT ? valueSource : valueTarget;
			return createReferenceConflictString(change, valueBase, value);
		}
		return createReferenceDiffString(change, valueSource, valueTarget);
	}

	private String createReferenceConflictString(ReferenceChange change, String from, String to) {
		String source = change.getSource() == DifferenceSource.RIGHT ? "Source" : "Target";
		return String.format("<%s> [%s] %s => %s", source, change.getReference().getName(), from, to);
	}

	private String createReferenceDiffString(ReferenceChange change, String from, String to) {
		return String.format("[%s] %s => %s", change.getReference().getName(), from, to);
	}

	protected JSONTreeNode attributeChangeToJSONTree(AttributeChange change) {
		AttributeChangeObjectManager manager = new AttributeChangeObjectManager(change);

		updateChangeMapping(manager);

		this.changeMapping.addDifference(manager.getSource().getUuid(), change.getKind());
		this.changeMapping.addDifference(manager.getTarget().getUuid(), change.getKind());

		this.changeMapping.addGraphicalDifference(manager.getSource().getGraphicalId(), change.getKind());
		this.changeMapping.addGraphicalDifference(manager.getTarget().getGraphicalId(), change.getKind());

		boolean isConflict = change.getConflict() != null;

		// create tree node
		JSONTreeNode node = new JSONTreeNode("diff", getAttributeDiffString(change, manager.getBase().getValue(),
				manager.getSource().getValue(), manager.getTarget().getValue(), isConflict));
		node.setUuid(manager.getUuid());
		node.setIcon(differenceKindToIcon(change.getKind()));

		if (isConflict) {
			node.setTypeConflict();
		} else {
			node.setTypeDiff();
		}

		return node;
	}

	private void updateChangeMapping(ObjectManager<?> manager) {
		this.changeMapping.addConnection(manager.getUuid(), manager.getSource().getUuid());
		this.changeMapping.addConnection(manager.getUuid(), manager.getTarget().getUuid());
		this.changeMapping.addConnection(manager.getUuid(), manager.getBase().getUuid());
	}

	private String getAttributeDiffString(AttributeChange change, String valueBase, String valueSource,
			String valueTarget, boolean isConflict) {
		if (isConflict) {
			String value = change.getSource() == DifferenceSource.RIGHT ? valueSource : valueTarget;
			return createAttributeConflictString(change, valueBase, value);
		}
		return createAttributeDiffString(change, valueSource, valueTarget);
	}

	private String createAttributeConflictString(AttributeChange change, String from, String to) {
		String source = change.getSource() == DifferenceSource.RIGHT ? "Source" : "Target";
		return String.format("<%s> [%s] %s => %s", source, change.getAttribute().getName(), from, to);
	}

	private String createAttributeDiffString(AttributeChange change, String from, String to) {
		return String.format("[%s] %s => %s", change.getAttribute().getName(), from, to);
	}

	public TreeNodeCollection createModelTree(ResourceSet resourceSet) {
		EList<Resource> resources = resourceSet.getResources();
		TreeNodeCollection tree = new TreeNodeCollection();

		for (Resource resource : resources) {
			EList<EObject> eObjects = resource.getContents();

			for (EObject eObject : eObjects) {
				JSONTreeNode node = objectToJSONTree(eObject);
				tree.addChild(node);
			}
		}

		return tree;
	}

	protected JSONTreeNode objectToJSONTree(EObject eObject) {
		// root element of object
		JSONTreeNode treeNode = new JSONTreeNode("node", getLabelName(eObject));
		treeNode.setIcon(IconProvider.getObjectIcon());
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
			attribute.setIcon(IconProvider.getAttributeIcon());
			attribute.setUuid(UUID_Provider.getUUID(eObject, eAttribute));
			DifferenceKind kindAttribute = this.changeMapping.getDifferenceKind(attribute.getUuid());
			if (kindAttribute != null) {
				attribute.setColor(differenceKindToColor(kindAttribute));
			}
			treeNode.addChild(attribute);
		}

		// children
		List<EObject> children = eObject.eContents();
		for (EObject child : children) {
			treeNode.addChild(objectToJSONTree(child));
		}

		// references
		List<EReference> references = eObject.eClass().getEAllReferences();
		for (EReference reference : references) {
			// source/target entry
			JSONTreeNode referenceContainer = new JSONTreeNode("reference", reference.getName());
			referenceContainer.setIcon(IconProvider.getReferenceIcon());

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
								reference_child.setIcon(IconProvider.getReferenceChildIcon());
								reference_child.setUuid(UUID_Provider.getUUID(eObject, reference, ref));
								DifferenceKind kindRef = this.changeMapping
										.getDifferenceKind(reference_child.getUuid());
								if (kindRef != null) {
									reference_child.setColor(differenceKindToColor(kindRef));
									referenceContainer.setColor(differenceKindToColor(kindRef));
								}
								referenceContainer.addChild(reference_child);
							}
						}
					}
				}
			}

			// add non empty containers
			if (referenceContainer.getChildrenCount() > 0) {
				treeNode.addChild(
						referenceContainer);
			}
		}

		return treeNode;
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
			return IconProvider.getAddIcon();
		} else if (kind == DifferenceKind.DELETE) {
			return IconProvider.getDeleteIcon();
		} else if (kind == DifferenceKind.CHANGE) {
			return IconProvider.getChangeIcon();
		} else if (kind == DifferenceKind.MOVE) {
			return IconProvider.getMoveIcon();
		}
		return "";
	}

	protected String differenceSourceToString(DifferenceSource source) {
		if (source == DifferenceSource.LEFT) {
			return EObjectChangeMapping.SOURCE_SOURCE;
		} else if (source == DifferenceSource.RIGHT) {
			return EObjectChangeMapping.SOURCE_TARGET;
		}
		return "";
	}

	public static String differenceKindToColor(DifferenceKind kind) {
		if (kind == DifferenceKind.ADD) {
			return "added";
		} else if (kind == DifferenceKind.DELETE) {
			return "deleted";
		} else if (kind == DifferenceKind.CHANGE) {
			return "changed";
		} else if (kind == DifferenceKind.MOVE) {
			return "moved";
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
