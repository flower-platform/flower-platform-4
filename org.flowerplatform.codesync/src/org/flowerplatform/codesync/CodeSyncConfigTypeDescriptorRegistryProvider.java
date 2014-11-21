package org.flowerplatform.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.CODE_SYNC_CONFIG_PROPERTY_DIRS_KEY;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.ITypeDescriptorRegistryProvider;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncConfigTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {

	@Override
	public TypeDescriptorRegistry getTypeDescriptorRegistry(Object model) {
		if (model instanceof Node) {
			String nodeUri = ((Node) model).getNodeUri();
			
			// get resource uri
			IResourceHandler handler = CorePlugin.getInstance().getResourceService()
					.getResourceHandler(Utils.getScheme(nodeUri));
			String resourceUri = handler.getResourceUri(nodeUri);
			
			if (resourceUri == null || nodeUri.equals(resourceUri)) {
				return null;
			}
			
			// get resource node
			Node resourceNode = CorePlugin.getInstance().getResourceService().getNode(resourceUri);

			// get config dirs property
			String configDirsKey = (String) resourceNode.getPropertyValue(CODE_SYNC_CONFIG_PROPERTY_DIRS_KEY);
			if (configDirsKey != null) {
				return CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
						.getOrLoadCodeSyncConfig(configDirsKey);
			}
		}
		return null;
	}

}
