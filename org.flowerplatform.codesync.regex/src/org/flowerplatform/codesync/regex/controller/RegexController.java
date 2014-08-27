package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_WITH_MACROS;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SKIP_PROVIDER;
import static org.flowerplatform.core.CoreConstants.CONFIG_SETTING_DISABLED;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class RegexController extends AbstractController implements IPropertiesProvider, IPropertySetter {

	private final Pattern macroPattern = Pattern.compile("%(.*?)%");
	
	public RegexController() {
		super();
		// invoked after the persistence providers
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (context.getBooleanValue(SKIP_PROVIDER)) {
			return;
		}
		node.getProperties().put(CodeSyncRegexConstants.FULL_REGEX, getFullRegex(node));
		if(node.getProperties().get(CONFIG_SETTING_DISABLED) == null)
			node.getProperties().put(CONFIG_SETTING_DISABLED, false);
	}
	
	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		if (property.equals(REGEX_WITH_MACROS)) {
			node.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService()));
			context.getService().setProperty(node, FULL_REGEX, node.getPropertyValue(FULL_REGEX), new ServiceContext<NodeService>(context.getService()).add(EXECUTE_ONLY_FOR_UPDATER, true));
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {		
	}	
	
	private String getFullRegex(Node node) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());		
		return getFullRegex(
				resourceNode, 
				(String) node.getProperties().get(REGEX_WITH_MACROS), 
				CodeSyncRegexPlugin.getInstance().getChildren(resourceNode, REGEX_WITH_MACROS));
	}
	
	private String getFullRegex(Node resourceNode, String regexWithMacros, List<Node> macros) {	
		String fullRegex = regexWithMacros;
	
		Matcher matcher = macroPattern.matcher(regexWithMacros);
		while (matcher.find()) { // found %...%			
			// search ... in list and replace it with macro's regular expression
			for (Node macro : macros) {
				String macroName = (String) macro.getOrPopulateProperties(new ServiceContext<NodeService>(getNodeService()).add(SKIP_PROVIDER, true)).get(NAME);
				if (macroName.equals(matcher.group().substring(1, matcher.group().length() - 1))) {					
					fullRegex = fullRegex.replace(matcher.group(), getFullRegex(resourceNode, (String) macro.getPropertyValue(REGEX_WITH_MACROS), macros));
					break;
				}
			}			
		}
		return fullRegex;
	}

	private NodeService getNodeService() {
		return CorePlugin.getInstance().getNodeService();
	}
	
}
