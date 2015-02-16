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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

//CHECKSTYLE:OFF
/**
 * 
 * @author Claudiu Matei
 *
 */
public class EntityRegistryTest extends EclipseIndependentTestBase {
	
	private static Context ctx;
	
	private static Scriptable scope;
	
	private EntityOperationsAdapter entityOperationsAdapter;
	
	private Scriptable entityRegistryManager; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx = Context.enter();
		scope = ctx.initStandardObjects();	
		String pathToJsFolder = FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.jsutil/src_js_as/diff_update";
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/EntityRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/EntityRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/DiffUpdateProcessors.js"), StandardCharsets.UTF_8), null, 1, null);
	}
	
	@Before
	public void beforeTest() {
		entityOperationsAdapter = new EntityOperationsAdapter();
		entityRegistryManager = ctx.newObject(scope, "EntityRegistryManager", new Object[] { entityOperationsAdapter });
		scope.put("_entityRegistryManager", scope, entityRegistryManager);
	}
	
	@Test
	public void testRegisterEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(145);
		
		DetailEntity detailEntity;
		
		detailEntity = new DetailEntity();
		detailEntity.setId(1);
		masterEntity.getDetails().add(detailEntity);

		detailEntity = new DetailEntity();
		detailEntity.setId(2);
		masterEntity.getDetails().add(detailEntity);
		
		EntityChangeListener listener = mock(EntityChangeListener.class);					
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "addEntityChangeListener", listener);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertEquals("Entity was added to the registry", masterEntity, jsMasterEntity);
		verify(listener).entityRegistered(masterEntity);
		verify(listener).entityRegistered(detailEntity);
	}

	@Test
	public void testMergeEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity;

		masterEntity = new MasterEntity();
		masterEntity.setId(145);
		masterEntity.setName("original name");
		
		DetailEntity detailEntity, detailEntity1;
		
		detailEntity = new DetailEntity();
		detailEntity.setId(1);
		masterEntity.getDetails().add(detailEntity);
		detailEntity1 = detailEntity;
		
		detailEntity = new DetailEntity();
		detailEntity.setId(2);
		masterEntity.getDetails().add(detailEntity);

		SubdetailEntity subdetailEntity;
		subdetailEntity = new SubdetailEntity();
		subdetailEntity.setId(15);
		subdetailEntity.setValue(150);
		detailEntity.getSubdetails().add(subdetailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);

//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

//		System.out.println("********************************");
		
		{
			MasterEntity masterEntityNew;

			masterEntityNew = new MasterEntity();
			masterEntityNew.setId(145);
			masterEntityNew.setName("new name");
			
			DetailEntity detailEntityNew;

			detailEntityNew = new DetailEntity();
			detailEntityNew.setId(2);
			masterEntityNew.getDetails().add(detailEntityNew);

			SubdetailEntity subdetailEntityNew = new SubdetailEntity();
			subdetailEntityNew.setId(15);
			subdetailEntityNew.setValue(250);
			detailEntityNew.getSubdetails().add(subdetailEntityNew);

			JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntityNew);
		}
		
//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertEquals("Master entity instance was kept", masterEntity, jsMasterEntity);

		assertEquals("Master entity property was changed", "new name", jsMasterEntity.getName());

		assertEquals("Master entity 'name' property was changed to the new value", "new name", jsMasterEntity.getName());
		
		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity1));

		assertNull("Detail entity 1 was removed from registry", jsDetailEntity);
		
		assertEquals("Detail entity 2 instance was kept", detailEntity, jsMasterEntity.getDetails().get(0));

		SubdetailEntity jsSubdetailEntity = (SubdetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(subdetailEntity));

		assertEquals("Subdetail entity 'value' property was changed to the new value", 250, jsSubdetailEntity.getValue());
		
	}
	
	@Test
	public void testRegisterChildren() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(145);
		
		List<DetailEntity> details = new ArrayList<>();

		DetailEntity detailEntity = new DetailEntity();
		detailEntity.setId(1);
		details.add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerChildren", masterUid, "details", details);
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertEquals("Child entity was added to registry", detailEntity, jsDetailEntity);
		
		List<Object> jsDetails =  jsMasterEntity.getDetails();
		assertEquals("Child entity was added to parent's children", detailEntity, jsDetails.get(0));
		
		jsDetailEntity = (DetailEntity) jsDetails.get(0);
		assertEquals("Child entity's parentUid is set", jsDetailEntity.getParentUid(), masterUid);
		assertEquals("Child entity's parentChildrenProperty is set", jsDetailEntity.getParentChildrenProperty(), "details");
		
	}
	
	@Test
	public void testRegisterChildEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(145);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);

		DetailEntity detailEntity = new DetailEntity();
		detailEntity.setId(1);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", detailEntity, masterUid, "details", -1);
		
		detailEntity = new DetailEntity();
		detailEntity.setId(2);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", detailEntity, masterUid, "details", 0);

		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertEquals("Child entity was added to registry", detailEntity, jsDetailEntity);
		
		List<Object> jsDetails =  jsMasterEntity.getDetails();

		assertEquals("Root entity has 2 children", 2, jsDetails.size());

		assertEquals("Child entity was added to parent's children", detailEntity, jsDetails.get(0));
		
		jsDetailEntity = (DetailEntity) jsDetails.get(0);
		assertEquals("Child entity's parentUid is set", jsDetailEntity.getParentUid(), masterUid);
		assertEquals("Child entity's parentChildrenProperty is set", jsDetailEntity.getParentChildrenProperty(), "details");
		
	}
	
	@Test
	public void testUnregisterEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(145);
		
		List<DetailEntity> details = new ArrayList<>();

		DetailEntity detailEntity = new DetailEntity();
		detailEntity.setId(1);
		details.add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerChildren", masterUid, "details", details);

		EntityChangeListener listener = mock(EntityChangeListener.class);					
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "addEntityChangeListener", listener);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "unregisterEntity", masterUid);
		verify(listener).entityUnregistered(masterEntity);
		verify(listener).entityUnregistered(detailEntity);
		
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertNull("Root entity was removed from registry", jsMasterEntity);

		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertNull("Detail entity was removed from registry", jsDetailEntity);
		
	}
	
	@Test
	public void testUnregisterChildren() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(145);
		
		List<DetailEntity> details = new ArrayList<>();

		DetailEntity detailEntity = new DetailEntity();
		detailEntity.setId(1);
		details.add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerChildren", masterUid, "details", details);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "unregisterChildren", masterUid, "details");

		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertNull("Detail entity was removed from registry", jsDetailEntity);
		
	}
	
	@Test
	public void testUnregisterChild() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(145);
		
		List<DetailEntity> details = new ArrayList<>();

		DetailEntity detailEntity = new DetailEntity();
		detailEntity.setId(1);
		details.add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerChildren", masterUid, "details", details);
		String detailUid = entityOperationsAdapter.object_getEntityUid(detailEntity);

//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "unregisterEntity", detailUid);

		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertNull("Detail entity was removed from registry", jsDetailEntity);

		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertEquals("Detail entity was removed from root's children list", 0, jsMasterEntity.getDetails().size());

//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
		
	}
	
}
