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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexTestBase {

	protected static final String FILES_ROOT_DIR = TestUtil.getResourcesDir(RegexTestBase.class) + TestUtil.NORMAL + "/";
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	protected void assertAllExpectedElementsFound(String humanReadable, List<String> list, String elementPreffix, int startIndex, int endIndex) {
		int currentAttrSuffix = startIndex;
		for (String currentFoundAttr : list) {
			Assert.assertEquals(humanReadable + ": expected item vs found item", elementPreffix + currentAttrSuffix++, currentFoundAttr);
		}
		
		Assert.assertEquals(humanReadable + ": count", endIndex - startIndex + 1, currentAttrSuffix - 1);

	}
	
	protected void assertIdentifierInRange(String identifier, int[] range, String fileContent) {
		assertNotNull("Not found range for " + identifier, range);
		
		int[] expectedRange = findRangeByExactMatch(fileContent, identifier);
		assertEquals("Range compare with range found by exact match: start for " + identifier, expectedRange[0], range[0]);
		assertEquals("Range compare with range found by exact match: end for " + identifier, expectedRange[1], range[1]);
		
	}
	
	protected void assertAllExist(List<String> foundElements, List<String> expectedElements) {
		if (foundElements.equals(expectedElements))
			return;
		List<String> additionalFoundElements = new ArrayList<String>(foundElements);
		additionalFoundElements.removeAll(expectedElements);
		
		List<String> notFoundElements = new ArrayList<String>(expectedElements);
		notFoundElements.removeAll(foundElements);
		
		Assert.assertTrue(
						(notFoundElements.isEmpty() ? 
								"" : ("\nCould not find the following elements : "  + notFoundElements)) +
						(additionalFoundElements.isEmpty() ?
								"" : ("\nAdditional elements were found : " + additionalFoundElements)), 
						 additionalFoundElements.isEmpty() && notFoundElements.isEmpty());
	}
	
	protected int[] findRangeByExactMatch(String where, String what) {
		int i = where.indexOf(what);
		if (i >= 0 && where.substring(i).indexOf(what) >= 0) {
			if (logger.isWarnEnabled())
				logger.warn("Warning : the following string has been found twice, by direct match :" + what + "\n" +
						"If an assertion fails due to range not matching, it may be possible that the file content is incorrect");
		}
//		// am comentat pentru ca: attr1 ... attr15
//		if (where.indexOf(what) >= 0) {
//			Assert.fail("Issue with test file; the following string has been found twice, by direct match, which shouldn't happen: " + what);
//		}
		return new int[] { i, i + what.length() };
	}	

	static class CategoryRecorderRegexProcessingSession extends RegexProcessingSession {
	
		private HashMap<String, List<String>> recordedCategories = new HashMap<String, List<String>>();
	
		@Override
		public void candidateAnnounced(String category) {
			super.candidateAnnounced(category);
			getRecorderCategory(category).add(currentSubMatchesForCurrentRegex[0]);
		}
		
		public List<String> getRecorderCategory(String category) {
			if (!recordedCategories.containsKey(category))
				recordedCategories.put(category, new ArrayList<String>());
			return recordedCategories.get(category);
		}
	}

	static class CategoryRecorderRegexConfiguration extends RegexConfiguration {
		
		@Override
		protected RegexProcessingSession createSessionInstance() {
			return new CategoryRecorderRegexProcessingSession();
		}
	}

}