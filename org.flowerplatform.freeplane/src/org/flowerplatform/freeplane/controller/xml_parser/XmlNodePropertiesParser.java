package org.flowerplatform.freeplane.controller.xml_parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.flowerplatform.freeplane.FreeplaneConstants;

/**
 * @author Catalin Burcea
 *
 */
public class XmlNodePropertiesParser extends DefaultHandler {

	Map<String, ITagProcessor> xmlTagProcessors = new HashMap<String, ITagProcessor>();
	ITagProcessor defaultTagProcessor = new ConvertAllAttributesProcessor();
	private Node node;
	public ITagProcessor forcedTagProcessor = null;
	public String tagFullContent_tagName;
	public StringBuffer tagFullContent_stringBuffer = new StringBuffer();
	public HashSet<String> xmlTags = new HashSet<String>();
	public boolean convertAllAttributes_tagProcessorDinamicallyAdded;
	public int tagFullContent_nesting = 0;

	public XmlNodePropertiesParser(Node node) {
		this.node = node;
	}

	public ITagProcessor getXMLTagProcessor(String tag) {
		if (forcedTagProcessor != null) {
			return forcedTagProcessor;
		}
		if (xmlTagProcessors.containsKey(tag)) {
			return xmlTagProcessors.get(tag);
		}
		return defaultTagProcessor;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		node.getProperties().clear();
		xmlTags = new HashSet<String>();
		xmlTagProcessors.put(FreeplaneConstants.ICON, new TagsAsCsvListProcessor(FreeplaneConstants.ICONS, FreeplaneConstants.ICON_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.HOOK, new TagFullContentProcessor(FreeplaneConstants.HOOK_KEY_PROPERTY));
		xmlTagProcessors.put(FreeplaneConstants.RICHCONTENT, new TagFullContentProcessor(FreeplaneConstants.RICHCONTENT_KEY_PROPERTY));
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
