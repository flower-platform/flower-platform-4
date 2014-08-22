// This is the app configuration. Declares the modules and routes.

'use strict';

var flowerProject = angular.module('flowerProject', [ 'ngRoute', 'flowerControllers', 'flowerServices' ]);

flowerProject.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/users', {
		templateUrl : 'partials/composed/sideMenuLayout.html',
		controller : 'ComposedCtrl',
		resolve : {
			template : function() {
				return 'userList';
			}
		}
	}).when('/users/:id', {
		templateUrl : 'partials/composed/sideMenuLayout.html',
		controller : 'ComposedCtrl',
		resolve : {
			template : function() {
				return 'userProfile';
			}
		}
	}).otherwise({
		redirectTo : '/users'
	});
} ]);