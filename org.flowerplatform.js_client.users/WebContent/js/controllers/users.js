'use strict';

logger.debug('Initialize controllers: users');

///////////////////////////////////////////////////////
// User side menu controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('UserSideMenuCtrl', ['$scope', '$location', '$route', 'Auth', 
function($scope, $location, $route, Auth) {

	$scope.currentPath = $location.path();
  	$scope.content = $route.current.scope.template_sideMenuContentTemplate.url;

  	$scope.currentUser = Auth.currentUser();

  	$scope.logout = function() {
  		Auth.logout().$promise.then(function() {
  			// success on logout => clear current user
  			logger.debug('Auth logout:');
  			Auth.currentUser(null);
  			$route.reload();
  		});
  	};
  	
}]);

///////////////////////////////////////////////////////
// User list controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('UserListCtrl', ['$scope', 'User', function($scope, User) {
	
	$scope.users = User.query();
	
}]);

///////////////////////////////////////////////////////
// User form controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('UserFormCtrl', ['$scope', '$routeParams', '$location', 'User', 'UserNodeUri', 
function($scope, $routeParams, $location, User, UserNodeUri) {
	
	/**
	 * Get the user from the server, or create a new user for this $scope.
	 */
	$scope.nodeUri = $routeParams.id;
	$scope.user = $routeParams.id == 'new' ? new User() : User.get({ id: $scope.nodeUri });
	UserNodeUri.setProperty($scope.nodeUri);

	/**
	 * Save.
	 */
	$scope.save = function() {
		$scope.user.messageResult['@class'] = 'org.flowerplatform.core.node.remote.Node';
		$scope.user.messageResult.properties['@class'] = 'java.util.HashMap';
		User.save($scope.user.messageResult).$promise.then(function(result) {
			$scope.alert = {
				message: 'User information for ' + result.messageResult.properties.firstName + ' ' + result.messageResult.properties.lastName + ' has been successfully updated.',
				visible: true,
				success: true
			};
		}, function(error) {
			$scope.alert = {
				message: error.statusText + ": " + error.data.messageResult,
				visible: true,
				danger: true
			};
		});
	};

	/**
	 * Delete user.
	 */
	$scope.remove = function() {
		User.remove({
			id: $scope.user.messageResult.nodeUri
		}).$promise.then(function(result) {
			$scope.alert = {
				message : 'User was deleted.',
				visible : true,
				success : true
			};
		}, function(error) {
			$scope.alert = {
				message : 'Server error. Please try again.',
				visible : true,
				danger : true
			};
		});
	};
	
	/**
	 * Back to user list.
	 */
	$scope.cancel = function() {
		$location.path('/users');
	}
	
}]);

///////////////////////////////////////////////////////
// User account settings controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('UserAccountSettingsCtrl', ['$scope', 'ChangeSettings', 'UserNodeUri', 'User',
function($scope, ChangeSettings, UserNodeUri, User) {

	$scope.userNodeUri = UserNodeUri.getProperty();
	$scope.user = User.get({ id: decodeURIComponent($scope.userNodeUri)});

	/**
	 * Change password.
	 */
	$scope.changePassword = function(oldPassword, newPassword) {
		ChangeSettings.changeSettings({
			id: $scope.user.messageResult.nodeUri,
			path: "password"
		}, {
			'oldPassword': oldPassword,
			'newPassword': newPassword
		}).$promise.then(function(result) {
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
	 * Change login.
	 */
	$scope.changeLogin = function() {
		ChangeSettings.changeSettings({
			id : $scope.user.messageResult.nodeUri,
			path : "login" 
		}, $scope.user.messageResult.properties.login).$promise.then(function(result) {
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
	 * Delete user.
	 */
	$scope.remove = function() {
		User.remove({
			id: $scope.user.messageResult.nodeUri 
		}).$promise.then(function(result) {
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

///////////////////////////////////////////////////////
// Navigation controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('NavigationCtrl', ['$scope', '$location','UserNodeUri', function($scope, $location, UserNodeUri) {
	
	$scope.currentRoute = $location.path();
	$scope.uri = UserNodeUri.getProperty();
	$scope.decodeUri = decodeURIComponent($scope.uri);
	
}]);