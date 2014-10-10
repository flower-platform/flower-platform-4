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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.freeplane.FreeplaneConstants;

/**
 * @author Valentina Bojan
 */
public class XmlWritter {

	protected XmlConfiguration configuration;
	
	private Map<String, Object> properties;

	/**
	 * @author see class
	 */
	public XmlWritter(XmlConfiguration configuration, Map<String, Object> properties) {
		super();
		this.configuration = configuration;
		this.properties = properties;
	}

	/**
	 * @author see class
	 */
	public String getXmlContent() {
		StringBuffer xmlContent = new StringBuffer();
		String previousTag = null;
		Map<String, Object> nodeProperties = new TreeMap<String, Object>(properties);

		// Write the node tag with its attributes
		xmlContent.append("<" + FreeplaneConstants.NODE);
		xmlContent.append(createNodeTag(nodeProperties));
		xmlContent.append(">" + "\n");

		for (Entry<String, Object> entry : nodeProperties.entrySet()) {
			String property = entry.getKey();
			Object value = entry.getValue();

			// Write the icon tags with their attributes
			if (property.equals(FreeplaneConstants.ICONS)) {				
				xmlContent.append(closeTag(property, previousTag));
				xmlContent.append(createIconTags(value));
				previousTag = null;
				continue;				
			}
			
			// Write the content of the unknown tag
			if (property.equals(FreeplaneConstants.UNKNOWN)) {
				xmlContent.append("\t" + value + "\n");
				previousTag = null;
				continue;
			} 
			
			// Write the attributes of a tag which can also have a content (tags with propertyKey like hook, richContent)
			Matcher matcher = configuration.fullAttributeTagPattern.matcher(property);
			if (matcher.find()) {
				String tag = matcher.group(1);
				String propertyKey = matcher.group(2);
				String attribute = matcher.group(3);
				xmlContent.append(createFullTagAttributes(tag, propertyKey, attribute, value, previousTag));
				previousTag = tag + propertyKey;
				continue;
			}
			
			// Write the content of a tag which can also have attributes (tags with propertyKey like hook, richContent)
			matcher = configuration.fullContentTagPattern.matcher(property);
			if (matcher.find()) {
				String tag = matcher.group(1);
				String propertyKey = matcher.group(2);
				xmlContent.append(createFullTagContent(tag, propertyKey, value, previousTag));
				previousTag = null;
				continue;
			}
			
			// Create a tag without content or attributes, only with property key (also like hook, richContent)
			matcher = configuration.noContetNoAttributesPattern.matcher(property);
			if (matcher.find()) {
				String tag = matcher.group(1);
				String propertyKey = matcher.group(2);
				xmlContent.append(closeTag(tag + propertyKey, previousTag));
				String[] propertyKeySplit = propertyKey.split("=");
				xmlContent.append("\t<" + tag + " " + propertyKeySplit[0] + "='" + propertyKeySplit[1] + "'/>\n");
				previousTag = null;
				continue;
			}
			
			// Create a simple tag which has only attributes (like font, cloud, ...)
			matcher = configuration.simpleAttributesPattern.matcher(property);
			if (matcher.find()) {
				String tag = matcher.group(1);
				String attribute = matcher.group(2);
				xmlContent.append(createFullTagAttributes(tag, "", attribute, value, previousTag));
				previousTag = tag;
				continue;
			}
			
			// Create a simple tag which has content, but no property key (like unknownTag1, ...)
			matcher = configuration.simpleContentPattern.matcher(property);
			if (matcher.find()) {
				String tag = matcher.group(1);
				xmlContent.append(createFullTagContent(tag, "", value, previousTag));
				previousTag = null;
			}
		}
		
		xmlContent.append(closeTag("", previousTag));
		xmlContent.append("</" + FreeplaneConstants.NODE + ">");

		return xmlContent.toString();
	}

	/**
	 * Method which close a tag. If the current tag is different from the
	 * previous tag and the previous tag is not null, then the method will
	 * return the tag closure. In other case, it means that the tag was closed
	 * manually and there is no need for closure.
	 */
	private String closeTag(String tag, String previousTag) {
		return (!tag.equals(previousTag) && previousTag != null) ? "/>\n" : "";
	}
	
	/**
	 * Method which takes the node attributes and puts them into a StringBuffer.
	 * This method will be used to complete the tag <node> with its attributes.
	 * i.e. <node TEXT='MyNode' CREATED='1407493284393'>
	 */
	private StringBuffer createNodeTag(Map<String, Object> nodeProperties) {
		StringBuffer nodeAttributes = new StringBuffer();
		Pattern nodeAttributesPattern = Pattern.compile("^[^\\.\\(]+$");

		for (Iterator<Map.Entry<String, Object>> iterator = nodeProperties.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> entry = iterator.next();
			String property = entry.getKey();
			Object value = entry.getValue();

			Matcher matcher = nodeAttributesPattern.matcher(property);
			if (matcher.find() && !property.equals(FreeplaneConstants.ICONS)
							   && !property.equals(FreeplaneConstants.UNKNOWN)
							   && !property.contains(FreeplaneConstants.CONTENT_MARK)) {
				nodeAttributes.append(" " + property + "='" + value + "'");
				iterator.remove();
			}
		}

		return nodeAttributes;
	}
	
	/**
	 * Method which receives an array with attributes and converts them into icon tags.
	 * i.e. From the array [yes, checked, password] results the following output:
	 * 	<icon BUILTIN=yes/>
	 * 	<icon BUILTIN=checked/>
	 * 	<icon BUILTIN=password/>
	 */
	@SuppressWarnings("unchecked")
	private StringBuffer createIconTags(Object value) {
		StringBuffer iconTagsBuffer = new StringBuffer();
		for (String icon : (ArrayList<String>) value) {
			iconTagsBuffer.append("\t<" + FreeplaneConstants.ICON + " " + FreeplaneConstants.ICON_KEY_PROPERTY + "='" + icon + "'/>\n");
		}
		return iconTagsBuffer;
	}
	
	/**
	 * Method which create a new tag and adds its first attribute (if it is
	 * the first time this tag appears in the Map with properties) or which
	 * simply adds a attribute to the current tag. This method will be used to
	 * create tags like hook or richContent, tags which have both attributes
	 * and content, so appear twice in the map with properties
	 * (i.e. hook(NAME=myHook).COUNTER=4, hook(NAME=myHook)_content='myContent').
	 * If the parameter preopertKey is the empty string then this method is used 
	 * to create tags which have only attributes (i.e. font, cloud, ...).
	 */
	private StringBuffer createFullTagAttributes(String tag, String propertyKey, String attribute, Object value, String previousTag) {
		StringBuffer fullTagAttributesBuffer = new StringBuffer();

		fullTagAttributesBuffer.append(closeTag(tag + propertyKey, previousTag));
		if ((tag + propertyKey).equals(previousTag)) {
			fullTagAttributesBuffer.append(" " + attribute + "='" + value + "'");
		} else {
			if (propertyKey.equals("")) {
				fullTagAttributesBuffer.append("\t<" + tag + " " + attribute + "='" + value + "'");
			} else {
				String[] propertyKeySplit = propertyKey.split("=");
				fullTagAttributesBuffer.append("\t<" + tag + " " + propertyKeySplit[0] + "='" + propertyKeySplit[1] + "' " + attribute + "='" + value + "'");
			}
		}

		return fullTagAttributesBuffer;
	}
	
	/**
	 * Method which create a new tag and adds its property key and content(if it is
	 * the first time this tag appears in the Map with properties) or which
	 * simply adds the content to the current tag. This method will be used to
	 * create tags like hook or richContent, tags which have both attributes
	 * and content, so appear twice in the map with properties
	 * (i.e. hook(NAME=myHook).COUNTER=4, hook(NAME=myHook)_content='myContent').
	 * If the parameter preopertKey is the empty string then this method is used 
	 * to create tags which have content but not a property key (i.e. unknownTag1, ...).
	 */
	private StringBuffer createFullTagContent(String tag, String propertyKey, Object value, String previousTag) {
		StringBuffer fullTagContentBuffer = new StringBuffer();

		if ((tag + propertyKey).equals(previousTag)) {
			fullTagContentBuffer.append(">" + value + "</" + tag + ">\n");
		} else {
			fullTagContentBuffer.append(closeTag(tag + propertyKey, previousTag));
			if (propertyKey.equals("")) {
				fullTagContentBuffer.append("\t<" + tag + ">" + value + "</" + tag + ">\n");
			} else {
				String[] propertyKeySplit = propertyKey.split("=");
				fullTagContentBuffer.append("\t<" + tag + " " + propertyKeySplit[0] + "='" + propertyKeySplit[1] + "'>" + value + "</" + tag + ">\n");
			}
		}

		return fullTagContentBuffer;
	}
}
