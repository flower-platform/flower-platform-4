package org.flowerplatform.freeplane.controller.xml_parser;

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 *
 */
public class ConvertAllAttributesProcessor implements ITagProcessor {

	@Override
	public void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node) {
		if (parser.xmlTags.contains(tag)) {
			parser.convertAllAttributes_tagProcessorDinamicallyAdded = true;
			parser.xmlTagProcessors.put(tag, new DuplicateTagProcessor());
			return;
		}

		for (int i = 0; i < attributes.getLength(); i++) {
			node.getProperties().put(tag + "." + attributes.getQName(i), attributes.getValue(i));
		}
		parser.xmlTags.add(tag);
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
