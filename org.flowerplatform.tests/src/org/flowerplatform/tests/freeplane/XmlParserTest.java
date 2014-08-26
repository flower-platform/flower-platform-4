package org.flowerplatform.tests.freeplane;

import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.tests.EclipseIndependentTestSuite;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.tests.codesync.CodeSyncTestSuite;
import org.flowerplatform.tests.core.NodeRegistryScriptTest;
import org.junit.Test;

public class XmlParserTest {

	public static final String DIR = TestUtil.getResourcesDir(XmlParserTest.class);
	
	@Test
	public void testParseXmlToNode() {
		EclipseIndependentTestSuite.deleteFiles("user");
		EclipseIndependentTestSuite.copyFiles(DIR + TestUtil.INITIAL_TO_BE_COPIED, "user");
		
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		String nodeUri = CoreUtils.createNodeUriWithRepo("fpm1", "user/freeplane", "test2.mm");	
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", nodeUri, new ServiceContext<ResourceService>());
		Node root = CorePlugin.getInstance().getResourceService().getNode(nodeUri, new ServiceContext<ResourceService>().add(POPULATE_WITH_PROPERTIES, true));

		ArrayList<String> icons = new ArrayList<String>();
		icons.add("yes");
		icons.add("checked");
		icons.add("password");
		//assertEquals("message", "test", (String) root.getProperties().get("node.TEXT"));
		assertEquals("message", "false", (String) root.getProperties().remove("node.FOLDED"));
		assertEquals("message", "1407493284393", (String) root.getProperties().remove("node.CREATED"));
		assertEquals("message", "ID_1229967238", (String) root.getProperties().remove("node.ID"));
		assertEquals("message", "1407493284393", (String) root.getProperties().remove("node.MODIFIED"));
		assertEquals("message", "Segoe UI", (String) root.getProperties().remove("font.NAME"));
		assertEquals("message", "true", (String) root.getProperties().remove("font.BOLD"));
		assertEquals("message", "true", (String) root.getProperties().remove("font.ITALIC"));
		assertEquals("message", "18", (String) root.getProperties().remove("font.SIZE"));
		assertEquals("message", "#0033ff", (String) root.getProperties().remove("cloud.COLOR"));
		assertEquals("message", "RECT", (String) root.getProperties().remove("cloud.SHAPE"));
		assertEquals("message", icons, (ArrayList<String>) root.getProperties().remove("icons"));
		assertEquals("message", "true", (String) root.getProperties().remove("richcontent.DETAILS.HIDDEN"));
		assertEquals("message", "<html><head></head><body><p>Details .... </p></body></html>", (String) root.getProperties().remove("richcontent.DETAILS"));
		//assertEquals("message", "<map_styles><stylenode LOCALIZED_TEXT='styles.root_node'></stylenode></map_styles>", (String) root.getProperties().get("hook.MapStyle"));
		assertEquals("message", "<attribute NAME='a1' VALUE='v1'/><attribute NAME='a2' VALUE='v2'/><attribute NAME='a3' VALUE='v3'/>", (String) root.getProperties().remove("attribute"));
	}

}
