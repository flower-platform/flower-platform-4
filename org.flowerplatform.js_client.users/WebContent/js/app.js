'use strict';

logger.debug('load users');

routesConfig.push({
	path: '/users',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/users.js', 'js_client.users/js/controllers/repository.js'],
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
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/users.js', 'js_client.users/js/controllers/repository.js'],
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
	path: '/users/:id/AccountSettings',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/users.js', 'js_client.users/js/controllers/repository.js'],
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
	path: '/repository/:id',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/repository.js', 'js_client.users/js/controllers/users.js'],
	route: {
		templateUrl: 'partials/composed/sideMenuLayout.html',
		controller: 'ComposedCtrl',
		resolve: {
			contentTemplate : function() {
				return 'userDashboard';
			},
			sideMenuTemplate : function() {
				return 'userSideMenu';
			},
			sideMenuContentTemplate : function() {
				return 'userRepositorySideMenuContent';
			}
		}
	}
});

routesConfig.push({
	path: '/repository/:id/search',
	deps: ['js_client.users/js/services.js', 'js_client.users/js/controllers/repository.js', 'js_client.users/js/controllers/users.js'],
	route: {
		templateUrl: 'partials/composed/sideMenuLayout.html',
		controller: 'ComposedCtrl',
		resolve: {
			contentTemplate : function() {
				return 'searchRepository';
			},
			sideMenuTemplate : function() {
				return 'userSideMenu';
			},
			sideMenuContentTemplate : function() {
				return 'userRepositorySideMenuContent';
			}
		}
	}
});


