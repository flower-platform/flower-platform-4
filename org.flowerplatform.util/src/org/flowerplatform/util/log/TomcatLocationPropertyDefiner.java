package org.flowerplatform.util.log;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * @see logback.xml#define tomcatLocation
 * @author Sorin
 */
public class TomcatLocationPropertyDefiner extends PropertyDefinerBase {

	@Override
	public String getPropertyValue() {
		return System.getProperty("catalina.base");
	}
}
