// This is the middle-man between the views and the services.

'use strict';

var flowerControllers = angular.module('flowerControllers', []);

flowerControllers.controller('ComposedCtrl', ['$scope', 'contentTemplate', 'sideMenuContentTemplate','Template', function($scope, contentTemplate, sideMenuContentTemplate , Template) {
	
	// side menu layout
	$scope.template_content = { url: Template[contentTemplate] };
	$scope.template_sideMenu = { url: 'partials/sideMenu.html' };
	
	$scope.template_sideMenuContentTemplate = { url: Template[sideMenuContentTemplate] };
	// vertical layout
	$scope.templates = [ $scope.template_content, $scope.template_sideMenu ];
	
}]);

flowerControllers.controller('SideMenuCtrl', ['$scope', '$location', '$route', 'Login', function($scope, $location, $route, Login) {
	// Side Menu
	$scope.currentPath = $location.path();
	
	$scope.userID = Login.userID;
	$scope.userName = Login.userName;
	$scope.isAdmin = Login.isAdmin;
	$scope.repo = Login.repo;
	$scope.content = $route.current.scope.template_sideMenuContentTemplate.url;
	
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
 			User.save($scope.user.messageResult).$promise.then(function(result) {
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
 		
 		$scope.remove = function() {
 			User.remove({ id: $scope.user.messageResult.nodeUri }).$promise.then(function(result) {
 				$scope.alert = {
 					message: 'User was deleted.',
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
 		
 }]);

flowerControllers.controller('UserAccountSettingsCtrl',  ['$scope', '$routeParams', 'User' , 'Login' ,
        function($scope, $routeParams, User, Login) {
	
		/**
		 * Get the user from the server, or create new user for this $scope.
		 */
		$scope.login = Login.login;
		$scope.changePassword = function(oldPassword, newPassword) {
			var user = { nodeUri : Login.nodeUri , 
			properties: { 
				'oldPassword': oldPassword, 
				'newPassword': newPassword
			}};
			User.save(user).$promise.then(function(result) {
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
		
		$scope.changeUsername = function(newlogin) {
			var user = { nodeUri : Login.nodeUri , login : newlogin};
			User.save(user).$promise.then(function(result) {
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
 		 * Delete user
 		 */
 		$scope.deleteAccount = function() {
 			//var user = {nodeUri : Login.nodeUri};
 			User.remove({ id :Login.nodeUri }).$promise.then(function(result) {
 				$scope.alert = {
 					message: 'User was deleted',
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
}]);


