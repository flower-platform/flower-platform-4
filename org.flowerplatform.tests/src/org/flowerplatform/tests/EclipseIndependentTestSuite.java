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

import org.flowerplatform.tests.codesync.CodeSyncTestSuite;
import org.flowerplatform.tests.controllers.FileSystemControllersTest;
import org.flowerplatform.tests.core.CommandStackTest;
import org.flowerplatform.tests.core.CoreTestSuite;
import org.flowerplatform.tests.freeplane.XmlParserTest;
import org.flowerplatform.tests.regex.RegexTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//CHECKSTYLE:OFF
/**
 * @author Mariana Gheorghe
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CodeSyncTestSuite.class,
	FileSystemControllersTest.class,
	CommandStackTest.class,
	CoreTestSuite.class,
//	JsClientJavaTestSuite.class,
	XmlParserTest.class,
	RegexTestSuite.class
})
public class EclipseIndependentTestSuite {

	
}
