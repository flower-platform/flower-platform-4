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
  
var AddEntityDiffUpdateProcessor = function() { };
AddEntityDiffUpdateProcessor.prototype.applyDiffUpdate = function(entityRegistry, diffUpdate) {
	var entity = diffUpdate.properties;
	entityRegistry.registerEntity(entity);
	if (diffUpdate.parentUid) {
		var parent = entityRegistry.getEntityByUid(diffUpdate.parentUid);
		parent[diffUpdate.childrenProperty].push(entity); //TODO CM: adugare la o anumita pozitie
	}
};

var RemoveEntityDiffUpdateProcessor = function() { };
RemoveEntityDiffUpdateProcessor.prototype.applyDiffUpdate = function(entityRegistry, diffUpdate) {
	entityRegistry.unregisterEntity(diffUpdate.entityUid);
};

var PropertiesDiffUpdateProcessor = function() { };
PropertiesDiffUpdateProcessor.prototype.applyDiffUpdate = function(entityRegistry, diffUpdate) {

};
