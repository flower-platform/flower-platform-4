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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class XmlParser extends DefaultHandler {
	
	protected XmlConfiguration configuration;
	
	protected Map<String, Object> properties;
	protected boolean isRoot;
	protected AbstractTagProcessor forcedTagProcessor;
	
	// CHECKSTYLE:OFF
	protected String tagFullContent_TagName;
	protected StringBuffer tagFullContent_StringBuffer;
	protected int tagFullContent_Nesting;
	protected boolean tagFullContent_HasAttributes = false;
	
	protected Set<String> convertAllAttributes_ProcessedXmlTags;
	protected boolean convertAllAttributes_TagProcessorDinamicallyAdded;
	// CHECKSTYLE:ON
	
	/**
	 * @author see class
	 */
	public XmlParser(XmlConfiguration configuration, Map<String, Object> properties) {
		super();
		this.configuration = configuration;
		this.properties = properties;
	}

	/**
	 * @author Catalin Burcea
	 */
	public AbstractTagProcessor getXMLTagProcessor(String tag) {
		if (forcedTagProcessor != null) {
			return forcedTagProcessor;
		}
		if (configuration.xmlTagProcessors.containsKey(tag)) {
			return configuration.xmlTagProcessors.get(tag);
		}
		return configuration.defaultTagProcessor;
	}

	/**
	 * @author Valentina Bojan
	 */
	public void parseXML(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
		SAXParser saxParser = configuration.saxFactory.newSAXParser();
		InputSource inputSource = new InputSource(new StringReader(xmlContent));
		saxParser.parse(inputSource, this);
		if (this.convertAllAttributes_TagProcessorDinamicallyAdded) {
			properties.clear();
			saxParser.reset();
			inputSource = new InputSource(new StringReader(xmlContent));
			saxParser.parse(inputSource, this);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		// perform initializations here, in the event where the xml is parsed twice
		convertAllAttributes_ProcessedXmlTags = new HashSet<String>();
		isRoot = true;
		tagFullContent_Nesting = 0;
		forcedTagProcessor = null;
		tagFullContent_StringBuffer = new StringBuffer();
	}

	@Override
	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException {
		getXMLTagProcessor(tagName).processStartTag(this, tagName, attributes, properties);
	}

	@Override
	public void endElement(String uri, String localName, String tagName) throws SAXException {
		getXMLTagProcessor(tagName).processEndTag(this, tagName, properties);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		getXMLTagProcessor(null).processPlainText(this, new String(ch, start, length));
	}
}
