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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.flowerplatform.tests.diff_update.entity.DetailEntity;
import org.flowerplatform.tests.diff_update.entity.MasterEntity;
import org.flowerplatform.tests.diff_update.entity.SubdetailEntity;
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
	public void testAddNewEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity(145);
		
		DetailEntity detailEntity;
		
		detailEntity = new DetailEntity(1);
		masterEntity.getDetails().add(detailEntity);

		detailEntity = new DetailEntity(2);
		masterEntity.getDetails().add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertEquals("Entity was added to the registry", masterEntity, jsMasterEntity);
	}

	@Test
	public void testMergeEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity;

		masterEntity = new MasterEntity(145);
		masterEntity.setName("original name");
		
		DetailEntity detailEntity, detailEntity1;
		
		detailEntity = new DetailEntity(1);
		masterEntity.getDetails().add(detailEntity);
		detailEntity1 = detailEntity;
		
		detailEntity = new DetailEntity(2);
		masterEntity.getDetails().add(detailEntity);

		SubdetailEntity subdetailEntity;
		subdetailEntity = new SubdetailEntity(15);
		subdetailEntity.setValue(150);
		detailEntity.getSubdetails().add(subdetailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

//		System.out.println("********************************");
		
		{
			MasterEntity masterEntityNew;

			masterEntityNew = new MasterEntity(145);
			masterEntityNew.setName("new name");
			
			DetailEntity detailEntityNew;
			detailEntityNew = new DetailEntity(2);
			masterEntityNew.getDetails().add(detailEntityNew);

			SubdetailEntity subdetailEntityNew = new SubdetailEntity(15);
			subdetailEntityNew.setValue(250);
			detailEntityNew.getSubdetails().add(subdetailEntityNew);

			System.out.println("*** " + masterEntityNew);
			
			JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntityNew);
		}
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

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
	public void testAddChildren() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity(145);
		
		List<DetailEntity> details = new ArrayList<>();
		DetailEntity detailEntity = new DetailEntity(1);
		details.add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);

		masterEntity.setDetails(details);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertEquals("Child entity was added to registry", detailEntity, jsDetailEntity);
		
		List<DetailEntity> jsDetails =  jsMasterEntity.getDetails();
		assertEquals("Child entity was added to parent's children", detailEntity, jsDetails.get(0));
		
	}
	
	@Test
	public void testRemoveEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity(145);
		
		List<DetailEntity> details = new ArrayList<>();

		DetailEntity detailEntity = new DetailEntity(1);
		details.add(detailEntity);
		masterEntity.setDetails(details);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "remove", masterUid);
		
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertNull("Root entity was removed from registry", jsMasterEntity);

		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertNull("Detail entity was removed from registry", jsDetailEntity);
		
	}
	
	@Test
	public void testRemoveChildren() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity(145);
		List<DetailEntity> details = new ArrayList<>();
		DetailEntity detailEntity = new DetailEntity(1);
		details.add(detailEntity);
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);

		masterEntity  = new MasterEntity(145);
		details = new ArrayList<>();
		masterEntity.setDetails(details);
		
		String masterUid = entityOperationsAdapter.object_getEntityUid(masterEntity);
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", masterUid);
		assertEquals(jsMasterEntity.getDetails().size(), 0);
		
		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertNull("Detail entity was removed from registry", jsDetailEntity);
		
	}
	
	@Test
	public void testUnregisterChild() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel");

		MasterEntity masterEntity  = new MasterEntity(145);
		
		List<DetailEntity> details = new ArrayList<>();

		DetailEntity detailEntity = new DetailEntity(1);
		details.add(detailEntity);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);

		masterEntity.setDetails(details);

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "mergeEntity", masterEntity);
		
		String detailUid = entityOperationsAdapter.object_getEntityUid(detailEntity);

//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");

		JsClientJavaUtils.invokeJsFunction(entityRegistry, "remove", detailUid);

		DetailEntity jsDetailEntity = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(detailEntity));
		assertNull("Detail entity was removed from registry", jsDetailEntity);

		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.object_getEntityUid(masterEntity));
		assertEquals("Detail entity was removed from root's children list", 0, jsMasterEntity.getDetails().size());

//		JsClientJavaUtils.invokeJsFunction(entityRegistry, "printDebugInfo");
		
	}
	
}
