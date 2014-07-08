package org.flowerplatform.diagram;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;


public class DiagramPlugin extends AbstractFlowerJavaPlugin {

	protected static DiagramPlugin INSTANCE;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.start(context);
		INSTANCE = this;

		//TODO CM: Register type descriptors and controllers; add "view" and "diagram" types to category "view"
		
		//TODO CM: add viewLink type to category "link"
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.stop(context);
		INSTANCE = null;
	}

	public static DiagramPlugin getInstance() {
		return INSTANCE;
	}
	
}
