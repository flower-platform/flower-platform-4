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
	
	var f = $resource('../ws-dispatcher/users/:op', {}, {
		performLogin: 	{ method: 'POST', params: { op: 'login' } },
		performLogout: 	{ method: 'POST', params: { op: 'logout' } }
	});
	
	var key_currentUser = 'current_user';
	
	/**
	 * Getter/setter for current user cookie.
	 */
	f.currentUser = function(val) {
		if (val !== undefined) {
			logger.debug('set current user');
			if (val == null) {
				$.removeCookie(key_currentUser);
			} else {
				$.cookie(key_currentUser, val);
			}
		} else {
			logger.debug('get current user');
			return $.cookie(key_currentUser);
		}
	};
	
	return f;

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