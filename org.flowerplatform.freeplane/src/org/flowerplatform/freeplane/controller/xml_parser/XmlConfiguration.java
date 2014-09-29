package org.flowerplatform.freeplane.controller.xml_parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.flowerplatform.freeplane.FreeplaneConstants;

/**
 * @author Cristian Spiescu
 */
public class XmlConfiguration {
	protected Map<String, AbstractTagProcessor> xmlTagProcessors = new HashMap<String, AbstractTagProcessor>();
	protected AbstractTagProcessor defaultTagProcessor = new ConvertAllAttributesProcessor();

	protected Pattern fullAttributeTagPattern = Pattern.compile("^(.+)\\((.+)\\)\\.(.+)$");
	protected Pattern fullContentTagPattern = Pattern.compile("^(.+)\\((.+)\\)" + FreeplaneConstants.CONTENT_MARK + "$");
	protected Pattern noContetNoAttributesPattern = Pattern.compile("^(.+)\\((.+)\\)$");
	protected Pattern simpleAttributesPattern = Pattern.compile("^(.+)\\.(.+)$");
	protected Pattern simpleContentPattern = Pattern.compile("^(.+)" + FreeplaneConstants.CONTENT_MARK + "$");

	public XmlConfiguration() {
		super();

		xmlTagProcessors.put(FreeplaneConstants.ICON, new TagsAsListProcessor(FreeplaneConstants.ICONS, FreeplaneConstants.ICON_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.HOOK, new TagFullContentProcessor(FreeplaneConstants.HOOK_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.RICHCONTENT, new TagFullContentProcessor(FreeplaneConstants.RICHCONTENT_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.ATTRIBUTE, new TagFullContentProcessor(FreeplaneConstants.ATTRIBUTE_KEY_PROPERTY));
	}
	
}
