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

import java.util.ArrayList;
import java.util.List;

public class TreeNodeCollection {
	protected List<JSONTreeNode> children = new ArrayList<>();

	public TreeNodeCollection() {

	}

	public void addChild(JSONTreeNode node) {
		if (node != null) {
			this.children.add(node);
		}
	}

	public List<JSONTreeNode> getChildren() {
		return this.children;
	}

	public int getChildrenCount() {
		return this.children.size();
	}

	public JSONTreeNode findChildByUUID(String uuid) {
		for (JSONTreeNode jsonTreeNode : this.children) {
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
		for (JSONTreeNode jsonTreeNode : this.children) {
			sb.append(jsonTreeNode.toString(preTabs + 1));
			if (this.children.indexOf(jsonTreeNode) != this.children.size() - 1) {
				sb.append(",\n");
			}
		}
		sb.append(prefix).append("]\n");

		return sb.toString();
	}

	protected String getTabs(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
}
