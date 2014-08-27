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
		remove: { method: 'DELETE', params: { id: '@id' } }
	});
	
}]);

flowerServices.factory('Login', [function() {
	return {
		userID: 'user:test|John',
		userName: 'John Johnson',
		login: 'John',
		nodeUri: 'user:test|John',
		isAdmin: true,
		repo: 'Repository'
	}
}]); 

flowerServices.factory('Template', [function() {
	
	return {
		userList: 'partials/userList.html',
		userListInfo: 'partials/userListInfo.html',
		userForm: 'partials/userForm.html',
		userProfile: 'partials/userProfile.html',
		userAccountSettings: 'partials/userAccountSettings.html',
		sideMenuContent: 'partials/sideMenuContent.html'
	}
	
}]);