'use strict';

logger.debug('init directives');

flowerProject.directive('alert',function() {
	return {
	      restrict: 'E',
	      scope: {
	         alertMessage: '=info'
	        },
	      templateUrl: '../js_client.users/partials/alertMessage.html'
	    };
});

flowerProject.directive('focusMe', function($timeout) {
		return {
			scope: {trigger: '@focusMe'},
			link: function(scope,element) {
					scope.$watch('trigger', function(value) {
						if(value === "true") { 
							element[0].focus(); 
							scope.trigger = false;
						}
					});
				}
		};
});