package org.flowerplatform.util.regex;

public class IfFindThisAnnounceMatchCandidate extends RegexWithActions {

	protected String category;

	public IfFindThisAnnounceMatchCandidate(String humanReadableRegexMeaning, String regex, String category) {
		super(humanReadableRegexMeaning, regex);
		this.category = category;
	}

	@Override
	public void executeAction(RegexProcessingSession session) {
		Boolean ignoreMatches = (Boolean) session.context.get("ignoreMatches");
		if (ignoreMatches != null && !ignoreMatches && ((int) session.context.get("currentNestingLevel")) == session.configuration.targetNestingForMatches) {
			session.candidateAnnounced(category);
		}
	}

}
