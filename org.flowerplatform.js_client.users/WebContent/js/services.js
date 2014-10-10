'use strict';

logger.debug('Initialize services: users');

flowerProject.lazy.factory('Auth', ['$resource', '$location', 
function($resource, $location) {
	
	var service = $resource('../ws-dispatcher/users/:op', {}, {
		currentUser: { method: 'GET',  params: { op: 'login' } },
		login:		 { method: 'POST', params: { op: 'login' } },
		logout:		 { method: 'POST', params: { op: 'logout' } },
		register:	 { method: 'POST', params: { op: 'register' } },
		
		socialAccountInfo: { method: 'GET', params: { op: 'socialAccountInfo' } }
	});
	
	var oauthService = $resource('../ws-dispatcher/oauth/token', {} , {
		token: { method: 'POST' }
	});
	
	service.loginSuccessHandler = function(user) {
		logger.debug('Login successful for user:');
		logger.debug(user);
		
		callFlexCallback('loginSuccessHandler');
		
		if (oauth) {
			oauthService.token(getClientId()).$promise.then(function(token) {
				logger.debug('Got token: ' + token.messageResult);
				setAccessToken(token.messageResult);
			});
		} else {
			var url = window.location.href;
			var index = url.indexOf('return_to');
			logger.debug(url);
			if (index > 0) {
				// return to page after auth
				url = url.slice(index + 10);
				url = decodeURIComponent(url);
				logger.debug('Return to: ' + url);
				window.location.href = "../" + url;
			} else {
				if (window.opener != null) {
					// close the popup, reload parent
					window.opener.document.location.reload();
					self.close();
				} else {
					$location.path("/");
				}
			}
		}
	}
	
	return service;
}]);

flowerProject.lazy.factory('OAuth', ['$resource', function($resource) {
	
	return $resource('../ws-dispatcher/oauth/token', {}, {
		token: { method: 'POST' }
	});
	
}]);

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

flowerProject.lazy.factory('ChangeSettings' , ['$resource', function($resource) {
	
	return $resource('http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/users/:id/:path', {}, {
		changeSettings: { method: 'POST',  params: { id: '@id' , path: '@path' } }
	});
}]);

flowerProject.lazy.service('UserNodeUri', function () {
	 var userNodeUri = '';

   return {
       getProperty: function () {
           return userNodeUri;
       },
       setProperty: function(value) {
      	 userNodeUri = encodeURIComponent(value);
       }
   };
});

//Repository service
flowerProject.lazy.factory('Repository', ['$resource', function($resource) {
	
	return $resource('http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/repository/:id/:path', {}, {
		get:	{ method: 'GET', params: { id: '@id' } },
	    getAction: { method: 'GET', params: { id: '@id', path: '@path'} },
		getAllExtensions: { method: 'GET', params: { id: '', path: '@path' } },
		action:	{ method: 'POST', params: { id: '', path: '@path' } }
		
	});
}]);

flowerProject.lazy.service('SearchType', function () {
	 var searchType = '';

  return {
      getSearchType: function () {
          return searchType;
      },
      setSearchType: function(value) {
     	 searchType = encodeURIComponent(value);
      }
  };
});

/**
 * Decorate the Template service from core.
 */
flowerProject.lazy.decorator('Template', function($delegate) {
	$delegate.userSideMenu 		  = '../js_client.users/partials/composed/userSideMenu.html';
	
	$delegate.userList 			  = '../js_client.users/partials/userList.html';
	$delegate.userForm 			  = '../js_client.users/partials/userForm.html';
	$delegate.userAccountSettings = '../js_client.users/partials/userAccountSettings.html';
	$delegate.userDashboard 	  = '../js_client.users/partials/userDashboard.html';
	$delegate.searchRepository 	  = '../js_client.users/partials/searchRepository.html';
	
	$delegate.userListSideMenuContent = '../js_client.users/partials/userListSideMenuContent.html';
	$delegate.userFormSideMenuContent = '../js_client.users/partials/userFormSideMenuContent.html';
	$delegate.userRepositorySideMenuContent = '../js_client.users/partials/userRepositorySideMenuContent.html';
	
	return $delegate;
});