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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.tree.TreeNode;
import EAM_Metamodel.EAM_MetamodelPackage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
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
		
		args = new String[6];
		args[0] = ARG_OPERATION;
		args[1] = OPERATION_COMPARISON;
		args[2] = ARG_LEFT;
		args[3] = "file:///c%3A/Users/ldkpr/Documents/Studium/Master_Thesis/ws/eam-model-editor/client/workspace/Evaluation_0/1_comparison/diagram_new.eam";
		args[4] = ARG_RIGHT;
		args[5] = "file:///c%3A/Users/ldkpr/Documents/Studium/Master_Thesis/ws/eam-model-editor/client/workspace/Evaluation_0/1_comparison/diagram.eam";
		//args[6] = ARG_ORIGIN;
		//args[7] = "file:///c%3A/Users/ldkpr/Documents/Studium/Master_Thesis/ws/eam-model-editor/client/workspace/Presentation_Test/origin.eam";
		//args[8] = ARG_MERGES;
		//args[9] = "conflict;b1187042-6d4b-5f7b-a284-387e37df7a64;right";
		
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
		
		if (OPERATION_COMPARISON.equals(data.get(ARG_OPERATION))) {
			compare(data);
		} else if (OPERATION_HIGHLIGHT.equals(data.get(ARG_OPERATION))) {
			highlight(data);
		} else if (OPERATION_MERGE.equals(data.get(ARG_OPERATION))) {
			merge(data);
		} else {
			throw new RuntimeException("Unknown operation argument: " + data.get(ARG_OPERATION));
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
			
			JSONTreeCreator treeCreator = new JSONTreeCreator();
			TreeNodeCollection overview = new TreeNodeCollection();
			
			// add conflicts to overview
			List<TreeNode> conflictNodeList = new DifferenceGroup(comparison, DifferenceGroup.conflictFilter).generateTree();
			if (conflictNodeList.size() > 0) {
				JSONTreeNode conflicts = new JSONTreeNode("conflicts", "Conflicts");
				conflicts.setIcon("fas fa-exclamation red");
				for (TreeNode node : conflictNodeList) {
					JSONTreeNode n = treeCreator.createOverview(node);
					conflicts.addChild(n);
				}
				
				overview.addChild(conflicts);
			}
			
			// add differences to overview
			List<TreeNode> diffNodeList = new DifferenceGroup(comparison, DifferenceGroup.diffFilterLeft).generateTree();
			for (TreeNode node : diffNodeList) {
				JSONTreeNode n = treeCreator.createOverview(node);
				overview.addChild(n);
			}
			
			// preparing response
			JSONCompareResponse root = new JSONCompareResponse();
			root.setOverviewTree(overview.toString());
			TreeNodeCollection leftTree = treeCreator.createModelTree(resourceSetLeft);
			root.setLeftTree(leftTree.toString());
			TreeNodeCollection rightTree = treeCreator.createModelTree(resourceSetRight);
			root.setRightTree(rightTree.toString());
			root.setUuidConnection(treeCreator.getChangeMapping().getConnectionsString());
			
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
			
			// recreating overview to obtain change mapping
			JSONTreeCreator treeCreator = new JSONTreeCreator();
			List<TreeNode> list = new DifferenceGroup(comparison, DifferenceGroup.noPseudoFilterLeft).generateTree();
			for (TreeNode node : list) {
				treeCreator.createOverview(node);
			}
			
			// this will be read by the client
			System.out.print(treeCreator.getChangeMapping().getHighlightString());
			
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
		    			.filter(e -> e.getSource().equals(DifferenceSource.RIGHT) && e.getConflict() == null)
		    			.collect(Collectors.toList());
		    	merger.copyAllRightToLeft(diffs, new BasicMonitor());
		    } else {
		    	List<Diff> diffs = comparison.getDifferences().stream()
		    			.filter(e -> e.getSource().equals(DifferenceSource.LEFT) && e.getConflict() == null)
		    			.collect(Collectors.toList());
		    	merger.copyAllLeftToRight(diffs, new BasicMonitor());
		    }
		    
		} else {
			JSONTreeCreator treeCreator = new JSONTreeCreator(changeMapping);
			for (Diff diff : comparison.getDifferences()) {
				JSONTreeNode node = treeCreator.diffToJSONTree(diff);
				if (node != null && node.getUuid().equals(target)) {	
					List<Diff> diffList = new ArrayList<>();
					if (type.equals("conflict") && diff.getConflict() != null) {
						diffList.addAll(diff.getConflict().getDifferences());
					} else {
						diffList.add(diff);
					}
					
					if (direction.equals("left")) {
				    	merger.copyAllRightToLeft(diffList, new BasicMonitor());
				    } else {
				    	merger.copyAllLeftToRight(diffList, new BasicMonitor());
				    }
					break;
				}
			}
		}
	}
	
	private static void applyAllMerges(Map<String, String> data, Comparison comparison) {
		if (data.get(ARG_MERGES) != null) {
			String[] merges = data.get(ARG_MERGES).split(",");
			
			// recreating overview to obtain change mapping
			EObjectChangeMapping changeMapping = new EObjectChangeMapping();
			JSONTreeCreator treeCreator = new JSONTreeCreator(changeMapping);
			for (TreeNode node : new DifferenceGroup(comparison, DifferenceGroup.noPseudoFilter).buildMatchTrees()) {
				treeCreator.createOverview(node);
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
	
	private static void load(URI uri, ResourceSet resourceSet) {
	  resourceSet.getPackageRegistry().put(EAM_MetamodelPackage.eINSTANCE.getNsURI(), EAM_MetamodelPackage.eINSTANCE);
	  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
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
