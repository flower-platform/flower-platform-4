package org.flowerplatform.util.regex;

public class UntilFoundThisIgnoreAll extends RegexWithActions {

	public UntilFoundThisIgnoreAll(String humanReadableRegexMeaning, String regex) {
		super(humanReadableRegexMeaning, regex);
	}

	@Override
	public void executeActions(RegexProcessingSession session) {
		session.context.put("ignoreMatches", false);
	}
}
