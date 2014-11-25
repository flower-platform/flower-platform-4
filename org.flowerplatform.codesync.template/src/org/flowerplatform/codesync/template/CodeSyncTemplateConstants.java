/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.codesync.template;


/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncTemplateConstants {
	
	public static final String GEN = "gen";
	
	///////////////////////////////////
	// Node types and properties
	///////////////////////////////////
	
	// Node types
	
	public static final String INNER_TEMPLATE = "innerTemplate";
	
	// Value features
	
	public static final String LABEL = "label";
	public static final String TEMPLATE = "template";
	public static final String ENTITY = "entity";
	public static final String PARENT = "parent";
	
	// Containment features
	
	public static final String INNER_TEMPLATES = "innerTemplates";

	///////////////////////////////////
	// Configuration
	///////////////////////////////////
	
	// Configuration files
	
	public static final String CODE_SYNC_CONFIG_TEMPLATES = "templates";
	
	// Loaded configurations
	
	public static final String CODE_SYNC_CONFIG_VELOCITY_ENGINE = "codeSyncConfigVelocityEngine";
	
	private CodeSyncTemplateConstants() {
		// utility class
	}
	
}
