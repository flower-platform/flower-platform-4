'use strict';

logger.debug('init user controllers');

flowerProject.lazy.controller('UserSideMenuCtrl', ['$scope', '$location', '$route', 'Auth', 
  	function($scope, $location, $route, Auth) {
  	
  		$scope.currentPath = $location.path();
  		$scope.content = $route.current.scope.template_sideMenuContentTemplate.url;
  		
  		$scope.currentUser = Auth.currentUser();
  		
//  		$scope.logout = function() {
//  			Auth.performLogout().$promise.then(function() {
//  				// success on logout => clear current user
//  				logger.debug('logout ' + $scope.currentUser);
//  				Auth.currentUser(null);
//  				$route.reload();
//  			});
//  		};
  	
}]);

flowerProject.lazy.controller('AuthCtrl',  ['$scope', '$location', 'oauthProviders', 'Auth',
	function($scope, $location, oauthProviders, Auth) {
	
		var loginSuccessHandler = function(user) {
			callFlexCallback('loginSuccessHandler');
			if (window.opener != null) {
				// close the popup, reload parent
				window.opener.document.location.reload();
				self.close();
			} else {
				$location.path("/");
			}
		}
		
		
		logger.debug('get user');
		// there is a user logged in => redirect to main
		Auth.currentUser().$promise.then(function(user) {
			logger.debug('got user');
			if (user.messageResult != null) {
				logger.debug(user.messageResult);
				loginSuccessHandler(user.messageResult.properties.login);
			}
		}); 
	
		$scope.oauthProviders = oauthProviders.data.messageResult.providers;
		
		// bound to form view
		$scope.loginInfo = {};
	
		$scope.performLogin = function(provider) {
			logger.debug(provider);
			if (provider != undefined) {
				var auth = provider.uri;
				auth += '?client_id=' + provider.clientId;
				auth += '&scope=' + provider.scope;
				auth += '&state=' + oauthProviders.data.messageResult.state;
				logger.debug(auth);
				var popup = window.open(auth, 'name', 'width=1100,height=600');
				if (window.focus) {
					popup.focus();
				}
			} else {
				Auth.performLogin($scope.loginInfo).$promise.then(loginSuccessHandler);
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