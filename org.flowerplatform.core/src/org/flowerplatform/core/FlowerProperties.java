/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central repository for configuration related properties.
 * 
 * <p>
 * For doc, rules and conventions, please see the wiki page	http://csp1/dokuwiki/proiecte/flower/general_instructions/properties_and_configuration
 * 
 * <p>
 * Note : all new {@link AddProperty} validators must be added also in the wiki.
 * 
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 * @author Cristina Brinza
 */
public class FlowerProperties extends Properties {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FlowerProperties.class);
	
	public static final long DB_VERSION = 0;
	
	private static final String PROPERTIES_FILE = "META-INF/flower-platform.properties";
	private static final String PROPERTIES_FILE_LOCAL = CoreConstants.FLOWER_PLATFORM_HOME + "/flower-platform.properties";
	
	/**
	 *@author see class
	 **/
	/* package */ FlowerProperties() {
		super();
		
		defaults = new Properties();
		
		// get properties from global and local file, if exists
		try {
			// get properties from global file first
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);	
			if (is != null) {
				this.load(is);
				IOUtils.closeQuietly(is);
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error while loading properties from %s file.", PROPERTIES_FILE), e);
		}
		
		try {
			// get properties from local file, if exists
			File file = new File(PROPERTIES_FILE_LOCAL);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				if (fis != null) {
					this.load(fis);
					IOUtils.closeQuietly(fis);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error while loading properties from %s file.", PROPERTIES_FILE_LOCAL), e);
		}
	}
	
	/**
	 *@author see class
	 **/
	public void addProperty(AddProperty p) {
		if (p.propertyName == null || p.propertyDefaultValue == null) {
			throw new IllegalArgumentException("Property name and default value shouldn't be null.");
		}

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Adding property with name = {}, default value = {}, user value = {}", 
					new Object[] { p.propertyName, p.propertyDefaultValue, get(p.propertyName) });
		}
		
		defaults.put(p.propertyName, p.propertyDefaultValue);

		String userValue = (String) get(p.propertyName);
		if (userValue != null) {
			// if the user has given a value, validate it
			String validationErrorMessage = p.validateProperty(userValue);
			if (validationErrorMessage != null) {
				// validation failed; 
				LOGGER.error("Property Validation Error! Failed to set property = {} to value = {}; reverting to default = {}. Reason: {}", 
						new Object[] {p.propertyName, userValue, p.propertyDefaultValue, validationErrorMessage});
				remove(p.propertyName);
			}
		} else {
			if (p.inputFromFileMandatory) {
				// Mariana: if the user did not provide a value and the property is mandatory => validation error
				LOGGER.error("Property Validation Error! Failed to provide a value for mandatory property = {}; default value is set to = {}.",
					new Object[] {p.propertyName, p.propertyDefaultValue});
			}
		}
	}
	
	public Properties getDefaults() {
		return defaults;
	}
	
	/**
	 *@author see class
	 **/
	public abstract static class AddProperty {
		
		protected String propertyName;
		
		protected String propertyDefaultValue;
		
		protected boolean inputFromFileMandatory = false;
		
		/**
		 *@author see class
		 **/
		public AddProperty(String propertyName, String propertyDefaultValue) {
			super();
			this.propertyName = propertyName;
			this.propertyDefaultValue = propertyDefaultValue;
		}

		/**
		 *@author see class
		 **/
		protected abstract String validateProperty(String input);
		
		/**
		 * Set to <code>true</code> if the user must provide a value for this property in the properties file. 
		 * If the user fails to provide a value, a validation error will be logged at property validation. 
		 * Default value is <code>false</code>, i.e. we allow <code>null</code> as a valid value for this property.
		 * 
		 * @author Mariana
		 */
		public AddProperty setInputFromFileMandatory(boolean isInputFromFileMandatory) {
			this.inputFromFileMandatory = isInputFromFileMandatory;
			return this;
		}
	}
	
	/**
	 * Accepts a boolean property; i.e. values can be 'true' or 'false'
	 */
	public static class AddBooleanProperty extends AddProperty {

		/**
		 *@author see class
		 **/
		public AddBooleanProperty(String propertyName,
				String propertyDefaultValue) {
			super(propertyName, propertyDefaultValue);
		}

		@Override
		protected String validateProperty(String input) {
			if ("true".equals(input) || "false".equals(input)) {
				return null;
			} else {
				return "Value should be 'true' or 'false'";
			}
		}
		
	}

	/**
	 *@author see class
	 **/
	public static class AddIntegerProperty extends AddProperty {

		/**
		 *@author see class
		 **/
		public AddIntegerProperty(String propertyName, String propertyDefaultValue) {
			super(propertyName, propertyDefaultValue);
		}

		@Override
		protected String validateProperty(String input) {
			try {
				Integer.valueOf(input);
				return null;
			} catch (Exception e) {
				return "Value is not a valid integer";
			}
		} 
	}
	
	/**
	 *@author see class
	 **/
	public static class AddStringProperty extends AddProperty {

		/**
		 *@author see class
		 **/
		public AddStringProperty(String propertyName, String propertyDefaultValue) {
			super(propertyName, propertyDefaultValue);
		}

		@Override
		protected String validateProperty(String input) {
			if (input == null || input.trim().length() == 0) {
				return "Value is null or empty";
			}
			return null;
		}
	}

}
