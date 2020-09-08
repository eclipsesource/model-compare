package org.eclipse.emfcloud.EMF_Compare_Ui;

public class JSONCompareResponse {
	private String overviewTree;
	private String leftTree;
	private String rightTree;
	private String conflicts;
	private String error;
	
	public JSONCompareResponse() {
		this("");
	}
	
	public JSONCompareResponse(String error) {
		this("{}", "{}", "{}", "", error);
	}
	
	public JSONCompareResponse(String overviewTree, String leftTree,
			String rightTree, String conflicts, String error) {
		super();
		this.overviewTree = overviewTree;
		this.leftTree = leftTree;
		this.rightTree = rightTree;
		this.conflicts = conflicts;
		this.error = error;
	}

	public String getOverviewTree() {
		return overviewTree;
	}

	public void setOverviewTree(String overviewTree) {
		this.overviewTree = overviewTree;
	}

	public String getLeftTree() {
		return leftTree;
	}

	public void setLeftTree(String leftTree) {
		this.leftTree = leftTree;
	}

	public String getRightTree() {
		return rightTree;
	}

	public void setRightTree(String rightTree) {
		this.rightTree = rightTree;
	}

	public String getConflicts() {
		return conflicts;
	}

	public void setConflicts(String conflicts) {
		this.conflicts = conflicts;
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
	    // TODO: escape other non-printing characters using uXXXX notation
	    return escaped;
	}

	public String toString() {
		// TODO: escape JSON
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("\"").append("overviewTree").append("\": ").append(overviewTree);
		sb.append(",\n");
		sb.append("\"").append("leftTree").append("\": ").append(leftTree);
		sb.append(",\n");
		sb.append("\"").append("rightTree").append("\": ").append(rightTree);
		sb.append(",\n");
		sb.append("\"").append("conflicts").append("\": ").append("\"").append(conflicts).append("\"");
		sb.append(",\n");
		sb.append("\"").append("error").append("\": ").append("\"").append(escapeJSON(error)).append("\"");
		sb.append("\n");
		sb.append("}");
		return sb.toString();
	}
}