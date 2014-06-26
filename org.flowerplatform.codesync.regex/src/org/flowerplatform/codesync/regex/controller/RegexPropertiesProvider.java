package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TYPE;
import static org.flowerplatform.core.CoreConstants.ICONS;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

public class RegexPropertiesProvider extends AbstractController implements IPropertiesProvider {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(ICONS, ResourcesPlugin.getInstance().getResourceUrl(node.getType().equals(REGEX_TYPE) ? "images/codesync.regex/bricks.png" : "images/codesync.regex/percent.png"));
		
		// populate full_regex
	}
	
//	private String getFullRegexInternal(Node node, String regexWithMacros) {		
//		String fullRegex = regexWithMacros;
//		if (macroPattern == null) {
//			macroPattern = Pattern.compile("%(.*?)%");
//		}
//		Matcher matcher = macroPattern.matcher(regexWithMacros);
//		while (matcher.find()) { // found %...%			
//			// search ... in list and replace it with macro's regular expression
//			for (MacroRegex macroRegex : root.getMacroRegexes()) {
//				if (macroRegex.getName().equals(matcher.group().substring(1, matcher.group().length() - 1))) {					
//					fullRegex = fullRegex.replace(matcher.group(), getFullRegexInternal(root, macroRegex.getRegex()));
//					break;
//				}
//			}			
//		}
//		return fullRegex;
//	}	

}
