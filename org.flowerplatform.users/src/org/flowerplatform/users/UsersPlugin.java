package org.flowerplatform.users;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;


public class UsersPlugin extends AbstractFlowerJavaPlugin {

	protected static UsersPlugin INSTANCE;
	
	public static UsersPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("userService", new UserService());
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// no messages
	}
	
}
