/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.Match.MatchType._1MATCH_ANCESTOR;
import static org.flowerplatform.codesync.Match.MatchType._1MATCH_LEFT;
import static org.flowerplatform.codesync.Match.MatchType._1MATCH_RIGHT;
import static org.flowerplatform.codesync.Match.MatchType._2MATCH_ANCESTOR_LEFT;
import static org.flowerplatform.codesync.Match.MatchType._2MATCH_ANCESTOR_RIGHT;
import static org.flowerplatform.codesync.Match.MatchType._3MATCH;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.ANNOTATION_VALUE_VALUE;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.SUPER_CLASS;
import static org.flowerplatform.codesync.code.java.CodeSyncJavaConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.nodeService;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.DIR;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.PROJECT;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.CODE_SYNC_SERVICE;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.getChild;
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.testMatchTree;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.code.java.CodeSyncJavaConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mariana
 */
public class CodeSyncJavaTest {

	public static final String INITIAL = "initial";
	public static final String CACHE_DELETED = "cache_deleted";
	public static final String MODIFIED_NO_CONFLICTS = "modified_no_conflicts";
	public static final String MODIFIED_CONFLICTS = "modified_conflicts";
	public static final String MODIFIED_NO_CONFLICTS_PERFORM_SYNC = "modified_no_conflicts_perform_sync";
	
	public static final String SOURCE_FILE = "Test.java";
	
	private static final String RESOURCE_NODE_ID = new Node(Utils.getUri(FREEPLANE_PERSISTENCE_RESOURCE_KEY, PROJECT + "|.codesync"), CodeSyncConstants.CODESYNC).getNodeUri();
	
	/**
	 *@author Valentina Bojan
	 */
	@BeforeClass
	public static void beforeClassMethod() {
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId",  RESOURCE_NODE_ID,  new ServiceContext<ResourceService>());
	}
	
	/**
	 *@author Valentina Bojan
	 */
	@Test
	public void testMatchWhenSync() throws IOException {
		String fullyQualifiedName = PROJECT + "/" + INITIAL;		
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { INITIAL }).getNodeUri();

		Match match = CODE_SYNC_SERVICE.synchronize(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncJavaConstants.JAVA, true);
		
		TestMatch expected = new TestMatch(INITIAL, _3MATCH);
		TestMatch cls = expected.addChild("Test.java", _3MATCH).addChild("Test", _3MATCH);
		
		cls.addChild("Deprecated", _3MATCH).addSibling("public", _3MATCH).addSibling("ITest", _3MATCH);
		
		cls.addChild("test(String)", _3MATCH)
				.addChild("st", _3MATCH)
				.addSibling("public", _3MATCH)
				.addSibling("OneToMany", _3MATCH)
					.addChild("mappedBy", _3MATCH);
				
		cls.addChild("getTest()", _3MATCH)
				.addChild("public", _3MATCH)
				.addSibling("static", _3MATCH)
				.addSibling("OverrideAnnotationOf", _3MATCH)
					.addChild("_", _3MATCH);

		cls.addChild("x", _3MATCH).addChild("private", _3MATCH);
		cls.addChild("y", _3MATCH).addChild("private", _3MATCH);
		
		testMatchTree(match, expected, true, false);
	}
	
	/**
	 *@author see class
	 */
	@Test
	public void testMatchNoConflicts() {
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS;
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { MODIFIED_NO_CONFLICTS }).getNodeUri();
		
		Node root = CodeSyncPlugin.getInstance().getResource(RESOURCE_NODE_ID);
		
		// simulate model modifications
		simulateNonConflictingChanges(root, MODIFIED_NO_CONFLICTS);
		
		Match match = CODE_SYNC_SERVICE.generateMatch(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncJavaConstants.JAVA, false);
		
		TestMatch expected = new TestMatch(MODIFIED_NO_CONFLICTS, _3MATCH);
		TestMatch cls = expected.addChild("Test.java", _3MATCH).addChild("Test", _3MATCH);
		
		cls.addChild("Deprecated", _3MATCH)
			.addSibling("public", _3MATCH)
			.addSibling("ITest", _2MATCH_ANCESTOR_LEFT)
			.addSibling("IFromModel", _1MATCH_LEFT)
			.addSibling("IFromSource", _1MATCH_RIGHT);
		
		TestMatch test = cls.addChild("test(String)", _3MATCH);
		test.addChild("st", _3MATCH).addChild("final", _1MATCH_RIGHT);
		test.addChild("public", _2MATCH_ANCESTOR_RIGHT)
			.addSibling("private", _1MATCH_LEFT)
			.addSibling("static", _1MATCH_RIGHT)
			.addSibling("OneToMany", _3MATCH)
				.addChild("mappedBy", _3MATCH)
				.addSibling("orphanRemoval", _1MATCH_LEFT);
		
		TestMatch getTest = cls.addChild("getTest()", _3MATCH);
		getTest.addChild("a", _1MATCH_LEFT);
		getTest.addChild("public", _3MATCH)
			.addSibling("static", _1MATCH_ANCESTOR)
			.addSibling("OverrideAnnotationOf", _3MATCH)
				.addChild("_", _3MATCH);

		cls.addChild("x", _3MATCH).addChild("private", _3MATCH);
		cls.addChild("y", _2MATCH_ANCESTOR_RIGHT).addChild("private", _2MATCH_ANCESTOR_RIGHT);
		cls.addChild("t", _1MATCH_LEFT).addChild("public", _1MATCH_LEFT);
		cls.addChild("z", _1MATCH_RIGHT).addChild("private", _1MATCH_RIGHT);
		
		TestMatch actionType = cls.addChild("ActionType", _1MATCH_RIGHT);
		actionType.addChild("public", _1MATCH_RIGHT);
		actionType.addChild("ActionType(Object)", _1MATCH_RIGHT).addChild("private", _1MATCH_RIGHT).addSibling("action", _1MATCH_RIGHT);
		actionType.addChild("diffAction", _1MATCH_RIGHT).addChild("public", _1MATCH_RIGHT);
		actionType.addChild("ACTION_TYPE_COPY_LEFT_RIGHT", _1MATCH_RIGHT).addChild("new Test()", _1MATCH_RIGHT);
		actionType.addChild("ACTION_TYPE_COPY_RIGHT_LEFT", _1MATCH_RIGHT).addChild("new InternalClsFromSource()", _1MATCH_RIGHT);
		
		cls.addChild("InternalClsFromSource", _1MATCH_RIGHT)
			.addChild("public", _1MATCH_RIGHT)
			.addSibling("x", _1MATCH_RIGHT)
				.addChild("private", _1MATCH_RIGHT);
		
		cls.addChild("AnnotationTest", _1MATCH_RIGHT)
			.addChild("public", _1MATCH_RIGHT)
			.addSibling("value1", _1MATCH_RIGHT)
			.addSibling("value2", _1MATCH_RIGHT);
		
		testMatchTree(match, expected, false, false);
		assertFalse("Conflicts not expected!", match.isChildrenConflict());
	}

	/**
	 *@author see class
	 */
	@Test
	public void testMatchNoConflictsAndPerformSync() throws Exception {
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC /*+ "/" + SOURCE_FILE*/;
		File dir = CodeSyncTestSuite.getFile(fullyQualifiedName);
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { MODIFIED_NO_CONFLICTS_PERFORM_SYNC }).getNodeUri();
		
		Node root = CodeSyncPlugin.getInstance().getResource(RESOURCE_NODE_ID);

		// simulate model modifications
		simulateNonConflictingChanges(root, MODIFIED_NO_CONFLICTS_PERFORM_SYNC);

		CODE_SYNC_SERVICE.synchronize(nodeUri, dir, CodeSyncJavaConstants.JAVA, true);

		String expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/" + SOURCE_FILE);
		String actual = FileUtils.readFileToString(new File(dir, SOURCE_FILE));
		assertEquals("Source not in sync", expected, actual);
		
//		expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation");
//		actual = FileUtils.readFileToString(cseLocation);
//		assertEquals("CSE not in sync", expected, actual);
//		
//		expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/ACE.notation");
//		actual = FileUtils.readFileToString(aceLocation);
//		assertEquals("ACE not in sync", expected, actual);
	}

	private void simulateNonConflictingChanges(Node root, String srcDir) {
		// change superCls, superInterfaces
		Node cls = getChild(root, new String[] {srcDir, SOURCE_FILE, "Test"});
		nodeService.setProperty(cls, SUPER_CLASS, "SuperClassFromModel", new ServiceContext<NodeService>(nodeService));
		Node superInterface = new Node(Utils.getUri(CodeSyncJavaConstants.
				SUPER_INTERFACE, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.SUPER_INTERFACE);
		nodeService.addChild(cls, superInterface, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(superInterface, CoreConstants.NAME, "IFromModel", new ServiceContext<NodeService>(nodeService));
//		Node deprecated = getChild(cls, new String[] {"Deprecated"});
//		Node val = new Node(Utils.getUri();
//		val.setType(MEMBER_VALUE_PAIR);
//		nodeService.addChild(deprecated, val);
//		nodeService.setProperty(val, NAME, "_");
//		nodeService.setProperty(val, ANNOTATION_VALUE_VALUE, "test");

		// change typed element type
		Node x = getChild(cls, new String[] {"x"});
		nodeService.setProperty(x, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
		// change modifiers + annotations
		Node test = getChild(cls, new String[] {"test(String)"});
		Node privateModif = new Node(Utils.getUri(CodeSyncJavaConstants.MODIFIER, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.MODIFIER);
		nodeService.addChild(test, privateModif, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(privateModif, CoreConstants.NAME, "private", new ServiceContext<NodeService>(nodeService));
		Node publicModif = getChild(test, new String[] {"public"});
		nodeService.removeChild(test, publicModif, new ServiceContext<NodeService>(nodeService));
		Node a = getChild(test, new String[] {"OneToMany"});
		Node mappedBy = getChild(a, new String[] {"mappedBy"});
		nodeService.setProperty(mappedBy, ANNOTATION_VALUE_VALUE, "\"modified_by_model\"", new ServiceContext<NodeService>(nodeService));
		Node orphanRemoval = new Node(Utils.getUri(CodeSyncJavaConstants
				.MEMBER_VALUE_PAIR, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.MEMBER_VALUE_PAIR);
		nodeService.addChild(a, orphanRemoval, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(orphanRemoval, CoreConstants.NAME, "orphanRemoval", new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(orphanRemoval, ANNOTATION_VALUE_VALUE, "true", new ServiceContext<NodeService>(nodeService));
		
		// change parameters
		Node getTest = getChild(cls, new String[] {"getTest()"});
		Node param = new Node(Utils.getUri(CodeSyncJavaConstants.PARAMETER, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.PARAMETER);
		nodeService.addChild(getTest, param, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(param, CoreConstants.NAME, "a", new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(param, TYPED_ELEMENT_TYPE, "int", new ServiceContext<NodeService>(nodeService));
		Node staticModif = getChild(getTest, new String[] {"static"});
		nodeService.removeChild(getTest, staticModif, new ServiceContext<NodeService>(nodeService));
//		nodeService.setProperty(getTest, DOCUMENTATION, "modified from model\n@author test");
		
//				featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//				CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//				featureChange.setOldValue(op.getModifiers());
//				modifiers = new BasicEList<ExtendedModifier>();
//				modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//				modifier.setType(1); // public
//				modifiers.add(modifier);
//				a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//				a.setName("OverrideAnnotationOf");
//				value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//				value.setName("value1");
//				value.setValue("true");
//				CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//				value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//				value.setName("value2");
//				value.setValue("false");
//				CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//				modifiers.add(a);
//				featureChange.setNewValue(modifiers);
////				CodeSyncCodePlugin.getInstance().getUtils().setModifiers(op, modifiers);

		// add element
		Node t = new Node(Utils.getUri(CodeSyncJavaConstants.ATTRIBUTE, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.ATTRIBUTE);
		nodeService.addChild(cls, t, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(t, CoreConstants.NAME, "t", new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(t, TYPED_ELEMENT_TYPE, "int", new ServiceContext<NodeService>(nodeService));
//		nodeService.setProperty(t, DOCUMENTATION, "doc from model @author test");
		publicModif = new Node(Utils.getUri(CodeSyncJavaConstants.MODIFIER, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.MODIFIER);
		nodeService.addChild(t, publicModif, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(publicModif, CoreConstants.NAME, "public", new ServiceContext<NodeService>(nodeService));
		
		// remove element
		Node y = getChild(cls, new String[] {"y"});
		nodeService.removeChild(cls, y, new ServiceContext<NodeService>(nodeService));
	}
	
	/**
	 *@author see class
	 */
	@Test
	public void testMatchConflicts() {
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_CONFLICTS /*+ "/" + SOURCE_FILE*/;
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { MODIFIED_CONFLICTS }).getNodeUri();
		
		Node root = CodeSyncPlugin.getInstance().getResource(RESOURCE_NODE_ID);		
		
		// simulate model modifications
		
		// change super class
		Node test = getChild(root, new String[] {MODIFIED_CONFLICTS, SOURCE_FILE, "Test"});
		nodeService.setProperty(test, SUPER_CLASS, "SuperClassFromModel", new ServiceContext<NodeService>(nodeService));
		
		// change typed element type
		Node x = getChild(test, new String[] {"x"});
		nodeService.setProperty(x, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
		// change typed element type
		Node y = getChild(test, new String[] {"y"});
		nodeService.setProperty(y, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
		// change modifiers + annotations
		Node testFunc = getChild(test, new String[] {"test(String)"});
		Node a = getChild(testFunc, new String[] {"OneToMany"});
		Node mappedBy = getChild(a, new String[] {"mappedBy"});
		nodeService.setProperty(mappedBy, ANNOTATION_VALUE_VALUE, "\"modified_by_model\"", new ServiceContext<NodeService>(nodeService));

		Match match = CODE_SYNC_SERVICE.generateMatch(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncJavaConstants.JAVA, true);
		
		TestMatch expected = new TestMatch(INITIAL, _3MATCH, false, true);
		TestMatch cls = expected.addChild("Test.java", _3MATCH, false, true).addChild("Test", _3MATCH, true, true);
		
		cls.addChild("Deprecated", _3MATCH).addSibling("public", _3MATCH).addSibling("ITest", _3MATCH);
		
		cls.addChild("test(String)", _3MATCH, false, true)
				.addChild("st", _3MATCH)
				.addSibling("public", _3MATCH)
				.addSibling("OneToMany", _3MATCH, false, true)
					.addChild("mappedBy", _3MATCH, true, false);
				
		cls.addChild("getTest()", _3MATCH)
				.addChild("public", _3MATCH)
				.addSibling("static", _3MATCH)
				.addSibling("OverrideAnnotationOf", _3MATCH)
					.addChild("_", _3MATCH);

		cls.addChild("x", _3MATCH).addChild("private", _3MATCH);
		cls.addChild("y", _3MATCH, true, false).addChild("private", _3MATCH);
		
		assertTrue("Conflicts expected!", match.isChildrenConflict());
		testMatchTree(match, expected, false, true);
	}
	
}
