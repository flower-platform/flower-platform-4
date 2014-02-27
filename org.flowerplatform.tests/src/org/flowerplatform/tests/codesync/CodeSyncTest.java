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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.Match.MatchType;
import org.flowerplatform.codesync.code.CodeSyncCodePlugin;
import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaPlugin;
import org.flowerplatform.core.mindmap.remote.MindMapService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.tests.TestUtil;
import org.junit.Before;
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
	
	private MindMapService mindMapService = new MindMapService();
	
	@BeforeClass
	public static void setUpBeforeClass() {
		TestUtil.copyFilesAndCreateProject(DIR + TestUtil.INITIAL_TO_BE_COPIED, PROJECT);
	}
	
	@Before
	public void before() {
		mindMapService.reload();
	}
	
	@Test
	public void testMatchWhenSync() {
		CodeSyncPlugin.getInstance().addSrcDir(INITIAL);
		String fullyQualifiedName = PROJECT + "/" + INITIAL /*+ "/" + SOURCE_FILE*/;
		
		File project = getProject();
		
		Node root = mindMapService.getNode(null);
		Node model = mindMapService.addNode(root.getId(), CodeSyncPlugin.FOLDER);
		mindMapService.setProperty(model.getId(), Node.NAME, INITIAL);
		model = mindMapService.getNode(model.getId());
		Match match = CodeSyncCodePlugin.getInstance().runCodeSyncAlgorithm(model, project, fullyQualifiedName, null, CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		
		assertEquals(1, match.getSubMatches().size());
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),			// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
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
								
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3),					// ITest
				};
		testMatchTree(match, typeList, true);
	}
	
//	@Test
	public void testMatchCacheDeleted() {
		CodeSyncPlugin.getInstance().addSrcDir(CACHE_DELETED);
		String fullyQualifiedName = PROJECT + "/" + CACHE_DELETED + "/" + SOURCE_FILE;

		File file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getNode(getProject(), file, CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		Node root = new MindMapService().getNode(null);
		Match match = CodeSyncCodePlugin.getInstance().runCodeSyncAlgorithm(root, getProject(), file.getName(), file.getName(), CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),				// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
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
								
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3),					// ITest
				};
		testMatchTree(match, typeList, true);
	}
	
//	@Test
	public void testMatchNoConflicts() {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS + "/" + SOURCE_FILE;

		File file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getNode(getProject(), file, CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		Node root = new MindMapService().getNode(null);
		Match match = CodeSyncCodePlugin.getInstance().runCodeSyncAlgorithm(root, getProject(), file.getName(), file.getName(), CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		
//		// create FeatureChanges to simulate model modifications
//		IProject project = getProject(PROJECT);
//		CodeSyncElement srcDir = CodeSyncCodePlugin.getInstance().getSrcDir(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project), MODIFIED_NO_CONFLICTS);
//		FeatureChange featureChange = null;
//		
//		// change superCls, superInterfaces
//		CodeSyncElement Test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test"});
//		com.crispico.flower.mp.model.astcache.code.Class cls = (com.crispico.flower.mp.model.astcache.code.Class) Test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getClass_SuperClasses(), featureChange);
//		featureChange.setOldValue(cls.getSuperClasses());
//		List<String> superClasses = Collections.singletonList("SuperClassFromModel");
//		featureChange.setNewValue(superClasses);
////		CodeSyncCodePlugin.getInstance().getUtils().setSuperClasses(cls, superClasses);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces(), featureChange);
//		featureChange.setOldValue(cls.getSuperInterfaces());
//		List<String> superInterfaces = new ArrayList<String>();
//		superInterfaces.add("IFromModel");
//		superInterfaces.add("ITest");
//		featureChange.setNewValue(superInterfaces);
////		CodeSyncCodePlugin.getInstance().getUtils().setSuperInterfaces(cls, superInterfaces);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(cls.getModifiers());
//		Modifier modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		List<ExtendedModifier> modifiers = new BasicEList<ExtendedModifier>();
//		modifier.setType(1); // public
//		modifiers.add(modifier);
//		Annotation a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("Deprecated");
//		AnnotationValue value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("_");
//		value.setValue("test");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
////		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(cls, modifiers);
//		
//		// add class
//		CodeSyncElement InternalCls = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
//		InternalCls.setAdded(true);
//		InternalCls.setName("InternalClassFromModel");
//		InternalCls.setType("Class");
//		com.crispico.flower.mp.model.astcache.code.Class internalCls = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createClass();
//		InternalCls.setAstCacheElement(internalCls);
//		CodeSyncCodePlugin.getInstance().getUtils().addChild(Test, InternalCls);
//		CodeSyncCodePlugin.getInstance().getUtils().addToResource(project, internalCls);
//		
//		// change typed element type
//		CodeSyncElement x = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "x"});
//		TypedElement attr = (TypedElement) x.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(x, AstCacheCodePackage.eINSTANCE.getTypedElement_Type(), featureChange);
//		featureChange.setOldValue(attr.getType());
//		featureChange.setNewValue("Test");
////		attr.setType("Test");
//		
//		// change modifiers + annotations
//		CodeSyncElement test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "test(String)"});
//		Operation op = (Operation) test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(test, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(op.getModifiers());
//		modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		modifiers = new BasicEList<ExtendedModifier>();
//		modifier.setType(2); // private
//		modifiers.add(modifier);
//		a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("OneToMany");
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("mappedBy");
//		value.setValue("\"modified_by_model\"");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("orphanRemoval");
//		value.setValue("true");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
////		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(op, modifiers);
//		
//		// change parameters
//		CodeSyncElement getTest = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "getTest()"});
//		op = (Operation) getTest.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getOperation_Parameters(), featureChange);
//		featureChange.setOldValue(op.getParameters());
//		Parameter param = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createParameter();
//		param.setName("a");
//		param.setType("int");
//		List<Parameter> params = new BasicEList<Parameter>();
//		params.add(param);
//		featureChange.setNewValue(params);
////		CodeSyncCodePlugin.getInstance().getUtils().setParams(op, params);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation(), featureChange);
//		featureChange.setOldValue(op.getDocumentation());
//		String doc = "modified from model\n@author test";
//		featureChange.setNewValue(doc);
////		op.setDocumentation(doc);
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(getTest, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(op.getModifiers());
//		modifiers = new BasicEList<ExtendedModifier>();
//		modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		modifier.setType(1); // public
//		modifiers.add(modifier);
//		a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("OverrideAnnotationOf");
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("value1");
//		value.setValue("true");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("value2");
//		value.setValue("false");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
////		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(op, modifiers);
//		
//		// add element
//		CodeSyncElement newCSE = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
//		newCSE.setAdded(true);
//		newCSE.setName("t");
//		newCSE.setType("Attribute");
//		Attribute t = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAttribute();
//		t.setDocumentation("doc from model @author test");
//		t.setType("int");
//		modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		modifier.setType(1);
//		CodeSyncCodePlugin.getInstance().getUtils().setModifiers(t, Collections.singletonList((ExtendedModifier) modifier));
//		newCSE.setAstCacheElement(t);
//		CodeSyncCodePlugin.getInstance().getUtils().addChild(Test, newCSE);
//		CodeSyncCodePlugin.getInstance().getUtils().addToResource(project, t);
//		
//		// remove element
//		CodeSyncElement y = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "y"});
//		y.setDeleted(true);
//		
//		CodeSyncMergePlugin.getInstance().saveResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncMergePlugin.getInstance().discardResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		service.unsubscribeAllClientsForcefully(project.getFullPath().toString(), false);
//		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, getFile(fullyQualifiedName), CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel);
		
		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),					// src
					new Pair(MatchType._3MATCH, 1),					// Test.java
						new Pair(MatchType._3MATCH, 2),					// @Deprecated public class Test
						
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
								new Pair(MatchType._3MATCH, 4),					// public
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// @OverrideAnnotationOf(x+y) (removed from model)
									new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 5),		// x+y
								new Pair(MatchType._1MATCH_ANCESTOR, 4),			// removed static from model and source
								new Pair(MatchType._1MATCH_LEFT, 4),				// @overrideAnnotationOf(valu1=true, value2=false) (added to model)
									new Pair(MatchType._1MATCH_LEFT, 5),				// value1 (added to model)
									new Pair(MatchType._1MATCH_LEFT, 5),				// value2 (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// added param to model
								
							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// private int y (removed from model)
								new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 4),		// private
								
							new Pair(MatchType._3MATCH, 3),					// private Test x <> private int x
								new Pair(MatchType._3MATCH, 4),					// private
								
							new Pair(MatchType._1MATCH_LEFT, 3),				// private int t (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// private
								
							new Pair(MatchType._1MATCH_LEFT, 3),				// class InternalClassFromModel
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public enum ActionType
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public Object diffAction
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// private ActionType(Object action)
									new Pair(MatchType._1MATCH_RIGHT, 5),				// Object action
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// private
								new Pair(MatchType._1MATCH_RIGHT, 4),				// ACTION_TYPE_COPY_LEFT_RIGHT(new Test())
									new Pair(MatchType._1MATCH_RIGHT, 5),				// new Test()
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// ACTION_TYPE_COPY_RIGHT_LEFT(new InternalClsFromSource());
									new Pair(MatchType._1MATCH_RIGHT, 5),				// new InternalClsFromSource()
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public class InternalClsFromSource
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public int x
									new Pair(MatchType._1MATCH_RIGHT, 5), 				// public
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// public @interface AnnotationTest
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// boolean value1() default true
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// boolean value2() default false
								new Pair(MatchType._1MATCH_RIGHT, 4),				// public
								
							new Pair(MatchType._1MATCH_RIGHT, 3),				// private int z (added to source)
								new Pair(MatchType._1MATCH_RIGHT, 4), 				// private
								
							new Pair(MatchType._2MATCH_ANCESTOR_RIGHT, 3),		// @Deprecated (removed from model)
							new Pair(MatchType._3MATCH, 3),					// public
							new Pair(MatchType._1MATCH_LEFT, 3),				// @Deprecated(test) (added to model)
								new Pair(MatchType._1MATCH_LEFT, 4),				// test
							new Pair(MatchType._2MATCH_ANCESTOR_LEFT, 3),		// ITest
							new Pair(MatchType._1MATCH_LEFT, 3),				// IFromModel
							new Pair(MatchType._1MATCH_RIGHT, 3),				// IFromSource
		};
		testMatchTree(match, typeList, false);
		assertFalse("Conflicts not expected!", match.isChildrenConflict());
	}
	
//	@Test
	public void testMatchNoConflictsAndPerformSync() throws IOException {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_NO_CONFLICTS_PERFORM_SYNC);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/" + SOURCE_FILE;
		File project = getProject();
		File cseLocation = (File) CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CodeSyncPlugin.getInstance().CSE_MAPPING_FILE_LOCATION);
		File aceLocation = (File) CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CodeSyncPlugin.getInstance().ACE_FILE_LOCATION);

		
		File file = getFile(fullyQualifiedName);
//		CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(file.getFullPath().toString());
		CodeSyncCodePlugin.getInstance().getNode(project, file, CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		Node root = new MindMapService().getNode(null);
		Match match = CodeSyncCodePlugin.getInstance().runCodeSyncAlgorithm(root, getProject(), file.getName(), file.getName(), CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
//		
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		service.synchronize(new StatefulServiceInvocationContext(communicationChannel), EditorPlugin.getInstance().getFileAccessController().getPath(project));
//		service.applySelectedActions(new StatefulServiceInvocationContext(communicationChannel), EditorPlugin.getInstance().getFileAccessController().getPath(project), true);
		
		String expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/" + SOURCE_FILE);
		String actual = FileUtils.readFileToString(file);
		assertEquals("Source not in sync", expected, actual);
		
		expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation");
		actual = FileUtils.readFileToString(cseLocation);
		assertEquals("CSE not in sync", expected, actual);
		
		expected = TestUtil.readFile(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/ACE.notation");
		actual = FileUtils.readFileToString(aceLocation);
		assertEquals("ACE not in sync", expected, actual);
		
//		Resource expectedCSE = CodeSyncMergePlugin.getInstance().getResource(null, new File(DIR + TestUtil.EXPECTED + "/" + MODIFIED_NO_CONFLICTS_PERFORM_SYNC + "/CSE.notation"));
//		Resource actualCSE = CodeSyncMergePlugin.getInstance().getResource(null, project.getFile(CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION));
//		
//		assertTrue(CodeSyncCodePlugin.getInstance().getUtils().testEquality(expectedCSE, actualCSE, MODIFIED_NO_CONFLICTS_PERFORM_SYNC));
	}
	
//	@Test
	public void testMatchConflicts() {
		CodeSyncPlugin.getInstance().addSrcDir(MODIFIED_CONFLICTS);
		String fullyQualifiedName = PROJECT + "/" + MODIFIED_CONFLICTS + "/" + SOURCE_FILE;

		File file = getFile(fullyQualifiedName);
		Node root = new MindMapService().getNode(null);
		Match match = CodeSyncCodePlugin.getInstance().runCodeSyncAlgorithm(root, getProject(), file.getName(), file.getName(), CodeSyncCodeJavaPlugin.TECHNOLOGY, true);
		
//		// create FeatureChanges to simulate model modifications
//		IProject project = getProject(PROJECT);
//		CodeSyncElement srcDir = CodeSyncCodePlugin.getInstance().getSrcDir(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project), MODIFIED_CONFLICTS);
//		FeatureChange featureChange = null;
//		
//		// change super class
//		CodeSyncElement Test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test"});
//		com.crispico.flower.mp.model.astcache.code.Class cls = (com.crispico.flower.mp.model.astcache.code.Class) Test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(Test, AstCacheCodePackage.eINSTANCE.getClass_SuperClasses(), featureChange);
//		featureChange.setOldValue(cls.getSuperClasses());
//		List<String> superClasses = Collections.singletonList("SuperClassFromModel");
//		featureChange.setNewValue(superClasses);
//		// change typed element type
//		CodeSyncElement x = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "x"});
//		TypedElement attr = (TypedElement) x.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(x, AstCacheCodePackage.eINSTANCE.getTypedElement_Type(), featureChange);
//		featureChange.setOldValue(attr.getType());
//		featureChange.setNewValue("Test");
//		// change typed element type
//		CodeSyncElement y = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "y"});
//		TypedElement attry = (TypedElement) y.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(y, AstCacheCodePackage.eINSTANCE.getTypedElement_Type(), featureChange);
//		featureChange.setOldValue(attry.getType());
//		featureChange.setNewValue("Test");
//		// change modifiers + annotations
//		CodeSyncElement test = CodeSyncCodePlugin.getInstance().getCodeSyncElement(srcDir, new String[] {SOURCE_FILE, "Test", "test(String)"});
//		Operation op = (Operation) test.getAstCacheElement();
//		featureChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		CodeSyncCodePlugin.getInstance().getUtils().addFeatureChange(test, AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), featureChange);
//		featureChange.setOldValue(op.getModifiers());
//		Modifier modifier = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createModifier();
//		List<ExtendedModifier> modifiers = new BasicEList<ExtendedModifier>();
//		modifier.setType(1); // public
//		modifiers.add(modifier);
//		Annotation a = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotation();
//		a.setName("OneToMany");
//		AnnotationValue value = AstCacheCodePackage.eINSTANCE.getAstCacheCodeFactory().createAnnotationValue();
//		value.setName("mappedBy");
//		value.setValue("\"modified_by_model\"");
//		CodeSyncCodePlugin.getInstance().getUtils().addAnnotationValue(a, value);
//		modifiers.add(a);
//		featureChange.setNewValue(modifiers);
//		
//		CodeSyncMergePlugin.getInstance().saveResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncMergePlugin.getInstance().discardResource(CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project));
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		service.unsubscribeAllClientsForcefully(project.getFullPath().toString(), false);
//		CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, getFile(fullyQualifiedName), CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel);

		Pair[] typeList = {
				new Pair(MatchType._3MATCH, 0),				// src
					new Pair(MatchType._3MATCH, 1),				// Test.java
						new Pair(MatchType._3MATCH, 2),				// @Deprecated public class Test
						
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
								
							new Pair(MatchType._3MATCH, 3),				// @Deprecated
							new Pair(MatchType._3MATCH, 3),				// public
							new Pair(MatchType._3MATCH, 3)				// ITest
				};
		boolean[] conflicts = {
				false,
					false,
						true,			// superClass changed on model and source
						
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
							
						false,
						false,
							false
			};
		assertTrue("Conflicts expected!", match.isChildrenConflict());
		testMatchTree(match, typeList, false);
		testConflicts(match, conflicts);
	}
	
//	@Test
//	public void testValueAsString() throws CoreException {
//		// STEP 1 : create FeatureChange
//		CodeSyncElement expectedCSE = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
//		FeatureChange expectedChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
//		expectedCSE.getFeatureChanges().put(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), expectedChange); 
//		EList<ExtendedModifier> modifiers = new BasicEList<ExtendedModifier>();
//		Modifier modifier = AstCacheCodeFactory.eINSTANCE.createModifier();
//		modifier.setType(3);
//		modifiers.add(modifier);
//		expectedChange.setOldValue(modifiers);
//		
//		// STEP 2 : put FC in resource and save
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
//		if (!project.exists()) {
//			project.create(null);
//		}
//		project.open(null);
//		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(CodeSyncTestSuite.getProject("test"), "diagramEditorStatefulService");
//		File codeSyncElementsFile = CodeSyncTestSuite.getFile("test/CSE.notation");
//		Resource resource = null;
//		if (!codeSyncElementsFile.exists()) {
//			resource = CodeSyncPlugin.getInstance().getResource(resourceSet, codeSyncElementsFile);
//			resource.getContents().add(expectedCSE);
//			CodeSyncPlugin.getInstance().saveResource(resource);
//		}
//		
//		CodeSyncPlugin.getInstance().discardResource(resource);
//		
//		// STEP 3 : get value from resource
//		resource = CodeSyncPlugin.getInstance().getResource(resourceSet, codeSyncElementsFile);
//		assertEquals(1, resource.getContents().size());
//		CodeSyncElement actualCSE = (CodeSyncElement) resource.getContents().get(0);
//		FeatureChange actualChange = actualCSE.getFeatureChanges().get(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
//		assertNotNull(actualChange);
//		EList<ExtendedModifier> actual = (EList<ExtendedModifier>) actualChange.getOldValue();
//		assertEquals(1, actual.size());
//		assertEquals(3, ((Modifier) actual.get(0)).getType());
//	}
	
	/////////////////////////////
	// Utils
	/////////////////////////////
	
	private File getProject() {
		return CodeSyncTestSuite.getProject(PROJECT);
	}
	
	private File getFile(String path) {
		return CodeSyncTestSuite.getFile(path);
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