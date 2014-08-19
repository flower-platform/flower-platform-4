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
package org.flowerplatform.codesync.code.java.adapter;

import static org.flowerplatform.core.CoreConstants.NAME;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;

/**
 * Mapped to {@link Modifier}.
 * 
 * @author Mariana
 */
public class JavaModifierModelAdapter extends JavaAbstractAstNodeModelAdapter {

	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return ((Modifier) element).getKeyword().toString();
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature,
			Object correspondingChild, IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncJavaConstants.MODIFIERS.equals(feature)) {
			if (!(parent instanceof BodyDeclaration || parent instanceof SingleVariableDeclaration)) {
				throw new RuntimeException("Cannot create modifier for " + parent);
			}
			Modifier modifier = null;
			
			AST ast = ((ASTNode) parent).getAST();
			String keyword = (String) correspondingModelAdapterSet.getModelAdapter(correspondingChild, codeSyncAlgorithm)
					.getValueFeatureValue(correspondingChild, NAME, null, codeSyncAlgorithm);
			modifier = ast.newModifier(ModifierKeyword.toKeyword(keyword));
			addModifier((ASTNode) parent, modifier);
			return modifier;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, correspondingModelAdapterSet, codeSyncAlgorithm);
	}
	
}