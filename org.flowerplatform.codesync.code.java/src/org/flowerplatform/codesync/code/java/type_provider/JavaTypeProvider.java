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
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.ANNOTATION_TYPE;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.CLASS;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.ENUM;
import static org.flowerplatform.codesync.code.java.adapter.JavaTypeModelAdapter.INTERFACE;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.flowerplatform.codesync.code.CodeSyncCodePlugin;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
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
		} else {
			return directMap.get(object.getClass());
		}
	}

}
