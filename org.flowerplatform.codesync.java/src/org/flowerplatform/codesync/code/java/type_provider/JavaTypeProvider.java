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
package org.flowerplatform.codesync.code.java.type_provider;

import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_MEMBER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.CLASS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM_CONSTANT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ENUM_CONSTANT_ARGUMENT;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.INTERFACE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MEMBER_VALUE_PAIR;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.MODIFIER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.OPERATION;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.PARAMETER;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.SUPER_INTERFACE;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Mariana Gheorghe
 */
public class JavaTypeProvider implements ITypeProvider {

	private Map<Class<?>, String> directMap = new HashMap<Class<?>, String>();
	
	public JavaTypeProvider() {
		directMap.put(EnumDeclaration.class, ENUM);
		directMap.put(AnnotationTypeDeclaration.class, ANNOTATION_TYPE);
		directMap.put(FieldDeclaration.class, ATTRIBUTE);
		directMap.put(MethodDeclaration.class, OPERATION);
		directMap.put(SingleVariableDeclaration.class, PARAMETER);
		directMap.put(Modifier.class, MODIFIER);
		directMap.put(MemberValuePair.class, MEMBER_VALUE_PAIR);
		directMap.put(EnumConstantDeclaration.class, ENUM_CONSTANT);
		directMap.put(AnnotationTypeMemberDeclaration.class, ANNOTATION_MEMBER);
	}
	
	@Override
	public String getType(Object object, CodeSyncAlgorithm codeSyncAlgorithm) {
		IFileAccessController fileAccessController = codeSyncAlgorithm.getFileAccessController();
		if (object instanceof CodeSyncFile) {
			Object file = ((CodeSyncFile) object).getFile();
			if (fileAccessController.isFile(file)) {
				if (fileAccessController.isDirectory(file)) {
					return CodeSyncConstants.FOLDER;
				} else {
					return CodeSyncConstants.FILE;
				}
			}
		} else if (object instanceof TypeDeclaration) {
			if (((TypeDeclaration) object).isInterface()) {
				return INTERFACE;
			} else {
				return CLASS;
			}
		} else if (object instanceof Annotation) {
			return ANNOTATION;
		} else if (object instanceof Expression || object instanceof Type) {
			ASTNode node = (ASTNode) object;
			StructuralPropertyDescriptor descriptor = node.getLocationInParent();
			if (descriptor.equals(TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY)
				|| descriptor.equals(EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY)) {
					return SUPER_INTERFACE;
			} else if (descriptor.equals(EnumConstantDeclaration.ARGUMENTS_PROPERTY)) {
				return ENUM_CONSTANT_ARGUMENT;
			}
			return null;
		}
		return directMap.get(object.getClass());
	}

}