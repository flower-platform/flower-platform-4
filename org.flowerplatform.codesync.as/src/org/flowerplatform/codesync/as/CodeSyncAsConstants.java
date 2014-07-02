package org.flowerplatform.codesync.as;

import org.flowerplatform.codesync.CodeSyncConstants;

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
	
	public static final String META_TAG = "asMetaTag";
	public static final String META_TAG_ATTRIBUTE = "asMetaTagAttribute";
	
	public static final String CATEGORY_VARIABLE = CodeSyncConstants.CATEGORY_CODESYNC + ".asVar";
	public static final String VARIABLE = "asVar";
	public static final String CONST = "asConst";
	
	public static final String CATEGORY_FUNCTION = CodeSyncConstants.CATEGORY_CODESYNC + ".asFunction";
	public static final String FUNCTION = "asFunction";
	public static final String GETTER = "asGettter";
	public static final String SETTER = "asSetter";
	
	public static final String MODIFIER = "asModifier";
	public static final String PARAMETER = "asParameter";
	
	////////////////////////////////////////////
	// Node properties
	////////////////////////////////////////////
	
	// Value features
	
	public static final String DOCUMENTATION = "documentation";
	public static final String VISIBILITY = "visibility";
	public static final String TYPED_ELEMENT_TYPE = "typedElementType";

	public static final String VARIABLE_INITIALIZER = "variableInitializer";
	
	public static final String PARAMETER_IS_REST = "isRest";
	public static final String PARAMETER_DEFAULT_VALUE = "defaultValue";

	public static final String SUPER_CLASS = "superClass";
	
	public static final String META_TAG_ATTRIBUTE_VALUE = "value";
	
	public static final String PARAMETER_NULL_VALUE = "null";
	
	// Containment features
	
	public static final String STATEMENTS = "statements";
	public static final String MODIFIERS = "modifiers";
	public static final String META_TAGS = "metaTags";
	public static final String META_TAG_ATTRIBUTES = "metaTagAttributes";
	public static final String SUPER_INTERFACES = "superInterfaces";
	public static final String FUNCTION_PARAMETERS = "functionParameters";

	////////////////////////////////////////////
	// Icons and decorators
	////////////////////////////////////////////
	
	public static final String IMG_TYPE_CLASS = "ActionScriptClass.gif";
	public static final String IMG_TYPE_INTERFACE = "ActionScriptInterface.gif";
	
	public static final String IMG_INTERFACE_REALIZATION = "ActionScriptInterfaceRealization.gif";
	public static final String IMG_GENERALIZATION = "ActionScriptGeneralization.gif";
	
	public static final String IMG_VARIABLE = "ActionScriptProperty.gif";
	public static final String IMG_FUNCTION = "ActionScriptOperation.gif";
	
	public static String getImagePath(String image) {
		return "org.flowerplatform.resources/images/codesync.as/" + image;
	}
}
