package org.flowerplatform.codesync.template;

/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncTemplateConstants {
	
	///////////////////////////////////
	// Node types
	///////////////////////////////////
	
	public static final String CODESYNC_TEMPLATE_ROOT = "codesyncTemplateRoot";
	public static final String INNER_TEMPLATE_TYPE = "innerTemplate";
	
	///////////////////////////////////
	// Node properties
	///////////////////////////////////
	
	// Value features
	
	public static final String LABEL = "label";
	public static final String TEMPLATE = "template";
	public static final String ENTITY = "entity";
	public static final String FIELD = "field";
	
	// Containment features
	
	public static final String INNER_TEMPLATES = "innerTemplates";

	
	// TODO I think these should be in a codesync.js project?
	
	public static final String TEMPLATE_FORM = "form";
	public static final String TEMPLATE_ROW = "formRow";
	public static final String TEMPLATE_FIELD_STRING = "stringField";
	public static final String TEMPLATE_FIELD_COMBO_BOX = "comboBoxField";
	
	public static final String TEMPLATE_TABSET = "tabSet";
	public static final String TEMPLATE_TAB = "tab";
	
	public static final String TEMPLATE_TABLE = "table";
	public static final String TEMPLATE_COLUMN = "column";
	
	public static final String HTML = "html";
	
	private CodeSyncTemplateConstants() {
		// utility class
	}
	
}
