'use strict';

logger.debug('init user controllers');

flowerProject.lazy.controller('UserSideMenuCtrl', ['$scope', '$location', '$route', 'Login', 'SearchType',
  	function($scope, $location, $route, Login, SearchType) {
  	
  		// Side Menu
  		$scope.currentPath = $location.path();
  		
  		// set type of search for repositories
  		$scope.setSearchType = function(type) {
  			SearchType. setSearchType(type);
  		}
  		
  		$scope.userID = Login.userID;
  		$scope.nodeUri = Login.userID;
  		$scope.userName = Login.userName;
  		$scope.isAdmin = Login.isAdmin;
  		$scope.content = $route.current.scope.template_sideMenuContentTemplate.url;
  	
  }]);

flowerProject.lazy.controller('UserListCtrl', ['$scope', '$location', 'User', 'Template', 
	function($scope, $location, User, Template) {
	
		$scope.users = User.query();
	
}]);

flowerProject.lazy.controller('UserFormCtrl', ['$scope', '$routeParams', '$location', '$http', 'User', 'ChangeSettings', 'Login', 'UserNodeUri', 
     function($scope, $routeParams, $location, $http, User, ChangeSettings, Login, UserNodeUri) {
	
		/**
		 * Get the user from the server, or create new user for this $scope.
		 */
		$scope.nodeUri = $routeParams.id;
		$scope.user = $routeParams.id == 'new' ? new User() : User.get({ id: $scope.nodeUri });
		UserNodeUri.setProperty($scope.nodeUri);
		
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
		
		
		
}]);

flowerProject.lazy.controller('UserAccountSettingsCtrl', ['$scope', '$routeParams', '$location', '$http', 'ChangeSettings', 'Login', 'UserNodeUri', 'User',
      function($scope, $routeParams, $location, $http, ChangeSettings, Login , UserNodeUri, User) {
	
	$scope.userNodeUri = UserNodeUri.getProperty();
	$scope.user = User.get({ id: decodeURIComponent($scope.userNodeUri)});
	
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
	
}]);

flowerProject.lazy.controller('NavigationCtrl', ['$scope', '$location','UserNodeUri', 
       function($scope, $location, UserNodeUri) {
		
	   $scope.currentRoute = $location.path();
	   $scope.uri = UserNodeUri.getProperty();
	   $scope.decodeUri = decodeURIComponent($scope.uri);
}]);