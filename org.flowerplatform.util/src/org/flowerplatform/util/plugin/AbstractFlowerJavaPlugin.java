/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package org.flowerplatform.util.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.flowerplatform.util.UtilConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 */
public abstract class AbstractFlowerJavaPlugin implements BundleActivator {

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
		return getBundleContext().getBundle().getSymbolicName() + "/" + UtilConstants.PUBLIC_RESOURCES_DIR + "/" + UtilConstants.MESSAGES_FILE;
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
		return UtilConstants.PUBLIC_RESOURCES_SERVLET + "/" + 
				getBundleContext().getBundle().getSymbolicName() + "/" +
				resource;
	}
	
	/**
	 * Returns the request string for the image composed from the URLs. 
	 * E.g. <tt>servlet/image-composer/url1|url2|url3</tt>
	 * 
	 * <p>
	 * Checks if the first URL already contains the image-composer prefix; 
	 * this way it can be used to append images to the same string.
	 * 
	 * @see AbstractFlowerFlexPlugin#getImageComposerUrl()
	 * 
	 * @author Mariana Gheorghe
	 */
	public String getImageComposerUrl(String... resources) {
		if (resources.length == 0) {
			return null;
		}
		String composedUrl = "";
		for (String resource : resources) {
			if (resource != null) {
				composedUrl += (composedUrl.length() > 0 ? UtilConstants.RESOURCE_PATH_SEPARATOR : "") + resource;
			}
		}
		if (composedUrl.length() == 0) {
			return null;
		}
		if (composedUrl.indexOf(UtilConstants.IMAGE_COMPOSER_SERVLET) < 0) {
			composedUrl = UtilConstants.IMAGE_COMPOSER_SERVLET + composedUrl;
		}
		return composedUrl;
	}
	
}
