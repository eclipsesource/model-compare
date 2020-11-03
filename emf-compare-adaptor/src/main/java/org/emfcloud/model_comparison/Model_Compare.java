package org.emfcloud.model_comparison;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
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
	private static final String ARG_MERGES = "-merges";
	
	private static final String OPERATION_COMPARISON = "comparison";
	private static final String OPERATION_HIGHLIGHT = "highlight";
	private static final String OPERATION_MERGE = "merge";


	static {
		validArgs = new ArrayList<String>();
		validArgs.add(ARG_OPERATION);
		validArgs.add(ARG_LEFT);
		validArgs.add(ARG_RIGHT);
		validArgs.add(ARG_ORIGIN);
		validArgs.add(ARG_MERGES);
	}
	
	private static void printArgs() {
		System.err.println("Valid arguments are");
		for (String string : validArgs) {
			System.err.println("  " + string);
		}
	}

	public static void main(String[] args) {
		
		
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
		} else if (data.get(ARG_OPERATION).equals(OPERATION_MERGE)) {
			merge(data);
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
						
			// apply merges
			applyAllMerges(data, comparison);
			
			// compare after merge
			comparison = EMFCompare.builder().build().compare(scope);
			
			EObjectChangeMapping changeMapping = new EObjectChangeMapping();
			JSONCompareResponse root = new JSONCompareResponse();
			TreeNodeCollection nodes = new TreeNodeCollection();
			
			List<TreeNode> conflictNodeList = new DifferenceGroup(comparison, DifferenceGroup.conflictFilter).generateTree();
			if (conflictNodeList.size() > 0) {
				JSONTreeNode conflicts = new JSONTreeNode("conflicts", "Conflicts");
				conflicts.setIcon("fas fa-exclamation red");
				for (TreeNode node : conflictNodeList) {
					JSONTreeNode n = getOverview(node, changeMapping);
					conflicts.addChild(n);
				}
				
				nodes.addChild(conflicts);
			}
			
			List<TreeNode> diffNodeList = new DifferenceGroup(comparison, DifferenceGroup.diffFilter).generateTree();
			for (TreeNode node : diffNodeList) {
				JSONTreeNode n = getOverview(node, changeMapping);
				nodes.addChild(n);
			}
			
			root.setOverviewTree(nodes.toString());
			TreeNodeCollection leftTree = new TreeNodeCollection();
			leftTree.addChild(buildTree(resourceSetLeft, changeMapping));
			root.setLeftTree(leftTree.toString());
			TreeNodeCollection rightTree = new TreeNodeCollection();
			rightTree.addChild(buildTree(resourceSetRight, changeMapping));
			root.setRightTree(rightTree.toString());
			root.setUuidConnection(changeMapping.getConnectionsString());
			
			// this will be read by the client
			System.out.print(root);
			
		} else {
			System.out.print(new JSONCompareResponse("Left or Right file missing or not readable!"));
		}
	}
	
	private static void highlight(Map<String, String> data) {
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
						
			List<TreeNode> list = new DifferenceGroup(comparison, DifferenceGroup.noPseudoFilter).generateTree();
			for (TreeNode node : list) {
				getOverview(node, changeMapping);
			}
			
			// this will be read by the client
			System.out.print(changeMapping.getHighlightString());
		} else {
			System.err.print("Left or Right file missing or not readable!");
		}
	}
	
	private static void applyMergeInformation(String type, String target, String direction, Comparison comparison, EObjectChangeMapping changeMapping) {
		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
	    IBatchMerger merger = new BatchMerger(mergerRegistry);
		if (target.equals("all")) {
		    if (direction.equals("left")) {
		    	List<Diff> diffs = comparison.getDifferences().stream()
		    			.filter(e -> e.getSource().equals(DifferenceSource.RIGHT))
		    			.collect(Collectors.toList());
		    	merger.copyAllRightToLeft(diffs, new BasicMonitor());
		    } else {
		    	List<Diff> diffs = comparison.getDifferences().stream()
		    			.filter(e -> e.getSource().equals(DifferenceSource.LEFT))
		    			.collect(Collectors.toList());
		    	merger.copyAllLeftToRight(diffs, new BasicMonitor());
		    }
		} else {
			for (Diff diff : comparison.getDifferences()) {
				JSONTreeNode node = JSONTreeCreator.diffToJSONTree(diff, changeMapping);
				if (node.getUuid().equals(target)) {	
					List<Diff> diffList = new ArrayList<>();
					if (type.equals("conflict") && diff.getConflict() != null) {
						diffList.addAll(diff.getConflict().getDifferences());
					} else {
						diffList.add(diff);
					}
					
					if (direction.equals("left")) {
				    	merger.copyAllRightToLeft(List.of(diff), new BasicMonitor());
				    } else {
				    	merger.copyAllLeftToRight(List.of(diff), new BasicMonitor());
				    }
					break;
				}
			}
		}
	}
	
	private static void applyAllMerges(Map<String, String> data, Comparison comparison) {
		if (data.get(ARG_MERGES) != null) {
			String[] merges = data.get(ARG_MERGES).split(",");
			
			// rebuild change mapping
			EObjectChangeMapping changeMapping = new EObjectChangeMapping();
			for (TreeNode node : new DifferenceGroup(comparison, DifferenceGroup.noPseudoFilter).buildMatchTrees()) {
				getOverview(node, changeMapping);
			}
			
			if (merges.length > 0 && merges[0].length() > 0) {
				for (String merge : merges) {
					String[] info = merge.split(";");
					if (info.length == 3) {
						applyMergeInformation(info[0], info[1], info[2], comparison, changeMapping);
					}
				}
			}
		}
	}
	
	private static void merge(Map<String, String> data) {
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
			
			EcoreUtil.resolveAll(resourceSetLeft);
			EcoreUtil.resolveAll(resourceSetRight);
			EcoreUtil.resolveAll(resourceSetOrigin);
			
			// merge
			applyAllMerges(data, comparison);
			
			// save
			ResourceUtil.saveAllResources(resourceSetLeft, Collections.emptyMap());
			ResourceUtil.saveAllResources(resourceSetRight, Collections.emptyMap());
			ResourceUtil.saveAllResources(resourceSetOrigin, Collections.emptyMap());
			
			// this will be read by the client
			System.out.println("Merged");
		} else {
			System.err.print("Left or Right file missing or not readable!");
		}
	}
	
	private static JSONTreeNode getOverview(TreeNode treeNode, EObjectChangeMapping changeMapping) {
		EObject data = treeNode.getData();
		
		JSONTreeNode jsonNode = JSONTreeCreator.getOverview(data, changeMapping);

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
				JSONTreeNode node = JSONTreeCreator.objectToJSONTree(eObject, changeMapping);
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
