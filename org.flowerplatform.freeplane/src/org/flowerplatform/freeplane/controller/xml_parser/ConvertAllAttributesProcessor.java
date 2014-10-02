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

import java.util.Map;

import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class ConvertAllAttributesProcessor extends AbstractTagProcessor {

	@Override
	public void processStartTag(XmlParser parser, String tag, Attributes attributes, Map<String, Object> properties) {
		if (!parser.isRoot) {
			addStartContentAndAttributes(parser, tag, attributes, properties, "");
		} else {
			for (int i = 0; i < attributes.getLength(); i++) {
				properties.put(attributes.getQName(i), attributes.getValue(i));
			}
			parser.isRoot = false;
		}
		parser.convertAllAttributes_ProcessedXmlTags.add(tag);
	}

	@Override
	public void processEndTag(XmlParser parser, String tag, Map<String, Object> properties) {
		addEndContent(parser, tag, properties, "");
	}
}