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

public class JSONCompareResponse {
	private String changesTree;
	private String sourceTree;
	private String targetTree;
	private String uuidConnection;
	private String error;

	public JSONCompareResponse() {
		this("");
	}

	public JSONCompareResponse(String error) {
		this("{}", "{}", "{}", "{}", error);
	}

	public JSONCompareResponse(String changesTree, String sourceTree, String targetTree, String uuidConnection,
			String error) {
		super();
		this.changesTree = changesTree;
		this.sourceTree = sourceTree;
		this.targetTree = targetTree;
		this.uuidConnection = uuidConnection;
		this.error = error;
	}

	public String getChangesTree() {
		return changesTree;
	}

	public void setChangesTree(String changesTree) {
		this.changesTree = changesTree;
	}

	public String getSourceTree() {
		return sourceTree;
	}

	public void setSourceTree(String sourceTree) {
		this.sourceTree = sourceTree;
	}

	public String getTargetTree() {
		return targetTree;
	}

	public void setTargetTree(String targetTree) {
		this.targetTree = targetTree;
	}

	public String getUuidConnection() {
		return uuidConnection;
	}

	public void setUuidConnection(String uuidConnection) {
		this.uuidConnection = uuidConnection;
	}

	public static String escapeJSON(String raw) {
		String escaped = raw;
		escaped = escaped.replace("\\", "\\\\");
		escaped = escaped.replace("\"", "\\\"");
		escaped = escaped.replace("\b", "\\b");
		escaped = escaped.replace("\f", "\\f");
		escaped = escaped.replace("\n", "\\n");
		escaped = escaped.replace("\r", "\\r");
		escaped = escaped.replace("\t", "\\t");
		return escaped;
	}

	public String toString() {
		String changesTree = this.changesTree;
		if (changesTree.matches("\\s*\\{\\s*\\}\\s*")) {
			TreeNodeCollection collection = new TreeNodeCollection();
			collection.addChild(new JSONTreeNode("information", "< No differences detected for this comparison. >"));
			changesTree = collection.toString();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("\"").append("changesTree").append("\": ").append(changesTree);
		sb.append(",\n");
		sb.append("\"").append("sourceTree").append("\": ").append(sourceTree);
		sb.append(",\n");
		sb.append("\"").append("targetTree").append("\": ").append(targetTree);
		sb.append(",\n");
		sb.append("\"").append("uuidConnection").append("\": ").append(uuidConnection);
		sb.append(",\n");
		sb.append("\"").append("error").append("\": ").append("\"").append(escapeJSON(error)).append("\"");
		sb.append("\n");
		sb.append("}");
		return sb.toString();
	}
}
