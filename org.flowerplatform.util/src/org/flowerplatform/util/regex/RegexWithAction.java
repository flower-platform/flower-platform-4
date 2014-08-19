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
package org.flowerplatform.util.regex;

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
	public RegexAction getRegexAction() {
		// isn't used
		return null;
	}

	@Override
	public int getNumberOfCaptureGroups() {		
		return numberOfCaptureGroups;
	}

	/**
	 * @author see class
	 */
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
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static class IfFindThisSkip extends RegexWithAction {

		/**
		 * @author Cristina Constantinescu
		 */
		public IfFindThisSkip(String humanReadableRegexMeaning, String regex) {
			super(humanReadableRegexMeaning, regex);
		}

		@Override
		public void executeAction(RegexProcessingSession param) {
			// do nothing			
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static class IfFindThisAnnounceMatchCandidate extends RegexWithAction {

		protected String category;
		
		/**
		 * @author Cristina Constantinescu
		 */
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
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static class IfFindThisModifyNesting extends RegexWithAction {

		protected int increment;
		
		/**
		 * @author Cristina Constantinescu
		 */
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
	
	/**
	 * @author Cristina Constantinescu
	 */
	public static class UntilFoundThisIgnoreAll extends RegexWithAction {

		/**
		 * @author Cristina Constantinescu
		 */
		public UntilFoundThisIgnoreAll(String humanReadableRegexMeaning, String regex) {
			super(humanReadableRegexMeaning, regex);
		}

		@Override
		public void executeAction(RegexProcessingSession session) {
			session.ignoreMatches = false;			
		}				
	}
	
}
