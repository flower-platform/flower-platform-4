package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE_INITIALIZER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

import org.flowerplatform.codesync.as.adapter.AsVariableModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsVariableModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsVariableFeatureProvider extends NodeFeatureProvider {

	public AsVariableFeatureProvider() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		valueFeatures.add(VISIBILITY);
		valueFeatures.add(VARIABLE_INITIALIZER);
		
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(MODIFIERS);
	}
	
}
