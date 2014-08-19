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

import static org.flowerplatform.tests.regex.ActionscriptRegexConfigurationProvider.buildASConfiguration;
import static org.flowerplatform.tests.regex.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.tests.regex.JavaRegexConfigurationProvider.METHOD_CATEGORY;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexException;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegexActionscriptTest extends RegexTestBase {
	

	private static final String testFile2Path = FILES_ROOT_DIR + "TestFile2.as";
	
	private static CharSequence testFile2;

	private List<String> testFile2_attributeNames = Arrays.asList(
								"attr1", 
								"attr2", 
								"attr3", 
								"attr4", 
								"_attr5", 
								"$attr6");
	
	private List<String> testFile2_methodNames = Arrays.asList(
								"method1", 
								"method2", 
								"method3", 
								"method4", 
								"method5");
	@BeforeClass
	public static void loadTestFile() {
		testFile2 = TestUtil.readFile(testFile2Path);
	}

	@Test
	public void testActionscriptDeclarationsExist() {
		RegexConfiguration config = new RegexTestBase.CategoryRecorderRegexConfiguration();
		buildASConfiguration(config);
		RegexTestBase.CategoryRecorderRegexProcessingSession session = (RegexTestBase.CategoryRecorderRegexProcessingSession) config.startSession(testFile2);
		
		try {
			session.find(null);
		} catch (RegexException e) {
			throw new RuntimeException(e);
		}
		
		assertAllExist(session.getRecorderCategory(ATTRIBUTE_CATEGORY), testFile2_attributeNames);
		assertAllExist(session.getRecorderCategory(METHOD_CATEGORY), testFile2_methodNames);
	}
	
	@Test
	public void testActionscriptFindRange() {
		RegexConfiguration config = new RegexConfiguration();
		ActionscriptRegexConfigurationProvider.buildASConfiguration(config);
		RegexProcessingSession session = config.startSession(testFile2);
		
		String testFileContent = testFile2.toString();
		
		/////////////////////////////
		// Do action/Check result: range for a given attribute is correct
		/////////////////////////////
	
		for (String attributeName : testFile2_attributeNames) {
			session.reset(true);
			int[] range;
			try {
				range = session.findRangeFor(ATTRIBUTE_CATEGORY, attributeName);
				assertIdentifierInRange(attributeName, range, testFileContent);
			} catch (RegexException e) {
				throw new RuntimeException(e);
			}			
		}
		
		/////////////////////////////
		// Do action/Check result: range for a given method is correct
		/////////////////////////////

		for (String methodName : testFile2_methodNames) {
			session.reset(true);
			try {
				int[] range = session.findRangeFor(METHOD_CATEGORY, methodName);
				assertIdentifierInRange(methodName, range, testFileContent);
			} catch (RegexException e) {
				throw new RuntimeException(e);
			}			
		}
	}
}