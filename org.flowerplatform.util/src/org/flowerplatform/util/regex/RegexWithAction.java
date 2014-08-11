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
package org.flowerplatform.util.regex;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Cristi
 */
public abstract class RegexWithAction extends AbstractRegexWithAction {

	protected String name;
	protected String regex;
	
	protected int numberOfCaptureGroups;
		
	public String getRegex() {
		return regex;
	}
	
	@Override
	public String getName() {	
		return name;
	}	
	
	@Override
	public List<RegexAction> getRegexActions() {
		// isn't used
		return null;
	}

	@Override
	public int getNumberOfCaptureGroups() {		
		return numberOfCaptureGroups;
	}

	public RegexWithAction(String name, String regex) {
		super();
		this.name = name;
		this.regex = regex;
		this.numberOfCaptureGroups = Pattern.compile(regex).matcher("").groupCount();
	}	
		
	@Override
	public String toString() {
		return "RegexWithAction [name="
				+ name + ", regex=" + regex
				+ ", numberOfCaptureGroups=" + numberOfCaptureGroups + "]";
	}
	
	public static class IfFindThisSkip extends RegexWithAction {

		public IfFindThisSkip(String humanReadableRegexMeaning, String regex) {
			super(humanReadableRegexMeaning, regex);
		}

		@Override
		public void executeAction(RegexProcessingSession param) {
			// do nothing			
		}
	}
	
	public static class IfFindThisAnnounceMatchCandidate extends RegexWithAction {

		protected String category;
		
		public IfFindThisAnnounceMatchCandidate(
				String humanReadableRegexMeaning, String regex, String category) {
			super(humanReadableRegexMeaning, regex);
			this.category = category;
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			if (!session.ignoreMatches && session.currentNestingLevel == session.configuration.targetNestingForMatches) {
				session.candidateAnnounced(category);
			}			
		}
		
	}
	
	public static class IfFindThisModifyNesting extends RegexWithAction {

		protected int increment;
		
		public IfFindThisModifyNesting(String humanReadableRegexMeaning,
				String regex, int increment) {
			super(humanReadableRegexMeaning, regex);
			this.increment = increment;
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			session.currentNestingLevel += increment;			
		}
				
	}
	
	public static class UntilFoundThisIgnoreAll extends RegexWithAction {

		public UntilFoundThisIgnoreAll(String humanReadableRegexMeaning, String regex) {
			super(humanReadableRegexMeaning, regex);
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			session.ignoreMatches = false;			
		}				
	}
	
}