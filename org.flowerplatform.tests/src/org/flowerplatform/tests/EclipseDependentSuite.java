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
