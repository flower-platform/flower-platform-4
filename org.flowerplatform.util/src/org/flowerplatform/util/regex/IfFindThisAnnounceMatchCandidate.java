package org.flowerplatform.util.regex;

public class IfFindThisAnnounceMatchCandidate extends RegexWithActions {

	protected String category;

	public IfFindThisAnnounceMatchCandidate(String humanReadableRegexMeaning, String regex, String category) {
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
