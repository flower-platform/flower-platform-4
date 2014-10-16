package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.ENTITY;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATES;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.LABEL;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.Collections;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateModelAdapter extends AbstractModelAdapter {

	public CodeSyncTemplateModelAdapter() {
		containmentFeatures.add(INNER_TEMPLATES);
		
		valueFeatures.add(TEMPLATE);
		valueFeatures.add(ENTITY);
		valueFeatures.add(LABEL);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getVelocityContext(element).get(NAME);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (INNER_TEMPLATES.equals(feature)) {
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (INNER_TEMPLATES.equals(feature)) {
			NestedVelocityContext child = new NestedVelocityContext();
			if (parent instanceof NestedVelocityContext) {
				child.setParent((NestedVelocityContext) parent);
			} else if (parent instanceof CodeSyncFile) {
				child.setParent((NestedVelocityContext) ((CodeSyncFile) parent).getFileInfo());
			}
			return child;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, modelAdapterSet, codeSyncAlgorithm);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (valueFeatures.contains(feature)) {
			return (getVelocityContext(element).get((String) feature));
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (valueFeatures.contains(feature)) {
			getVelocityContext(element).put((String) feature, value);
		} else {
			super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
		}
	}

	private NestedVelocityContext getVelocityContext(Object element) {
		return (NestedVelocityContext) element;
	}

}
