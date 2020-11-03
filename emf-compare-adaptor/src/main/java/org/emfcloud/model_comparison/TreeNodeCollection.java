package org.emfcloud.model_comparison;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeCollection {
	protected List<JSONTreeNode> children = new ArrayList<>();
	
	public TreeNodeCollection() {
		
	}
	
	public void addChild(JSONTreeNode node) {
		if (node != null) {
			children.add(node);
		}
	}
	
	public int getChildrenCount() {
		return children.size();
	}
	
	public JSONTreeNode findChildByUUID(String uuid) {
		for (JSONTreeNode jsonTreeNode : children) {
			if (jsonTreeNode.getUuid().equals(uuid)) {
				return jsonTreeNode;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.toString(0);
	}
	
	public String toString(int preTabs) {
		String prefix = getTabs(preTabs);		
		StringBuilder sb = new StringBuilder();
		
		sb.append(prefix).append("[\n");
		for (JSONTreeNode jsonTreeNode : children) {
			sb.append(jsonTreeNode.toString(preTabs + 1));
			if (children.indexOf(jsonTreeNode) != children.size() - 1) {
				sb.append(",\n");
			}
		}
		sb.append(prefix).append("]\n");
		
		return sb.toString();
	}
	
	protected String getTabs(int n) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<n; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
}
