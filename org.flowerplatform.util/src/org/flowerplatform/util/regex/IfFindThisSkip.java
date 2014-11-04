package org.flowerplatform.util.regex;

/**
 * @author Cristian Spiescu
 */
public class IfFindThisSkip extends RegexWithActions {

	public IfFindThisSkip(String humanReadableRegexMeaning, String regex) {
		super(humanReadableRegexMeaning, regex);
	}

	@Override
	public void executeActions(RegexProcessingSession param) {
		// do nothing
	}
}
