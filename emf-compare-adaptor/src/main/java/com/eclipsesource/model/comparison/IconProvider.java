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
		return "fas fa-plus-circle green";
	}

	public static String getDeleteIcon() {
		return "fas fa-minus-circle red";
	}

	public static String getChangeIcon() {
		return "fas fa-pen yellow";
	}

	public static String getMoveIcon() {
		return "fas fa-exchange-alt yellow";
	}

	public static String getConflictIcon() {
		return "fas fa-exclamation red";
	}

	public static String getMatchIcon() {
		return "fas fa-stream gray";
	}

	public static String getObjectIcon() {
		return "fas fa-circle gray";
	}

	public static String getAttributeIcon() {
		return "fas fa-tag gray";
	}

	public static String getReferenceIcon() {
		return "fas fa-arrows-alt-h gray";
	}

	public static String getReferenceChildIcon() {
		return "fas fa-long-arrow-alt-right gray";
	}
}
