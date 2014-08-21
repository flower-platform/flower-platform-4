package org.flowerplatform.freeplane.controller.xml_parser;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 *
 */
public class TagsAsCsvListProcessor implements ITagProcessor {

	private String propertyName;
	private String keyAttribute;

	public TagsAsCsvListProcessor(String propertyName, String keyAttribute) {
		this.propertyName = propertyName;
		this.keyAttribute = keyAttribute;
	}

	@Override
	public void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node) {
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) node.getProperties().get(propertyName);
		if (list == null) {
			list = new ArrayList<String>();
			node.getProperties().put(propertyName, list);
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			if (attributes.getQName(i).equals(keyAttribute)) {
				list.add(attributes.getValue(i));
			}
		}
	}

	@Override
	public void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {
		// nothing to do
	}

	@Override
	public void processPlainText(XmlNodePropertiesParser parser, String plainText) {
		parser.tagFullContent_plainTextBuffer.append(plainText);
	}
}
