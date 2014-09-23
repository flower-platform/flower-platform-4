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
	$scope.newrepo = Repository.get({ id: $scope.nodeUri });
	
	/* get available extensions and repositories */
	$scope.extensions = UserRepositories.getExtensions();
	$scope.repositories = UserRepositories.getRepositories();
	$scope.repositoryDescription = "Here will be a short description of repository.Post no so what deal evil rent by real in. But her ready least set lived spite solid. " +
								   "September how men saw tolerably two behaviour arranging. She offices for highest and replied one venture pasture." +
								   "Applauded no discovery in newspaper allowance am northward." +
								   "Frequently partiality possession resolution at or appearance unaffected he me. ";
	
	$scope.setRepo = function(currentRepo) {
        $scope.currentRepo = currentRepo;
	};		
	
	/**
	 * Create Repository
	 */
	$scope.createRepository = function() {
		$scope.repositories.unshift({ name : '', description : '' });
	};
	
	/**
	 * Open Repository
	 */
	$scope.open = function() {
	};
	
	/**
	 * Apply Extension to Repository
	 */
	$scope.applyExtension = function(extension) {
		logger.debug("before " + $scope.currentRepo.extensions);
		for (var index in $scope.repositories) {
			if($scope.repositories[index].name == $scope.currentRepo.name) {
				$scope.repositories[index].extensions.push(extension);
			}
		}
		logger.debug("after " + $scope.currentRepo.extensions);
	};
	
	/**
	 * Unapply Extension to Repository
	 */
	$scope.unapplyExtension = function(extension) {
		logger.debug("before " + $scope.currentRepo.extensions + " extension " + extension);
		for (var index in $scope.repositories) {
			if ($scope.repositories[index].name == $scope.currentRepo.name) {
				for (var indexExt in $scope.repositories[index].extensions) {
					if ($scope.repositories[index].extensions[indexExt].label == extension) {
						$scope.repositories[index].extensions.splice(indexExt,1);
					}
				}
			}
		}
		logger.debug("after " + $scope.currentRepo.extensions);
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
	 * Save - edit 
	 */
	$scope.save = function(initialName, newRepositoryName, newRepositoryDescription) {
		$scope.newName = newRepositoryName;
		$scope.newDes = newRepositoryDescription;
		$scope.initialName = initialName;
		logger.debug("initialName " + $scope.initialName + " newName " + $scope.newDes + " newDescrip " + $scope.newName);

		for (var index in $scope.repositories) {
			if($scope.repositories[index].name == $scope.initialName) {
				$scope.repositories[index].name = $scope.newName;
				$scope.repositories[index].description = $scope.newDes;
			}
		}
	};
	
}]);

