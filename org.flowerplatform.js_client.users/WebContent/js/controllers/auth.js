'use strict';

logger.debug('Initialize controllers: auth');

///////////////////////////////////////////////////////
// Auth controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('LoginCtrl',  ['$scope', 'Auth', function($scope, Auth) {

	/**
	 * Callback from view.
	 */
	$scope.login = function(loginInfo) {
		logger.debug('Auth login with password: ');
		logger.debug(loginInfo);
		loginInfo['@class'] = 'java.util.HashMap';
		Auth.login(loginInfo).$promise.then(Auth.loginSuccessHandler, function(error) {
			logger.debug('Auth error: ' + error.data.messageResult);
			$scope.alert.message = error.statusText + ': ' + error.data.messageResult;
			$scope.alert.visible = true;
			$scope.alert.danger = true;
		});
	};
	
}]);

///////////////////////////////////////////////////////
// Register controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('RegisterCtrl', ['$scope', 'Auth', function($scope, Auth) {
	
	/**
	 * Callback from view.
	 */
	$scope.register = function(registerInfo) {
		logger.debug('register');
		logger.debug(registerInfo);
		registerInfo['@class'] = 'java.util.HashMap';
		Auth.register(registerInfo).$promise.then(Auth.loginSuccessHandler, function(error) {
			logger.debug('Auth error: ' + error.data.messageResult);
			$scope.alert.message = error.statusText + ': ' + error.data.messageResult;
			$scope.alert.visible = true;
			$scope.alert.danger = true;
		});
	}
	
}]);

///////////////////////////////////////////////////////
// Login + sign in with oauth provider controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('AuthCtrl', ['$scope', 'oauthProviders', 'Auth', function($scope, oauthProviders, Auth) {
	
	$scope.alert = {
		visible: false
	};
	
	/**
	 * Check if user logged in.
	 */
	Auth.currentUser().$promise.then(function(user) {
		logger.debug('Auth login status:');
		logger.debug(user.messageResult);
		if (user.messageResult != null) {
			Auth.loginSuccessHandler(user.messageResult);
		}
	});
	
	$scope.oauthProviders = oauthProviders.data.messageResult.providers;
	delete $scope.oauthProviders['@class'];
	
	$scope.signInWithProvider = function(provider) {
		var auth = provider.uri;
		auth += '?client_id=' + provider.clientId;
		auth += '&scope=' + provider.scope;
		auth += '&response_type=code';
		auth += '&state=' + oauthProviders.data.messageResult.state;
		logger.debug('Auth sign in with ' + provider + ': ' + auth);
		if (oauthPopup) {
			var popup = window.open(auth, 'name', 'width=1100,height=600');
			if (window.focus) {
				popup.focus();
			}
		} else {
			window.location.href = auth;
		}
	};
	
}]);

///////////////////////////////////////////////////////
// Link social account controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('LinkCtrl',  ['$scope', 'Auth', function($scope, Auth) {
	
	$scope.alert = {
		visible: false
	};
	
	$scope.loginInfo = {};
	$scope.registerInfo = {};
	
	/**
	 * Get social account info to link.
	 */
	Auth.socialAccountInfo().$promise.then(function(socialAccountInfo) {
		logger.debug('Auth link status:');
		logger.debug(socialAccountInfo.messageResult);
		if (socialAccountInfo.messageResult != null) {
			$scope.loginInfo.socialAccount = socialAccountInfo.messageResult.socialAccount;
			
			$scope.registerInfo.login = socialAccountInfo.messageResult.login;
			$scope.registerInfo.name = socialAccountInfo.messageResult.name;
			$scope.registerInfo.email = socialAccountInfo.messageResult.email;
			$scope.registerInfo.socialAccount = socialAccountInfo.messageResult.socialAccount;
		} else {
			$scope.alert.message = 'Could not obtain social account information.';
			$scope.alert.visible = true;
			$scope.alert.danger = true;
		}
	});
	
}]);

///////////////////////////////////////////////////////
// Access token controller
///////////////////////////////////////////////////////

flowerProject.lazy.controller('AccessCtrl', ['$scope', 'OAuth', function($scope, OAuth) {
	
	$scope.token = OAuth.token(getClientId());
	
}]);