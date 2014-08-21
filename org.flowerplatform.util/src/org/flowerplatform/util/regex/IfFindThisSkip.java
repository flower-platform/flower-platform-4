package org.flowerplatform.util.regex;

public class IfFindThisSkip extends RegexWithActions {

	public IfFindThisSkip(String humanReadableRegexMeaning, String regex) {
		super(humanReadableRegexMeaning, regex);
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		// do nothing
	}
}
