/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplaneConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class XmlNodePropertiesParser extends DefaultHandler {

	public Logger logger = LoggerFactory.getLogger(XmlNodePropertiesParser.class);
	Map<String, AbstractTagProcessor> xmlTagProcessors = new HashMap<String, AbstractTagProcessor>();
	AbstractTagProcessor defaultTagProcessor = new ConvertAllAttributesProcessor();
	private Node node;
	public AbstractTagProcessor forcedTagProcessor;
	public String tagFullContent_tagName;
	public StringBuffer tagFullContent_stringBuffer;
	public int tagFullContent_nesting;
	public boolean tagFullContent_hasAttributes = false;
	public HashSet<String> convertAllAttributes_processedXmlTags;
	public boolean convertAllAttributes_tagProcessorDinamicallyAdded;
	public boolean isRoot;

	public XmlNodePropertiesParser(Node node) {
		this.node = node;
	}

	public AbstractTagProcessor getXMLTagProcessor(String tag) {
		if (forcedTagProcessor != null) {
			return forcedTagProcessor;
		}
		if (xmlTagProcessors.containsKey(tag)) {
			return xmlTagProcessors.get(tag);
		}
		return defaultTagProcessor;
	}

	public void parseXML(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		InputSource inputSource = new InputSource(new StringReader(xmlContent));
		saxParser.parse(inputSource, this);
		if (this.convertAllAttributes_tagProcessorDinamicallyAdded) {
			saxParser.reset();
			inputSource = new InputSource(new StringReader(xmlContent));
			saxParser.parse(inputSource, this);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		node.getProperties().clear();
		convertAllAttributes_processedXmlTags = new HashSet<String>();
		isRoot = true;
		tagFullContent_nesting = 0;
		forcedTagProcessor = null;
		tagFullContent_stringBuffer = new StringBuffer();
		xmlTagProcessors.put(FreeplaneConstants.ICON, new TagsAsListProcessor(FreeplaneConstants.ICONS, FreeplaneConstants.ICON_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.HOOK, new TagFullContentProcessor(FreeplaneConstants.HOOK_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.RICHCONTENT, new TagFullContentProcessor(FreeplaneConstants.RICHCONTENT_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.ATTRIBUTE, new TagFullContentProcessor(FreeplaneConstants.ATTRIBUTE_KEY_PROPERTY));
	}

	@Override
	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException {
		getXMLTagProcessor(tagName).processStartTag(this, tagName, attributes, node);
	}

	@Override
	public void endElement(String uri, String localName, String tagName) throws SAXException {
		getXMLTagProcessor(tagName).processEndTag(this, tagName, node);
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		getXMLTagProcessor(null).processPlainText(this, new String(ch, start, length));
	}
}
