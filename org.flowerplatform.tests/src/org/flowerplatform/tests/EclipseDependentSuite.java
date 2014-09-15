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
package org.flowerplatform.tests;

import java.io.File;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Set Flower Platform home in the system properties
 * before the plugins are loaded. This property is
 * set from FlowerFrameworkLauncher in the servlet
 * container.
 * 
 * @author Mariana Gheorghe
 */
public class EclipseDependentSuite extends Suite {

	static {
		System.setProperty("FLOWER_PLATFORM_HOME", new File("").getAbsolutePath());
	}
	/**
	 *@author Branza Cristina
	 **/
	public EclipseDependentSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
	}
}
