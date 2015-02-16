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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.flowerplatform.tests.diff_update.entity.HumanResource;
import org.flowerplatform.tests.diff_update.entity.HumanResourceSchedule;
import org.flowerplatform.tests.diff_update.entity.Mission;
import org.flowerplatform.tests.diff_update.entity.ObjectAction;
import org.flowerplatform.tests.diff_update.entity.ObjectActionGroup;
import org.flowerplatform.tests.diff_update.entity.Task;
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
	
	private Scriptable entityRegistryManager; 

	private Mission mission101;
	private Task task301;
	private ObjectActionGroup objectActionGroup201, objectActionGroup202;
	private HumanResource humanResource501;
	private HumanResourceSchedule humanResourceSchedule601;
	private ObjectAction objectAction401, objectAction402, objectAction403;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    String rhino = "transport=socket,suspend=y,address=9000";
	    RhinoDebugger debugger = new RhinoDebugger(rhino);
	    debugger.start();
	    
	    ContextFactory factory = new ContextFactory();
	    factory.addListener(debugger);
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
		resetModel();
	}

	private void resetModel() {
		mission101 = new Mission(101); 
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
		humanResourceSchedule601 = new HumanResourceSchedule(601);
		humanResourceSchedule601.getMissions().add(mission101);
		humanResourceSchedule601.setHumanResource(humanResource501);
		
	}

	
	@Test
	public void testMerge() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", mission101);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
		
		
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", task301);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
//		
//		// remove objectActionGroup201 from task
//		task301.getObjectActionGroups().remove(objectActionGroup201);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", task301);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
//
//		// remove objectActionGroup201 from mission
//		mission101.getObjectActionGroups().remove(objectActionGroup201);
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", SerializationUtils.clone(mission101));
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

		
//		EntityChangeListener listener = mock(EntityChangeListener.class);					
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "addEntityChangeListener", listener);
//
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
//		
//		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.getEntityUid(masterEntity));
//		assertEquals("Entity was added to the registry", masterEntity, jsMasterEntity);
//		verify(listener).entityRegistered(masterEntity);
//		verify(listener).entityRegistered(detailEntity);
	}
	
}
