package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION_PARAMETERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.flowerplatform.codesync.as.adapter.AsFunctionModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsFunctionModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsFunctionFeatureProvider extends NodeFeatureProvider {

	public AsFunctionFeatureProvider() {
		valueFeatures.add(DOCUMENTATION);
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		containmentFeatures.add(MODIFIERS);
		containmentFeatures.add(FUNCTION_PARAMETERS);
	}
	
}
