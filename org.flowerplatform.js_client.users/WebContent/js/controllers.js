'use strict';

logger.debug('init user controllers');

flowerProject.lazy.controller('UserSideMenuCtrl', ['$scope', '$location', '$route', 'Login', 
  	function($scope, $location, $route, Login) {
  	
  		// Side Menu
  		$scope.currentPath = $location.path();
  		
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

flowerProject.lazy.controller('UserDashboardCtrl', ['$scope', '$routeParams', '$location', 'UserRepositories', 'Login', 'Repository',
       function($scope, $routeParams, $location, UserRepositories, Login, Repository) {
	
	$scope.nodeUri = Login.nodeUri;
	$scope.endodedNodeUri = encodeURIComponent($scope.nodeUri);
	// get all available extensions
	$scope.availableExtensions = Repository.getAllExtensions({ path: "allExtensions" });
	// get all repositories
	$scope.repositories = Repository.get({ id: $scope.nodeUri });
	
	// get all extensions of a repository
	$scope.getExtensions = function (repoUri){
		return Repository.getExtensionsForRepository({ id: encodeURIComponent(repoUri), path: "extensions" });
//		  .$promise.then(function(result) {
//			  $scope.currentRepositoryExtensions = result;
//		});
	};
	
	// get all properties of a dependence 
	$scope.getDependence = function (dependenceName) {
		for (var index in $scope.availableExtensions.messageResult) {
				if ($scope.availableExtensions.messageResult[index].id == dependenceName) {
					return $scope.availableExtensions.messageResult[index];					
				}
		}
	};
	
	$scope.repositoryDescription = "Here will be a short description of repository.Post no so what deal evil rent by real in. But her ready least set lived spite solid. " +
								   "September how men saw tolerably two behaviour arranging. She offices for highest and replied one venture pasture." +
								   "Applauded no discovery in newspaper allowance am northward." +
								   "Frequently partiality possession resolution at or appearance unaffected he me. ";
	
	/* save current repository + extensions for popup - it doesn't know about it */
	$scope.setExtensions = function(repositoryExtensions, repository) {
        $scope.currentRepositoryExtensions = repositoryExtensions;
        $scope.currentRepository = repository; 
	};
	
	/**
	 * Create Repository
	 */
	$scope.createRepository = function() {
		$scope.newRepository = { nodeUri:'', properties: { user: Login.login , name:  '', description: '', extensions: [] }};
		$scope.repositories.messageResult.unshift($scope.newRepository);
	};
	
	/**
	 * Save - edit 
	 */
	$scope.save = function(editRepository,newRepositoryName, newRepositoryDescription) {
		// save node
		$scope.editRepository = editRepository;
		$scope.editRepository.properties.name = newRepositoryName;
		$scope.editRepository.properties.description = newRepositoryDescription;
		//call server method
		$scope.newRepository = Repository.action({ path: "save" }, $scope.editRepository);
	};
	
	/**
	 * Open Repository
	 */
	$scope.open = function() {
	};
		 
	/**
	 * Apply Extension to Repository
	 */
	$scope.applyExtension = function(extensionId) {
		Repository.action({ path: "applyExtension" },{ 'login': Login.login, 'repositoryName': $scope.currentRepository.properties.name, 'extensionId': extensionId })
				  .$promise.then(function(result) {
					  $scope.currentRepositoryExtensions = result;
				});
	};
	
	/**
	 * Unapply Extension to Repository 
	 */
	$scope.unapplyExtension = function(extensionId) {
		Repository.action({ path: "unapplyExtension" },{ 'login': Login.login, 'repositoryName': $scope.currentRepository.properties.name, 'extensionId': extensionId })
		  .$promise.then(function(result) {
			  $scope.currentRepositoryExtensions = result;
		});
	};
	
	/**
	 * Search repository 
	 */
	$scope.searchRepository = function(repoName) {
		for(var index in $scope.repositories) {
			if($scope.repositories[index].name == repoName) {
				$scope.findRepository = $scope.repositories[index];
			}
		}		
	};
	
	/**
	 * Delete repository
	 */
	$scope.deleteRepository = function(repositoryNodeUri) {
		logger.debug("uri " + repositoryNodeUri);
		Repository.action({ path: "deleteRepository" }, repositoryNodeUri)
		  .$promise.then(function(result) {
			  $scope.repositories = result;
		});
	}
	
}]);

