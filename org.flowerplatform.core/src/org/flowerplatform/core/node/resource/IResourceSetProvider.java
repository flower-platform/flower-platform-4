package org.flowerplatform.core.node.resource;

import java.net.URI;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceSetProvider {

	String getResourceSet(URI resourceUri);
	
}
