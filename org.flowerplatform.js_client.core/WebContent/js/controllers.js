// This is the middle-man between the views and the services.

'use strict';

var flowerControllers = angular.module('flowerControllers', []);

flowerControllers.controller('ComposedCtrl', ['$scope', 'template', 'Template', function($scope, template, Template) {
	
	// side menu layout
	$scope.template_content = { url: Template[template] };
	$scope.template_sideMenu = { url: 'partials/sideMenu.html' };
	
	// vertical layout
	$scope.templates = [ $scope.template_content, $scope.template_sideMenu ];
	
}]);

flowerControllers.controller('SideMenuCtrl', ['$scope', '$location', function($scope, $location) {
	
	// Side Menu
	
}]);

flowerControllers.controller('UserListCtrl', ['$scope', '$location', 'User', 'Template', function($scope, $location, User, Template) {
	
	$scope.users = User.query();
	
}]);

flowerControllers.controller('UserFormCtrl', ['$scope', '$routeParams', '$location', '$http', 'User', 
     function($scope, $routeParams, $location, $http, User) {

 		/**
 		 * Get the user from the server, or create new user for this $scope.
 		 */
 		$scope.user = $routeParams.id == 'new' ? new User() : User.get({ id: $routeParams.id });
 		
 		$scope.save = function() {
 			if ($scope.user.password == null) {
 				// set password for new user
 				$scope.user.password = 'user';
 			}
 			User.save($scope.user).$promise.then(function(result) {
 				$scope.alert = {
 					message: 'User information for ' + result.firstName + ' ' + result.lastName + ' has been successfully updated.',
 					visible: true,
 					success: true
 				};
 			}, function(error) {
 				$scope.alert = {
 					message: 'Server error. Please try again.',
 					visible: true,
 					danger: true
 				};
 			});
 		};
 		
 		/**
 		 * Go to users list.
 		 */
 		$scope.cancel = function() {
 			$location.path('/users');
 		}
 	
 }]);