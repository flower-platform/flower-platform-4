// This is the app configuration.

'use strict';

/**
 * Array of routeConfig objects.
 * 
 * routeConfig = { 
 * 		path: String, 
 * 		deps: Array,
 * 		route: {
 * 			templateUrl: String,
 * 			controller: String,
 * 			resolve: Object
 * 		}
 * }
 */
var routesConfig = [];

// Initialize the app.
var flowerProject = angular.module('flowerProject', [ 'ngRoute', 'flowerControllers', 'flowerServices' ]);

// Load plugins. Manually bootstrap the app after plugins are loaded.
loadScripts(plugins, function() {
	logger.debug('bootstrap');
	angular.bootstrap(document, ['flowerProject']);
});

flowerProject.config(['$routeProvider', '$provide', '$controllerProvider', function($routeProvider, $provide, $controllerProvider) {

	logger.debug('do config');
	
	// Expose lazy loading for plugins.
	flowerProject.lazy = {
		controller: $controllerProvider.register,
        factory: 	$provide.factory,
        service: 	$provide.service,
        decorator: 	$provide.decorator
	};
	
	var loadRouteDependencies = function($q, $rootScope, routeConfig) {
		logger.debug('resolve route' + routeConfig.path);
		var deferred = $q.defer();
		loadScripts(routeConfig.deps, function() {
			deferred.resolve();
			logger.debug('resolved');
		});
		return deferred.promise;
	};
	
	// Register routes from loaded plugins.
	angular.forEach(routesConfig, function(routeConfig) {
		// inject dependencies from config
		// this returns a promise, so the route will be resolved AFTER the deps are loaded
		routeConfig.route.resolve.deps = function($q, $rootScope) {
			return loadRouteDependencies($q, $rootScope, routeConfig);
		};
		// add route
		$routeProvider.when(routeConfig.path, routeConfig.route);
		logger.debug('route added' + routeConfig.path);
	});
	
}]);