/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.code.java.type_provider;

import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationModelAdapter.ANNOTATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter.ANNOTATION_MEMBER;
import static org.flowerplatform.codesync.code.java.adapter.JavaAttributeModelAdapter.ATTRIBUTE;
import static org.flowerplatform.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter.ENUM_CONSTANT;
import static org.flowerplatform.codesync.code.java.adapter.JavaMemberValuePairModelAdapter.MEMBER_VALUE_PAIR;
import static org.flowerplatform.codesync.code.java.adapter.JavaModifierModelAdapter.MODIFIER;
import static org.flowerplatform.codesync.code.java.adapter.JavaOperationModelAdapter.OPERATION;
import static org.flowerplatform.codesync.code.java.adapter.JavaParameterModelAdapter.PARAMETER;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.CLASS;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.ENUM;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeDeclarationModelAdapter.INTERFACE;
import static org.flowerplatform.codesync.code.java.feature_provider.JavaEnumConstantDeclarationFeatureProvider.ENUM_CONSTANT_ARGUMENT;
import static org.flowerplatform.codesync.code.java.feature_provider.JavaTypeDeclarationFeatureProvider.SUPER_INTERFACE;

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
import org.flowerplatform.codesync.code.CodeSyncCodePlugin;
import org.flowerplatform.codesync.type_provider.ClassTypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Mariana Gheorghe
 */
public class JavaTypeProvider extends ClassTypeProvider {

	public JavaTypeProvider() {
		classToTypeMap.put(EnumDeclaration.class, ENUM);
		classToTypeMap.put(AnnotationTypeDeclaration.class, ANNOTATION_TYPE);
		classToTypeMap.put(FieldDeclaration.class, ATTRIBUTE);
		classToTypeMap.put(MethodDeclaration.class, OPERATION);
		classToTypeMap.put(SingleVariableDeclaration.class, PARAMETER);
		classToTypeMap.put(Modifier.class, MODIFIER);
		classToTypeMap.put(MemberValuePair.class, MEMBER_VALUE_PAIR);
		classToTypeMap.put(EnumConstantDeclaration.class, ENUM_CONSTANT);
		classToTypeMap.put(AnnotationTypeMemberDeclaration.class, ANNOTATION_MEMBER);
	}
	
	@Override
	public String getType(Object object) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		if (fileAccessController.isFile(object)) {
			if (fileAccessController.isDirectory(object)) {
				return CodeSyncCodePlugin.FOLDER;
			} else {
				return CodeSyncCodePlugin.FILE;
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
			if (descriptor.equals(TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY) ||
					descriptor.equals(EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY)) {
				return SUPER_INTERFACE;
			} else if (descriptor.equals(EnumConstantDeclaration.ARGUMENTS_PROPERTY)) {
				return ENUM_CONSTANT_ARGUMENT;
			}
			return null;
		} else {
			return super.getType(object);
		}
	}

}
