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
public class ConvertAllAttributesProcessor extends AbstractTagProcessor {

	@Override
	public void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node) {
		if (!parser.isRoot) {
			addStartContentAndAttributes(parser, tag, attributes, node, "");
		} else {
			for (int i = 0; i < attributes.getLength(); i++) {
				node.getProperties().put(attributes.getQName(i), attributes.getValue(i));
			}
			parser.isRoot = false;
		}
		parser.convertAllAttributes_processedXmlTags.add(tag);
	}
	
	@Override
	public void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {
		addEndContent(parser, tag, node, "");
	}
}