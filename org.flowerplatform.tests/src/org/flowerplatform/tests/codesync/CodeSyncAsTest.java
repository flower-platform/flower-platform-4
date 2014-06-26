package org.flowerplatform.tests.codesync;

import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.PROJECT;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.codeSyncService;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.CodeSyncAsPlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncAsTest {

	public static final String INITIAL_AS = "initial_as";
	
	private static final String resourceNodeId = new Node(Utils.getUri(FREEPLANE_PERSISTENCE_RESOURCE_KEY, PROJECT + "|.codesync"), CodeSyncConstants.CODESYNC).getNodeUri();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		startPlugin(new CodeSyncAsPlugin());
	}
	
	@Test
	public void test() {
		CodeSyncPlugin.getInstance().addSrcDir(INITIAL_AS);
		String fullyQualifiedName = PROJECT + "/" + INITIAL_AS;
		
		Match match = codeSyncService.synchronize(resourceNodeId, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncAsConstants.TECHNOLOGY, true);
		
		System.out.println(match);
	}
	
}
