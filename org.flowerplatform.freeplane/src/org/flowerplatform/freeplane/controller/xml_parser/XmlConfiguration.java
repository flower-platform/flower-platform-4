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
