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
		if (parser.convertAllAttributes_processedXmlTags.contains(tag)) {
			parser.convertAllAttributes_tagProcessorDinamicallyAdded = true;
			// TODO CS: de adaugat ce trebuie; si log: "Dynamically adding new processor for unknown tag = {}"
//			parser.xmlTagProcessors.put(tag, new DuplicateTagProcessor());
			return;
		}

		// TODO CS: l-as pune intr-o metoda: addAttributesToProperties(excludeProperty); ar fi apelata din Abstr...start(). Clasa asta ar face: fac bla-bla, super(), bla-bla. 
		// TagsAsList: ar suprascrie metoda si nu face nimic
		// FullContent: 
		for (int i = 0; i < attributes.getLength(); i++) {
			node.getProperties().put(tag + "." + attributes.getQName(i), attributes.getValue(i));
		}
		parser.convertAllAttributes_processedXmlTags.add(tag);
	}

	@Override
	public void processEndTag(XmlNodePropertiesParser parser, String tag, Node node) {
		// nothing to do
	}

	@Override
	public void processPlainText(XmlNodePropertiesParser parser, String plainText) {
		// TODO CS: de transformat ITagPr -> AbstractTagProcessor; va contine acest cod
		parser.tagFullContent_stringBuffer.append(plainText);
	}
}
