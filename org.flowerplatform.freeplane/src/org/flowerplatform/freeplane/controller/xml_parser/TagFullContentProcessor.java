package org.flowerplatform.freeplane.controller.xml_parser;

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 *
 */
public class TagFullContentProcessor implements ITagProcessor {
// TODO CS: trebuie facut sa mearga si in modul "unknown"; cred ca pentru asta ar trebui sa fie keyProperty = null
	
	private String keyProperty;
	private int tagIndex;

	public TagFullContentProcessor(String keyProperty) {
		this.keyProperty = keyProperty;
		this.tagIndex = 0;
	}

	@Override
	public void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node) {
		if (parser.tagFullContent_nesting == 0) {
			// i.e. first invocation of this processor
			parser.forcedTagProcessor = this;
			parser.tagFullContent_tagName = getPropertyName(tag, attributes);
			parser.tagFullContent_stringBuffer = new StringBuffer();
			for (int i = 0; i < attributes.getLength(); i++) {
				if (!attributes.getQName(i).equals(keyProperty)) {
					node.getProperties().put(parser.tagFullContent_tagName + "." + attributes.getQName(i), attributes.getValue(i));
				}
			}
		} else {
			// i.e. we are here because this tag was the current "forcedTagProcessor" => record what we see
			parser.tagFullContent_stringBuffer.append("<" + tag);
			for (int i = 0; i < attributes.getLength(); i++) {
				parser.tagFullContent_stringBuffer.append(" " + attributes.getQName(i) + "='" + attributes.getValue(i) + "'");
			}
			parser.tagFullContent_stringBuffer.append(">");
		}
		parser.tagFullContent_nesting++;
	}

	@Override
	public void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {
		parser.tagFullContent_nesting--;
		if (parser.tagFullContent_nesting == 0) {
			// i.e. we have reached the end tag
			// TODO CS: as folosi si testul pentru numele tagului; dc nu e corect => exceptie
			node.getProperties().put(parser.tagFullContent_tagName, parser.tagFullContent_stringBuffer.toString());
			parser.forcedTagProcessor = null;
			parser.tagFullContent_tagName = null;
		} else {
			// i.e. record a tag ending
			parser.tagFullContent_stringBuffer.append("</" + tag + ">");
		}
	}

	// TODO CS: de scos tagIndex; de sters metoda asta
	private String getPropertyName(String tag, Attributes attributes) {
		if (keyProperty == null) {
			return tag + "." + tagIndex++;
		}
		String result = null;
		for (int i = 0; i < attributes.getLength(); i++) {
			if (attributes.getQName(i).equals(keyProperty)) {
				result = tag + "." + attributes.getValue(i);
			}
		}
		return result;
	}

	@Override
	public void processPlainText(XmlNodePropertiesParser parser, String plainText) {
		parser.tagFullContent_stringBuffer.append(plainText);
	}
}
