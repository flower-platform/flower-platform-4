/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
import static org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants.ANNOTATION_VALUE_VALUE;
import static org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants.SUPER_CLASS;
import static org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants.TYPED_ELEMENT_TYPE;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.nodeService;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.Match.MatchType;
import org.flowerplatform.codesync.code.CodeSyncCodePlugin;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaConstants;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaPlugin;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.tests.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mariana
 */
public class CodeSyncTest {

	public static final String PROJECT = "codesync";
	public static final String INITIAL = "initial";
	public static final String CACHE_DELETED = "cache_deleted";
	public static final String MODIFIED_NO_CONFLICTS = "modified_no_conflicts";
	public static final String MODIFIED_CONFLICTS = "modified_conflicts";
	public static final String MODIFIED_NO_CONFLICTS_PERFORM_SYNC = "modified_no_conflicts_perform_sync";
	
	public static final String DIR = TestUtil.getResourcesDir(CodeSyncTest.class);
	
	public static final String SOURCE_FILE = "Test.java";
	public static final String MODEL_FILE = "CSE.notation";
	
	private CodeSyncOperationsService codeSyncService = new CodeSyncOperationsService();
	
	private static final String resourceNodeId = new Node(CoreConstants.CODE_TYPE, CoreConstants.SELF_RESOURCE, "workspace/" + PROJECT + "/FAP-FlowerPlatform4.mm", null).getFullNodeId();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtil.copyFiles(DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);

		startPlugin(new CodeSyncCodePlugin());
		startPlugin(new CodeSyncCodeJavaPlugin());
		
		CorePlugin.getInstance().getResourceService().sessionSubscribedToResource(resourceNodeId, "", new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
	}
	
	@Test
	public void testMatchWhenSync() throws IOException {
		CodeSyncPlugin.getInstance().addSrcDir(INITIAL);
		String fullyQualifiedName = PROJECT + "/" + INITIAL /*+ "/" + SOURCE_FILE*/;
		
		File project = getProject();
		
		Match match = codeSyncService.synchronize(resourceNodeId, new File(project, fullyQualifiedName), CodeSyncCodeJavaConstants.TECHNOLOGY, true);
		
		assertEquals(1, match.getSubMatches().size());
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),			// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3),				// ITest
						
							new Pair(MatchType._3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OneToMany
									new Pair(MatchType._3MATCH, 5),				// mappedBy = test
								new Pair(MatchType._3MATCH, 4),				// String st
							
							new Pair(MatchType._3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(MatchType._3MATCH, 4),				// static	
								new Pair(MatchType._3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(MatchType._3MATCH, 5),				// x+y
								new Pair(MatchType._3MATCH, 4),				// public
								
							new Pair(MatchType._3MATCH, 3),				// private int y
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3), 			// private int x
								new Pair(MatchType._3MATCH, 4),				// private
								
							
							
				};
		testMatchTree(match, typeList, true);
	}
	
	@Test
	public void testMatchNoConflicts() {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS /*+ "/" + SOURCE_FILE*/;

		File project = getProject();
		
		Node root = CodeSyncPlugin.getInstance().getResource(resourceNodeId);
		
		// simulate model modifications
		simulateNonConflictingChanges(root, MODIFIED_NO_CONFLICTS);
		
		Match match = codeSyncService.generateMatch(resourceNodeId, new File(project, fullyQualifiedName), CodeSyncCodeJavaConstants.TECHNOLOGY, false);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),					// src
					new Pair(MatchType._3MATCH, 1),					// Test.java
						new Pair(MatchType._3MATCH, 2),					// @Deprecated public class Test
						
//							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// @Deprecated (removed from model)
//							new Pair(MatchType._3MATCH, 3),						// public
//							new Pair(MatchType._1MATCH_LEFT, 3),				// @Deprecated(test) (added to model)
//								new Pair(MatchType._1MATCH_LEFT, 4),				// test
						
							new Pair(MatchType._3MATCH, 3),						// @Deprecated
							new Pair(MatchType._3MATCH, 3),						// public
							new Pair(MatchType._2MATCH_ANCESTOR_LEFT, 3),		// ITest
							new Pair(MatchType._1MATCH_LEFT, 3),				// IFromModel
							new Pair(MatchType._1MATCH_RIGHT, 3),				// IFromSource
						
							new Pair(MatchType._3MATCH, 3),					// @OneToMany(mappedBy="test") public int test(String st) {
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// removed public from model
								new Pair(MatchType._3MATCH, 4),					// @OneToMany
									new Pair(MatchType._3MATCH, 5),					// mappedBy
									new Pair(MatchType._1MATCH_LEFT, 5),				// orphanRemoval
								new Pair(MatchType._1MATCH_LEFT, 4),				// added private to model
								new Pair(MatchType._1MATCH_RIGHT, 4),				// added static to source
								new Pair(MatchType._3MATCH, 4),					// String st
									new Pair(MatchType._1MATCH_RIGHT, 5),				// final (added to source)
								
							new Pair(MatchType._3MATCH, 3),					// @OverrideAnnotationOf(x+y) public static Test getTest() {
								new Pair(MatchType._1MATCH_ANCESTOR, 4),			// removed static from model and source
								new Pair(MatchType._3MATCH, 4),						// @OverrideAnnotationOf
									new Pair(MatchType._3MATCH, 5),						// x+y
//								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// @OverrideAnnotationOf(x+y) (removed from model)
//									new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 5),		// x+y
//								new Pair(MatchType._1MATCH_LEFT, 4),				// @overrideAnnotationOf(valu1=true, value2=false) (added to model)
//									new Pair(MatchType._1MATCH_LEFT, 5),				// value1 (added to model)
//									new Pair(MatchType._1MATCH_LEFT, 5),				// value2 (added to model)
								new Pair(MatchType._3MATCH, 4),						// public
								new Pair(MatchType._1MATCH_LEFT, 4),				// added param to model
								
							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// private int y (removed from model)
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// private
								
							new Pair(MatchType._3MATCH, 3),					// private Test x <> private int x
								new Pair(MatchType._3MATCH, 4),					// private
								
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

	@Test
	public void testMatchNoConflictsAndPerformSync() throws Exception {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS_PERFORM_SYNC);
		
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC /*+ "/" + SOURCE_FILE*/;

		File project = getProject();
		File dir = new File(project, fullyQualifiedName);
		
		Node root = CodeSyncPlugin.getInstance().getResource(resourceNodeId);
		
//		File cseLocation = (File) CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CodeSyncPlugin.getInstance().CSE_MAPPING_FILE_LOCATION);
//		File aceLocation = (File) CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CodeSyncPlugin.getInstance().ACE_FILE_LOCATION);

		// simulate model modifications
		simulateNonConflictingChanges(root, MODIFIED_NO_CONFLICTS_PERFORM_SYNC);
		
		codeSyncService.synchronize(resourceNodeId, dir, CodeSyncCodeJavaConstants.TECHNOLOGY, true);
		
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
		Node superInterface = new Node(CodeSyncCodeJavaConstants.SUPER_INTERFACE, root.getResource(), null, null);
		nodeService.addChild(cls, superInterface, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(superInterface, CoreConstants.NAME, "IFromModel", new ServiceContext<NodeService>(nodeService));
//		Node deprecated = getChild(cls, new String[] {"Deprecated"});
//		Node val = new Node();
//		val.setType(MEMBER_VALUE_PAIR);
//		nodeService.addChild(deprecated, val);
//		nodeService.setProperty(val, NAME, "_");
//		nodeService.setProperty(val, ANNOTATION_VALUE_VALUE, "test");

		// add class
		Node internalCls = new Node(CodeSyncCodeJavaConstants.CLASS, root.getResource(), null, null);
		nodeService.addChild(cls, internalCls, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(internalCls, CoreConstants.NAME, "InternalClassFromModel", new ServiceContext<NodeService>(nodeService));

		// change typed element type
		Node x = getChild(cls, new String[] {"x"});
		nodeService.setProperty(x, TYPED_ELEMENT_TYPE, "Test", new ServiceContext<NodeService>(nodeService));
		
		// change modifiers + annotations
		Node test = getChild(cls, new String[] {"test(String)"});
		Node privateModif = new Node(CodeSyncCodeJavaConstants.MODIFIER, root.getResource(), null, null);
		nodeService.addChild(test, privateModif, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(privateModif, CoreConstants.NAME, "private", new ServiceContext<NodeService>(nodeService));
		Node publicModif = getChild(test, new String[] {"public"});
		nodeService.setProperty(publicModif, REMOVED, true, new ServiceContext<NodeService>(nodeService));
		Node a = getChild(test, new String[] {"OneToMany"});
		Node mappedBy = getChild(a, new String[] {"mappedBy"});
		nodeService.setProperty(mappedBy, ANNOTATION_VALUE_VALUE, "\"modified_by_model\"", new ServiceContext<NodeService>(nodeService));
		Node orphanRemoval = new Node(CodeSyncCodeJavaConstants.MEMBER_VALUE_PAIR, root.getResource(), null, null);
		nodeService.addChild(a, orphanRemoval, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(orphanRemoval, CoreConstants.NAME, "orphanRemoval", new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(orphanRemoval, ANNOTATION_VALUE_VALUE, "true", new ServiceContext<NodeService>(nodeService));
		
		// change parameters
		Node getTest = getChild(cls, new String[] {"getTest()"});
		Node param = new Node(CodeSyncCodeJavaConstants.PARAMETER, root.getResource(), null, null);
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
		Node t = new Node(CodeSyncCodeJavaConstants.ATTRIBUTE, root.getResource(), null, null);
		nodeService.addChild(cls, t, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(t, CoreConstants.NAME, "t", new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(t, TYPED_ELEMENT_TYPE, "int", new ServiceContext<NodeService>(nodeService));
//		nodeService.setProperty(t, DOCUMENTATION, "doc from model @author test");
		publicModif = new Node(CodeSyncCodeJavaConstants.MODIFIER, root.getResource(), null, null);
		nodeService.addChild(t, publicModif, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(publicModif, CoreConstants.NAME, "public", new ServiceContext<NodeService>(nodeService));
		
		// remove element
		Node y = getChild(cls, new String[] {"y"});
		nodeService.setProperty(y, REMOVED, true, new ServiceContext<NodeService>(nodeService));
	}
	
	@Test
	public void testMatchConflicts() {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_CONFLICTS);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_CONFLICTS /*+ "/" + SOURCE_FILE*/;

		File project = getProject();
		
		Node root = CodeSyncPlugin.getInstance().getResource(resourceNodeId);		
		
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
		Node orphanRemoval = new Node(CodeSyncCodeJavaConstants.MEMBER_VALUE_PAIR, root.getResource(), null, null);

		Match match = codeSyncService.generateMatch(resourceNodeId, new File(project, fullyQualifiedName), CodeSyncCodeJavaConstants.TECHNOLOGY, false);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),				// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3),				// ITest
						
							new Pair(MatchType._3MATCH, 3),				// @OneToMany(mappedBy="test") public int test(String st)
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OneToMany
									new Pair(MatchType._3MATCH, 5),				// mappedBy = test
								new Pair(MatchType._3MATCH, 4),				// String st
							
							new Pair(MatchType._3MATCH, 3),				// @OverrideAnnotationOf(mappedBy="test") public static Test getTest()
								new Pair(MatchType._3MATCH, 4),				// public
								new Pair(MatchType._3MATCH, 4),				// @OverrideAnnotationOf
									new Pair(MatchType._3MATCH, 5),				// x+y
								new Pair(MatchType._3MATCH, 4),				// static
								
							new Pair(MatchType._3MATCH, 3),				// private int y
								new Pair(MatchType._3MATCH, 4),				// private
								
							new Pair(MatchType._3MATCH, 3), 				// private int x
								new Pair(MatchType._3MATCH, 4),				// private
				};
		boolean[] conflicts = {
				false,
					false,
						true,			// superClass changed on model and source
						
							false,
							false,
							false,
							
							false,
								false,
								false,
									true,	// annotation value changed on model and source
								false,
							
							false,
								false,
								false,
									false,
								false,
							
							true,			// type changed on model and source
								false,
							
							false,			
								false,
			};
		assertTrue("Conflicts expected!", match.isChildrenConflict());
		testMatchTree(match, typeList, false);
		testConflicts(match, conflicts);
	}
	
	
	/////////////////////////////
	// Utils
	/////////////////////////////
	
	private File getProject() {
		return CodeSyncTestSuite.getProject(PROJECT);
	}
	
	private File getFile(String path) {
		return CodeSyncTestSuite.getFile(path);
	}
	
	private Node getChild(Node node, String[] names) {
		Node parent = node;
		for (String name : names) {
			List<Node> children = nodeService.getChildren(parent, new ServiceContext<NodeService>(nodeService).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
			for (Node child : children) {
				if (name.equals(child.getOrPopulateProperties().get(CoreConstants.NAME))) {
					parent = child;
					break;
				}
			}
		}
		return parent;
	}
	
	private Pair[] typeList;
	private boolean[] conflicts;
	private int i;
	
	private void testConflicts(Match match, boolean[] conflicts) {
		i = 0;
		this.conflicts = conflicts;
		checkTree_conflict(match);
	}
	
	private Match testMatchTree(Match match, Pair[] typeList, boolean checkNoDiffs) {
		assertNotNull("Match was not created", match);
		i = 0;
		this.typeList = typeList;
		checkTree_type(match, checkNoDiffs, 0);
		assertEquals(typeList.length, i + 1);
		return match;
	}
	
	private void checkTree_type(Match parentMatch, boolean checkNoDiffs, int level) {
		checkMatch_type(parentMatch, level);
		if (checkNoDiffs) {
			assertEquals("No diffs expected", 0, parentMatch.getDiffs().size());
		}
		for (Match subMatch : parentMatch.getSubMatches()) {
			i++;
			checkTree_type(subMatch, checkNoDiffs, level + 1);
		}
	}
	
	private void checkMatch_type(Match match, int level) {
		assertEquals("Wrong match at index " + i, typeList[i].type, match.getMatchType());
		assertEquals("Wrong level at index " + i, typeList[i].level, level);
	}
	
	private void checkTree_conflict(Match parentMatch) {
		checkMatch_conflict(parentMatch);
		for (Match subMatch : parentMatch.getSubMatches()) {
			i++;
			checkTree_conflict(subMatch);
		}
	}
	
	private void checkMatch_conflict(Match match) {
		assertEquals("Wrong conflict state at index " + i, conflicts[i], match.isConflict());
	}
	
	class Pair {
		public MatchType type;
		public int level;
		
		public Pair(MatchType type, int level) {
			this.type = type;
			this.level = level;
		}
	}
}
