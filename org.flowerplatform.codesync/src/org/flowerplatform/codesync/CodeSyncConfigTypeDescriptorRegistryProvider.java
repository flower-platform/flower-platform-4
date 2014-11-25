package org.flowerplatform.codesync;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.core.node.resource.in_memory.InMemoryResourceService;
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
			
			if (resourceUri != null) {
				InMemoryResourceService service = (InMemoryResourceService) CorePlugin.getInstance().getResourceService();
				String configDirsKey = service.getConfigDirsKey(resourceUri);
				if (configDirsKey != null) {
					return CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
							.getOrLoadCodeSyncConfig(configDirsKey);
				}
			}
		}
		return null;
	}

}
