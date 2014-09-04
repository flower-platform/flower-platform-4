/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.tests.freeplane;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.controller.xml_parser.XmlNodePropertiesParser;
import org.flowerplatform.tests.TestUtil;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class XmlParserTest {

	public static final String DIR = TestUtil.getResourcesDir(XmlParserTest.class);

	public static final String FULL_CONTENT_XML_FILE = "fullContentFile.xml";

	private void assertAndRemoveProperty(Node node, String property, Object value) {
		assertEquals("Wrong actual value of '" + property + "'", value, node.getProperties().remove(property));
	}
	
	@Test
	public void testParseFullContentXmlToNode() throws SAXException, IOException, ParserConfigurationException {
		String xmlContent = TestUtil.readFile(DIR + "/" + FULL_CONTENT_XML_FILE);
		Node node = new Node(null, null);
		XmlNodePropertiesParser handler = new XmlNodePropertiesParser(node);
		handler.parseXML(xmlContent);
		
		assertAndRemoveProperty(node, "TEXT", "test");
		assertAndRemoveProperty(node, "FOLDED", "false");
		assertAndRemoveProperty(node, "CREATED", "1407493284393");
		assertAndRemoveProperty(node, "ID", "ID_1229967238");
		assertAndRemoveProperty(node, "MODIFIED", "1407493284393");
		assertAndRemoveProperty(node, "richcontent(TYPE=DETAILS).HIDDEN", "true");
		assertAndRemoveProperty(node, "richcontent(TYPE=DETAILS)", "\n\t\t<html>\n\t\t\t" + 
										"<head></head>\n\t\t\t<body><p>Details .... </p></body>" +
										"\n\t\t</html>\n\t");
		assertAndRemoveProperty(node, "icons", Arrays.asList("yes", "checked", "password"));
		assertAndRemoveProperty(node, "cloud.COLOR", "#0033ff");
		assertAndRemoveProperty(node, "cloud.SHAPE", "RECT");
		assertAndRemoveProperty(node, "font.NAME", "Segoe UI");
		assertAndRemoveProperty(node, "font.BOLD", "true");
		assertAndRemoveProperty(node, "font.ITALIC", "true");
		assertAndRemoveProperty(node, "font.SIZE", "18");
		assertAndRemoveProperty(node, "hook(NAME=FirstGroupNode)", null);
		assertAndRemoveProperty(node, "hook(NAME=MapStyle)", "\n\t\t<map_styles>\n\t\t\t" + 
										"<stylenode LOCALIZED_TEXT='styles.root_node'></stylenode>" + 
										"\n\t\t</map_styles>\n\t");
		assertAndRemoveProperty(node, "hook(NAME=NodeConditionalStyles)", 
										"\n\t\t<conditional_style ACTIVE='true' STYLE_REF='TitlesContent' LAST='false'></conditional_style>" + 
										"\n\t\t<conditional_style ACTIVE='true' STYLE_REF='BeginnerTopic' LAST='false'></conditional_style>\n\t");
		assertAndRemoveProperty(node, "attribute(NAME=a1).VALUE", "v1");
		assertAndRemoveProperty(node, "attribute(NAME=a2).VALUE", "v2");
		assertAndRemoveProperty(node, "attribute(NAME=a3).VALUE", "v3");
		assertAndRemoveProperty(node, "unknownTag2.unknownProp1", "value1");
		assertAndRemoveProperty(node, "unknownTag2.unknownProp2", "value2");
		assertAndRemoveProperty(node, "unknownTag2", "\n\t\t<icon BUILTIN='password'></icon>\n\t");
		assertAndRemoveProperty(node, "unknown", "<unknownTag1 unknownProp1='value1' unknownProp2='value2'>\n\t\t" + 
										"<node TEXT='test' ID='ID_1229967238' CREATED='1407493284393' MODIFIED='1407493284393' FOLDED='false'>" + 
										"\n\t\t\t<richcontent TYPE='DETAILS' HIDDEN='true'>" +
										"\n\t\t\t\t<html><head></head><body><p>Details .... </p></body></html>" + 
										"\n\t\t\t</richcontent>\n\t\t</node>\n\t</unknownTag1><unknownTag1 unknownProp1='value1' unknownProp2='value2'>" + 
										"\n\t\t<attribute NAME='a1' VALUE='v1'></attribute>\n\t</unknownTag1>");
		assertEquals("Node still has properties", 0, node.getProperties().size());
	}
}