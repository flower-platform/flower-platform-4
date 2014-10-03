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
		if (oauth) {
			// oauth with password grant (i.e. trade credentials for access token)
			var auth = 'grant_type=password';
			auth += '&client_id=' + 'testclientid';
			auth += '&username=' + loginInfo.username;
			auth += '&password=' + loginInfo.password;
			logger.debug('Auth login with password grant:' + auth);
			
			// post to token endpoint; use jquery because angular does not easily support form-urlencoded format
			$.post('../oauth/token', auth).done(function(data) {
				window.location.href = '../js_client.users/authAccess.html#access_token=' + data.access_token;
			}).fail(function(xhr) {
				// $apply to trigger angular binding to alert
				$scope.$apply(function() {
					var errorDescription = JSON.parse(xhr.responseText).error_description;
					logger.debug('Auth error: ' + errorDescription);
					$scope.alert.message = errorDescription;
					$scope.alert.visible = true;
					$scope.alert.danger = true;
				});
			});
		} else {
			// basic auth
			logger.debug('Auth login with password: ');
			logger.debug(loginInfo);
			loginInfo['@class'] = 'java.util.HashMap';
			Auth.login(loginInfo).$promise.then(Auth.loginSuccessHandler, function(error) {
				logger.debug('Auth error: ' + error.data.messageResult);
				$scope.alert.message = error.statusText + ': ' + error.data.messageResult;
				$scope.alert.visible = true;
				$scope.alert.danger = true;
			});
		}
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
	 * 
	 */
	Auth.currentUser().$promise.then(function(user) {
		logger.debug('Auth link status:');
		logger.debug(user.messageResult);
		if (user.messageResult != null) {
			$scope.registerInfo.username = user.messageResult.properties.login;
			$scope.registerInfo.name = user.messageResult.properties.name;
			$scope.registerInfo.email = user.messageResult.properties.email;
			$scope.registerInfo.socialAccounts = user.messageResult.properties.socialAccounts;
		} else {
			$scope.alert.message = 'Could not obtain social account information.';
			$scope.alert.visible = true;
			$scope.alert.danger = true;
		}
	});
	
}]);