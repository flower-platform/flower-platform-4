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
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public abstract class AbstractTagProcessor {

	/**
	 *@author Catalin Burcea
	 **/
	void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node) {
	}

	/**
	 *@author Catalin Burcea
	 **/
	void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {
	}

	/**
	 *@author Catalin Burcea
	 **/
	void processPlainText(XmlNodePropertiesParser parser, String plainText) {
		parser.tagFullContentStringBuffer.append(plainText);
	}

	/**
	 * @author Catalin Burcea
	 * @author Valentina Bojan
	 */
	void addStartContentAndAttributes(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node, String keyProperty) {
		// first invocation of this processor for a well-known tag
		if (parser.tagFullContentNesting == 0 && keyProperty != null) {
			parser.forcedTagProcessor = this;

			// check the current tag is <node> => exception for nested node tags
			// (our node is a single node => no children)
			if (tag.equals(FreeplaneConstants.NODE)) {
				throw new RuntimeException("'Nested node tags' exception for Single Node");
			}

			// check for duplicates tags; if there is any duplicate => assign
			// this type of tag to a specific processor
			if (parser.convertAllAttributesProcessedXmlTags.contains(tag)) {
				parser.convertAllAttributesTagProcessorDinamicallyAdded = true;
				parser.logger.debug(String.format("Dynamically adding new processor for unknown tag = {%s}", TagFullContentProcessor.class.getSimpleName()));
				parser.xmlTagProcessors.put(tag, new TagFullContentProcessor(null));
				parser.tagFullContentNesting++;
				return;
			}

			// i.e. <attribute NAME="a1" VALUE="v1"/>
			if (!keyProperty.isEmpty()) {
				parser.tagFullContentTagName = tag + "(" + keyProperty + "=" + attributes.getValue(keyProperty) + ")";
			} else {
				// i.e. <font NAME="Segoe UI"/>
				parser.tagFullContentTagName = tag;
			}
			parser.tagFullContentStringBuffer = new StringBuffer();
			for (int i = 0; i < attributes.getLength(); i++) {
				if (!attributes.getQName(i).equals(keyProperty)) {
					node.getProperties().put(parser.tagFullContentTagName + "." + attributes.getQName(i), attributes.getValue(i));
					parser.tagFullContentHasAttributes = true;
				}
			}
		} else {
			// we are here because this tag was the current "forcedTagProcessor" or
			// because we have to process an unknown tag => record what we see
			
			// for an unknown tag we must reset the buffer
			if (parser.tagFullContentNesting == 0) {
				parser.tagFullContentStringBuffer = new StringBuffer();
			}

			// in both cases we must take the content of the tag and put it in
			// the buffer
			parser.tagFullContentStringBuffer.append("<" + tag);
			for (int i = 0; i < attributes.getLength(); i++) {
				parser.tagFullContentStringBuffer.append(" " + attributes.getQName(i) + "='" + attributes.getValue(i) + "'");
			}
			parser.tagFullContentStringBuffer.append(">");
		}
		parser.tagFullContentNesting++;
	}
	
	/**
	 * @author Catalin Burcea
	 * @author Valentina Bojan
	 */
	void addEndContent(XmlNodePropertiesParser parser, String tag, Node node, String keyProperty) {
		parser.tagFullContentNesting--;
		// we have reached the end of a well-known tag
		if (parser.tagFullContentNesting == 0 && keyProperty != null) {
			// i.e. the tag has content
			if (parser.tagFullContentStringBuffer.length() != 0) {
				node.getProperties().put(parser.tagFullContentTagName + FreeplaneConstants.CONTENT_MARK, parser.tagFullContentStringBuffer.toString());
			} else {
				// i.e. <hook NAME="FreeNode"/> => no content, no attributes
				if (!parser.tagFullContentHasAttributes) {
					node.getProperties().put(parser.tagFullContentTagName, null);
				}
			}

			parser.forcedTagProcessor = null;
			parser.tagFullContentTagName = null;
			parser.tagFullContentHasAttributes = false;
		} else {
			// i.e. record a tag ending for a nested tag
			parser.tagFullContentStringBuffer.append("</" + tag + ">");
		}
	}
}
