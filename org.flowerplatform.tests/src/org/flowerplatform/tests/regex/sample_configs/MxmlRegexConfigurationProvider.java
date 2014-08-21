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
package org.flowerplatform.tests.regex.sample_configs;

import static org.flowerplatform.tests.regex.RegexUtil.CLOSE_BRACKET;
import static org.flowerplatform.tests.regex.RegexUtil.MULTI_LINE_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.OPEN_BRACKET;
import static org.flowerplatform.tests.regex.RegexUtil.SINGLE_LINE_COMMENT;
import static org.flowerplatform.tests.regex.RegexUtil.XML_CDATA_END;
import static org.flowerplatform.tests.regex.RegexUtil.XML_CDATA_START;
import static org.flowerplatform.tests.regex.RegexUtil.XML_MULTI_LINE_COMMENT;
import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.ATTRIBUTE_CATEGORY;
import static org.flowerplatform.tests.regex.sample_configs.JavaRegexConfigurationProvider.METHOD_CATEGORY;

import java.util.regex.Pattern;

import org.flowerplatform.util.regex.IfFindThisAnnounceMatchCandidate;
import org.flowerplatform.util.regex.IfFindThisModifyNesting;
import org.flowerplatform.util.regex.IfFindThisSkip;
import org.flowerplatform.util.regex.RegexConfiguration;

/**
 * @author Sorin
 */
public class MxmlRegexConfigurationProvider extends	ActionscriptRegexConfigurationProvider {
	
	private static int MXML_NESTING_LEVEL_FOR_DECLARATIONS = 1; // must pass <![CDATA[ which is considered as increasing the nesting
	
	public static void buildMxmlConfiguration(RegexConfiguration config) {
		config
			.setTargetNestingForMatches(MXML_NESTING_LEVEL_FOR_DECLARATIONS) 
			.setUseUntilFoundThisIgnoreAll(false)
			.add(new IfFindThisAnnounceMatchCandidate(ATTRIBUTE_CATEGORY, ACTIONSCRIPT_ATTRIBUTE, ATTRIBUTE_CATEGORY))
			.add(new IfFindThisAnnounceMatchCandidate(METHOD_CATEGORY, ACTIONSCRIPT_METHOD, METHOD_CATEGORY))
			
			.add(new IfFindThisModifyNesting("Opening CDATA", XML_CDATA_START, 1))
			.add(new IfFindThisModifyNesting("Closing CDATA", XML_CDATA_END, -1))
			.add(new IfFindThisModifyNesting("Opening curly bracket", OPEN_BRACKET, 1))
			.add(new IfFindThisModifyNesting("Closing curly bracket", CLOSE_BRACKET, -1))
			

			.add(new IfFindThisSkip("XML comment", XML_MULTI_LINE_COMMENT))
			.add(new IfFindThisSkip("Multi-line comment", MULTI_LINE_COMMENT))
			.add(new IfFindThisSkip("Single-line comment", SINGLE_LINE_COMMENT))
			.compile(Pattern.DOTALL);
	}
}