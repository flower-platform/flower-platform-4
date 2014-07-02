package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_DEFAULT_VALUE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER_IS_REST;
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
		valueFeatures.add(PARAMETER_IS_REST);
		valueFeatures.add(PARAMETER_DEFAULT_VALUE);
		
		containmentFeatures.add(MODIFIERS);
	}
	
}
