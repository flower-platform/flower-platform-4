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

import java.util.Map;

import org.flowerplatform.freeplane.FreeplaneConstants;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class TagFullContentProcessor extends AbstractTagProcessor {

	private String keyProperty;

	/**
	 *@author Catalin Burcea
	 **/
	public TagFullContentProcessor(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	@Override
	public void processStartTag(XmlParser parser, String tag, Attributes attributes, Map<String, Object> properties) {
		addStartContentAndAttributes(parser, tag, attributes, properties, keyProperty);
	}

	@Override
	public void processEndTag(XmlParser parser, String tag, Map<String, Object> properties) {
		addEndContent(parser, tag, properties, keyProperty);

		// we have reached the end of an unknown tag
		if (parser.tagFullContent_Nesting == 0 && keyProperty == null) {
			String currentContent = (String) properties.get(FreeplaneConstants.UNKNOWN);
			properties.put(FreeplaneConstants.UNKNOWN,
						   currentContent == null ? parser.tagFullContent_StringBuffer.toString() : currentContent + "\n\t" + parser.tagFullContent_StringBuffer.toString());
		}
	}
}