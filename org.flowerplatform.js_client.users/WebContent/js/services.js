'use strict';

logger.debug('init user services');

flowerProject.lazy.factory('User', ['$resource', function($resource) {
	
	var getMessageResult = function(response) {
		var messageResult = response.resource.messageResult;
		return messageResult;
	};
	
	// get model from service
	return $resource('../ws-dispatcher/users/:id', {}, {
		query: 	{ method: 'GET'/*, interceptor: { 'response': getMessageResult }*/ },
		get:	{ method: 'GET', params: { id: '@id' } },
		save:	{ method: 'POST' },
		remove: { method: 'DELETE', params: { id: '@id' } }
	});
	
}]);

flowerProject.lazy.factory('Auth', ['$resource', function($resource) {
	
	return $resource('../ws-dispatcher/users/:op', {}, {
		currentUser:	{ method: 'GET', params: {op: 'login' } },
		performLogin: 	{ method: 'POST', params: { op: 'login' } },
		performLogout: 	{ method: 'POST', params: { op: 'logout' } }
	});
	
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