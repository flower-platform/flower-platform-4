/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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

	protected List<AbstractRegexWithAction> regexes = new ArrayList<AbstractRegexWithAction>();
	
	protected AbstractRegexWithAction[] captureGroupToRegexMapping;
	
	protected Pattern pattern;
	
	protected int targetNestingForMatches;
	
	protected boolean useUntilFoundThisIgnoreAll = true;
	
	/**
	 * @author Cristina Constantinescu
	 */
	protected RegexProcessingSession createSessionInstance() {
		return new RegexProcessingSession();
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	//CHECKSTYLE:OFF
	public RegexConfiguration setTargetNestingForMatches(int targetNestingForMatches) {
		this.targetNestingForMatches = targetNestingForMatches;
		return this;
	}
	//CHECKSTYLE:ON
	
	/**
	 * Option needed if the configuration does not need any filtering before returning output 
	 */
	//CHECKSTYLE:OFF
	public RegexConfiguration setUseUntilFoundThisIgnoreAll(boolean useUntilFoundThisIgnoreAll) {
		this.useUntilFoundThisIgnoreAll = useUntilFoundThisIgnoreAll;
		return this;
	}
	//CHECKSTYLE:ON

	/**
	 * @author Cristina Constantinescu
	 */
	public RegexConfiguration add(AbstractRegexWithAction regex) {
		regexes.add(regex);
		return this;
	}
		
	public List<AbstractRegexWithAction> getRegexes() {
		return regexes;
	}

	/**
	 * Iterates on {@link #regexes} to find out how big the array should be. Then
	 * creates the array.
	 */
	protected void createCaptureGroupToRegexMappingArray() {
		int nextCaptureGroupIndex = 1;
		for (AbstractRegexWithAction regex : regexes) {
			nextCaptureGroupIndex += 1 + regex.getNumberOfCaptureGroups();
		}
		captureGroupToRegexMapping = new AbstractRegexWithAction[nextCaptureGroupIndex];
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
		
			AbstractRegexWithAction regex = regexes.get(i);
			
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Adding to capture group = {} regex = {} having {} capture groups",
				new Object[] { nextCaptureGroupIndex, regex.getRegex(), regex.getNumberOfCaptureGroups()});
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

	public AbstractRegexWithAction[] getCaptureGroupToRegexMapping() {
		return captureGroupToRegexMapping;
	}
	
	
}
