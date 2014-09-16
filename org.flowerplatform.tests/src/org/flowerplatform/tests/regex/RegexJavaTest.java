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
import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.buildJavaConfiguration;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider;
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
	
	private static final String TEST_FILE_1_PATH = FILES_ROOT_DIR + "TestFile1.java";
	
	private static final int NUMBER_OF_FIELDS_IN_FILE_1 = 28;
	
	private static final String FIELD_PREFFIX_IN_FILE_1 = "attr";
	
	private static final int NUMBER_OF_METHODS_IN_FILE_1 = 3;
	
	private static final String METHODS_PREFFIC_IN_FILE_1 = "meth";

	private CharSequence testFile1;

	/**
	 * settinf up the test environment
	 */
	@Before
	public void setUp() {
		testFile1 = TestUtil.readFile(TEST_FILE_1_PATH);
	}

	/**
	 * testJavaDeclarationsExist
	 */
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
		
		assertAllExpectedElementsFound("attributes", session.getRecorderCategory(ATTRIBUTE_CATEGORY), FIELD_PREFFIX_IN_FILE_1, 1, NUMBER_OF_FIELDS_IN_FILE_1);
		assertAllExpectedElementsFound("methods", session.getRecorderCategory(METHOD_CATEGORY), METHODS_PREFFIC_IN_FILE_1, 1, NUMBER_OF_METHODS_IN_FILE_1);
	}
	
	/**
	 * testJavaFindRange
	 */
	@Test
	public void testJavaFindRange() {
		RegexConfiguration config = new RegexConfiguration();
		JavaRegexConfigurationProvider.buildJavaConfiguration(config);
		RegexProcessingSession session = config.startSession(testFile1);
		String testInputFileAsString = testFile1.toString();
		
		for (int i = 1; i <= NUMBER_OF_FIELDS_IN_FILE_1; i++) {
			session.reset(true);
			
			String elementName = FIELD_PREFFIX_IN_FILE_1 + i;
			
			try {
				int[] range = session.findRangeFor(JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY, elementName);
				assertIdentifierInRange(elementName, range, testInputFileAsString);
			} catch (RegexException e) {
				throw new RuntimeException(e);
			}
			
		}
	}
}