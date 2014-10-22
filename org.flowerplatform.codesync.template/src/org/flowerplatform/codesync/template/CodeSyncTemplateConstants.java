package org.flowerplatform.codesync.template;

/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncTemplateConstants {
	
	///////////////////////////////////
	// Node types
	///////////////////////////////////
	
	public static final String CODESYNC_TEMPLATE_ROOT = "codesyncTemplateRoot";
	public static final String INNER_TEMPLATE = "innerTemplate";
	
	///////////////////////////////////
	// Node properties
	///////////////////////////////////
	
	// Value features
	
	public static final String LABEL = "label";
	public static final String TEMPLATE = "template";
	public static final String ENTITY = "entity";
	public static final String PARENT = "parent";
	
	// Containment features
	
	public static final String INNER_TEMPLATES = "innerTemplates";

	private CodeSyncTemplateConstants() {
		// utility class
	}
	
}
