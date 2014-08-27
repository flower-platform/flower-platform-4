// This is the app configuration. Declares the modules and routes.

'use strict';

var flowerProject = angular.module('flowerProject', [ 'ngRoute', 'flowerControllers', 'flowerServices' ]);

flowerProject.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/users', {
		templateUrl : 'partials/composed/sideMenuLayout.html',
		controller : 'ComposedCtrl',
		resolve : {
			contentTemplate : function() {
				return 'userList';
			}, 
			sideMenuContentTemplate : function() {
				return 'userListInfo';
			}
			
		}
	}).when('/users/:id', {
		templateUrl : 'partials/composed/sideMenuLayout.html',
		controller : 'ComposedCtrl',
		resolve : {
			contentTemplate : function() {
				return 'userForm';
			}, 
			sideMenuContentTemplate : function() {
				return 'userListInfo';
			}
		}
//	}).when('/users/:id/:name', {
//		templateUrl : 'partials/composed/sideMenuLayout.html',
//		controller : 'ComposedCtrl',
//		resolve : {
//			contentTemplate : function() {
//				return 'userAccountSettings';
//			}
//		}
	}).when('/users/:id/General', {
		templateUrl : 'partials/composed/sideMenuLayout.html',
		controller : 'ComposedCtrl',
		resolve : {
			contentTemplate : function() {
				return 'userForm';
			},
			sideMenuContentTemplate : function() {
				return 'sideMenuContent';
			}
		}
	}).when('/users/:id/AccountSettings', {
		templateUrl : 'partials/composed/sideMenuLayout.html',
		controller : 'ComposedCtrl',
		resolve : {
			contentTemplate : function() {
				return 'userAccountSettings';
			},
			sideMenuContentTemplate : function() {
				return 'sideMenuContent';
			}
		}
	}).otherwise({
		redirectTo : '/users'
	});
} ]);