package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_WITH_MACROS;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.regex.Pattern;

import org.flowerplatform.codesync.regex.action.DelegatingRegexWithAction;
import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexConfiguration;

/**
 * @author Elena Posea
 */
public class RegexWithActionProcessor extends AbstractController implements IConfigNodeProcessor<DelegatingRegexWithAction, RegexConfiguration> {

	@Override
	public DelegatingRegexWithAction processConfigNode(Node node, RegexConfiguration parentProcessedDataStructure, ServiceContext<NodeService> context) {
		DelegatingRegexWithAction rwa = new DelegatingRegexWithAction();
		rwa.setName((String) node.getPropertyValue(NAME));
		rwa.setRegexWithMacros((String) node.getPropertyValue(REGEX_WITH_MACROS));
		rwa.setRegex((String) node.getPropertyValue(FULL_REGEX));
  		rwa.setNumberOfCaptureGroups(Pattern.compile(rwa.getRegex()).matcher("").groupCount());
		parentProcessedDataStructure.add(rwa);
		return rwa;
	}

}
