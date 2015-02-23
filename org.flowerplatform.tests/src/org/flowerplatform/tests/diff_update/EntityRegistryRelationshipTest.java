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
package org.flowerplatform.tests.diff_update;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.flowerplatform.tests.diff_update.entity.AbstractEntity;
import org.flowerplatform.tests.diff_update.entity.HumanResource;
import org.flowerplatform.tests.diff_update.entity.HumanResourceSchedule;
import org.flowerplatform.tests.diff_update.entity.Mission;
import org.flowerplatform.tests.diff_update.entity.ObjectAction;
import org.flowerplatform.tests.diff_update.entity.ObjectActionGroup;
import org.flowerplatform.tests.diff_update.entity.Task;
import org.flowerplatform.tests.diff_update.entity.unnamed.E1;
import org.flowerplatform.tests.diff_update.entity.unnamed.E2;
import org.flowerplatform.tests.diff_update.entity.unnamed.E3;
import org.flowerplatform.tests.diff_update.entity.unnamed.E4;
import org.flowerplatform.tests.diff_update.entity.unnamed.E5;
import org.flowerplatform.tests.diff_update.entity.unnamed.E6;
import org.flowerplatform.tests.diff_update.entity.unnamed.E7;
import org.flowerplatform.tests.diff_update.entity.unnamed.E8;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

//CHECKSTYLE:OFF
/**
 * 
 * @author Claudiu Matei
 *
 */
public class EntityRegistryRelationshipTest extends EclipseIndependentTestBase {
	
	private static Context ctx;
	
	private static Scriptable scope;
	
	private EntityOperationsAdapter entityOperationsAdapter;
	
	private Scriptable entityRegistryManager, entityRegistry; 

	private Mission mission101, mission102;
	private Task task301;
	private ObjectActionGroup objectActionGroup201, objectActionGroup202;
	private HumanResource humanResource501, humanResource502;
	private HumanResourceSchedule humanResourceSchedule601, humanResourceSchedule602;
	private ObjectAction objectAction401, objectAction402, objectAction403;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//	    String rhino = "transport=socket,suspend=y,address=9000";
//	    RhinoDebugger debugger = new RhinoDebugger(rhino);
//	    debugger.start();
	    
	    ContextFactory factory = new ContextFactory();
//	    factory.addListener(debugger);
	    ctx = factory.enterContext();		
	    scope = ctx.initStandardObjects();	
		
	    String pathToJsFolder = FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.jsutil/src_js_as/diff_update";
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/EntityRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/DiffUpdateProcessors.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/EntityRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
	}
	
	@Before
	public void beforeTest() {
		entityOperationsAdapter = new EntityOperationsAdapter();
		entityRegistryManager = ctx.newObject(scope, "EntityRegistryManager", new Object[] { entityOperationsAdapter });
		scope.put("_entityRegistryManager", scope, entityRegistryManager);
		entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");
		resetModel();
	}

	private void resetModel() {
		mission101 = new Mission(101); 
		mission102 = new Mission(102); 
		task301 = new Task(301);
		objectActionGroup201 = new ObjectActionGroup(201);
		objectActionGroup202 = new ObjectActionGroup(202);
		
		objectAction401 = new ObjectAction(401);
		objectAction402 = new ObjectAction(402);
		objectAction403 = new ObjectAction(403);
		
		objectActionGroup201.getObjectActions().add(objectAction401);
		objectActionGroup201.getObjectActions().add(objectAction402);
		objectActionGroup202.getObjectActions().add(objectAction403);
		
		mission101.getObjectActionGroups().add(objectActionGroup201);
		mission101.getObjectActionGroups().add(objectActionGroup202);
		task301.getObjectActionGroups().add(objectActionGroup202);

		humanResource501 = new HumanResource(501);
		humanResource502 = new HumanResource(502);

		humanResourceSchedule601 = new HumanResourceSchedule(601);
		humanResourceSchedule601.getMissions().add(mission101);
		humanResourceSchedule601.setHumanResource(humanResource501);

		humanResourceSchedule602 = new HumanResourceSchedule(602);

	}
	
	private AbstractEntity getEntityFromRegistry(String uid) {
		AbstractEntity entity = (AbstractEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", uid);
		return entity;
	}
	
	private boolean isInRegistry(AbstractEntity entity) {
		Object registeredEntity = JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(entity));
		return registeredEntity != null;
	}

	@Test
	public void testMergeInstances() {
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);

		Mission missionInstance = mission101;
		ObjectActionGroup objectActionGroup = objectActionGroup201;
		ObjectAction objectAction = objectAction401;
		
		resetModel();

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);
		assertTrue("Mission:101 instance was kept", getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(mission101)) == missionInstance);
		assertTrue("ObjectActionGroup:201 instance was kept", getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(objectActionGroup201)) == objectActionGroup);
		assertTrue("ObjectAction:401 instance was kept", getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(objectAction401)) == objectAction);

	}
	
	@Test
	public void testMergeWithRemove() {
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", task301);

		assertTrue("Mission:101 was registered", isInRegistry(mission101));
		assertTrue("Task:301 was registered", isInRegistry(task301));
		assertTrue("ObjectActionGroup:201 was registered", isInRegistry(objectActionGroup201));
		assertTrue("ObjectActionGroup:202 was registered", isInRegistry(objectActionGroup202));
		assertTrue("ObjectAction:401 was registered", isInRegistry(objectAction401));
		assertTrue("ObjectAction:402 was registered", isInRegistry(objectAction402));
		assertTrue("ObjectAction:403 was registered", isInRegistry(objectAction403));
		
		// remove objectActionGroup201 from task
		resetModel();
		task301.getObjectActionGroups().remove(objectActionGroup202);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", task301);
		assertTrue("ObjectActionGroup:202 was kept", isInRegistry(objectActionGroup202));
		
		// remove objectActionGroup201 from mission
		mission101.getObjectActionGroups().remove(objectActionGroup202);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);
		assertFalse("ObjectActionGroup:202 was removed", isInRegistry(objectActionGroup202));
	}

	@Test
	public void testOneToManyUnidirectionalNavigable() {
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission102);
		
		Mission mission = (Mission) getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(mission101));
		ObjectActionGroup objectActionGroup = (ObjectActionGroup) getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(objectActionGroup201));
		
		assertTrue("ObjectActionGroup:201 was added to (Mission:101).objectActionGroups", 
				mission.getObjectActionGroups().contains(objectActionGroup) && mission.getObjectActionGroups().size() == 2);
		
		resetModel();
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);
		
		assertTrue("ObjectActionGroup:201 was not added twice to (Mission:101).objectActionGroups", 
				mission.getObjectActionGroups().contains(objectActionGroup) && mission.getObjectActionGroups().size() == 2);
		
		resetModel();

		mission102.getObjectActionGroups().add(objectActionGroup201);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission102);
		
		assertTrue("ObjectActionGroup:201 was removed from (Mission:101).objectActionGroups", 
				!mission.getObjectActionGroups().contains(objectActionGroup201) && mission.getObjectActionGroups().size() == 1);

		mission = (Mission) getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(mission102));
		
		assertTrue("ObjectActionGroup:201 was added to (Mission:102).objectActionGroups", 
				mission.getObjectActionGroups().contains(objectActionGroup) && mission.getObjectActionGroups().size() == 1);
		
	}

	@Test
	public void testManyToOneUnidirectionalNavigable() {
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", humanResourceSchedule601);
		
		HumanResource humanResource = (HumanResource) getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(humanResource501));
		HumanResourceSchedule humanResourceSchedule = (HumanResourceSchedule) getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(humanResourceSchedule601));
		
		assertTrue("HumanResourceSchedule:601 was added to (HumanResource:501).humanResourceSchedules", 
				humanResource.getHumanResourceSchedules().contains(humanResourceSchedule) && humanResource.getHumanResourceSchedules().size() == 1);
		
		resetModel();
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", humanResourceSchedule601);
		
		assertTrue("HumanResourceSchedule:601 was not added twice to (HumanResource:501).humanResourceSchedules", 
				humanResource.getHumanResourceSchedules().contains(humanResourceSchedule) && humanResource.getHumanResourceSchedules().size() == 1);
		
		resetModel();
		humanResourceSchedule601.setHumanResource(humanResource502);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", humanResourceSchedule601);
		
		assertTrue("HumanResourceSchedule:601 was removed from (HumanResource:501).humanResourceSchedules", 
				!humanResource.getHumanResourceSchedules().contains(humanResourceSchedule602) && humanResource.getHumanResourceSchedules().size() == 0);

		humanResource = (HumanResource) getEntityFromRegistry(entityOperationsAdapter.object_getEntityUid(humanResource502));
		
		assertTrue("HumanResourceSchedule:601 was added to (HumanResource:502).humanResourceSchedules", 
				humanResource.getHumanResourceSchedules().contains(humanResourceSchedule) && humanResource.getHumanResourceSchedules().size() == 1);

	}
	
	@Test
	public void testComplexRemove1() {
		E1 e1 = new E1(1);
		E2 e2 = new E2(2);
		E3 e3 = new E3(3);
		E4 e4 = new E4(4);
		E5 e5 = new E5(5);
		E6 e6 = new E6(6);
		E7 e7 = new E7(7);
		
		e1.e2List.add(e2);
		e1.e7Ref = e7;
		e2.e3List.add(e3);
		e3.e4Ref = e4;
		e4.e3List.add(e3);
		e4.e5List.add(e5);
		e5.e2List.add(e2);
		e6.e5Ref = e5;
		e7.e6Ref = e6;
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", e1);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
		
		// determine removal of E7 
		e1 = new E1(1);
		e1.e2List.add(e2); 

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", e1);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

		assertTrue("E1 is in registry", isInRegistry(e1));
		assertTrue("E2 is in registry", isInRegistry(e2));
		assertTrue("E3 is in registry", isInRegistry(e3));
		assertTrue("E4 is in registry", isInRegistry(e4));
		assertTrue("E5 is in registry", isInRegistry(e5));
		assertFalse("E6 is not in registry", isInRegistry(e6));
		assertFalse("E7 is not in registry", isInRegistry(e7));
		
	}

	@Test
	public void testComplexRemove2() {
		E1 e1 = new E1(1);
		E2 e2 = new E2(2);
		E3 e3 = new E3(3);
		E8 e8 = new E8(8);
		E5 e5 = new E5(5);
		E6 e6 = new E6(6);
		E7 e7 = new E7(7);
		
		e1.e2List.add(e2);
		e1.e7Ref = e7;
		e2.e3List.add(e3);
		e3.e8Ref = e8;
		e8.e3List.add(e3);
		e5.e8Ref = e8;
		e5.e2List.add(e2);
		e6.e5Ref = e5;
		e7.e6Ref = e6;
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", e1);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
		
		// determine removal of E7 
		e1 = new E1(1);
		e1.e2List.add(e2); 

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", e1);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

		assertTrue("E1 is in registry", isInRegistry(e1));
		assertTrue("E2 is in registry", isInRegistry(e2));
		assertTrue("E3 is in registry", isInRegistry(e3));
		assertTrue("E8 is in registry", isInRegistry(e8));
		assertFalse("E5 is not in registry", isInRegistry(e5));
		assertFalse("E6 is not in registry", isInRegistry(e6));
		assertFalse("E7 is not in registry", isInRegistry(e7));
		
	}

}
