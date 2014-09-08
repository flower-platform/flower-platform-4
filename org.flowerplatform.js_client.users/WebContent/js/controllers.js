'use strict';

logger.debug('init user controllers');

flowerProject.lazy.controller('UserSideMenuCtrl', ['$scope', '$location', '$route', 'Login', 
  	function($scope, $location, $route, Login) {
  	
  		// Side Menu
  		$scope.currentPath = $location.path();
  		
  		$scope.userID = Login.userID;
  		$scope.userName = Login.userName;
  		$scope.isAdmin = Login.isAdmin;
  		$scope.repo = Login.repo;
  		$scope.content = $route.current.scope.template_sideMenuContentTemplate.url;
  	
  }]);

flowerProject.lazy.controller('UserListCtrl', ['$scope', '$location', 'User', 'Template', 
	function($scope, $location, User, Template) {
	
		$scope.users = User.query();
	
}]);

flowerProject.lazy.controller('UserFormCtrl', ['$scope', '$routeParams', '$location', '$http', 'User', 'ChangeSettings',
     function($scope, $routeParams, $location, $http, User, ChangeSettings) {
		/**
		 * Get the user from the server, or create new user for this $scope.
		 */
		$scope.user = $routeParams.id == 'new' ? new User() : User.get({ id: $routeParams.id });
		
		$scope.save = function() {
			User.save($scope.user.messageResult).$promise.then(function(result) {
				$scope.alert = {
					message: 'User information for ' + result.messageResult.properties.firstName + ' ' + result.messageResult.properties.lastName + ' has been successfully updated.',
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
		
		/**
		 * Delete user
		 */
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
		
		/**
		 * Change password
		 */
		$scope.changePassword = function(oldPassword, newPassword) {
			ChangeSettings.changeSettings({ id : $scope.user.messageResult.nodeUri, path : "password"},
					{'oldPassword' : oldPassword, 'newPassword' : newPassword }).$promise.then(function(result) {
 				$scope.alert = {
 					message: result.messageResult,
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
		 * Change login
		 */
		$scope.changeLogin = function() {
			ChangeSettings.changeSettings({ id : $scope.user.messageResult.nodeUri, path : "login" }, $scope.user.messageResult.properties.login).$promise.then(function(result) {
 				$scope.alert = {
 					message: 'User information for ' + result.messageResult.properties.firstName + ' ' + result.messageResult.properties.lastName + ' has been successfully updated.',
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
