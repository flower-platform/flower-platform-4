/* license-start
 * 
<<<<<<< HEAD
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
=======
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
>>>>>>> refs/remotes/origin/master
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 * 
 */
public class RegexConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegexConfiguration.class); 

	protected List<AbstractRegexWithActions> regexes = new ArrayList<AbstractRegexWithActions>();
	
	protected AbstractRegexWithActions[] captureGroupToRegexMapping;
	
	protected Pattern pattern;
	
	public int targetNestingForMatches;
	
	protected boolean useUntilFoundThisIgnoreAll = true;
	
	/**
	 *@author Cristina Constantinescu
	 **/
	protected RegexProcessingSession createSessionInstance() {
		return new RegexProcessingSession();
	}
	
	/**
	 *@author Cristina Constantinescu
	 **/
	public RegexConfiguration setTargetNestingForMatches(int targetNestingForMatchesParameter) {
		this.targetNestingForMatches = targetNestingForMatchesParameter;
		return this;
	}
	
	/**
	 * Option needed if the configuration does not need any filtering before returning output 
	 */
	public RegexConfiguration setUseUntilFoundThisIgnoreAll(boolean useUntilFoundThisIgnoreAllParameter) {
		this.useUntilFoundThisIgnoreAll = useUntilFoundThisIgnoreAllParameter;
		return this;
	}
	/**
	 *@author Cristina Constantinescu
	 **/
	public RegexConfiguration add(AbstractRegexWithActions regex) {
		regexes.add(regex);
		return this;
	}
		
	public List<AbstractRegexWithActions> getRegexes() {
		return regexes;
	}

	/**
	 * Iterates on {@link #regexes} to find out how big the array should be. Then
	 * creates the array.
	 */
	protected void createCaptureGroupToRegexMappingArray() {
		int nextCaptureGroupIndex = 1;
		for (AbstractRegexWithActions regex : regexes) {
			nextCaptureGroupIndex += 1 + regex.getNumberOfCaptureGroups();
		}
		captureGroupToRegexMapping = new AbstractRegexWithActions[nextCaptureGroupIndex];
	}
	
	/**
	 * 
	 */
	public RegexConfiguration compile(int flags) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Compiling configuration...");
		}
		
		createCaptureGroupToRegexMappingArray();
		
		int nextCaptureGroupIndex = 1;
		StringBuilder composedRegex = new StringBuilder();
		for (int i = 0; i < regexes.size(); i++) {
			composedRegex.append('(');
		
			AbstractRegexWithActions regex = regexes.get(i);
			
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Adding to capture group = {} regex = {} having {} capture groups", new Object[] { nextCaptureGroupIndex, regex.getRegex(), 
						regex.getNumberOfCaptureGroups()});
			}
			
			composedRegex.append(regex.getRegex());
			composedRegex.append(')');
			if (i != regexes.size() - 1) {
				composedRegex.append('|');
			}
			
			captureGroupToRegexMapping[nextCaptureGroupIndex] = regex;
			nextCaptureGroupIndex += 1 + regex.getNumberOfCaptureGroups();
		}
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Composed regex = {} having {} capture groups. Compiling pattern...", composedRegex.toString(), nextCaptureGroupIndex - 1);
		}
		
		pattern = Pattern.compile(composedRegex.toString(), flags);
		return this;
	}
	
	/**
	 * 
	 */
	public RegexProcessingSession startSession(CharSequence input) {
		RegexProcessingSession session = createSessionInstance();
		session.configuration = this;
		session.matcher = pattern.matcher(input);
		session.reset(false);
		return session;
	}

	public AbstractRegexWithActions[] getCaptureGroupToRegexMapping() {
		return captureGroupToRegexMapping;
	}
}
