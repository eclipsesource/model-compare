package org.emfcloud.model_comparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.ecore.EObject;

public class EObjectChangeMapping {
	private Map<String, String> connections = new HashMap<>();
	private Map<String, DifferenceKind> differences = new HashMap<>();
	private Map<EObject, Map<String, EObject>> matches = new HashMap<>();
	
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
	
	public void addMatch(EObject left, EObject right, EObject origin) {
		Map<String, EObject> match = new HashMap<>();
		if (left != null) {
			match.put("left", left);
		}
		if (right != null) {
			match.put("right", right);
		}
		if (origin != null) {
			match.put("origin", origin);
		}
		if (match.keySet().size() > 1) {
			if (left != null) {
				matches.put(left, match);
			}
			if (right != null) {
				matches.put(right, match);
			}
			if (origin != null) {
				matches.put(origin, match);
			}
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
	
	public Map<String, EObject> getMatch(EObject obj) {
		return matches.get(obj);
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
			sb.append("\t").append("\"").append(JSONCompareResponse.escapeJSON(key)).append("\": ").append("\"").append(JSONCompareResponse.escapeJSON(JSONTreeCreator.differenceKindToColor(differences.get(key)))).append("\"");
			sb.append(",\n");
		}
		if (connections.keySet().size() > 0) {
            sb.setLength(sb.length() - 2);
		}
		sb.append("\n}\n");
		return sb.toString();
	}
}
