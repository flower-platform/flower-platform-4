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

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public abstract class AbstractTagProcessor {
	
	void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node){}

	void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {}

	void processPlainText(XmlNodePropertiesParser parser, String plainText) {
		parser.tagFullContent_stringBuffer.append(plainText);
	}
	
	void addStartContentAndAttributes(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node, String keyProperty){
		// first invocation of this processor for a well-known tag
		if (parser.tagFullContent_nesting == 0 && keyProperty != null) {
			parser.forcedTagProcessor = this;
			
			// check for duplicates tags; if there is any duplicate => assign this type of tag to a specific processor
			if (parser.convertAllAttributes_processedXmlTags.contains(tag)) {
				parser.convertAllAttributes_tagProcessorDinamicallyAdded = true;
				parser.logger.debug(String.format("Dynamically adding new processor for unknown tag = {%s}", TagFullContentProcessor.class.getSimpleName()));
				parser.xmlTagProcessors.put(tag, new TagFullContentProcessor(null));
				parser.tagFullContent_nesting++;
				return;
			}			
			
			// i.e. <attribute NAME="a1" VALUE="v1"/>
			if (!keyProperty.isEmpty()) {
				parser.tagFullContent_tagName = tag + "(" + keyProperty + "=" + attributes.getValue(keyProperty) + ")";
			} else {
				// i.e. <font NAME="Segoe UI"/>
				parser.tagFullContent_tagName = tag;
			}
			parser.tagFullContent_stringBuffer = new StringBuffer();
			for (int i = 0; i < attributes.getLength(); i++) {
				if (!attributes.getQName(i).equals(keyProperty)) {
					node.getProperties().put(parser.tagFullContent_tagName + "." + attributes.getQName(i), attributes.getValue(i));
					parser.tagFullContent_hasAttributes = true;
				}
			}
		} 
		// we are here because this tag was the current "forcedTagProcessor" or
		// because we have to process an unknown tag => record what we see
		else {
			// for an unknown tag we must reset the buffer
			if (parser.tagFullContent_nesting == 0) {
				parser.tagFullContent_stringBuffer = new StringBuffer();
			}
			
			// in both cases we must take the content of the tag and put it in the buffer
			parser.tagFullContent_stringBuffer.append("<" + tag);
			for (int i = 0; i < attributes.getLength(); i++) {
				parser.tagFullContent_stringBuffer.append(" " + attributes.getQName(i) + "='" + attributes.getValue(i) + "'");
			}
			parser.tagFullContent_stringBuffer.append(">");
		}
		parser.tagFullContent_nesting++;		
	}
	
	void addEndContent(XmlNodePropertiesParser parser, String tag, Node node, String keyProperty){
		parser.tagFullContent_nesting--;
		// we have reached the end of a well-known tag
		if (parser.tagFullContent_nesting == 0 && keyProperty != null) {
			// i.e. the tag has content
			if (parser.tagFullContent_stringBuffer.length() != 0) {
					node.getProperties().put(parser.tagFullContent_tagName, parser.tagFullContent_stringBuffer.toString());
			} else {
				// i.e. <hook NAME="FreeNode"/> => no content, no attributes
				if (!parser.tagFullContent_hasAttributes) {
					node.getProperties().put(parser.tagFullContent_tagName, null);
				} 
			}
			
			parser.forcedTagProcessor = null;
			parser.tagFullContent_tagName = null;
			parser.tagFullContent_hasAttributes = false;
		} else {
			// i.e. record a tag ending for a nested tag
			parser.tagFullContent_stringBuffer.append("</" + tag + ">");
		}
	}
}
