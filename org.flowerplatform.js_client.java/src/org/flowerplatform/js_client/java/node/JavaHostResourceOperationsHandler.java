package org.flowerplatform.js_client.java.node;

import java.util.List;

import org.mozilla.javascript.Function;

/**
 * @author Cristina Constantinescu
 */
public class JavaHostResourceOperationsHandler {

	public Object nodeRegistryManager;

	public void updateGlobalDirtyState(Object someResourceNodeHasBecomeDirty) {	
		System.out.println(someResourceNodeHasBecomeDirty);
	}
	
	public void showSaveDialog(Object[] nodeRegistries, List<?> dirtyResourceNodes, Function handler) {	
		// TODO implement
	}
	
	public void showReloadDialog(Object[] nodeRegistries, Object[] resourceSets) {		
		// TODO implement
	}
	
}
