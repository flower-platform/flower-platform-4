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

import org.apache.commons.io.FileUtils;
import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.flowerplatform.util.diff_update.AddEntityDiffUpdate;
import org.flowerplatform.util.diff_update.PropertiesDiffUpdate;
import org.flowerplatform.util.diff_update.RemoveEntityDiffUpdate;
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
public class DiffUpdateTest extends EclipseIndependentTestBase {
	
	private static Context ctx;
	
	private static Scriptable scope;
	
	private static EntityOperationsAdapter entityOperationsAdapter;
	
	private static Scriptable entityRegistryManager; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ctx = Context.enter();
		scope = ctx.initStandardObjects();	
		String pathToJsFolder = FileUtils.getFile("src/").getAbsolutePath() + "/../../org.flowerplatform.util/WebContent/js";
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/EntityRegistry.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/EntityRegistryManager.js"), StandardCharsets.UTF_8), null, 1, null);
		ctx.evaluateReader(scope, Files.newBufferedReader(Paths.get(pathToJsFolder + "/DiffUpdateProcessors.js"), StandardCharsets.UTF_8), null, 1, null);

		entityOperationsAdapter = new EntityOperationsAdapter();
		entityRegistryManager = ctx.newObject(scope, "EntityRegistryManager", new Object[] { entityOperationsAdapter });
		scope.put("_entityRegistryManager", scope, entityRegistryManager);
		
		ctx.evaluateString(scope, "_entityRegistryManager.addDiffUpdateProcessor(Constants.ADDED, new AddEntityDiffUpdateProcessor());", null, 1, null); 
		ctx.evaluateString(scope, "_entityRegistryManager.addDiffUpdateProcessor(Constants.REMOVED, new RemoveEntityDiffUpdateProcessor());", null, 1, null); 
		ctx.evaluateString(scope, "_entityRegistryManager.addDiffUpdateProcessor(Constants.UPDATED, new PropertiesDiffUpdateProcessor());", null, 1, null); 

		
	}

	@Before
	public void beforeTest() {
	}
	
	@Test
	public void testAddRootEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel1");
		
		MasterEntity masterEntity = new MasterEntity();
		masterEntity.setId(145);
		masterEntity.setName("entity1");
		
		AddEntityDiffUpdate update = new AddEntityDiffUpdate();
		update.setId(1);
		update.setEntity(masterEntity);
		JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "processDiffUpdate", "testChannel1", update);
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.getEntityUid(masterEntity));
		assertEquals("Entity was added to the registry", masterEntity, jsMasterEntity);
		assertEquals("Property name is set to \"entity1\"", "entity1", jsMasterEntity.getName());
	}

	@Test
	public void testAddChildEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel2");
		
		MasterEntity masterEntity = new MasterEntity();
		masterEntity.setId(145);
		masterEntity.setName("entity1");
		
		AddEntityDiffUpdate update = new AddEntityDiffUpdate();
		update.setId(1);
		update.setEntity(masterEntity);
		JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "processDiffUpdate", "testChannel2", update);

		DetailEntity detailEntity = new DetailEntity();
		detailEntity.setId(1);
		detailEntity.setValue(10);
		
		update = new AddEntityDiffUpdate();
		update.setId(2);
		update.setEntity(detailEntity);
		update.setParentChildrenProperty("details");
		update.setParentUid(new EntityOperationsAdapter().getEntityUid(masterEntity));
		
		JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "processDiffUpdate", "testChannel2", update);
		
		DetailEntity jsDetail = (DetailEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.getEntityUid(detailEntity));
		assertEquals("Entity was added to the registry", detailEntity, jsDetail);
	}

	
	@Test
	public void testRemoveRootEntity() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel3");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(150);
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);
		
		RemoveEntityDiffUpdate update = new RemoveEntityDiffUpdate();
		update.setId(1);
		update.setEntityUid(entityOperationsAdapter.getEntityUid(masterEntity));
		JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "processDiffUpdate", "testChannel3", update);
		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.getEntityUid(masterEntity));
		assertNull("Entity was removed from the registry", jsMasterEntity);
	}

	@Test
	public void testSetProperties() {
		NativeObject entityRegistry = (NativeObject) JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "createEntityRegistry", "testChannel4");

		MasterEntity masterEntity  = new MasterEntity();
		masterEntity.setId(150);
		masterEntity.setName("entity");
		
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "registerEntity", masterEntity);

		EntityChangeListener listener = mock(EntityChangeListener.class);					
		JsClientJavaUtils.invokeJsFunction(entityRegistry, "addEntityChangeListener", listener);

		PropertiesDiffUpdate update = new PropertiesDiffUpdate();
		update.setId(1);
		update.setEntityUid(entityOperationsAdapter.getEntityUid(masterEntity));
		update.addProperty("name", "entity3");
		JsClientJavaUtils.invokeJsFunction(entityRegistryManager, "processDiffUpdate", "testChannel4", update);
		verify(listener).entityUpdated(masterEntity);

		
		MasterEntity jsMasterEntity = (MasterEntity) JsClientJavaUtils.invokeJsFunction(entityRegistry, "getEntityByUid", entityOperationsAdapter.getEntityUid(masterEntity));
		assertEquals("Entity name was changed to \"entity3\"", "entity3", jsMasterEntity.getName());
	}

}
