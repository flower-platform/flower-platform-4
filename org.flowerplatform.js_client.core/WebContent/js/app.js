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

/**
 * Default route. The last plugin to set it wins.
 */
var defaultRoute = null;

// Initialize the app.
var flowerProject = angular.module('flowerProject', [ 'ngRoute', 'flowerControllers', 'flowerServices' ]);

// Load plugins. Manually bootstrap the app after plugins are loaded.
loadScripts(plugins, function() {
	logger.debug('Bootstrap application');
	angular.bootstrap(document, ['flowerProject']);
});

flowerProject.config(['$routeProvider', '$provide', '$controllerProvider', '$compileProvider', '$httpProvider', function($routeProvider, $provide, $controllerProvider, $compileProvider, $httpProvider) {

	logger.debug('Configure application');
	
	// Expose lazy loading for plugins.
	flowerProject.lazy = {
		controller: $controllerProvider.register,
        factory: 	$provide.factory,
        service: 	$provide.service,
        decorator: 	$provide.decorator,
        directive:  $compileProvider.directive
	};
	
	// Add jsessionid header for the first API call
	$provide.factory('flowerHttpInterceptor', function($q) {
		return {
			'request' : function(config) {
				if (config.url.indexOf('../ws-dispatcher') != 0) {
					// not an API call
					return config;
				}
				var jsessionid = $.cookie('jsessionid');
				if (jsessionid != null) {
					// first call => clear cookie and add header
					$.removeCookie('jsessionid');
					logger.debug('get from cookie ' + jsessionid);
					config.headers.JSESSIONID = jsessionid;
				}
				return config;
			}
		} 
	});
	
	$httpProvider.interceptors.push('flowerHttpInterceptor');
	 
	var syncCookies = function($q) {
		var deferred = $q.defer();
		logger.debug('sync cookies');
		var map = callFlexCallback('syncCookies', function(map) {
			logger.debug(map);
			if (map != undefined) {
				$.each(map, function(key, value) {
					logger.debug('set cookie ' + key + value);
					$.cookie(key, value);
				});
			}
			deferred.resolve(0);
		});
		return deferred.promise;
	}
	
	var loadRouteDependencies = function($q, $rootScope, routeConfig) {
		logger.debug('Resolving route: ' + routeConfig.path);
		var deferred = $q.defer();
		loadScripts(routeConfig.deps, function($rootScope) {
			deferred.resolve(0);
			logger.debug('Route resolved: ' + routeConfig.path);
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
//		routeConfig.route.resolve.syncCookies = function($q) {
//			return syncCookies($q);
//		};
		// add route
		$routeProvider.when(routeConfig.path, routeConfig.route);
		logger.debug('Route added: ' + routeConfig.path);
	});
	
	if (defaultRoute != null) {
		logger.debug('Set default route: ' + defaultRoute);
		$routeProvider.otherwise({ redirectTo: defaultRoute });
	}
}]);