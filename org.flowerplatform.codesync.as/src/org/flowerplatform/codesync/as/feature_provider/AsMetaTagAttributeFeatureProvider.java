package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTE_VALUE;

import org.flowerplatform.codesync.as.adapter.AsMetaTagAttributeModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsMetaTagAttributeModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsMetaTagAttributeFeatureProvider extends NodeFeatureProvider {

	public AsMetaTagAttributeFeatureProvider() {
		valueFeatures.add(META_TAG_ATTRIBUTE_VALUE);
	}
	
}
