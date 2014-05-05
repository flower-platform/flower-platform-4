package org.flowerplatform.tests;

import org.eclipse.core.runtime.Assert;
import org.flowerplatform.core.CorePlugin;
import org.junit.Test;

public class EclipseDependentTest extends EclipseDependentTestSuiteBase {

	@Test
	public void test() {
		Assert.isNotNull(CorePlugin.getInstance(), "CorePlugin not started");
	}
	
}
