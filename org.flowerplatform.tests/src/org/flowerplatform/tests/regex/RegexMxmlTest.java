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

import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.METHOD_CATEGORY;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.tests.regex.sample_configs.MxmlRegexConfigurationProvider;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexException;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Cristi
 *
 */
public class RegexMxmlTest extends RegexTestBase {
	
	private static final String TEST_FILE_3_PATH = FILES_ROOT_DIR + "TestFile3.mxml";

	private static CharSequence testFile3;

	private List<String> testFile3attributeNames = Arrays.asList(
								"keyBindings", 
								"MODULES_LOCATION", 
								"showDebugMenu", 
								"shouldUpdatePerspectiveUserEntryOnLayoutChange");
	
	private List<String> testFile3methodNames = Arrays.asList(
								"viewerClass",
								"getModulesToLoad",
								"sendObject",
								"applicationCompleteHandler");
	
	/**
	 * load test file
	 */
	@BeforeClass
	public static void loadTestFile() {
		testFile3 = TestUtil.readFile(TEST_FILE_3_PATH);
	}
	
	/**
	 * testMxmlDeclarationsExist
	 */
	@Test
	public void testMxmlDeclarationsExist() {
		RegexConfiguration config = new RegexTestBase.CategoryRecorderRegexConfiguration();
		MxmlRegexConfigurationProvider.buildMxmlConfiguration(config);
		RegexTestBase.CategoryRecorderRegexProcessingSession session = (RegexTestBase.CategoryRecorderRegexProcessingSession) config.startSession(testFile3);
		
		try {
			session.find(null);
		} catch (RegexException e) {
			throw new RuntimeException(e);
		}
		
		assertAllExist(session.getRecorderCategory(ATTRIBUTE_CATEGORY), testFile3attributeNames);
		assertAllExist(session.getRecorderCategory(METHOD_CATEGORY), testFile3methodNames);
	}
	
	/**
	 * testMxmlFindRange
	 */
	@Test
	public void testMxmlFindRange() {
		RegexConfiguration config = new RegexConfiguration();
		MxmlRegexConfigurationProvider.buildMxmlConfiguration(config);
		RegexProcessingSession session = config.startSession(testFile3);
		
		String testFileContent = testFile3.toString();
		
		/////////////////////////////
		// Do action/Check result: range for a given attribute is correct
		/////////////////////////////
	
		for (String attributeName : testFile3attributeNames) {
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

		for (String methodName : testFile3methodNames) {
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
