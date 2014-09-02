package org.flowerplatform.tests.freeplane;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.controller.xml_parser.XmlNodePropertiesParser;
import org.flowerplatform.tests.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Catalin Burcea
 *
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
		parseXML(node, xmlContent);
		
		assertAndRemoveProperty(node, "node.TEXT", "test");
		assertAndRemoveProperty(node, "node.FOLDED", "false");
		assertAndRemoveProperty(node, "node.CREATED", "1407493284393");
		assertAndRemoveProperty(node, "node.ID", "ID_1229967238");
		assertAndRemoveProperty(node, "node.MODIFIED", "1407493284393");
		assertAndRemoveProperty(node, "font.NAME", "Segoe UI");
		assertAndRemoveProperty(node, "font.BOLD", "true");
		assertAndRemoveProperty(node, "font.ITALIC", "true");
		assertAndRemoveProperty(node, "font.SIZE", "18");
		assertAndRemoveProperty(node, "cloud.COLOR", "#0033ff");
		assertAndRemoveProperty(node, "cloud.SHAPE", "RECT");
		assertAndRemoveProperty(node, "icons", Arrays.asList("yes", "checked", "password"));
		assertAndRemoveProperty(node, "richcontent.DETAILS.HIDDEN", "true");
		assertAndRemoveProperty(node, "richcontent.DETAILS", "\n		<html><head></head><body><p>Details .... </p></body></html>\n	");
		assertAndRemoveProperty(node, "hook.MapStyle", "\n		<map_styles><stylenode LOCALIZED_TEXT='styles.root_node'></stylenode></map_styles>\n	");
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
	
	// TODO CS: de testat: cu attributes, cu unknown. As fi vrut ca chestiile "root" sa nu mai se numeasca "node.TEXT"; ci doar "TEXT".
	// apoi de cautat alte cazuri. E.g. atunci cand avem un nod/noduri imbricate: ar trebui sa intre pe "unknown"; insa dupa ce vedem ca merge, ar trebui sa facem un procesor special pentru
	// "node", si daca intra, sa dea exceptie. 
	// Si apoi de luat MM, de pus chestii diverse prin el, si de gasit si alte cazuri
}