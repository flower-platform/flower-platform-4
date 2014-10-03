'use strict';

logger.debug('Load module: users');

var authInfo = {
	client_id: '',
	client_secret: '',
	access_token: ''
}

defaultRoute = '/users';

///////////////////////////////////////////////////////
// User list
///////////////////////////////////////////////////////

routesConfig.push({
	path: '/users',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/users.js'],
	route: {
		templateUrl: 'partials/composed/sideMenuLayout.html',
		controller: 'ComposedCtrl',
		resolve: {
			contentTemplate : function() {
				return 'userList';
			},
			sideMenuTemplate : function() {
				return 'userSideMenu';
			},
			sideMenuContentTemplate : function() {
				return 'userListSideMenuContent';
			}
		}
	}
});

///////////////////////////////////////////////////////
// User form
///////////////////////////////////////////////////////

routesConfig.push({
	path: '/users/:id',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/users.js'],
	route: {
		templateUrl: 'partials/composed/sideMenuLayout.html',
		controller: 'ComposedCtrl',
		resolve: {
			contentTemplate : function() {
				return 'userForm';
			},
			sideMenuTemplate : function() {
				return 'userSideMenu';
			},
			sideMenuContentTemplate : function() {
				return 'userFormSideMenuContent';
			}
		}
	}
});

///////////////////////////////////////////////////////
// User account settings
///////////////////////////////////////////////////////

routesConfig.push({
	path: '/users/:id/settings',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/users.js'],
	route: {
		templateUrl: 'partials/composed/sideMenuLayout.html',
		controller: 'ComposedCtrl',
		resolve: {
			contentTemplate : function() {
				return 'userAccountSettings';
			},
			sideMenuTemplate : function() {
				return 'userSideMenu';
			},
			sideMenuContentTemplate : function() {
				return 'userFormSideMenuContent';
			}
		}
	}
});

///////////////////////////////////////////////////////
// Login + sign in with oauth provider
///////////////////////////////////////////////////////

routesConfig.push({
	path: '/auth',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/auth.js'],
	route: {
		templateUrl: '../js_client.users/partials/auth/auth.html',
		controller: 'AuthCtrl',
		resolve: {
			oauthProviders: function($http) {
				return $http.get('../ws-dispatcher/oauthProviders', { params: { embed: embeddingClientId } });
			}
		}
	}
});

///////////////////////////////////////////////////////
// Link social account
///////////////////////////////////////////////////////

routesConfig.push({
	path: '/link',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/auth.js'],
	route: {
		templateUrl: '../js_client.users/partials/auth/link.html',
		controller: 'LinkCtrl',
		resolve: {}
	}
});