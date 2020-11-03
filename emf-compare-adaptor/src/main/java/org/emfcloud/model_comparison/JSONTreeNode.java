package org.emfcloud.model_comparison;

import java.util.ArrayList;
import java.util.List;

public class JSONTreeNode extends TreeNodeCollection {
	private String eClass = "";
	private String name = "";
	private String icon = "";
	private String color = "";
	private String uuid = "";
	private String type = "";
	
	public JSONTreeNode(String eClass, String name) {
		super();
		this.eClass = eClass;
		this.name = name;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setTypeMatch() {
		this.type = "match";
	}
	
	public void setTypeDiff() {
		this.type = "diff";
	}
	
	public void setTypeConflict() {
		this.type = "conflict";
	}
	
	public String getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return toString(0);
	}
	
	public String toString(int preTabs) {
		String prefix = getTabs(preTabs);
		String prefix_plus = getTabs(preTabs+1);
		
		StringBuilder sb = new StringBuilder();
		sb.append(prefix).append("{\n");
		sb.append(prefix_plus).append("\"").append("eClass").append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(eClass)).append("\"");
		sb.append(",\n");
		sb.append(prefix_plus).append("\"").append("name").append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(name)).append("\"");
		sb.append(",\n");
		sb.append(prefix_plus).append("\"").append("icon").append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(icon)).append("\"");
		sb.append(",\n");
		sb.append(prefix_plus).append("\"").append("color").append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(color)).append("\"");
		sb.append(",\n");
		sb.append(prefix_plus).append("\"").append("uuid").append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(uuid)).append("\"");
		sb.append(",\n");
		sb.append(prefix_plus).append("\"").append("type").append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(type)).append("\"");
		sb.append(",\n");
		sb.append(prefix_plus).append("\"").append("children").append("\": ").append("[");
		if (children.size() > 0) {
			sb.append("\n");
		}
		for(int i=0; i<children.size(); i++) {
			sb.append(children.get(i).toString(preTabs+2));
			if (i != children.size()-1) {
				sb.append(",\n");
			}
		}
		sb.append("]\n");
		sb.append(prefix).append("}\n");
		return sb.toString();
	}
}