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
package com.eclipsesource.model.comparison;

public class IconProvider {
	public static String getAddIcon() {
		return "diff-added green";
	}

	public static String getDeleteIcon() {
		return "diff-removed red";
	}

	public static String getChangeIcon() {
		return "edit yellow";
	}

	public static String getMoveIcon() {
		return "diff-modified yellow";
	}

	public static String getConflictIcon() {
		return "report yellow";
	}

	public static String getMatchIcon() {
		return "list-flat gray";
	}

	public static String getObjectIcon() {
		return "circle-large-outline gray";
	}

	public static String getAttributeIcon() {
		return "tag gray";
	}

	public static String getReferenceIcon() {
		return "arrow-both gray";
	}

	public static String getReferenceChildIcon() {
		return "arrow-right gray";
	}
}
