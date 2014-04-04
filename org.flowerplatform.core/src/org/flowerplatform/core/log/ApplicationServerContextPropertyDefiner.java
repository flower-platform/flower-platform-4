package org.flowerplatform.core.log;

import org.eclipse.osgi.framework.internal.core.FrameworkProperties;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * @see logback.xml#define applicationServerContext
 * @author Sorin
 */
@SuppressWarnings("restriction")
public class ApplicationServerContextPropertyDefiner extends PropertyDefinerBase {

	@Override
	public String getPropertyValue() {
		return FrameworkProperties.getProperty("flower.server.app.context");
	}
}
