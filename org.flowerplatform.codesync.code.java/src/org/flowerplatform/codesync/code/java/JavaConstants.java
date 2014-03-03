package org.flowerplatform.codesync.code.java;

/**
 * @author Mariana Gheorghe
 */
public class JavaConstants {

	////////////////////////////////////////////
	// Label constants
	////////////////////////////////////////////
	
	public static final String LABEL_PACKAGE = "Package";
	public static final String LABEL_FILE = "File";
	
	public static final String LABEL_TYPE_CLASS = "Class";
	public static final String LABEL_TYPE_INTERFACE = "Interface";
	public static final String LABEL_TYPE_ENUM = "Enum";
	public static final String LABEL_TYPE_ANNOTATION = "Annotation";
	
	public static final String LABEL_FIELD = "Field";
	public static final String LABEL_METHOD = "Method";
	public static final String LABEL_PARAMETER = "Parameter";
	
	public static final String LABEL_ENUM_CONSTANT = "Enum Constant";
	public static final String LABEL_ENUM_CONSTANT_ARGUMENT = "Enum Constant Argument";
	
	public static final String LABEL_MODIFIER = "Modifier";
	public static final String LABEL_ANNOTATION = "Annotation";
	
	public static final String LABEL_SUPER_INTERFACE = "Super Interface";
	
	////////////////////////////////////////////
	// Icon and decorator constants
	////////////////////////////////////////////
	
	private static final String IMAGES = "images/";
	
	public static final String IMG_PACKAGE = IMAGES + "package_obj.gif";
	public static final String IMG_FILE = IMAGES + "jcu_obj.gif";
	
	public static final String IMG_TYPE_CLASS = IMAGES + "class_obj.gif";
	public static final String IMG_TYPE_INTERFACE = IMAGES + "interface_obj.gif";
	public static final String IMG_TYPE_ENUM = IMAGES + "enum_obj.gif";
	public static final String IMG_TYPE_ANNOTATION = IMAGES + "annotation_obj.gif";
	
	public static final String IMG_FIELD = IMAGES + "field_obj.gif";
	public static final String IMG_METHOD = IMAGES + "method_obj.gif";
	public static final String IMG_LOCAL_VAR = IMAGES + "localvariable_obj.gif";
	
	public static final String IMG_ANNOTATION = IMAGES + "annotation_alt_obj.gif";
	
	private static final String WIZ = IMAGES + "wiz/";
	
	public static final String IMG_WIZ_PACKAGE = WIZ + "newpack_wiz.gif";
	public static final String IMG_WIZ_TYPE_CLASS = WIZ + "newclass_wiz.gif";
	public static final String IMG_WIZ_TYPE_INTERFACE = WIZ + "newint_wiz.gif";
	public static final String IMG_WIZ_TYPE_ENUM = WIZ + "newenum_wiz.gif";
	public static final String IMG_WIZ_TYPE_ANNOTATION = WIZ + "newannotation_wiz.gif";
	
	private static final String DECORATOR = IMAGES + "decorator/";
	
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
	
//	public static String getImagePath(String image) {
//		return "org.flowerplatform.codesync.code.java/images/" + image;
//	}
//	
//	public static String getImagePathFromPublicResources(String image) {
//		return CodeSyncCodeJavaPlugin.getInstance().getResourceUrl("/images/" + image);
//	}
}
