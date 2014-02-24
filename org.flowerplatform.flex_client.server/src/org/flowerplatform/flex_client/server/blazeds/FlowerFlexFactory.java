package org.flowerplatform.flex_client.server.blazeds;

import org.flowerplatform.core.CorePlugin;

import flex.messaging.FactoryInstance;
import flex.messaging.FlexFactory;
import flex.messaging.config.ConfigMap;

/**
 * We use this factory, instead of the default one, because only one instance of
 * the service must be used, and the default factory was creating a new instance
 * every time a method of the service was invoked.
 * <p>
 * Inspired form <a href=
 * "http://www.epseelon.com/2008/04/15/flex-spring-and-blazeds-the-full-stack-part-4/"
 * >http://www.epseelon.com/2008/04/15/flex-spring-and-blazeds-the-full-stack-
 * part-4/</a>
 * 
 * @author Sebastian Solomon
 */
public class FlowerFlexFactory implements FlexFactory {
	private static final String SOURCE = "source";

	/**
	 * This method can be used to initialize the factory itself. It is called
	 * with configuration parameters from the factory tag which defines the id
	 * of the factory.
	 */
	public void initialize(String id, ConfigMap configMap) {
	}

	/**
	 * This method is called when we initialize the definition of an instance
	 * which will be looked up by this factory. It should validate that the
	 * properties supplied are valid to define an instance. Any valid properties
	 * used for this configuration must be accessed to avoid warnings about
	 * unused configuration elements. If your factory is only used for
	 * application scoped components, this method can simply return a factory
	 * instance which delegates the creation of the component to the
	 * FactoryInstance's lookup method.
	 */
	public FactoryInstance createFactoryInstance(String id, ConfigMap properties) {
		FlowerFactoryInstance instance = new FlowerFactoryInstance(this, id,
				properties);
		instance.setSource(properties.getPropertyAsString(SOURCE,
				instance.getId()));
		return instance;
	}

	/**
	 * Returns the instance specified by the source and properties arguments.
	 * For the factory, this may mean constructing a new instance, optionally
	 * registering it in some other name space such as the session or JNDI, and
	 * then returning it or it may mean creating a new instance and returning
	 * it. This method is called for each request to operate on the given item
	 * by the system so it should be relatively efficient.
	 * <p>
	 * If your factory does not support the scope property, it report an error
	 * if scope is supplied in the properties for this instance.
	 */
	public Object lookup(FactoryInstance inst) {
		FlowerFactoryInstance factoryInstance = (FlowerFactoryInstance) inst;
		return factoryInstance.lookup();
	}

	static class FlowerFactoryInstance extends FactoryInstance {
		FlowerFactoryInstance(FlowerFlexFactory factory, String id,
				ConfigMap properties) {
			super(factory, id, properties);
		}

		public String toString() {
			return "FlowerFactory instance for id=" + getId() + " source="
					+ getSource() + " scope=" + getScope();
		}

		public Object lookup() {
			return CorePlugin.getInstance().getServiceRegistry()
					.getService(getSource());
		}

	}

}
