'use strict';

logger.debug('load users');

var authInfo = {
	client_id: '',
	client_secret: '',
	access_token: ''
}

routesConfig.push({
	path: '/users',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers.js'],
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

routesConfig.push({
	path: '/users/:id',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers.js'],
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

routesConfig.push({
	path: '/users/:id/settings',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers.js'],
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

routesConfig.push({
	path: '/auth',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers.js'],
	route: {
		templateUrl: '../js_client.users/partials/auth.html',
		controller: 'AuthCtrl',
		resolve: {
			oauthProviders: function($http) {
				return $http.get('../ws-dispatcher/oauthProviders');
			}
		}
	}
});