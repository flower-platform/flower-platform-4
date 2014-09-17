'use strict';

logger.debug('init user controllers');

flowerProject.lazy.controller('UserSideMenuCtrl', ['$scope', '$location', '$route', 'Auth', 
  	function($scope, $location, $route, Auth) {
  	
  		$scope.currentPath = $location.path();
  		$scope.content = $route.current.scope.template_sideMenuContentTemplate.url;
  		
  		$scope.currentUser = Auth.currentUser();
  		
  		$scope.logout = function() {
  			Auth.performLogout().$promise.then(function() {
  				// success on logout => clear current user
  				logger.debug('logout ' + $scope.currentUser);
  				Auth.currentUser(null);
  				$route.reload();
  			});
  		};
  	
}]);

flowerProject.lazy.controller('AuthCtrl',  ['$scope', '$location', 'Auth',
	function($scope, $location, Auth) {
	
		var redirectToMain = function() {
			$location.path("/");
		}
	
		// there is a user logged in => redirect to main
		if (Auth.currentUser() != null) {
			redirectToMain();
		}
	
		var keepLogin = function(user) {
			logger.debug('login ' + user.messageResult.properties.login);
			Auth.currentUser(user.messageResult.properties.login);
			redirectToMain();
		};
	
		var redirectUri = 'http://localhost:8080/org.flowerplatform.host.web_app/oauth/redirect';
		
		var providers = {
			'github': {
				uri		  : 'https://github.com/login/oauth/authorize',
				client_id : 'a4101c760942cd94905f',
				scope	  : 'user'
			}
		}
		
		// bound to form view
		$scope.loginInfo = {};
	
		$scope.performLogin = function(provider) {
			logger.debug(provider);
			if (provider != undefined) {
				var auth = providers[provider].uri;
				auth += '?client_id=' + providers[provider].client_id;
				auth += '&scope=' + providers[provider].scope;
				auth += '&redirect_uri=' + redirectUri + '?provider=' + provider;
				logger.debug(auth);
				document.location.href = auth;
			} else {
				Auth.performLogin($scope.loginInfo).$promise.then(keepLogin);
			}
		}
 	
}]);

flowerProject.lazy.controller('UserListCtrl', ['$scope', '$location', 'User', 'Template', 
   	function($scope, $location, User, Template) {
   	
  		$scope.users = User.query();
   	
}]);

flowerProject.lazy.controller('UserFormCtrl', ['$scope', '$routeParams', '$location', '$http', 'User', 
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

flowerProject.lazy.controller('UserAccountSettingsCtrl',  ['$scope', '$routeParams', 'User', 'Auth',
	function($scope, $routeParams, User, Auth) {
	
		/**
		 * Get the user from the server, or create new user for this $scope.
		 */
		$scope.login = Auth.login;
		$scope.changePassword = function(oldPassword, newPassword) {
			var user = { nodeUri : Auth.nodeUri , 
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
 			//var user = {nodeUri : Auth.nodeUri};
 			User.remove({ id: Auth.nodeUri }).$promise.then(function(result) {
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