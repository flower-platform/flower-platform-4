// This is the app configuration.

'use strict';

//TODO config plugins?
var plugins = ['js_client.users/js/app.js'];

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

var scriptCache = {};

/**
 * Loads the list of scripts. If all the scripts are
 * loaded successfully, then loadComplete is invoked.
 */
var loadScripts = function(scripts, allScriptsLoadedCallback) {
	var loaded = 0;
	angular.forEach(scripts, function(script) {
		if (scriptCache[script]) {
			loaded = scriptLoadedHandler(loaded, scripts.length, allScriptsLoadedCallback);
		} else {
			$.getScript('../' + script)
			.done(function() {
				console.log('Loaded script: ' + script);
				scriptCache[script] = true;
				loaded = scriptLoadedHandler(loaded, scripts.length, allScriptsLoadedCallback);
			})
			.fail(function(jqxhr, settings, exception) {
				console.log('Failed to load script: ' + script + ' with exception ' + exception);
			});
		}
	});
};

var scriptLoadedHandler = function(loaded, all, allScriptsLoadedCallback) {
	loaded++;
	if (loaded == all) {
		allScriptsLoadedCallback();
	}
	return loaded;
}

// Load plugins. Manually bootstrap the app after plugins are loaded.
loadScripts(plugins, function() {
	console.log('bootstrap');
	angular.bootstrap(document, ['flowerProject']);
});

flowerProject.config(['$routeProvider', '$provide', '$controllerProvider', function($routeProvider, $provide, $controllerProvider) {

	console.log('do config');
	
	// Expose lazy loading for plugins.
	flowerProject.lazy = {
		controller: $controllerProvider.register,
        factory: 	$provide.factory,
        service: 	$provide.service,
        decorator: 	$provide.decorator
	};
	
	var loadRouteDependencies = function($q, $rootScope, routeConfig) {
		console.log('resolve route' + routeConfig.path);
		var deferred = $q.defer();
		loadScripts(routeConfig.deps, function() {
			deferred.resolve();
			console.log('resolved');
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
		console.log('route added' + routeConfig.path);
	});
	
}]);