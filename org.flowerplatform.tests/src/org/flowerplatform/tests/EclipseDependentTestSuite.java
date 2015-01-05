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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.tests.codesync.CodeSyncSdiffTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

//CHECKSTYLE:OFF
/**
 * @author Mariana Gheorghe
 **/
@RunWith(EclipseDependentSuite.class)
@SuiteClasses({ 
	EclipseDependentTest.class,
	CodeSyncSdiffTest.class
//	SecurityPermissionsTests.class,
//	ListenerTestSuite.class,
//	ChangesProcessorTest.class
})
public class EclipseDependentTestSuite {
//CHECKSTYLE:ON	
	
	/**
	 * 
	 */
	@BeforeClass
	public static void beforeClass() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession httpSession = mock(HttpSession.class);
		when(request.getSession()).thenReturn(httpSession);
		when(httpSession.getId()).thenReturn("mockId");
		CorePlugin.getInstance().getRequestThreadLocal().set(request);
	}
	
}