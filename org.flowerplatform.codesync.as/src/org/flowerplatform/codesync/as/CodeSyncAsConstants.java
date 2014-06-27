package org.flowerplatform.codesync.as;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncAsConstants {

	public static final String TECHNOLOGY = "as";
	
	////////////////////////////////////////////
	// Node types
	////////////////////////////////////////////
	
	public static final String CLASS = "asClass";
	public static final String INTERFACE = "asInterface";
	
	public static final String SUPER_INTERFACE = "asSuperInterface";
	
	public static final String VARIABLE = "asVariable";
	public static final String FUNCTION = "asFunction";
	
	public static final String MODIFIER = "asModifier";
	public static final String PARAMETER = "asParameter";
	
	////////////////////////////////////////////
	// Node properties
	////////////////////////////////////////////
	
	// Value features
	
	public static final String DOCUMENTATION = "documentation";
	public static final String TYPED_ELEMENT_TYPE = "typedElementType";
	public static final String SUPER_CLASS = "superClass";
	public static final String VARIABLE_INITIALIZER = "variableInitializer";
	
	// Containment features
	
	public static final String STATEMENTS = "statements";
	public static final String MODIFIERS = "modifiers";
	public static final String SUPER_INTERFACES = "superInterfaces";
	public static final String FUNCTION_PARAMETERS = "functionParameters";

	////////////////////////////////////////////
	// Icons and decorators
	////////////////////////////////////////////
	
	public static final String IMG_TYPE_CLASS = "ActionScriptClass.gif";
	public static final String IMG_TYPE_INTERFACE = "ActionScriptInterface.gif";
	
	public static final String IMG_SUPER_INTERFACE = "ActionScriptInterfaceRealization.gif";
	
	public static final String IMG_VARIABLE = "ActionScriptProperty.gif";
	public static final String IMG_FUNCTION = "ActionScriptOperation.gif";
	
	public static String getImagePath(String image) {
		return "org.flowerplatform.resources/images/codesync.as/" + image;
	}
}
