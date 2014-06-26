package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TYPED_ELEMENT_TYPE;

import org.flowerplatform.codesync.as.adapter.AsParameterModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsParameterModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsParameterFeatureProvider extends NodeFeatureProvider {

	public AsParameterFeatureProvider() {
		valueFeatures.add(TYPED_ELEMENT_TYPE);
		containmentFeatures.add(MODIFIERS);
	}
	
}
