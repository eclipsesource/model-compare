package org.emfcloud.compare.EMF_Compare_Ui;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.compare.DifferenceKind;

public class EObjectChangeMapping {
	private Map<String, String> connections = new HashMap<>();
	private Map<String, DifferenceKind> differences = new HashMap<>();
	
	public void addConnection(String uuid, String uuid2) {
		if (uuid != null && uuid2 != null) {
			connections.put(uuid, uuid2);
		}
	}
	
	public void addDifference(String uuid, DifferenceKind kind) {
		if (uuid != null) {
			differences.put(uuid, kind);
		}
	}
	
	public String getConnection(String uuid) {
		return connections.get(uuid);
	}
	
	public DifferenceKind getDifferenceKind(String uuid) {
		return differences.get(uuid);
	}
	
	public Map<String, DifferenceKind> getDifferences() {
		return differences;
	}

	public String getConnectionsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (String key : connections.keySet()) {
			sb.append("\t").append("\"").append(JSONCompareResponse.escapeJSON(key)).append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(connections.get(key))).append("\"");
			sb.append(",\n");
		}
		if (connections.keySet().size() > 0) {
            sb.setLength(sb.length() - 2);
		}
		sb.append("\n}\n");
		return sb.toString();
	}
	
	public String getHighlightString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (String key : differences.keySet()) {
			sb.append("\t").append("\"").append(JSONCompareResponse.escapeJSON(key)).append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(ToJSONHelper.differenceKindToColor(differences.get(key)))).append("\"");
			sb.append(",\n");
		}
		if (connections.keySet().size() > 0) {
            sb.setLength(sb.length() - 2);
		}
		sb.append("\n}\n");
		return sb.toString();
	}
}
