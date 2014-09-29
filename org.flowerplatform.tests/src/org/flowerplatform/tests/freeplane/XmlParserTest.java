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

import static org.flowerplatform.tests.TestUtil.assertEqualsMaps;
import static org.flowerplatform.tests.TestUtil.getResourcesDir;
import static org.flowerplatform.tests.TestUtil.readFile;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.controller.xml_parser.XmlConfiguration;
import org.flowerplatform.freeplane.controller.xml_parser.XmlParser;
import org.flowerplatform.freeplane.controller.xml_parser.XmlWritter;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public class XmlParserTest {

	public static final String DIR = getResourcesDir(XmlParserTest.class);
	public static final String UNKNOWN_TAGS_XML_FILE = "unknownTagsFile.xml";
	public static final String DIFFERENT_TAGS_XML_FILE = "differentTagsFile.xml";
	
	protected XmlConfiguration configuration = new XmlConfiguration();
	
	/**
	 * @author see class
	 */
	private void assertAndRemoveProperty(Node node, String property, Object value) {
		assertEquals("Wrong actual value of '" + property + "'", value, node.getProperties().remove(property));
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testParseXmlToNodeDifferentTags() throws SAXException, IOException, ParserConfigurationException {
		String xmlContent = readFile(DIR + "/" + DIFFERENT_TAGS_XML_FILE);
		Node node = new Node(null, null);
		XmlParser handler = new XmlParser(configuration, node);
		handler.parseXML(xmlContent);
		
		assertAndRemoveProperty(node, "TEXT", "Node1");
		assertAndRemoveProperty(node, "ID", "ID_93279313");
		assertAndRemoveProperty(node, "CREATED", "1409904418111");
		assertAndRemoveProperty(node, "MODIFIED", "1409905083387");
		assertAndRemoveProperty(node, "COLOR", "#ffff99");
		assertAndRemoveProperty(node, "BACKGROUND_COLOR", "#ff3366");
		assertAndRemoveProperty(node, "LINK", "#ID_1532596670");
		assertAndRemoveProperty(node, "icons", Arrays.asList("stop-sign", "password"));
		assertAndRemoveProperty(node, "edge.COLOR", "#660000");
		assertAndRemoveProperty(node, "cloud.COLOR", "#ccffff");
		assertAndRemoveProperty(node, "cloud.SHAPE", "ARC");
		assertAndRemoveProperty(node, "attribute(NAME=attribute1).VALUE", "value1");
		assertAndRemoveProperty(node, "font.NAME", "Serif");
		assertAndRemoveProperty(node, "font.BOLD", "true");
		assertAndRemoveProperty(node, "font.ITALIC", "true");
		assertAndRemoveProperty(node, "font.SIZE", "12");
		assertAndRemoveProperty(node, "arrowlink.SHAPE", "CUBIC_CURVE");
		assertAndRemoveProperty(node, "arrowlink.COLOR", "#000000");
		assertAndRemoveProperty(node, "arrowlink.TRANSPARENCY", "80");
		assertAndRemoveProperty(node, "richcontent(TYPE=DETAILS)_content", "\n\t\t<html><head></head><body><p>Node1_details</p></body></html>\n\t");
		assertAndRemoveProperty(node, "richcontent(TYPE=NOTE)_content", "\n\t\t<html><head></head><body><p>Node1_Note</p></body></html>\n\t");
		assertAndRemoveProperty(node, "richcontent(TYPE=NEW_TYPE)_content", "\n\t\t<node></node>\n\t");
		assertAndRemoveProperty(node, "hook(NAME=AutomaticEdgeColor).COUNTER", "4");
		assertAndRemoveProperty(node, "hook(NAME=MapStyle)_content", 
										"\n\t\t<properties show_note_icons='true' show_icon_for_attributes='true'></properties>"
										+ "\n\t\t<map_styles>\n\t\t\t<stylenode LOCALIZED_TEXT='styles.root_node'>\n\t\t\t\t"
										+ "<stylenode LOCALIZED_TEXT='styles.predefined' POSITION='right'>\n\t\t\t\t\t"
										+ "<stylenode LOCALIZED_TEXT='default' MAX_WIDTH='600' COLOR='#000000' STYLE='as_parent'>\n\t\t\t\t\t\t"
										+ "<font NAME='SansSerif' SIZE='10' BOLD='false' ITALIC='false'></font>\n\t\t\t\t\t\t"
										+ "<icon BUILTIN='yes'></icon>\n\t\t\t\t\t</stylenode>\n\t\t\t\t\t<stylenode LOCALIZED_TEXT='defaultstyle.note'></stylenode>\n\t\t\t\t\t"
										+ "<stylenode LOCALIZED_TEXT='defaultstyle.floating'>\n\t\t\t\t\t\t<edge STYLE='hide_edge'></edge>\n\t\t\t\t\t\t"
										+ "<cloud COLOR='#f0f0f0' SHAPE='ROUND_RECT'></cloud>\n\t\t\t\t\t</stylenode>\n\t\t\t\t</stylenode>\n\t\t\t" 
										+ "</stylenode>\n\t\t</map_styles>\n\t");
		assertEquals("Node still has properties", 0, node.getProperties().size());
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testParseXmlToNodeUnknownTags() throws SAXException, IOException, ParserConfigurationException {
		String xmlContent = readFile(DIR + "/" + UNKNOWN_TAGS_XML_FILE);
		Node node = new Node(null, null);
		XmlParser handler = new XmlParser(configuration, node);
		handler.parseXML(xmlContent);
		
		assertAndRemoveProperty(node, "TEXT", "test");
		assertAndRemoveProperty(node, "FOLDED", "false");
		assertAndRemoveProperty(node, "CREATED", "1407493284393");
		assertAndRemoveProperty(node, "ID", "ID_1229967238");
		assertAndRemoveProperty(node, "MODIFIED", "1407493284393");
		assertAndRemoveProperty(node, "richcontent(TYPE=DETAILS).HIDDEN", "true");
		assertAndRemoveProperty(node, "richcontent(TYPE=DETAILS)_content", "\n\t\t<html>\n\t\t\t" 
										+ "<head></head>\n\t\t\t<body><p>Details .... </p></body>" 
										+ "\n\t\t</html>\n\t");
		assertAndRemoveProperty(node, "icons", Arrays.asList("yes", "checked", "password"));
		assertAndRemoveProperty(node, "cloud.COLOR", "#0033ff");
		assertAndRemoveProperty(node, "cloud.SHAPE", "RECT");
		assertAndRemoveProperty(node, "font.NAME", "Segoe UI");
		assertAndRemoveProperty(node, "font.BOLD", "true");
		assertAndRemoveProperty(node, "font.ITALIC", "true");
		assertAndRemoveProperty(node, "font.SIZE", "18");
		assertAndRemoveProperty(node, "hook(NAME=FirstGroupNode)", null);
		assertAndRemoveProperty(node, "hook(NAME=MapStyle)_content", "\n\t\t<map_styles>\n\t\t\t" 
										+ "<stylenode LOCALIZED_TEXT='styles.root_node'></stylenode>" 
										+ "\n\t\t</map_styles>\n\t");
		assertAndRemoveProperty(node, "hook(NAME=NodeConditionalStyles)_content", 
										"\n\t\t<conditional_style ACTIVE='true' STYLE_REF='TitlesContent' LAST='false'></conditional_style>" 
										+ "\n\t\t<conditional_style ACTIVE='true' STYLE_REF='BeginnerTopic' LAST='false'></conditional_style>\n\t");
		assertAndRemoveProperty(node, "attribute(NAME=a1).VALUE", "v1");
		assertAndRemoveProperty(node, "attribute(NAME=a2).VALUE", "v2");
		assertAndRemoveProperty(node, "attribute(NAME=a3).VALUE", "v3");
		assertAndRemoveProperty(node, "unknownTag2.unknownProp1", "value1");
		assertAndRemoveProperty(node, "unknownTag2.unknownProp2", "value2");
		assertAndRemoveProperty(node, "unknownTag2_content", "\n\t\t<icon BUILTIN='password'></icon>\n\t");
		assertAndRemoveProperty(node, "unknown", "<unknownTag1 unknownProp1='value1' unknownProp2='value2'>\n\t\t" 
										+ "<node TEXT='test' ID='ID_1229967238' CREATED='1407493284393' MODIFIED='1407493284393' FOLDED='false'>" 
										+ "\n\t\t\t<richcontent TYPE='DETAILS' HIDDEN='true'>" 
										+ "\n\t\t\t\t<html><head></head><body><p>Details .... </p></body></html>" 
										+ "\n\t\t\t</richcontent>\n\t\t</node>\n\t</unknownTag1>\n\t<unknownTag1 unknownProp1='value1' unknownProp2='value2'>" 
										+ "\n\t\t<attribute NAME='a1' VALUE='v1'></attribute>\n\t</unknownTag1>");
		assertEquals("Node still has properties", 0, node.getProperties().size());
	}
	
	/**
	 * @author see class
	 */
	private void serializeDeserializeAndCompareXMLContent(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
		String oldXmlContent = readFile(DIR + "/" + xmlFileName);
		Node oldNode = new Node(null, null);
		XmlParser handler = new XmlParser(configuration, oldNode);
		handler.parseXML(oldXmlContent);
		
		XmlWritter xmlCreator = new XmlWritter(configuration, oldNode);
		Node newNode = new Node(null, null);
		handler = new XmlParser(configuration, newNode);
		handler.parseXML(xmlCreator.getXmlContent());
		
		assertEqualsMaps(newNode.getProperties(), oldNode.getProperties());
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testParseNodeToXmlDifferentTags() throws SAXException, IOException, ParserConfigurationException {
		serializeDeserializeAndCompareXMLContent(DIFFERENT_TAGS_XML_FILE);
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testParseNodeToXmlUnknownTags() throws SAXException, IOException, ParserConfigurationException {
		serializeDeserializeAndCompareXMLContent(UNKNOWN_TAGS_XML_FILE);
	}
}