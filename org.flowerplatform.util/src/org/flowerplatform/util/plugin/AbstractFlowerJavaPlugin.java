/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.util.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 */
public abstract class AbstractFlowerJavaPlugin implements BundleActivator {

	public static final String PUBLIC_RESOURCES_DIR = "public-resources";
	
	public static final String MESSAGES_FILE = "messages.properties";

	private final static Logger logger = LoggerFactory.getLogger(AbstractFlowerJavaPlugin.class);
	
	private BundleContext bundleContext;
	
	private ResourceBundle resourceBundle;
	
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	protected String getMessagesFilePath() {
		return getBundleContext().getBundle().getSymbolicName() + "/" + PUBLIC_RESOURCES_DIR + "/" + MESSAGES_FILE;
	}
	
	public void registerMessageBundle() throws Exception {
		String messageFilePath = getMessagesFilePath();
		URL messagesFileUrl;
		InputStream inputStream = null;
		
		try {
			messagesFileUrl = new URL("platform:/plugin/" + messageFilePath);
			inputStream = messagesFileUrl.openStream();
			resourceBundle = new PropertyResourceBundle(inputStream);
		} catch (IOException e) {
			logger.warn(String.format("For bundle %s cannot find (or we had exception while loading) corresponding resources bundle/file %s", getBundleContext().getBundle().getSymbolicName(), messageFilePath), e);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	protected void setupExtensionPointsAndExtensions() throws Exception {
		// nothing to do here (yet)
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.bundleContext = context;
		
		setupExtensionPointsAndExtensions();
		registerMessageBundle();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.bundleContext = null;
	}
	
	public String getMessage(String messageKey, Object... substitutions) {
		String message = resourceBundle.getString(messageKey);
		if (substitutions.length == 0) {
			return message;
		} else {
			return MessageFormat.format(message, substitutions);
		}
	}

	public String getResourceUrl(String resource) {
		if (getBundleContext() == null) {
			return null;
		}
		return "servlet/" + PUBLIC_RESOURCES_DIR + "/" + 
				getBundleContext().getBundle().getSymbolicName() + "/" +
				resource;
	}
	
}