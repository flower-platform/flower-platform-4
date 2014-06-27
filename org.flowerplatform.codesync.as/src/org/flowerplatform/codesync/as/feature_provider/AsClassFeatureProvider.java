package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.STATEMENTS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;

import org.flowerplatform.codesync.as.adapter.AsClassModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

/**
 * @see AsClassModelAdapter
 * 
 * @author Mariana Gheorghe
 */
public class AsClassFeatureProvider extends NodeFeatureProvider {

	public AsClassFeatureProvider() {
		valueFeatures.add(SUPER_CLASS);
		valueFeatures.add(DOCUMENTATION);
		containmentFeatures.add(STATEMENTS);
		containmentFeatures.add(SUPER_INTERFACES);
		containmentFeatures.add(MODIFIERS);
	}
	
}
