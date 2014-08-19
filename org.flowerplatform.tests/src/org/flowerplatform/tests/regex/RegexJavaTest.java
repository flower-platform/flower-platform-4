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
package org.flowerplatform.tests.regex;

import static org.flowerplatform.tests.regex.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.tests.regex.JavaRegexConfigurationProvider.METHOD_CATEGORY;
import static org.flowerplatform.tests.regex.JavaRegexConfigurationProvider.buildJavaConfiguration;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexException;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Cristi
 * @author Sorin
 */
public class RegexJavaTest extends RegexTestBase {
	
	private static final String testFile1Path = FILES_ROOT_DIR + "TestFile1.java";
	
	private static final int numberOfFieldsInFile1 = 28;
	
	private static final String fieldPreffixInFile1 = "attr";
	
	private static final int numberOfMethodsInFile1 = 3;
	
	private static final String methodsPreffixInFile1 = "meth";

	private CharSequence testFile1;

	@Before
	public void setUp() {
		testFile1 = TestUtil.readFile(testFile1Path);
	}

	@Test
	public void testJavaDeclarationsExist() {
		RegexConfiguration config = new RegexTestBase.CategoryRecorderRegexConfiguration();
		buildJavaConfiguration(config);
		RegexTestBase.CategoryRecorderRegexProcessingSession session = (RegexTestBase.CategoryRecorderRegexProcessingSession) config.startSession(testFile1);
		
		try {
			session.find(null);
		} catch (RegexException e) {
			throw new RuntimeException(e);
		}
		
		assertAllExpectedElementsFound("attributes", session.getRecorderCategory(ATTRIBUTE_CATEGORY), fieldPreffixInFile1, 1, numberOfFieldsInFile1);
		assertAllExpectedElementsFound("methods", session.getRecorderCategory(METHOD_CATEGORY), methodsPreffixInFile1, 1, numberOfMethodsInFile1);
	}
	
	@Test
	public void testJavaFindRange() {
		RegexConfiguration config = new RegexConfiguration();
		JavaRegexConfigurationProvider.buildJavaConfiguration(config);
		RegexProcessingSession session = config.startSession(testFile1);
		String testInputFileAsString = testFile1.toString();
		
		for (int i = 1; i <= numberOfFieldsInFile1; i++) {
			session.reset(true);
			
			String elementName = fieldPreffixInFile1 + i;
			
			try {
				int[] range = session.findRangeFor(JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY, elementName);
				assertIdentifierInRange(elementName, range, testInputFileAsString);
			} catch (RegexException e) {
				throw new RuntimeException(e);
			}
			
		}
	}
}