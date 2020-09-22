package org.emfcloud.compare.model_comparison;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.emfcloud.compare.EMF_Compare_Ui.DifferenceGroup;
import org.emfcloud.compare.EMF_Compare_Ui.EObjectChangeMapping;
import org.emfcloud.compare.EMF_Compare_Ui.JSONCompareResponse;
import org.emfcloud.compare.EMF_Compare_Ui.JSONTreeNode;
import org.emfcloud.compare.EMF_Compare_Ui.ToJSONHelper;

import EAM_Metamodel.EAM_MetamodelPackage;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.impl.AttributeChangeImpl;
import org.eclipse.emf.compare.internal.spec.AttributeChangeSpec;
import org.eclipse.emf.compare.internal.spec.EObjectUtil;
import org.eclipse.emf.compare.internal.spec.ReferenceChangeSpec;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;


public class Model_Compare {
	
	private static List<String> validArgs;
	private static final String ARG_OPERATION = "-operation";
	private static final String ARG_LEFT = "-left";
	private static final String ARG_RIGHT = "-right";
	private static final String ARG_ORIGIN = "-origin";
	
	private static final String OPERATION_COMPARISON = "comparison";
	private static final String OPERATION_HIGHLIGHT = "highlight";

	static {
		validArgs = new ArrayList<String>();
		validArgs.add(ARG_OPERATION);
		validArgs.add(ARG_LEFT);
		validArgs.add(ARG_RIGHT);
		validArgs.add(ARG_ORIGIN);
	}
	
	private static void printArgs() {
		System.err.println("Valid arguments are");
		for (String string : validArgs) {
			System.err.println("  " + string);
		}
	}

	public static void main(String[] args) {
		args = new String[6];
		args[0] = ARG_OPERATION;
		args[1] = OPERATION_COMPARISON;
		args[2] = ARG_LEFT;
		args[3] = "file:///c%3A/Users/ldkpr/Documents/Studium/Master_Thesis/ws/model-compare/model-comparison-framework/data/diagram.eam";
		args[4] = ARG_RIGHT;
		args[5] = "file:///c%3A/Users/ldkpr/Documents/Studium/Master_Thesis/ws/model-compare/model-comparison-framework/data/diagram_new.eam";
		//args[6] = ARG_ORIGIN;
		//args[7] = "file:///c%3A/Users/ldkpr/Documents/Studium/Master_Thesis/ws/eclipse_ws/model-comparison-framework/data/diagram.eam";
		
		Map<String, String> data = new HashMap<String, String>();
		if (args.length < 2) {
			printArgs();
			return;
		}
		
		String lastArg = null;
		for (String arg : args) {
			if (validArgs.contains(arg.toLowerCase())) {
				if (lastArg != null) {
					throw new RuntimeException("Missing value for argument " + lastArg);
				}
				lastArg = arg.toLowerCase();
				continue;
			}
			if (lastArg != null) {
				data.put(lastArg, arg);
				lastArg = null;
			} else {
				printArgs();
				throw new RuntimeException("Argument " + arg + " not recognized");
			}
		}
		
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.OFF);
		
		if (data.get(ARG_OPERATION).equals(OPERATION_COMPARISON)) {
			compare(data);
		} else if (data.get(ARG_OPERATION).equals(OPERATION_HIGHLIGHT)) {
			highlight(data);
		} else {
			throw new RuntimeException("Unknown operation argument:  " + data.get(ARG_OPERATION));
		}
	}
	
	private static void compare(Map<String, String> data) {
		URI leftURI = getUri(data.get(ARG_LEFT));
		URI rightURI = getUri(data.get(ARG_RIGHT));
		URI originURI = getUri(data.get(ARG_ORIGIN));
		
		if (isValidFile(leftURI) && isValidFile(rightURI)) {
			// creates the resourceSets where we will load the models
			final ResourceSet resourceSetLeft = new ResourceSetImpl();
			final ResourceSet resourceSetRight = new ResourceSetImpl();
			final ResourceSet resourceSetOrigin = new ResourceSetImpl();
			
			// loading resources
			boolean originProvided = isValidFile(originURI);
			load(leftURI, resourceSetLeft);
			load(rightURI, resourceSetRight);
			if (originProvided) {
				load(originURI, resourceSetOrigin);
			}
			
			// setting scope
			IComparisonScope scope = new DefaultComparisonScope(resourceSetLeft, resourceSetRight, (originProvided) ? resourceSetOrigin : null);
			
			// comparing
			Comparison comparison = EMFCompare.builder().build().compare(scope);
			
			EObjectChangeMapping changeMapping = new EObjectChangeMapping();
			
			JSONCompareResponse root = new JSONCompareResponse();
			
			List<TreeNode> list = new DifferenceGroup().buildOverviewTree(comparison);
			for(TreeNode node : list) {
				JSONTreeNode n = getOverview(node, changeMapping);
				root.setOverviewTree(n.toString());
				break;
			}
			
			root.setLeftTree(buildTree(resourceSetLeft, changeMapping).toString());
			root.setRightTree(buildTree(resourceSetRight, changeMapping).toString());
			root.setUuidConnection(changeMapping.getConnectionsString());
			
			// this will be read by the client
			System.out.print(root);
			
			
			// cancel rest of code
			int one = 1;
			if (one == 1) {
				return;
			}

			
			
			
			
			
			
			
			List<Diff> differences = comparison.getDifferences();
			
			System.out.println("Differences:");
			for (Diff diff : differences) {
				System.out.println(diff);
				
				/*
				if (diff instanceof ReferenceChangeSpec) {
					ReferenceChangeSpec change = (ReferenceChangeSpec) diff;
					if (diff.getKind().equals(DifferenceKind.MOVE)) {
						System.out.println(diff.getKind() + ": " + getLabelName(change.getValue()) + " to " + getLabelName(change.getMatch().getLeft()));
					} else if (diff.getKind().equals(DifferenceKind.ADD)) {
						if (change.getMatch().getRight() instanceof EPackage && change.getMatch().getLeft() instanceof EPackage) {
							System.out.println(diff.getKind() + ": " + getLabelName(change.getValue()));
						} else {
							System.out.println(diff.getKind() + ": Edge " + getLabelName(change.getMatch().getLeft()) + " --> " + getLabelName(change.getValue()));
						}
					} else if (diff.getKind().equals(DifferenceKind.CHANGE)) {
						
					}
				
				} else if (diff instanceof AttributeChangeSpec) {
					AttributeChangeSpec change = (AttributeChangeSpec) diff;
					
					EDataType eAttributeType = change.getAttribute().getEAttributeType();
					final String valueString;
					if (eAttributeType.isSerializable()) {
						valueString = EcoreUtil.convertToString(eAttributeType, change.getValue());
					} else {
						valueString = change.getValue().toString();
					}
					System.out.println(diff.getKind() + ": " + change.getAttribute().getEContainingClass().getName() + " " + change.getAttribute().getName() + " to " + valueString);
				} else {
					
					

				}
				*/

			}
			
			//System.out.println("Merging");
			// Let's merge every single diff
			//IMerger.Registry mergerRegistry = new IMerger.RegistryImpl();
			
			//IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
			//IBatchMerger merger = new BatchMerger(mergerRegistry);
			//merger.copyAllLeftToRight(differences, new BasicMonitor());
			
		} else {
			System.out.print(new JSONCompareResponse("Left or Right file missing or not readable!"));
		}
	}
	
	public static EObjectChangeMapping gethighliteMapping(Map<String, String> data) {
		URI leftURI = getUri(data.get(ARG_LEFT));
		URI rightURI = getUri(data.get(ARG_RIGHT));
		
		if (isValidFile(leftURI) && isValidFile(rightURI)) {
			// creates the resourceSets where we will load the models
			final ResourceSet resourceSetLeft = new ResourceSetImpl();
			final ResourceSet resourceSetRight = new ResourceSetImpl();
			
			// loading resources
			load(leftURI, resourceSetLeft);
			load(rightURI, resourceSetRight);

			// setting scope
			IComparisonScope scope = new DefaultComparisonScope(resourceSetLeft, resourceSetRight, null);
			
			// comparing
			Comparison comparison = EMFCompare.builder().build().compare(scope);
			
			EObjectChangeMapping changeMapping = new EObjectChangeMapping();
						
			List<TreeNode> list = new DifferenceGroup().buildOverviewTree(comparison);
			for (TreeNode node : list) {
				getOverview(node, changeMapping);
			}
			
			return changeMapping;
		} else {
			System.err.print("Left or Right file missing or not readable!");
			return new EObjectChangeMapping();
		}
	}
	
	private static void highlight(Map<String, String> data) {
		System.out.print(gethighliteMapping(data).getHighlightString());
	}
	
	private static JSONTreeNode getOverview(TreeNode treeNode, EObjectChangeMapping changeMapping) {
		EObject data = treeNode.getData();
		
		JSONTreeNode jsonNode = ToJSONHelper.getOverview(data, changeMapping);

		for(TreeNode node : treeNode.getChildren()) {
			jsonNode.addChild(getOverview(node, changeMapping));
		}
		
		return jsonNode;
	}
	
	private static JSONTreeNode buildTree(ResourceSet resourceSet, EObjectChangeMapping changeMapping) {
		EList<Resource> resources = resourceSet.getResources();
		for (Resource resource : resources) {
			EList<EObject> eObjects = resource.getContents();
			
			for (EObject eObject : eObjects) {
				JSONTreeNode node = ToJSONHelper.objectToJSONTree(eObject, changeMapping);
				return node;
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	private static void load(URI uri, ResourceSet resourceSet) {
	  //resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
	  //resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("eam", new EcoreResourceFactoryImpl());
	  resourceSet.getPackageRegistry().put(EAM_MetamodelPackage.eINSTANCE.getNsURI(), EAM_MetamodelPackage.eINSTANCE);

	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	  
	  // Resource will be loaded within the resource set
	  resourceSet.getResource(uri, true);
	}
	
	private static String getLabelName(EObject object) {
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
	
	private static URI getUri(String path) {
		if (path == null) return null;
		
		if (path.startsWith("file:/")) {
			try {
				return URI.createURI(java.net.URLDecoder.decode(path, StandardCharsets.UTF_8.name()));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Failed to decode URI");
			}
		}
		return URI.createFileURI(path);
	}
	
	private static boolean isValidFile(URI uri) {
		if (uri == null) {
			return false;
		}
		try {
			return new File(java.net.URLDecoder.decode(uri.path(), StandardCharsets.UTF_8.name())).canRead();
		} catch (UnsupportedEncodingException e) {
		   	return false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
