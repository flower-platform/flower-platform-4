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
package org.flowerplatform.codesync.code.java.adapter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.feature_provider.JavaEnumConstantDeclarationFeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to {@link EnumConstantDeclaration}. Children are enum constant arguments (i.e. {@link Expression}).
 * 
 * @see JavaEnumConstantDeclarationFeatureProvider
 * 
 * @author Mariana	
 */
public class JavaEnumConstantDeclarationModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public Object getMatchKey(Object element) {
		return getEnumConstant(element).getName().getIdentifier();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getMatchKey(element);
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CoreConstants.NAME.equals(feature)) {
			EnumConstantDeclaration enumConstant = getEnumConstant(element);
			enumConstant.setName(enumConstant.getAST().newSimpleName((String) value));
		}
		super.setValueFeatureValue(element, feature, value);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncCodeJavaConstants.ENUM_CONSTANT_ARGUMENTS.equals(feature)) {
			return getEnumConstant(element).arguments();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
		if (CodeSyncCodeJavaConstants.ENUM_CONSTANT_ARGUMENTS.equals(feature)) {
			EnumConstantDeclaration parent = (EnumConstantDeclaration) element;
			AST ast = parent.getAST();
			Expression arg = getExpressionFromString(ast, ((Node) correspondingChild).getOrPopulateProperties().get(CoreConstants.NAME).toString());
			parent.arguments().add(arg);
			return arg;
		}
		return super.createChildOnContainmentFeature(element, feature, correspondingChild, typeProvider);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}
	
	private EnumConstantDeclaration getEnumConstant(Object element) {
		return (EnumConstantDeclaration) element;
	}

}