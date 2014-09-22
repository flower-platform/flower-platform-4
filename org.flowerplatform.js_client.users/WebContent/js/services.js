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
		remove: { method: 'DELETE', params: { id: '@id' } },
	});
	
}]);

flowerProject.lazy.factory('ChangeSettings' , ['$resource', function($resource) {
	var getMessageResult = function(response) {
		var messageResult = response.resource.messageResult;
		return messageResult;
	};
	
	return $resource('http://localhost:8080/org.flowerplatform.host.web_app/ws-dispatcher/users/:id/:path', {}, {
		changeSettings: { method: 'POST',  params: { id: '@id' , path: '@path' } }
	});
}]);

flowerProject.lazy.factory('Login', [function() {
	
	return {
		userID:  encodeURIComponent("fpp:|.users#ID_1960040384"),
		userName: 'John Johnson',
		login: 'John',
		nodeUri: "fpp:|.users#ID_1960040384",
		isAdmin: true,
		repo: 'Repository'
	};
	
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

flowerProject.lazy.service('UserRepositories', function() {
	
	var repositoryDescription = "Here will be a short description of repository.Post no so what deal evil rent by real in. But her ready least set lived spite solid. " +
							   "September how men saw tolerably two behaviour arranging. She offices for highest and replied one venture pasture." +
							   "Applauded no discovery in newspaper allowance am northward." +
							   "Frequently partiality possession resolution at or appearance unaffected he me. ";
	var extensionDescription = "Here will be a short description!";
	
	var extensions = [{ label: 'Git', color: '#ffdaa1', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription },
	                  { label:'Freeplane', color: '#f5ffa1', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription },
	                  { label: 'CodeSync', color: '#feaeff', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription }];
	var repositories = [{ name: 'FlowerPlatform', extensions: [{ label: 'Git', color: '#ffdaa1', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription }, 
	                                                           { label: 'CodeSync', color: '#feaeff', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription }],
	                      description: repositoryDescription},
	                    { name: 'FreeplaneRepo', extensions: [{ label:'Freeplane', color: '#f5ffa1', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription}, 
	                                                          { label: 'CodeSync', color: '#feaeff', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription },
	                                                          { label: 'Git', color: '#ffdaa1', dependencies: [{ label: 'FileSystem', color: '#2eccfa' }], description: extensionDescription }],
	                      description: repositoryDescription}];
	
	return {
		getExtensions: function () {
			return extensions;
		},
		getRepositories: function () {
			return repositories;
		}
	};
});

flowerProject.lazy.service('Repository', function () {
	 var currentRepository = '';

  return {
      getProperty: function () {
          return currentRepository;
      },
      setProperty: function(value) {
    	  currentRepository = encodeURIComponent(value);
      }
  };
});

/**
 * Decorate the Template service from core.
 */
flowerProject.lazy.decorator('Template', function($delegate) {
	logger.debug('decorate');
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
