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
import org.flowerplatform.freeplane.FreeplaneConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public abstract class AbstractTagProcessor {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTagProcessor.class);

	/**
	 *@author Catalin Burcea
	 */
	protected void processStartTag(XmlParser parser, String tag, Attributes attributes, Node node) {
	}

	/**
	 *@author Catalin Burcea
	 */
	protected void processEndTag(XmlParser parser, String tag, Node node) {
	}

	/**
	 *@author Catalin Burcea
	 */
	protected void processPlainText(XmlParser parser, String plainText) {
		parser.tagFullContent_StringBuffer.append(plainText);
	}

	/**
	 * @author Catalin Burcea
	 * @author Valentina Bojan
	 */
	protected void addStartContentAndAttributes(XmlParser parser, String tag, Attributes attributes, Node node, String keyProperty) {
		if (parser.tagFullContent_Nesting == 0 && keyProperty != null) {
			// first invocation of this processor for a well-known tag
			parser.forcedTagProcessor = this;

			// check the current tag is <node> => exception for nested node tags
			// (our node is a single node => no children)
			if (tag.equals(FreeplaneConstants.NODE)) {
				throw new RuntimeException("'Nested node tags' exception for Single Node");
			}

			// check for duplicates tags; if there is any duplicate => assign
			// this type of tag to a specific processor
			if (parser.convertAllAttributes_ProcessedXmlTags.contains(tag)) {
				parser.convertAllAttributes_TagProcessorDinamicallyAdded = true;
				LOGGER.debug(String.format("Dynamically adding new processor for unknown tag = {%s}", TagFullContentProcessor.class.getSimpleName()));
				parser.configuration.xmlTagProcessors.put(tag, new TagFullContentProcessor(null));
				parser.tagFullContent_Nesting++;
				return;
			}

			// i.e. <attribute NAME="a1" VALUE="v1"/>
			if (!keyProperty.isEmpty()) {
				parser.tagFullContent_TagName = tag + "(" + keyProperty + "=" + attributes.getValue(keyProperty) + ")";
			} else {
				// i.e. <font NAME="Segoe UI"/>
				parser.tagFullContent_TagName = tag;
			}
			parser.tagFullContent_StringBuffer = new StringBuffer();
			for (int i = 0; i < attributes.getLength(); i++) {
				if (!attributes.getQName(i).equals(keyProperty)) {
					node.getProperties().put(parser.tagFullContent_TagName + "." + attributes.getQName(i), attributes.getValue(i));
					parser.tagFullContent_HasAttributes = true;
				}
			}
		} else {
			// we are here because this tag was the current "forcedTagProcessor" or
			// because we have to process an unknown tag => record what we see
			
			// for an unknown tag we must reset the buffer
			if (parser.tagFullContent_Nesting == 0) {
				parser.tagFullContent_StringBuffer = new StringBuffer();
			}

			// in both cases we must take the content of the tag and put it in
			// the buffer
			parser.tagFullContent_StringBuffer.append("<" + tag);
			for (int i = 0; i < attributes.getLength(); i++) {
				parser.tagFullContent_StringBuffer.append(" " + attributes.getQName(i) + "='" + attributes.getValue(i) + "'");
			}
			parser.tagFullContent_StringBuffer.append(">");
		}
		parser.tagFullContent_Nesting++;
	}
	
	/**
	 * @author Catalin Burcea
	 * @author Valentina Bojan
	 */
	protected void addEndContent(XmlParser parser, String tag, Node node, String keyProperty) {
		parser.tagFullContent_Nesting--;
		// we have reached the end of a well-known tag
		if (parser.tagFullContent_Nesting == 0 && keyProperty != null) {
			// i.e. the tag has content
			if (parser.tagFullContent_StringBuffer.length() != 0) {
				node.getProperties().put(parser.tagFullContent_TagName + FreeplaneConstants.CONTENT_MARK, parser.tagFullContent_StringBuffer.toString());
			} else {
				// i.e. <hook NAME="FreeNode"/> => no content, no attributes
				if (!parser.tagFullContent_HasAttributes) {
					node.getProperties().put(parser.tagFullContent_TagName, null);
				}
			}

			parser.forcedTagProcessor = null;
			parser.tagFullContent_TagName = null;
			parser.tagFullContent_HasAttributes = false;
		} else {
			// i.e. record a tag ending for a nested tag
			parser.tagFullContent_StringBuffer.append("</" + tag + ">");
		}
	}
}
