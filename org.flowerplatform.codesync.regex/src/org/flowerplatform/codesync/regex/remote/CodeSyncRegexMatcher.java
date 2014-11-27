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
package org.flowerplatform.codesync.regex.remote;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END_C;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END_L;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START_C;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START_L;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * Create the match tree corresponding to the parsed matches.
 * 
 * @author Cristina Constantinescu
 */
class CodeSyncRegexMatcher implements Runnable {

	private NodeService nodeService;
	private RegexProcessingSession session;
	private String testFileContent;
	private Node matchRoot;
	
	public CodeSyncRegexMatcher(NodeService nodeService, RegexProcessingSession session,
			String testFileContent, Node matchRoot) {
		super();
		this.nodeService = nodeService;
		this.session = session;
		this.testFileContent = testFileContent;
		this.matchRoot = matchRoot;
	}

	private final Pattern newLinePattern = Pattern.compile("(\r\n)|(\n)|(\r)");
	private int currentIndex = 1;
	
	@Override
	public void run() {
		// match found => create node
		Node match = new Node(null, REGEX_MATCH_TYPE);
		RegexWithActions regex = (RegexWithActions) session.getCurrentRegex();
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.getContext().put(NAME, String.format("%s) %s", String.valueOf(currentIndex++), session.getCurrentRegex().getName()));
		context.getContext().put(FULL_REGEX, regex.getRegex());
		context.getContext().put(REGEX_NAME, regex.getName());

		int[] start = formatIndex(testFileContent, session.getMatcher().start());
		context.getContext().put(START, String.format("L%d C%d", start[0], start[1]));
		context.getContext().put(START_L, start[0]);
		context.getContext().put(START_C, start[1]);

		int[] end = formatIndex(testFileContent, session.getMatcher().end());
		context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
		context.getContext().put(END_L, end[0]);
		context.getContext().put(END_C, end[1]);

		nodeService.addChild(matchRoot, match, context);

		if (session.getCurrentSubMatchesForCurrentRegex() != null) {
			// has subMatches -> add them too as children
			for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
				int index = session.getCurrentMatchGroupIndex() + i + 1;

				context = new ServiceContext<NodeService>();
				context.getContext().put(NAME, session.getCurrentSubMatchesForCurrentRegex()[i]);

				int startIndex = session.getMatcher().start(index);

				if (startIndex == -1) {
					continue;
				}

				start = formatIndex(testFileContent, startIndex);
				context.getContext().put(START, String.format("L%d C%d", start[0], start[1]));
				context.getContext().put(START_L, start[0]);
				context.getContext().put(START_C, start[1]);

				int endIndex = session.getMatcher().end(index);
				end = formatIndex(testFileContent, endIndex);
				context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
				context.getContext().put(END_L, end[0]);
				context.getContext().put(END_C, end[1]);
				nodeService.addChild(match, new Node(null, REGEX_MATCH_TYPE), context);
			}
		}
	}
	
	private int[] formatIndex(String str, int index) {
		String prefix = str.substring(0, index);

		Matcher m = newLinePattern.matcher(prefix); // search for new line
													// separator
		int lines = 0;
		int lineEndIndex = -1;
		while (m.find()) {
			lines++;
			lineEndIndex = m.end();
		}
		return new int[] { lines, (lineEndIndex == -1 ? index : index - lineEndIndex) };
	}
}