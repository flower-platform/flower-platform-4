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
package org.flowerplatform.codesync.code.java;

import static org.flowerplatform.util.UtilConstants.CATEGORY_PREFIX;

import org.flowerplatform.resources.ResourcesPlugin;

/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncJavaConstants {
	
	public static final String JAVA = "java";

	public static final String EXTENSION_JAVA = "java";
	
	////////////////////////////////////////////
	// Node types
	////////////////////////////////////////////
	
	public static final String CATEGORY_TYPE = CATEGORY_PREFIX + "codesync-java-type";
	public static final String CATEGORY_CAN_CONTAIN_TYPES = CATEGORY_PREFIX + "codesync-java-can-contain-types";
	public static final String CATEGORY_HAS_SUPER_INTERFACES = CATEGORY_PREFIX + "codesync-java-has-super-interfaces";
	public static final String CATEGORY_MODIFIABLE = CATEGORY_PREFIX + "codesync-java-modifiable";
	
	public static final String CLASS = "javaClass";
	public static final String INTERFACE = "javaInterface";
	public static final String ENUM = "javaEnum";
	public static final String ANNOTATION_TYPE = "javaAnnotationType";
	
	public static final String ATTRIBUTE = "javaAttribute";
	public static final String OPERATION = "javaOperation";
	public static final String ENUM_CONSTANT = "javaEnumConstant";
	public static final String ANNOTATION_MEMBER = "javaAnnotationMember";
	
	public static final String ANNOTATION = "javaAnnotation";
	public static final String SUPER_INTERFACE = "javaSuperInterface";
	public static final String ENUM_CONSTANT_ARGUMENT = "javaEnumConstantArgument";
	public static final String MEMBER_VALUE_PAIR = "javaMemberValuePair";
	public static final String MODIFIER = "javaModifier";
	public static final String PARAMETER = "javaParameter";
	
	////////////////////////////////////////////
	// Node properties
	////////////////////////////////////////////
	
	// Value features
	
	public static final String DOCUMENTATION = "documentation";
	public static final String TYPED_ELEMENT_TYPE = "typedElementType";
	public static final String SUPER_CLASS = "superClass";
	public static final String ATTRIBUTE_INITIALIZER = "attributeInitializer";
	public static final String OPERATION_HAS_BODY = "hasBody";
	public static final String ANNOTATION_VALUE_VALUE = "annotationValueValue";
	public static final String ANNOTATION_MEMBER_DEFAULT_VALUE = "annotationMemberDefaultValue";
	public static final String SINGLE_MEMBER_ANNOTATION_VALUE_NAME = "_";
	
	// Containment features
	
	public static final String TYPE_MEMBERS = "typeMembers";
	public static final String MODIFIERS = "modifiers";
	public static final String OPERATION_PARAMETERS = "operationParameters";
	public static final String ANNOTATION_VALUES = "annotationValues";
	public static final String SUPER_INTERFACES = "superInterfaces";
	public static final String ENUM_CONSTANT_ARGUMENTS = "enumConstantArguments";

	////////////////////////////////////////////
	// Icons and decorators
	////////////////////////////////////////////
	
	public static final String IMG_PACKAGE = "package_obj.gif";
	public static final String IMG_FILE = "jcu_obj.gif";
	
	public static final String IMG_TYPE_CLASS = "class_obj.gif";
	public static final String IMG_TYPE_INTERFACE = "interface_obj.gif";
	public static final String IMG_TYPE_ENUM = "enum_obj.gif";
	public static final String IMG_TYPE_ANNOTATION = "annotation_obj.gif";
	
	public static final String IMG_FIELD = "field_obj.gif";
	public static final String IMG_METHOD = "method_obj.gif";
	public static final String IMG_LOCAL_VAR = "localvariable_obj.gif";
	
	public static final String IMG_ANNOTATION = "annotation_alt_obj.gif";
	
	private static final String WIZ = "wiz/";
	
	public static final String IMG_WIZ_PACKAGE = WIZ + "newpack_wiz.gif";
	public static final String IMG_WIZ_TYPE_CLASS = WIZ + "newclass_wiz.gif";
	public static final String IMG_WIZ_TYPE_INTERFACE = WIZ + "newint_wiz.gif";
	public static final String IMG_WIZ_TYPE_ENUM = WIZ + "newenum_wiz.gif";
	public static final String IMG_WIZ_TYPE_ANNOTATION = WIZ + "newannotation_wiz.gif";
	
	private static final String DECORATOR = "decorator/";
	
	public static final String DECORATOR_ABSTRACT = DECORATOR + "abstract.gif";
	public static final String DECORATOR_FINAL = DECORATOR + "final.gif";
	public static final String DECORATOR_VOLATILE = DECORATOR + "volatile.gif";
	public static final String DECORATOR_STATIC = DECORATOR + "static.gif";
	public static final String DECORATOR_NATIVE = DECORATOR + "native.gif";
	public static final String DECORATOR_TRANSIENT = DECORATOR + "transient.gif";
	public static final String DECORATOR_SYNCHRONIZED = DECORATOR + "synchronized.gif";
	
	public static final String VISIBILITY_PRIVATE = "_private";
	public static final String VISIBILITY_PROTECTED = "_protected";
	public static final String VISIBILITY_DEFAULT = "_default";
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getImagePathFromPublicResources(String image) {
		return ResourcesPlugin.getInstance().getResourceUrl("images/codesync.java/" + image);
	}
	
	private CodeSyncJavaConstants() {
		// utility class
	}

}