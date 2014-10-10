// This is the middle-man between the views and the services.

'use strict';

logger.debug('Initialize controllers: core');

var flowerControllers = angular.module('flowerControllers', []);

/**
 * This controller is used to pass templates to composed views.
 */
flowerControllers.controller('ComposedCtrl', ['$scope', 'contentTemplate', 'sideMenuTemplate', 'sideMenuContentTemplate', 'Template', 
	function($scope, contentTemplate, sideMenuTemplate, sideMenuContentTemplate, Template) {
	
		$scope.template_content = { url: Template[contentTemplate] };
	
		$scope.template_sideMenu = { url: Template[sideMenuTemplate] };
		$scope.template_sideMenuContentTemplate = { url: Template[sideMenuContentTemplate] };
		
}]);

