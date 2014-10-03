// This is where we talk to the REST service.

'use strict';

var flowerServices = angular.module('flowerServices', ['ngResource']);

logger.debug('Initialize services: core');

/**
 * Template service. May be decorated by other plugins to add their own templates.
 */
flowerServices.factory('Template', [function() {
	
	return {
		sideMenuContent: 'partials/sideMenuContent.html'
	}
	
}]);