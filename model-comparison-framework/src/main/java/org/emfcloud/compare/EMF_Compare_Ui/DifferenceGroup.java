package org.emfcloud.compare.EMF_Compare_Ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchResourceNode;
import org.eclipse.emf.edit.tree.TreeNode;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;


public class DifferenceGroup implements Adapter {
	
	protected final Predicate<? super Diff> filter = e -> true;

	
	public List<TreeNode> buildOverviewTree(Comparison comparison) {
		
		/*
		ECrossReferenceAdapter crossReferenceAdapter = new ECrossReferenceAdapter() {

			@Override
			protected boolean isIncluded(EReference eReference) {
				return eReference == TreePackage.Literals.TREE_NODE__DATA;
			}
		};
		BasicDifferenceGroupImpl group = new BasicDifferenceGroupImpl(comparison, e -> true, crossReferenceAdapter);

		group.buildSubTree();
		
		IDifferenceGroupProvider defaultGroup = (IDifferenceGroupProvider) ImmutableList.of(group);
		
		
		System.out.println(defaultGroup);
		*/
		
		
		List<TreeNode> children = new ArrayList<TreeNode>();
		children.addAll(buildMatchTrees(comparison));
		//children.addAll(buildMatchResourceTrees(comparison));
		return children;
	}
	
	
	protected List<TreeNode> buildMatchTrees(Comparison comparison) {
		final List<TreeNode> matchTrees = new ArrayList<TreeNode>();
		for (Match match : comparison.getMatches()) {
			MatchNode matchNode = buildTree(match);
			if (matchNode != null) {
				matchTrees.add(matchNode);
			}
		}
		return matchTrees;
	}
	
	protected MatchNode buildTree(Match match) {
		MatchNode result = null;
		MatchNode matchNode = createMatchNode(match);
		populateMatchNode(matchNode);
		if (!matchNode.getChildren().isEmpty()) {
			result = matchNode;
		}
		return result;
	}
	
	protected MatchNode createMatchNode(Match match) {
		MatchNode matchNode = new MatchNode(match);
		matchNode.eAdapters().add(this);
		return matchNode;
	}
	
	protected void populateMatchNode(MatchNode matchNode) {
		Match match = matchNode.getMatch();
		Multimap<Match, Diff> diffsBySubMatch = LinkedHashMultimap.create();
		//for (Diff diff : filter(match.getDifferences(), filter)) {
		for (Diff diff : match.getDifferences()) {
			// If a diff is part of a larger diff (is refined by), we don't want to add it to the tree. It
			// will be added by the algorithm in a second step. This way we avoid duplication and all diffs
			// that are part of a 'master' diff are grouped as children of this 'master' diff
			if (mustDisplayAsDirectChildOfMatch(diff)) {
				Match targetMatch = getTargetMatch(diff);
				if (match == targetMatch) {
					addDiffNode(matchNode, diff);
				} else if (match.getSubmatches().contains(targetMatch)) {
					diffsBySubMatch.put(targetMatch, diff);
				} else if (targetMatch != null) {
					MatchNode targetMatchNode = createMatchNode(targetMatch);
					matchNode.addSubMatchNode(targetMatchNode);
					addDiffNode(targetMatchNode, diff);
				}
			}
		}
		for (Match subMatch : match.getSubmatches()) {
			MatchNode subMatchNode = createMatchNode(subMatch);
			for (Diff subMatchDiff : diffsBySubMatch.get(subMatch)) {
				addDiffNode(subMatchNode, subMatchDiff);
			}
			diffsBySubMatch.removeAll(subMatch);
			populateMatchNode(subMatchNode);
			if (!subMatchNode.getChildren().isEmpty()) {
				matchNode.addSubMatchNode(subMatchNode);
			}
		}
	}
	
	protected Match getTargetMatch(Diff diff) {
		if (mustDisplayAsDirectChildOfMatch(diff)) {
			if (isContainmentRefChange(diff)) {
				Match valueMatch = diff.getMatch().getComparison()
						.getMatch(((ReferenceChange)diff).getValue());
				return valueMatch; // This match may not be a sub-match because the child may have moved
			} else if (isContainmentRefChange(diff.getPrimeRefining())) {
				Match valueMatch = diff.getMatch().getComparison()
						.getMatch(((ReferenceChange)diff.getPrimeRefining()).getValue());
				return valueMatch; // This match may not be a sub-match because the child may have moved
			}
			return diff.getMatch();
		}
		return null;
	}
	
	protected boolean mustDisplayAsDirectChildOfMatch(Diff diff) {
		return diff.getRefines().isEmpty();
	}
	
	protected boolean isContainmentRefChange(Diff diff) {
		return diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment();
	}
	
	protected void addDiffNode(MatchNode matchNode, Diff diff) {
		if (!(diff instanceof ResourceAttachmentChange)) {
			DiffNode diffNode = createDiffNode(diff);
			handleRefiningDiffs(diffNode);
			matchNode.addDiffNode(diffNode);
		}
	}
	
	protected void handleRefiningDiffs(DiffNode diffNode) {
		Diff diff = diffNode.getDiff();
		for (Diff refiningDiff : diff.getRefinedBy()) {
			DiffNode refinedDiffNode = createDiffNode(refiningDiff);
			diffNode.addRefinedDiffNode(refinedDiffNode);
			handleRefiningDiffs(refinedDiffNode);
		}
	}
	
	protected List<TreeNode> buildMatchResourceTrees(Comparison comparison) {
		final List<TreeNode> matchResourceTrees = new ArrayList<TreeNode>();
		if (comparison.getMatchedResources().isEmpty()) {
			return matchResourceTrees;
		}

		final Iterable<ResourceAttachmentChange> attachmentChanges = Iterables
				.filter(comparison.getDifferences(), ResourceAttachmentChange.class);

		final Multimap<String, ResourceAttachmentChange> uriToRAC = LinkedHashMultimap.create();
		for (ResourceAttachmentChange attachmentChange : attachmentChanges) {
			uriToRAC.put(attachmentChange.getResourceURI(), attachmentChange);
		}
		for (MatchResource matchResource : comparison.getMatchedResources()) {
			final Collection<ResourceAttachmentChange> leftRAC = uriToRAC.get(matchResource.getLeftURI());
			final Collection<ResourceAttachmentChange> rightRAC = uriToRAC.get(matchResource.getRightURI());
			final Collection<ResourceAttachmentChange> originRAC = uriToRAC.get(matchResource.getOriginURI());
			final LinkedHashSet<ResourceAttachmentChange> racForMatchResource = Sets.newLinkedHashSet();
			racForMatchResource.addAll(leftRAC);
			racForMatchResource.addAll(rightRAC);
			racForMatchResource.addAll(originRAC);

			MatchResourceNode matchNode = buildSubTree(matchResource, racForMatchResource);
			if (matchNode != null) {
				matchResourceTrees.add(matchNode);
			}

		}
		return matchResourceTrees;
	}
	
	protected MatchResourceNode buildSubTree(MatchResource matchResource,
			Set<ResourceAttachmentChange> attachmentChanges) {
		MatchResourceNode matchResourceNode = createMatchResourceNode(matchResource);
		Collection<ResourceAttachmentChange> filteredChanges = attachmentChanges; //filter(attachmentChanges, filter);
		for (ResourceAttachmentChange attachmentChange : filteredChanges) {
			DiffNode diffNode = createDiffNode(attachmentChange);
			matchResourceNode.addDiffNode(diffNode);
		}
		return matchResourceNode;
	}
	
	protected MatchResourceNode createMatchResourceNode(MatchResource matchResource) {
		MatchResourceNode matchResourceNode = new MatchResourceNode(matchResource);
		matchResourceNode.eAdapters().add(this);
		return matchResourceNode;
	}
	
	protected DiffNode createDiffNode(Diff diff) {
		DiffNode diffNode = new DiffNode(diff);
		diffNode.eAdapters().add(this);
		return diffNode;
	}


	@Override
	public void notifyChanged(Notification notification) {
		//throw new RuntimeException("Not implemented");
		// TODO Auto-generated method stub
		
	}


	@Override
	public Notifier getTarget() {
		throw new RuntimeException("Not implemented");
		// TODO Auto-generated method stub
		//return null;
	}


	@Override
	public void setTarget(Notifier newTarget) {
		// TODO Auto-generated method stub
		//throw new RuntimeException("Not implemented");
	}


	@Override
	public boolean isAdapterForType(Object type) {
		throw new RuntimeException("Not implemented");
		// TODO Auto-generated method stub
		//return false;
	}
}
