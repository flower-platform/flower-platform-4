package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAG_ATTRIBUTES;

import org.flowerplatform.codesync.as.adapter.AsMetaTagModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsMetaTagModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsMetaTagFeatureProvider extends NodeFeatureProvider {

	public AsMetaTagFeatureProvider() {
		containmentFeatures.add(META_TAG_ATTRIBUTES);
	}
	
}
