package org.flowerplatform.codesync.as.feature_provider;

import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.adapter.AsFileModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsFileModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsFileFeatureProvider extends NodeFeatureProvider {

	public AsFileFeatureProvider() {
		containmentFeatures.add(CodeSyncAsConstants.STATEMENTS);
	}
	
}
