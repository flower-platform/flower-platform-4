/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.codesync.as;

import org.flowerplatform.codesync.CodeSyncConstants;

/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncAsConstants {

	private CodeSyncAsConstants() {
	}
	public static final String ACTIONSCRIPT = "as";
	
	public static final String EXTENSION_AS = "as";
	public static final String EXTENSION_MXML = "mxml";
	
	////////////////////////////////////////////
	// Node types
	////////////////////////////////////////////
	
	public static final String CLASS = "asClass";
	public static final String INTERFACE = "asInterface";
	
	public static final String SUPER_INTERFACE = "asSuperInterface";
	
	public static final String META_TAG = "asMetaTag";
	public static final String META_TAG_ATTRIBUTE = "asMetaTagAttribute";
	
	public static final String CATEGORY_VARIABLE = CodeSyncConstants.CATEGORY_CODESYNC + ".asVar";
	public static final String VARIABLE = "asVarAttribute";
	public static final String CONST = "asConstAttribute";
	
	public static final String CATEGORY_FUNCTION = CodeSyncConstants.CATEGORY_CODESYNC + ".asFunction";
	public static final String FUNCTION = "asFunctionOperation";
	public static final String GETTER = "asGettterOperation";
	public static final String SETTER = "asSetterOperation";
	
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
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getImagePath(String image) {
		return "org.flowerplatform.resources/images/codesync.as/" + image;
	}
}