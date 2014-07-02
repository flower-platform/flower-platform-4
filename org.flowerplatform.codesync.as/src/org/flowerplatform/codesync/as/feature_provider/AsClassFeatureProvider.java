package org.flowerplatform.codesync.as.feature_provider;

import static org.flowerplatform.codesync.as.CodeSyncAsConstants.DOCUMENTATION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.META_TAGS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIERS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.STATEMENTS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACES;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VISIBILITY;

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
		valueFeatures.add(VISIBILITY);
		
		containmentFeatures.add(STATEMENTS);
		containmentFeatures.add(META_TAGS);
		containmentFeatures.add(SUPER_INTERFACES);
		containmentFeatures.add(MODIFIERS);
	}
	
}
