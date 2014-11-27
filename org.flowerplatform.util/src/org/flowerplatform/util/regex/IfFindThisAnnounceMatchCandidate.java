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

/**
 * @author Cristian Spiescu
 */
public class IfFindThisAnnounceMatchCandidate extends RegexWithActions {

	protected String category;

	public IfFindThisAnnounceMatchCandidate(String humanReadableRegexMeaning, String regex, String category) {
		super(humanReadableRegexMeaning, regex);
		this.category = category;
	}

	@Override
	public void executeActions(RegexProcessingSession session) {
		Boolean ignoreMatches = (Boolean) session.context.get("ignoreMatches");
		if (ignoreMatches != null && !ignoreMatches && ((int) session.context.get("currentNestingLevel")) == session.configuration.targetNestingForMatches) {
			session.candidateAnnounced(category);
		}
	}

}
