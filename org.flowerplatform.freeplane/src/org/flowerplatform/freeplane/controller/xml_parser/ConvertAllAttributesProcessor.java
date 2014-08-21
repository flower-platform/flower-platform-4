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
		String result = null;
		if (attributes.getLength() > 0) {
			result = (String) node.getProperties().get(tag + "." + attributes.getQName(0));
		}
		if (result != null) {
			parser.convertAllAttributes_tagProcessorDinamicallyAdded = true;
			parser.xmlTagProcessors.put(tag, new DuplicateTagProcessor());
		} else {
			for (int i = 0; i < attributes.getLength(); i++) {
				node.getProperties().put(tag + "." + attributes.getQName(i), attributes.getValue(i));
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
