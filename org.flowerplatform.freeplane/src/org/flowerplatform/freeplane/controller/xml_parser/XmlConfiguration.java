package org.flowerplatform.freeplane.controller.xml_parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.flowerplatform.freeplane.FreeplaneConstants;

/**
 * @author Cristian Spiescu
 */
public class XmlConfiguration {
	protected Map<String, AbstractTagProcessor> xmlTagProcessors = new HashMap<String, AbstractTagProcessor>();
	protected AbstractTagProcessor defaultTagProcessor = new ConvertAllAttributesProcessor();
	protected SAXParserFactory saxFactory;
	
	// i.e. richcontent(TYPE=DETAILS).HIDDEN, attribute(NAME=n1).VALUE
	protected Pattern fullAttributeTagPattern = Pattern.compile("^(.+)\\((.+)\\)\\.(.+)$");
	
	// i.e. hook(NAME=Node)_content
	protected Pattern fullContentTagPattern = Pattern.compile("^(.+)\\((.+)\\)" + FreeplaneConstants.CONTENT_MARK + "$");
	
	// i.e. hook(NAME=Node)
	protected Pattern noContetNoAttributesPattern = Pattern.compile("^(.+)\\((.+)\\)$");
	
	// i.e. font.NAME=Arial 
	protected Pattern simpleAttributesPattern = Pattern.compile("^(.+)\\.(.+)$");
	
	// i.e. unknown_content 
	protected Pattern simpleContentPattern = Pattern.compile("^(.+)" + FreeplaneConstants.CONTENT_MARK + "$");

	/**
	 * @author see class
	 */
	public XmlConfiguration() {
		super();

		// add the processors
		xmlTagProcessors.put(FreeplaneConstants.ICON, new TagsAsListProcessor(FreeplaneConstants.ICONS, FreeplaneConstants.ICON_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.HOOK, new TagFullContentProcessor(FreeplaneConstants.HOOK_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.RICHCONTENT, new TagFullContentProcessor(FreeplaneConstants.RICHCONTENT_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.ATTRIBUTE, new TagFullContentProcessor(FreeplaneConstants.ATTRIBUTE_KEY_PROPERTY));
	
		// create the SAXfactory for the SAXparser
		saxFactory = SAXParserFactory.newInstance();
	}	
}
