'use strict';

logger.debug('init user services');

flowerProject.lazy.factory('User', ['$resource', function($resource) {
	
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

flowerProject.lazy.factory('Login', [function() {
	
	return {
		userID: 'user:test|John',
		userName: 'John Johnson',
		login: 'John',
		nodeUri: 'user:test|John',
		isAdmin: true,
		repo: 'Repository'
	};
	
}]);

/**
 * Decorate the Template service from core.
 */
flowerProject.lazy.decorator('Template', function($delegate) {
	logger.debug('decorate');
	$delegate.userSideMenu 		  = '../js_client.users/partials/composed/userSideMenu.html';
	
	$delegate.userList 			  = '../js_client.users/partials/userList.html';
	$delegate.userForm 			  = '../js_client.users/partials/userForm.html';
	$delegate.userAccountSettings = '../js_client.users/partials/userAccountSettings.html';
	
	$delegate.userListSideMenuContent = '../js_client.users/partials/userListSideMenuContent.html';
	$delegate.userFormSideMenuContent = '../js_client.users/partials/userFormSideMenuContent.html';
	
	return $delegate;
});