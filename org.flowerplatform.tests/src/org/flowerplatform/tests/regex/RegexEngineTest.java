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
package org.flowerplatform.tests.regex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.flowerplatform.util.regex.IfFindThisAnnounceMatchCandidate;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexException;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.junit.Test;

/**
 * @author Cristi
 */
public class RegexEngineTest extends RegexTestBase {

	/**
	 * @author Cristi
	 */
	@Test
	public void testDispatchingFindResultToRegexWithAction() {
		final List<String> results = new ArrayList<String>();
		
		RegexConfiguration re = new RegexConfiguration();
		
		final String rule1Descr = "cuvant cu sufix cifre";
		final String rule2Descr = "string";
		final String rule3Descr = "cuvant:cuvant";
		
		re
			.add(new IfFindThisAnnounceMatchCandidate(rule1Descr, "suf(\\d*)", "categ: " + rule1Descr) {
				
				@Override
				public void executeActions(RegexProcessingSession session) {
					super.executeActions(session);
					results.add(rule1Descr);
					assertEquals(1, session.getCurrentSubMatchesForCurrentRegex().length);
					assertEquals("23", session.getCurrentSubMatchesForCurrentRegex()[0]);
				}
			})
			.add(new IfFindThisAnnounceMatchCandidate(rule2Descr, "string", "categ: " + rule2Descr) {
				
				@Override
				public void executeActions(RegexProcessingSession session) {
					super.executeActions(session);
					results.add(rule2Descr);					
					assertNull(session.getCurrentSubMatchesForCurrentRegex());
				}
			})
			.add(new IfFindThisAnnounceMatchCandidate(rule3Descr, "(\\w*):(\\w*)", "categ: " + rule3Descr) {
				
				@Override
				public void executeActions(RegexProcessingSession session) {
					super.executeActions(session);
					results.add(rule3Descr);					
					assertEquals(2, session.getCurrentSubMatchesForCurrentRegex().length);
					assertEquals("atr", session.getCurrentSubMatchesForCurrentRegex()[0]);
					assertEquals("tip", session.getCurrentSubMatchesForCurrentRegex()[1]);
				}
			})
			.compile(Pattern.DOTALL);	
		
		RegexProcessingSession session = re.startSession("Aceste este un string dar si un suf23. Am mai adaugat si atr:tip.");
		try {
			session.find(null);
		} catch (RegexException e) {
			throw new RuntimeException(e);
		}
		
		assertEquals("We have exactly 3 matches", 3, results.size());
		assertEquals("2nd match = 1st rule", rule1Descr, results.get(1));
		assertEquals("1st match = 2nd rule", rule2Descr, results.get(0));
		assertEquals("3rd match = 3rd rule", rule3Descr, results.get(2));
	}
}