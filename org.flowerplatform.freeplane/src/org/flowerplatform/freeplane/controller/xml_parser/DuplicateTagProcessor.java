package org.flowerplatform.freeplane.controller.xml_parser;

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 *
 */
public class DuplicateTagProcessor implements ITagProcessor {

	@Override
	public void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node) {
		String result = "<" + tag;
		for (int i = 0; i < attributes.getLength(); i++) {
			result += " " + attributes.getQName(i) + "='" + attributes.getValue(i) + "'";
		}
		result += "/>";
		String currentContent = (String) node.getProperties().get(tag);
		node.getProperties().put(tag, currentContent == null ? result : currentContent + result);
	}

	@Override
	public void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {
		// nothing to do
	}

	@Override
	public void processPlainText(XmlNodePropertiesParser parser, String plainText) {
		parser.tagFullContent_stringBuffer.append(plainText);
	}
}
