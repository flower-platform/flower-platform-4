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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class TagsAsListProcessor extends AbstractTagProcessor {

	private String propertyName;
	private String keyAttribute;

	/**
	 *@author Catalin Burcea
	 **/
	public TagsAsListProcessor(String propertyName, String keyAttribute) {
		this.propertyName = propertyName;
		this.keyAttribute = keyAttribute;
	}

	@Override
	public void processStartTag(XmlParser parser, String tag, Attributes attributes, Map<String, Object> properties) {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) properties.get(propertyName);
		if (list == null) {
			list = new ArrayList<String>();
			properties.put(propertyName, list);
		}
		list.add(attributes.getValue(keyAttribute));
	}
}