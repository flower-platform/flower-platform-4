package org.flowerplatform.util.regex;

public class IfFindThisModifyNesting extends RegexWithActions {

	protected int increment;

	public IfFindThisModifyNesting(String humanReadableRegexMeaning, String regex, int increment) {
		super(humanReadableRegexMeaning, regex);
		this.increment = increment;
	}

	@Override
	public void executeAction(RegexProcessingSession session) {
		int currentNestingLevel = (int) session.context.get("currentNestingLevel");
		session.context.put("currentNestingLevel", currentNestingLevel + increment);
	}

}
