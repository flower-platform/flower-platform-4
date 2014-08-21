package org.flowerplatform.util.regex;

public class IfFindThisModifyNesting extends RegexWithActions {

	protected int increment;

	public IfFindThisModifyNesting(String humanReadableRegexMeaning, String regex, int increment) {
		super(humanReadableRegexMeaning, regex);
		this.increment = increment;
	}

	@Override
	public void executeAction(RegexProcessingSession session) {
		session.currentNestingLevel += increment;
	}

}
