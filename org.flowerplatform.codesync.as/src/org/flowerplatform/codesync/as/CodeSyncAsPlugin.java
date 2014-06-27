package org.flowerplatform.codesync.as;

import static org.flowerplatform.codesync.CodeSyncConstants.FEATURE_PROVIDER;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_RIGHT;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_FUNCTION;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_SUPER_INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_TYPE_CLASS;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_TYPE_INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.IMG_VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.MODIFIER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.PARAMETER;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.SUPER_INTERFACE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.TECHNOLOGY;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.VARIABLE;
import static org.flowerplatform.codesync.as.CodeSyncAsConstants.getImagePath;
import static org.flowerplatform.codesync.code.CodeSyncCodeConstants.FILE;
import static org.flowerplatform.codesync.code.CodeSyncCodeConstants.FOLDER;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsClassModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsFileModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsFunctionModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsIdentifierModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsParameterModelAdapter;
import org.flowerplatform.codesync.as.adapter.AsVariableModelAdapter;
import org.flowerplatform.codesync.as.feature_provider.AsClassFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsFileFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsFunctionFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsParameterFeatureProvider;
import org.flowerplatform.codesync.as.feature_provider.AsVariableFeatureProvider;
import org.flowerplatform.codesync.as.type_provider.AsTypeProvider;
import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.codesync.code.feature_provider.FolderFeatureProvider;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncAsPlugin extends AbstractFlowerJavaPlugin {

protected static CodeSyncAsPlugin INSTANCE;
	
	public static CodeSyncAsPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CodeSyncPlugin.getInstance().addTypeProvider(TECHNOLOGY, new AsTypeProvider());
		
		createNodeTypeDescriptor(FOLDER, new FolderModelAdapter(), new FolderFeatureProvider());
		createNodeTypeDescriptor(FILE, new AsFileModelAdapter(), new AsFileFeatureProvider());
	
		AsClassModelAdapter classModelAdapter = new AsClassModelAdapter();
		AsClassFeatureProvider classFeatureProvider = new AsClassFeatureProvider();
		
		createNodeTypeDescriptor(CLASS, classModelAdapter, classFeatureProvider)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_TYPE_CLASS)));
		
		createNodeTypeDescriptor(INTERFACE, classModelAdapter, classFeatureProvider)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_TYPE_INTERFACE)));
		
		AsIdentifierModelAdapter identifierModelAdapter = new AsIdentifierModelAdapter();
		NodeFeatureProvider identifierFeatureProvider = new NodeFeatureProvider();
		
		createNodeTypeDescriptor(SUPER_INTERFACE, identifierModelAdapter, identifierFeatureProvider)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_SUPER_INTERFACE)));
		
		createNodeTypeDescriptor(VARIABLE, new AsVariableModelAdapter(), new AsVariableFeatureProvider())
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_VARIABLE)));
		
		createNodeTypeDescriptor(FUNCTION, new AsFunctionModelAdapter(), new AsFunctionFeatureProvider())
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICONS, getImagePath(IMG_FUNCTION)));
	
		createNodeTypeDescriptor(MODIFIER, identifierModelAdapter, identifierFeatureProvider);
		createNodeTypeDescriptor(PARAMETER, new AsParameterModelAdapter(), new AsParameterFeatureProvider());
	}
	
	private TypeDescriptor createNodeTypeDescriptor(String type, AbstractModelAdapter modelAdapterRight, FeatureProvider featureProvider) {
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type);
		descriptor.addCategory(CodeSyncConstants.CATEGORY_CODESYNC);
//		descriptor.addSingleController(MODEL_ADAPTER_LEFT, modelAdapterRight);
//		descriptor.addSingleController(MODEL_ADAPTER_ANCESTOR, modelAdapterRight);
		descriptor.addSingleController(MODEL_ADAPTER_RIGHT, modelAdapterRight);
		descriptor.addSingleController(FEATURE_PROVIDER, featureProvider);
		return descriptor;
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
}
