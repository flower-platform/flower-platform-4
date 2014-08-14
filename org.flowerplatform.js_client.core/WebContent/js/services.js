// This is where we talk to the REST service.

'use strict';

var flowerServices = angular.module('flowerServices', ['ngResource']);

flowerServices.factory('User', ['$resource', function($resource) {
	
	var getMessageResult = function(response) {
		var messageResult = response.resource.messageResult;
		return messageResult;
	};
	
	// get model from service
	return $resource('http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/users/:id', {}, {
		query: 	{ method: 'GET'/*, interceptor: { 'response': getMessageResult }*/ },
		get:	{ method: 'GET', params: { id: '@id' } },
		save:	{ method: 'POST' },
		remove: { method: 'DELETE' }
	});
	
}]);

flowerServices.factory('Template', [function() {
	
	return {
		userList: 'partials/userList.html',
		userForm: 'partials/userForm.html'
	}
	
}]);