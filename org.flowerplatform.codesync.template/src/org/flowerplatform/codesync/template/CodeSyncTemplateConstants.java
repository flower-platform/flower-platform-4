package org.flowerplatform.codesync.template;


/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncTemplateConstants {
	
	public static final String GEN = "gen";
	public static final String TEMPLATES_DIRS = "templatesDirs";
	
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
