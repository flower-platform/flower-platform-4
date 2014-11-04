package org.flowerplatform.util.regex;

/**
 * @author Cristian Spiescu
 */
public class IfFindThisModifyNesting extends RegexWithActions {

	protected int increment;

	public IfFindThisModifyNesting(String humanReadableRegexMeaning, String regex, int increment) {
		super(humanReadableRegexMeaning, regex);
		this.increment = increment;
	}

	@Override
	public void executeActions(RegexProcessingSession session) {
		int currentNestingLevel = (int) session.context.get("currentNestingLevel");
		session.context.put("currentNestingLevel", currentNestingLevel + increment);
	}

}
