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
package org.emfcloud.model_comparison;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.tree.TreeNode;
import org.xml.sax.SAXException;

public class Model_Compare {

	private static List<String> validArgs;
	private static final String ARG_OPERATION = "-operation";
	private static final String ARG_BASE = "-base";
	private static final String ARG_SOURCE = "-source";
	private static final String ARG_TARGET = "-target";
	private static final String ARG_MERGES = "-merges";
	private static final String ARG_JAR = "-model";
	private static final String ARG_PACKAGE = "-package";

	private static final String OPERATION_COMPARISON = "comparison";
	private static final String OPERATION_HIGHLIGHT = "highlight";
	private static final String OPERATION_MERGE = "merge";

	private static EPackage instance = null;

	static {
		validArgs = new ArrayList<String>();
		validArgs.add(ARG_OPERATION);
		validArgs.add(ARG_BASE);
		validArgs.add(ARG_SOURCE);
		validArgs.add(ARG_TARGET);
		validArgs.add(ARG_MERGES);
		validArgs.add(ARG_JAR);
		validArgs.add(ARG_PACKAGE);
	}

	private static void printArgs() {
		System.err.println("Valid arguments are");
		for (String string : validArgs) {
			System.err.println("  " + string);
		}
	}

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, IOException,
			ParserConfigurationException, SAXException, URISyntaxException {

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

		URLClassLoader child = new URLClassLoader(new URL[] { new URL("file:" + data.get(ARG_JAR)) },
				ClassLoader.getSystemClassLoader());
		Class<?> clazz = Class.forName(data.get(ARG_PACKAGE), true, child);
		Field field = clazz.getDeclaredField("eINSTANCE");
		instance = (EPackage) field.get(null);

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
		URI baseURI = getUri(data.get(ARG_BASE));
		URI sourceURI = getUri(data.get(ARG_SOURCE));
		URI targetURI = getUri(data.get(ARG_TARGET));

		if (isValidFile(sourceURI) && isValidFile(targetURI)) {
			// creates the resourceSets where we will load the models
			final ResourceSet resourceSetSource = new ResourceSetImpl();
			final ResourceSet resourceSetTarget = new ResourceSetImpl();
			final ResourceSet resourceSetBase = new ResourceSetImpl();

			// loading resources
			boolean baseProvided = isValidFile(baseURI);
			load(sourceURI, resourceSetSource);
			load(targetURI, resourceSetTarget);
			if (baseProvided) {
				load(baseURI, resourceSetBase);
			}

			// setting scope
			IComparisonScope scope = new DefaultComparisonScope(resourceSetTarget, resourceSetSource,
					(baseProvided) ? resourceSetBase : null);

			// comparing
			Comparison comparison = EMFCompare.builder().build().compare(scope);

			// apply merges
			applyAllMerges(data, comparison);

			// compare after merge
			comparison = EMFCompare.builder().build().compare(scope);

			JSONTreeCreator treeCreator = new JSONTreeCreator();
			TreeNodeCollection overview = new TreeNodeCollection();

			// add conflicts to overview
			List<TreeNode> conflictNodeList = new DifferenceGroup(comparison, DifferenceGroup.conflictFilter)
					.generateTree();
			if (conflictNodeList.size() > 0) {
				JSONTreeNode conflicts = new JSONTreeNode("conflicts", "Conflicts");
				conflicts.setIcon(IconProvider.getConflictIcon());
				for (TreeNode node : conflictNodeList) {
					JSONTreeNode n = treeCreator.createOverview(node);
					conflicts.addChild(n);
				}

				overview.addChild(conflicts);
			}

			// add differences to overview
			List<TreeNode> diffNodeList = new DifferenceGroup(comparison, DifferenceGroup.diffFilterLeft)
					.generateTree();
			for (TreeNode node : diffNodeList) {
				JSONTreeNode n = treeCreator.createOverview(node);
				overview.addChild(n);
			}

			// preparing response
			JSONCompareResponse root = new JSONCompareResponse();
			root.setChangesTree(overview.toString());
			TreeNodeCollection sourceTree = treeCreator.createModelTree(resourceSetSource);
			root.setSourceTree(sourceTree.toString());
			TreeNodeCollection targetTree = treeCreator.createModelTree(resourceSetTarget);
			root.setTargetTree(targetTree.toString());
			root.setUuidConnection(treeCreator.getChangeMapping().getConnectionsString());

			// this will be read by the client
			System.out.print(root);

		} else {
			System.out.print(new JSONCompareResponse("Source (" + sourceURI.toString() + ") or target ("
					+ targetURI.toString() + ") file missing or not readable!"));
		}
	}

	private static void highlight(Map<String, String> data) {
		URI baseURI = getUri(data.get(ARG_BASE));
		URI sourceURI = getUri(data.get(ARG_SOURCE));
		URI targetURI = getUri(data.get(ARG_TARGET));

		if (isValidFile(sourceURI) && isValidFile(targetURI)) {
			// creates the resourceSets where we will load the models
			final ResourceSet resourceSetSource = new ResourceSetImpl();
			final ResourceSet resourceSetTarget = new ResourceSetImpl();
			final ResourceSet resourceSetBase = new ResourceSetImpl();

			// loading resources
			boolean baseProvided = isValidFile(baseURI);
			load(sourceURI, resourceSetSource);
			load(targetURI, resourceSetTarget);
			if (baseProvided) {
				load(baseURI, resourceSetBase);
			}

			// setting scope
			IComparisonScope scope = new DefaultComparisonScope(resourceSetTarget, resourceSetSource,
					(baseProvided) ? resourceSetBase : null);

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
			System.out.print(new JSONCompareResponse("Source (" + sourceURI.toString() + ") or target ("
					+ targetURI.toString() + ") file missing or not readable!"));
		}
	}

	private static void applyMergeInformation(String type, String target, String direction, Comparison comparison,
			EObjectChangeMapping changeMapping) {
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
		URI baseURI = getUri(data.get(ARG_BASE));
		URI sourceURI = getUri(data.get(ARG_SOURCE));
		URI targetURI = getUri(data.get(ARG_TARGET));

		if (isValidFile(sourceURI) && isValidFile(targetURI)) {
			// creates the resourceSets where we will load the models
			final ResourceSet resourceSetSource = new ResourceSetImpl();
			final ResourceSet resourceSetTarget = new ResourceSetImpl();
			final ResourceSet resourceSetBase = new ResourceSetImpl();

			// loading resources
			boolean baseProvided = isValidFile(baseURI);
			load(sourceURI, resourceSetSource);
			load(targetURI, resourceSetTarget);
			if (baseProvided) {
				load(baseURI, resourceSetBase);
			}

			// setting scope
			IComparisonScope scope = new DefaultComparisonScope(resourceSetTarget, resourceSetSource,
					(baseProvided) ? resourceSetBase : null);

			// comparing
			Comparison comparison = EMFCompare.builder().build().compare(scope);

			EcoreUtil.resolveAll(resourceSetSource);
			EcoreUtil.resolveAll(resourceSetTarget);
			EcoreUtil.resolveAll(resourceSetBase);

			// merge
			applyAllMerges(data, comparison);

			// save
			ResourceUtil.saveAllResources(resourceSetSource, Collections.emptyMap());
			ResourceUtil.saveAllResources(resourceSetTarget, Collections.emptyMap());
			ResourceUtil.saveAllResources(resourceSetBase, Collections.emptyMap());

			// this will be read by the client
			System.out.println("Merged");
		} else {
			System.out.print(new JSONCompareResponse("Source (" + sourceURI.toString() + ") or target ("
					+ targetURI.toString() + ") file missing or not readable!"));
		}
	}

	private static void load(URI uri, ResourceSet resourceSet) {
		resourceSet.getPackageRegistry().put(instance.getNsURI(), instance);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		resourceSet.getResource(uri, true);
	}

	private static URI getUri(String path) {
		if (path == null)
			return null;

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
