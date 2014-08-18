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
package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
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
import static org.flowerplatform.tests.codesync.CodeSyncTestSuite.testConflicts;
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
import org.flowerplatform.codesync.Match.MatchType;
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
	 * @author Valentina Bojan
	 */
	@BeforeClass
	public static void beforeClassMethod() {
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId",  RESOURCE_NODE_ID,  new ServiceContext<ResourceService>());
	}
	
	/**
	 * @author Valentina Bojan
	 */
	@Test
	public void testMatchWhenSync() throws IOException {
		CodeSyncPlugin.getInstance().addSrcDir(INITIAL);
		String fullyQualifiedName = PROJECT + "/" + INITIAL /*+ "/" + SOURCE_FILE*/;		
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { INITIAL }).getNodeUri();

		Match match = CODE_SYNC_SERVICE.synchronize(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncJavaConstants.JAVA, true);
		
		assertEquals(1, match.getSubMatches().size());
		
		Pair[] typeList = {
				new Pair(_3MATCH, 0),			// src
					new Pair(_3MATCH, 1),				// Test.java
						new Pair(_3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(_3MATCH, 3),				// @Deprecated
							new Pair(_3MATCH, 3),				// public
							new Pair(_3MATCH, 3),				// ITest
						
							new Pair(_3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(_3MATCH, 4),				// public
								new Pair(_3MATCH, 4),				// @OneToMany
									new Pair(_3MATCH, 5),				// mappedBy = test
								new Pair(_3MATCH, 4),				// String st
							
							new Pair(_3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(_3MATCH, 4),				// static	
								new Pair(_3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(_3MATCH, 5),				// x+y
								new Pair(_3MATCH, 4),				// public
								
							new Pair(_3MATCH, 3),				// private int y
								new Pair(_3MATCH, 4),				// private
								
							new Pair(_3MATCH, 3), 			// private int x
								new Pair(_3MATCH, 4),				// private
								
							
							
				};
		testMatchTree(match, typeList, true);
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testMatchNoConflicts() {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS /*+ "/" + SOURCE_FILE*/;
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { INITIAL }).getNodeUri();
		
		Node root = CodeSyncPlugin.getInstance().getResource(RESOURCE_NODE_ID);
		
		// simulate model modifications
		simulateNonConflictingChanges(root, MODIFIED_NO_CONFLICTS);
		
		Match match = CODE_SYNC_SERVICE.generateMatch(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncJavaConstants.JAVA, false);
		
		Pair[] typeList = {
				new Pair(_3MATCH, 0),					// src
					new Pair(_3MATCH, 1),					// Test.java
						new Pair(_3MATCH, 2),					// @Deprecated public class Test
						
//							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// @Deprecated (removed from model)
//							new Pair(_3MATCH, 3),						// public
//							new Pair(MatchType._1MATCH_LEFT, 3),				// @Deprecated(test) (added to model)
//								new Pair(MatchType._1MATCH_LEFT, 4),				// test
						
							new Pair(_3MATCH, 3),						// @Deprecated
							new Pair(_3MATCH, 3),						// public
							new Pair(MatchType._2MATCH_ANCESTOR_LEFT, 3),		// ITest
							new Pair(MatchType._1MATCH_LEFT, 3),				// IFromModel
							new Pair(MatchType._1MATCH_RIGHT, 3),				// IFromSource
						
							new Pair(_3MATCH, 3),					// @OneToMany(mappedBy="test") public int test(String st) {
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// removed public from model
								new Pair(_3MATCH, 4),					// @OneToMany
									new Pair(_3MATCH, 5),					// mappedBy
									new Pair(MatchType._1MATCH_LEFT, 5),				// orphanRemoval
								new Pair(MatchType._1MATCH_LEFT, 4),				// added private to model
								new Pair(MatchType._1MATCH_RIGHT, 4),				// added static to source
								new Pair(_3MATCH, 4),					// String st
									new Pair(MatchType._1MATCH_RIGHT, 5),				// final (added to source)
								
							new Pair(_3MATCH, 3),					// @OverrideAnnotationOf(x+y) public static Test getTest() {
								new Pair(MatchType._1MATCH_ANCESTOR, 4),			// removed static from model and source
								new Pair(_3MATCH, 4),						// @OverrideAnnotationOf
									new Pair(_3MATCH, 5),						// x+y
//								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// @OverrideAnnotationOf(x+y) (removed from model)
//									new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 5),		// x+y
//								new Pair(MatchType._1MATCH_LEFT, 4),				// @overrideAnnotationOf(valu1=true, value2=false) (added to model)
//									new Pair(MatchType._1MATCH_LEFT, 5),				// value1 (added to model)
//									new Pair(MatchType._1MATCH_LEFT, 5),				// value2 (added to model)
								new Pair(_3MATCH, 4),						// public
								new Pair(MatchType._1MATCH_LEFT, 4),				// added param to model
								
							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// private int y (removed from model)
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// private
								
							new Pair(_3MATCH, 3),					// private Test x <> private int x
								new Pair(_3MATCH, 4),					// private
								
							new Pair(MatchType._1MATCH_LEFT, 3),				// public int t (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// public
							
							new Pair(MatchType._1MATCH_LEFT, 3),				// public class InternalClsFromModel
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public enum ActionType
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public Object diffAction
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// private ActionType(Object action)
									new Pair(MatchType._1MATCH_RIGHT, 5),				// Object action
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// private
								new Pair(MatchType._1MATCH_RIGHT, 4),				// ACTION_TYPE_COPY_LEFT_RIGHT(new Test())
									new Pair(MatchType._1MATCH_RIGHT, 5),				// new Test()
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// ACTION_TYPE_COPY_RIGHT_LEFT(new InternalClsFromSource());
									new Pair(MatchType._1MATCH_RIGHT, 5),				// new InternalClsFromSource()
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public class InternalClsFromSource
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public int x
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public @interface AnnotationTest
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// boolean value1() default true
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// boolean value2() default false
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// private int z (added to source)
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// private
		};
		testMatchTree(match, typeList, false);
		assertFalse("Conflicts not expected!", match.isChildrenConflict());
	}

	/**
	 * @author see class
	 */
	@Test
	public void testMatchNoConflictsAndPerformSync() throws Exception {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS_PERFORM_SYNC);		
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC /*+ "/" + SOURCE_FILE*/;
		File dir = CodeSyncTestSuite.getFile(fullyQualifiedName);
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { MODIFIED_NO_CONFLICTS_PERFORM_SYNC }).getNodeUri();
		
		Node root = CodeSyncPlugin.getInstance().getResource(RESOURCE_NODE_ID);
		
//		File cseLocation = (File) CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CodeSyncPlugin.getInstance().CSE_MAPPING_FILE_LOCATION);
//		File aceLocation = (File) CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CodeSyncPlugin.getInstance().ACE_FILE_LOCATION);

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
		Node superInterface = new Node(Utils.getUri(CodeSyncJavaConstants.SUPER_INTERFACE, Utils.getSchemeSpecificPart(root.getNodeUri()), null),
				CodeSyncJavaConstants.SUPER_INTERFACE);
		nodeService.addChild(cls, superInterface, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(superInterface, CoreConstants.NAME, "IFromModel", new ServiceContext<NodeService>(nodeService));
//		Node deprecated = getChild(cls, new String[] {"Deprecated"});
//		Node val = new Node(Utils.getUri();
//		val.setType(MEMBER_VALUE_PAIR);
//		nodeService.addChild(deprecated, val);
//		nodeService.setProperty(val, NAME, "_");
//		nodeService.setProperty(val, ANNOTATION_VALUE_VALUE, "test");

		// add class
		Node internalCls = new Node(Utils.getUri(CodeSyncJavaConstants.CLASS, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.CLASS);
		nodeService.addChild(cls, internalCls, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(internalCls, CoreConstants.NAME, "InternalClassFromModel", new ServiceContext<NodeService>(nodeService));

		// change typed element type
		Node x = getChild(cls, new String[] {"x"});
		nodeService.setProperty(x, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
		// change modifiers + annotations
		Node test = getChild(cls, new String[] {"test(String)"});
		Node privateModif = new Node(Utils.getUri(CodeSyncJavaConstants.MODIFIER, Utils.getSchemeSpecificPart(root.getNodeUri()), null), CodeSyncJavaConstants.MODIFIER);
		nodeService.addChild(test, privateModif, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(privateModif, CoreConstants.NAME, "private", new ServiceContext<NodeService>(nodeService));
		Node publicModif = getChild(test, new String[] {"public"});
		nodeService.setProperty(publicModif, REMOVED, true, new ServiceContext<NodeService>(nodeService));
		Node a = getChild(test, new String[] {"OneToMany"});
		Node mappedBy = getChild(a, new String[] {"mappedBy"});
		nodeService.setProperty(mappedBy, ANNOTATION_VALUE_VALUE, "\"modified_by_model\"", new ServiceContext<NodeService>(nodeService));
		Node orphanRemoval = new Node(Utils.getUri(CodeSyncJavaConstants.MEMBER_VALUE_PAIR, Utils.getSchemeSpecificPart(root.getNodeUri()), null),
				CodeSyncJavaConstants.MEMBER_VALUE_PAIR);
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
		nodeService.setProperty(staticModif, REMOVED, true, new ServiceContext<NodeService>(nodeService));
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
		nodeService.setProperty(y, REMOVED, true, new ServiceContext<NodeService>(nodeService));
	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testMatchConflicts() {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_CONFLICTS);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_CONFLICTS /*+ "/" + SOURCE_FILE*/;
		Node node = CorePlugin.getInstance().getResourceService().getNode(RESOURCE_NODE_ID);
		String nodeUri = getChild(node, new String[] { MODIFIED_CONFLICTS }).getNodeUri();
		
		Node root = CodeSyncPlugin.getInstance().getResource(RESOURCE_NODE_ID);		
		
		// simulate model modifications
		
		// change super class
		Node cls = getChild(root, new String[] {MODIFIED_CONFLICTS, SOURCE_FILE, "Test"});
		nodeService.setProperty(cls, SUPER_CLASS, "SuperClassFromModel", new ServiceContext<NodeService>(nodeService));
		
//		// change typed element type
		Node x = getChild(cls, new String[] {"x"});
		nodeService.setProperty(x, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
//		// change typed element type
		Node y = getChild(cls, new String[] {"y"});
		nodeService.setProperty(y, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
		// change modifiers + annotations
		Node test = getChild(cls, new String[] {"test(String)"});
		Node a = getChild(test, new String[] {"OneToMany"});
		Node mappedBy = getChild(a, new String[] {"mappedBy"});
		nodeService.setProperty(mappedBy, ANNOTATION_VALUE_VALUE, "\"modified_by_model\"", new ServiceContext<NodeService>(nodeService));

		Match match = CODE_SYNC_SERVICE.generateMatch(nodeUri, CodeSyncTestSuite.getFile(fullyQualifiedName), CodeSyncJavaConstants.JAVA, true);
		
		Pair[] typeList = {
				new Pair(_3MATCH, 0),				// src
					new Pair(_3MATCH, 1),				// Test.java
						new Pair(_3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(_3MATCH, 3),				// @Deprecated
							new Pair(_3MATCH, 3),				// public
							new Pair(_3MATCH, 3),				// ITest
						
							new Pair(_3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(_3MATCH, 4),				// public
								new Pair(_3MATCH, 4),				// @OneToMany
									new Pair(_3MATCH, 5),				// mappedBy = test
								new Pair(_3MATCH, 4),				// String st
							
							new Pair(_3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(_3MATCH, 4),				// public
								new Pair(_3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(_3MATCH, 5),				// x+y
								new Pair(_3MATCH, 4),				// static
								
							new Pair(_3MATCH, 3),				// private int y
								new Pair(_3MATCH, 4),				// private
								
							new Pair(_3MATCH, 3), 				// private int x
								new Pair(_3MATCH, 4),				// private
				};
		
		org.flowerplatform.util.Pair<Boolean, Boolean> conflictChildrenConflict = new org.flowerplatform.util.Pair<Boolean, Boolean>(true, true);
		org.flowerplatform.util.Pair<Boolean, Boolean> conflictNoChildrenConflict = new org.flowerplatform.util.Pair<Boolean, Boolean>(true, false);
		org.flowerplatform.util.Pair<Boolean, Boolean> noConflictChildrenConflict = new org.flowerplatform.util.Pair<Boolean, Boolean>(false, true);
		org.flowerplatform.util.Pair<Boolean, Boolean> noConflictNoChildrenConflict = new org.flowerplatform.util.Pair<Boolean, Boolean>(false, false);
		
		org.flowerplatform.util.Pair<?, ?>[] conflicts = {
				noConflictChildrenConflict,
					noConflictChildrenConflict,
						conflictChildrenConflict, 				// superClass changed on model and source
								
							noConflictNoChildrenConflict,
							noConflictNoChildrenConflict,
							noConflictNoChildrenConflict,
							
							noConflictChildrenConflict,
								noConflictNoChildrenConflict,
								noConflictChildrenConflict,
									conflictNoChildrenConflict,		// annotation value changed on model and source
								noConflictNoChildrenConflict,
								
							noConflictNoChildrenConflict,
								noConflictNoChildrenConflict,
								noConflictNoChildrenConflict,
									noConflictNoChildrenConflict,
									noConflictNoChildrenConflict,
							
							conflictNoChildrenConflict,				// type changed on model and source
								noConflictNoChildrenConflict,
							
							noConflictNoChildrenConflict,			// type changed on model and source, but the type is the same
								noConflictNoChildrenConflict,
		};
		assertTrue("Conflicts expected!", match.isChildrenConflict());
		testMatchTree(match, typeList, false);
		testConflicts(match, conflicts);
	}
	
}
