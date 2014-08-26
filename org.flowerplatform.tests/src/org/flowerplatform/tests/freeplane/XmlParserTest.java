package org.flowerplatform.tests.freeplane;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.controller.xml_parser.XmlNodePropertiesParser;
import org.flowerplatform.tests.TestUtil;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Catalin Burcea
 *
 */
public class XmlParserTest {

	public static final String DIR = TestUtil.getResourcesDir(XmlParserTest.class);

	public static final String SIMPLE_CONTENT_XML_FILE = "simpleContentFile.xml";

	public static final String FULL_CONTENT_XML_FILE = "fullContentFile.xml";

	public static final String DUPLICATE_CONTENT_XML_FILE = "dulpicateContentFile.xml";

	@Test
	public void testParseSimpleXmlToNode() throws SAXException, IOException, ParserConfigurationException {
		String xmlContent = TestUtil.readFile(DIR + "/" + SIMPLE_CONTENT_XML_FILE);
		Node node = new Node(null, "fpm1");
		parseXML(node, xmlContent);
		ArrayList<String> icons = new ArrayList<String>();
		icons.add("yes");
		icons.add("checked");
		icons.add("password");
		assertEquals("Wrong actual value of 'node.TEXT'", "test", (String) node.getProperties().remove("node.TEXT"));
		assertEquals("Wrong actual value of 'node.FOLDED'", "false", (String) node.getProperties().remove("node.FOLDED"));
		assertEquals("Wrong actual value of 'node.CREATED'", "1407493284393", (String) node.getProperties().remove("node.CREATED"));
		assertEquals("Wrong actual value of 'node.ID'", "ID_1229967238", (String) node.getProperties().remove("node.ID"));
		assertEquals("Wrong actual value of 'node.MODIFIED'", "1407493284393", (String) node.getProperties().remove("node.MODIFIED"));
		assertEquals("Wrong actual value of 'font.NAME'", "Segoe UI", (String) node.getProperties().remove("font.NAME"));
		assertEquals("Wrong actual value of 'font.BOLD'", "true", (String) node.getProperties().remove("font.BOLD"));
		assertEquals("Wrong actual value of 'font.ITALIC'", "true", (String) node.getProperties().remove("font.ITALIC"));
		assertEquals("Wrong actual value of 'font.SIZE'", "18", (String) node.getProperties().remove("font.SIZE"));
		assertEquals("Wrong actual value of 'cloud.COLOR'", "#0033ff", (String) node.getProperties().remove("cloud.COLOR"));
		assertEquals("Wrong actual value of 'cloud.SHAPE'", "RECT", (String) node.getProperties().remove("cloud.SHAPE"));
		assertEquals("Wrong actual value of 'icons'", icons, (ArrayList<String>) node.getProperties().remove("icons"));
		assertEquals("Node still has properties", 0, node.getProperties().size());
	}

	@Test
	public void testParseFullContentXmlToNode() throws SAXException, IOException, ParserConfigurationException {
		String xmlContent = TestUtil.readFile(DIR + "/" + FULL_CONTENT_XML_FILE);
		Node node = new Node(null, "fpm1");
		parseXML(node, xmlContent);
		ArrayList<String> icons = new ArrayList<String>();
		icons.add("yes");
		icons.add("checked");
		icons.add("password");
		assertEquals("Wrong actual value of 'node.TEXT'", "test", (String) node.getProperties().remove("node.TEXT"));
		assertEquals("Wrong actual value of 'node.FOLDED'", "false", (String) node.getProperties().remove("node.FOLDED"));
		assertEquals("Wrong actual value of 'node.CREATED'", "1407493284393", (String) node.getProperties().remove("node.CREATED"));
		assertEquals("Wrong actual value of 'node.ID'", "ID_1229967238", (String) node.getProperties().remove("node.ID"));
		assertEquals("Wrong actual value of 'node.MODIFIED'", "1407493284393", (String) node.getProperties().remove("node.MODIFIED"));
		assertEquals("Wrong actual value of 'font.NAME'", "Segoe UI", (String) node.getProperties().remove("font.NAME"));
		assertEquals("Wrong actual value of 'font.BOLD'", "true", (String) node.getProperties().remove("font.BOLD"));
		assertEquals("Wrong actual value of 'font.ITALIC'", "true", (String) node.getProperties().remove("font.ITALIC"));
		assertEquals("Wrong actual value of 'font.SIZE'", "18", (String) node.getProperties().remove("font.SIZE"));
		assertEquals("Wrong actual value of 'cloud.COLOR'", "#0033ff", (String) node.getProperties().remove("cloud.COLOR"));
		assertEquals("Wrong actual value of 'cloud.SHAPE'", "RECT", (String) node.getProperties().remove("cloud.SHAPE"));
		assertEquals("Wrong actual value of 'icons'", icons, (ArrayList<String>) node.getProperties().remove("icons"));
		assertEquals("Wrong actual value of 'richcontent.DETAILS.HIDDEN'", "true", (String) node.getProperties().remove("richcontent.DETAILS.HIDDEN"));
		assertEquals("Wrong actual value of 'richcontent.DETAILS'", "\n		<html><head></head><body><p>Details .... </p></body></html>\n	", (String) node.getProperties().remove("richcontent.DETAILS"));
		assertEquals("Wrong actual value of 'hook.MapStyle'", "\n		<map_styles><stylenode LOCALIZED_TEXT='styles.root_node'></stylenode></map_styles>\n	", (String) node.getProperties().remove("hook.MapStyle"));
		assertEquals("Node still has properties", 0, node.getProperties().size());
	}

	public void testParseDuplicateXmlToNode() throws SAXException, IOException, ParserConfigurationException {
		String xmlContent = TestUtil.readFile(DIR + "/" + DUPLICATE_CONTENT_XML_FILE);
		Node node = new Node(null, "fpm1");
		parseXML(node, xmlContent);
		ArrayList<String> icons = new ArrayList<String>();
		icons.add("yes");
		icons.add("checked");
		icons.add("password");
		assertEquals("Wrong actual value of 'node.TEXT'", "test", (String) node.getProperties().remove("node.TEXT"));
		assertEquals("Wrong actual value of 'node.FOLDED'", "false", (String) node.getProperties().remove("node.FOLDED"));
		assertEquals("Wrong actual value of 'node.CREATED'", "1407493284393", (String) node.getProperties().remove("node.CREATED"));
		assertEquals("Wrong actual value of 'node.ID'", "ID_1229967238", (String) node.getProperties().remove("node.ID"));
		assertEquals("Wrong actual value of 'node.MODIFIED'", "1407493284393", (String) node.getProperties().remove("node.MODIFIED"));
		assertEquals("Wrong actual value of 'font.NAME'", "Segoe UI", (String) node.getProperties().remove("font.NAME"));
		assertEquals("Wrong actual value of 'font.BOLD'", "true", (String) node.getProperties().remove("font.BOLD"));
		assertEquals("Wrong actual value of 'font.ITALIC'", "true", (String) node.getProperties().remove("font.ITALIC"));
		assertEquals("Wrong actual value of 'font.SIZE'", "18", (String) node.getProperties().remove("font.SIZE"));
		assertEquals("Wrong actual value of 'cloud.COLOR'", "#0033ff", (String) node.getProperties().remove("cloud.COLOR"));
		assertEquals("Wrong actual value of 'cloud.SHAPE'", "RECT", (String) node.getProperties().remove("cloud.SHAPE"));
		assertEquals("Wrong actual value of 'icons'", icons, (ArrayList<String>) node.getProperties().remove("icons"));
		assertEquals("Wrong actual value of 'richcontent.DETAILS.HIDDEN'", "true", (String) node.getProperties().remove("richcontent.DETAILS.HIDDEN"));
		assertEquals("Wrong actual value of 'richcontent.DETAILS'", "\n		<html><head></head><body><p>Details .... </p></body></html>\n	", (String) node.getProperties().remove("richcontent.DETAILS"));
		assertEquals("Wrong actual value of 'hook.MapStyle'", "\n		<map_styles><stylenode LOCALIZED_TEXT='styles.root_node'></stylenode></map_styles>\n	", (String) node.getProperties().remove("hook.MapStyle"));
		assertEquals("Wrong actual value of 'attribute'", "<attribute NAME='a1' VALUE='v1'/><attribute NAME='a2' VALUE='v2'/><attribute NAME='a3' VALUE='v3'/>", (String) node.getProperties().remove("attribute"));
		assertEquals("Node still has properties", 0, node.getProperties().size());
	}

	private void parseXML(Node node, String xmlContent) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		XmlNodePropertiesParser handler = new XmlNodePropertiesParser(node);
		InputSource inputSource = new InputSource(new StringReader(xmlContent));
		saxParser.parse(inputSource, handler);
		if (handler.convertAllAttributes_tagProcessorDinamicallyAdded) {
			saxParser.reset();
			inputSource = new InputSource(new StringReader(xmlContent));
			saxParser.parse(inputSource, handler);
		}
	}
}