package org.flowerplatform.freeplane.controller.xml_parser;

import org.flowerplatform.core.node.remote.Node;
import org.xml.sax.Attributes;

/**
 * @author Catalin Burcea
 *
 */
public interface ITagProcessor {

	void processStartTag(XmlNodePropertiesParser parser, String tag, Attributes attributes, Node node);

	void processEndTag(XmlNodePropertiesParser parser, String tag, Node node);

	void processPlainText(XmlNodePropertiesParser parser, String plainText);
}
