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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.ecore.EObject;

/**
 * Keeps track of differences and matches. Also stores connected UUIDs which
 * represent matches in the source and target model.
 */
public class EObjectChangeMapping {
	private Map<String, String> connections = new HashMap<>();
	private Map<String, DifferenceKind> differences = new HashMap<>();
	private Map<String, DifferenceKind> graphicalDifferences = new HashMap<>();
	private Map<EObject, Map<String, EObject>> matches = new HashMap<>();

	public static String SOURCE_SOURCE = "source";
	public static String SOURCE_TARGET = "target";
	public static String SOURCE_BASE = "base";

	public void addConnection(String uuid, String uuid2) {
		if (uuid != null && uuid2 != null && !uuid.equals(uuid2)) {
			connections.put(uuid, uuid2);
		}
	}

	public void addDifference(String uuid, DifferenceKind kind) {
		if (uuid != null) {
			differences.put(uuid, kind);
		}
	}

	public void addGraphicalDifference(String uuid, DifferenceKind kind) {
		if (uuid == null
				|| (graphicalDifferences.containsKey(uuid) && keepKind(graphicalDifferences.get(uuid), kind))) {
			return;
		}
		graphicalDifferences.put(uuid, kind);
	}

	private boolean keepKind(DifferenceKind current, DifferenceKind newKind) {
		// Do not override Add or Delete kind with move or change
		if (current == DifferenceKind.ADD || current == DifferenceKind.DELETE) {
			return true;
		} else {
			return false;
		}
	}

	public void addMatch(EObject source, EObject target, EObject base) {
		Map<String, EObject> match = new HashMap<>();
		if (source != null) {
			match.put(SOURCE_SOURCE, source);
		}
		if (target != null) {
			match.put(SOURCE_TARGET, target);
		}
		if (base != null) {
			match.put(SOURCE_BASE, base);
		}
		if (match.keySet().size() > 1) {
			if (source != null) {
				matches.put(source, match);
			}
			if (target != null) {
				matches.put(target, match);
			}
			if (base != null) {
				matches.put(base, match);
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
			sb.append("\t").append("\"").append(JSONCompareResponse.escapeJSON(key)).append("\": ").append("\"")
					.append(JSONCompareResponse.escapeJSON(connections.get(key))).append("\"");
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
		for (String key : graphicalDifferences.keySet()) {
			sb.append("\t").append("\"").append(JSONCompareResponse.escapeJSON(key)).append("\": ").append("\"")
					.append(JSONCompareResponse
							.escapeJSON(JSONTreeCreator.differenceKindToColor(graphicalDifferences.get(key))))
					.append("\"");
			sb.append(",\n");
		}
		if (differences.keySet().size() > 0) {
			sb.setLength(sb.length() - 2);
		}
		sb.append("\n}\n");
		return sb.toString();
	}
}
