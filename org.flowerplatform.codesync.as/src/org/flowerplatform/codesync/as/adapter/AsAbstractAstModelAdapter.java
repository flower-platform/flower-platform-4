package org.flowerplatform.codesync.as.adapter;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import java.util.Arrays;
import java.util.List;

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.definitions.IDefinition;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;
import org.apache.flex.compiler.internal.scopes.ASFileScope;
import org.apache.flex.compiler.projects.ICompilerProject;
import org.apache.flex.compiler.scopes.IASScope;
import org.flowerplatform.codesync.as.asdoc.AsDocComment;
import org.flowerplatform.codesync.code.adapter.AstModelElementAdapter;
import org.flowerplatform.core.CoreConstants;

/**
 * Mapped to {@link IDefinition}.
 * 
 * @author Mariana Gheorghe
 */
public abstract class AsAbstractAstModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CoreConstants.NAME.equals(feature)) {
			return getDefinition(element).getBaseName();
		} else if (TYPED_ELEMENT_TYPE.equals(feature)) {
			return getDefinition(element).getTypeAsDisplayString();
		} else if (VISIBILITY.equals(feature)) {
			return getDefinition(element).getNamespaceReference().getBaseName();
		} else if (DOCUMENTATION.equals(feature)) {
			if (element instanceof IDocumentableDefinition) {
				IASDocComment comment = ((IDocumentableDefinition) element).getExplicitSourceComment();
				if (comment == null) {
					return null;
				}
				return ((AsDocComment) comment).getText();
			}
		} 
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (MODIFIERS.equals(feature)) {
			return Arrays.asList(getDefinition(element).getModifiers().getAllModifiers());
		} else if (META_TAGS.equals(feature)) {
			return Arrays.asList(getDefinition(element).getAllMetaTags());
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}
	
	protected ICompilerProject getProject(Object element) {
		IASScope scope = getDefinition(element).getContainingScope();
		while (!(scope instanceof ASFileScope)) {
			scope = scope.getContainingScope();
		}
		return ((ASFileScope) scope).getCompilationUnit().getProject();
	}
	
	protected IDefinition getDefinition(Object element) {
		return (IDefinition) element;
	}
	
	@Override
	public boolean hasChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}

}
