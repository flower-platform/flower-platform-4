package org.flowerplatform.flex_client.server.blazeds;

import org.flowerplatform.core.CorePlugin;

import flex.messaging.FactoryInstance;
import flex.messaging.FlexFactory;
import flex.messaging.config.ConfigMap;

/**
 * This interface is implemented by factory components which provide
 * instances to the flex messaging framework.  To configure flex data services
 * to use this factory, add the following lines to your services-config.xml
 * file (located in the WEB-INF/flex directory of your web application).
 *
 *	&lt;factories&gt;
 *     &lt;factory id="Flower" class="flex.samples.factories.FlowerFactory" /&gt;
 *  &lt;/factories&gt;
 *
 * You also must configure the web application to use Flower and must copy the Flower.jar
 * file into your WEB-INF/lib directory.  To configure your app server to use Flower,
 * you add the following lines to your WEB-INF/web.xml file:
 *
 *   &lt;context-param&gt;
 *        &lt;param-name&gt;contextConfigLocation&lt;/param-name&gt;
 *        &lt;param-value&gt;/WEB-INF/applicationContext.xml&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 *
 *   &lt;listener&gt;
 *       &lt;listener-class&gt;org.Flowerframework.web.context.ContextLoaderListener&lt;/listener-class&gt;
 *   &lt;/listener&gt;
 * 
 * Then you put your Flower bean configuration in WEB-INF/applicationContext.xml (as per the
 * line above).  For example:
 * 
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *  &lt;!DOCTYPE beans PUBLIC "-//Flower//DTD BEAN//EN" "http://www.Flowerframework.org/dtd/Flower-beans.dtd"&gt;
 *   
 *  &lt;beans&gt;
 *    &lt;bean name="weatherBean" class="dev.weather.WeatherService" singleton="true"/&gt;
 *  &lt;/beans&gt;
 *  
 * Now you are ready to define a destination in flex that maps to this existing service. 
 * To do this you'd add this to your WEB-INF/flex/remoting-config.xml:
 *
 *  &lt;destination id="WeatherService"&gt;
 *      &lt;properties&gt;
 *          &lt;factory&gt;Flower&lt;/factory&gt;
 *          &lt;source&gt;weatherBean&lt;/source&gt;
 *      &lt;/properties&gt;
 *  &lt;/destination&gt;
 *
 * @author Jeff Vroom
 */
public class FlowerFactory implements FlexFactory
{
    private static final String SOURCE = "source";

    /**
     * This method can be used to initialize the factory itself.  It is called with configuration
     * parameters from the factory tag which defines the id of the factory.  
     */
    public void initialize(String id, ConfigMap configMap) {}

    /**
     * This method is called when we initialize the definition of an instance 
     * which will be looked up by this factory.  It should validate that
     * the properties supplied are valid to define an instance.
     * Any valid properties used for this configuration must be accessed to 
     * avoid warnings about unused configuration elements.  If your factory 
     * is only used for application scoped components, this method can simply
     * return a factory instance which delegates the creation of the component
     * to the FactoryInstance's lookup method.
     */
    public FactoryInstance createFactoryInstance(String id, ConfigMap properties)
    {
        FlowerFactoryInstance instance = new FlowerFactoryInstance(this, id, properties);
        instance.setSource(properties.getPropertyAsString(SOURCE, instance.getId()));
        return instance;
    } // end method createFactoryInstance()

    /**
     * Returns the instance specified by the source
     * and properties arguments.  For the factory, this may mean
     * constructing a new instance, optionally registering it in some other
     * name space such as the session or JNDI, and then returning it
     * or it may mean creating a new instance and returning it.
     * This method is called for each request to operate on the
     * given item by the system so it should be relatively efficient.
     * <p>
     * If your factory does not support the scope property, it
     * report an error if scope is supplied in the properties
     * for this instance.
     */
    public Object lookup(FactoryInstance inst)
    {
        FlowerFactoryInstance factoryInstance = (FlowerFactoryInstance) inst;
        return factoryInstance.lookup();
    } 


    static class FlowerFactoryInstance extends FactoryInstance
    {
        FlowerFactoryInstance(FlowerFactory factory, String id, ConfigMap properties)
        {
            super(factory, id, properties);
        }


        public String toString()
        {
            return "FlowerFactory instance for id=" + getId() + " source=" + getSource() + " scope=" + getScope();
        }

        public Object lookup() 
        {
                return CorePlugin.getInstance().getServiceRegistry().getService(getSource()); 
        }
        
    } 

} 

