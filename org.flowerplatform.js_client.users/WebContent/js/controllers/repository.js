'use strict';

logger.debug('init user controllers');

flowerProject.lazy.controller('UserDashboardCtrl', ['$scope', '$routeParams', '$location', 'Auth', 'Repository','SearchType',
       function($scope, $routeParams, $location, Auth, Repository, SearchType) {
	
	// description for repository
	$scope.repositoryDescription = "Here will be a short description of repository.Post no so what deal evil rent by real in. But her ready least set lived spite solid. " +
	   "September how men saw tolerably two behaviour arranging. She offices for highest and replied one venture pasture." +
	   "Applauded no discovery in newspaper allowance am northward." +
	   "Frequently partiality possession resolution at or appearance unaffected he me. ";
	
	// limit for load more/less repositories
	var limitStep = 2;
	$scope.limitAll = $scope.limitStarred = $scope.limitWhereIAmMember = limitStep; 
	
	// variables for AlertMessage directive
	$scope.userDashboardAlert = {};
	$scope.popupApplyUnapplyExtensionAlert = {};
	
	Auth.currentUser().$promise.then(function(user) {
		$scope.currentUser = user;
		$scope.nodeUri = user.messageResult.nodeUri;
		$scope.endodedNodeUri = encodeURIComponent($scope.nodeUri);
		$scope.availableExtensions = Repository.getAllExtensions({ path: "allExtensions" });
		$scope.repositories = Repository.get({ id: $scope.nodeUri });
		$scope.repositoriesWhereIAmMember = Repository.getAction({ id: $scope.nodeUri, path: "memberInRepository" });
		$scope.starredRepositories =  Repository.getAction({ id: $scope.nodeUri, path: "starredRepository" });
	});
	
	/**
	 * Set type of search
	 */
	$scope.setSearchType = function(type) {
		SearchType. setSearchType(type);
	}
	$scope.searchType = SearchType.getSearchType();
	
	/**
	 * Get all extensions of a repository
	 */
	$scope.getExtensions = function (repoUri){
		if (repoUri == '') {
			return [];
		} else {
			return Repository.getAction({ id: encodeURIComponent(repoUri), path: "extensions" });
		}
	};
	
	/**
	 * Get all properties of a dependence
	 */
	$scope.getDependence = function (dependenceName) {
		for (var index in $scope.availableExtensions.messageResult) {
				if ($scope.availableExtensions.messageResult[index].id == dependenceName) {
					return $scope.availableExtensions.messageResult[index];					
				}
		}
	};
	
	/**
	 * Save current repository + extensions for popup Apply/Unapply extension - it doesn't know about it 
	 */
	$scope.setExtensions = function(repositoryExtensions, repository) {
        $scope.currentRepositoryExtensions = repositoryExtensions;
        $scope.currentRepository = repository; 
	};
	
	/**
	 * Create Repository
	 */
	$scope.createRepository = function() {
		$scope.newRepository = { 
				'@class': 'org.flowerplatform.core.node.remote.Node',
				nodeUri: '', 
				properties: {
					'@class' : 'java.util.HashMap', 
					user: $scope.currentUser.messageResult.properties.login, 
					name:  '',
					description: '',
					extensions: [] }};
		$scope.repositories.messageResult.unshift($scope.newRepository);
	};
	
	/**
	 * Save - edit 
	 */
	$scope.save = function(editRepository,id) {
		$scope.editRepository = editRepository;
		$scope.editRepository.properties.name = document.getElementById(editRepository.properties.name + id).value;
		$scope.editRepository.properties.description = document.getElementById(editRepository.properties.description + id + id).value;

		$scope.newRepository = Repository.action({ path: "save" }, $scope.editRepository ).$promise.then(function(result) {
			 for (var index in $scope.repositories.messageResult) {
				 if ($scope.repositories.messageResult[index].nodeUri == '') {
					 $scope.repositories.messageResult[index] = result.messageResult;
				 }
			 }
		  },function(error) {
				$scope.userDashboardAlert = {
						message: error.data.messageResult,
						visible: true,
						danger: true
				};
				 for (var index in $scope.repositories.messageResult) {
					 if ($scope.repositories.messageResult[index].nodeUri == '') {
						 $scope.repositories.messageResult.splice($scope.repositories.messageResult[index],1);
					 }
				 }
		});
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
		Repository.action({ path: "applyExtension" },{ '@class' : 'java.util.HashMap',  'nodeUri': $scope.currentRepository.nodeUri, 'extensionId': extensionId })
				  .$promise.then(function(result) {
					  $scope.currentRepositoryExtensions = result;
				  }, function(error) {
						$scope.popupApplyUnapplyExtensionAlert = {
								message: error.data.messageResult,
								visible: true,
								danger: true
						};
				});
	};
	
	/**
	 * Unapply Extension to Repository 
	 */
	$scope.unapplyExtension = function(extensionId) {
		Repository.action({ path: "unapplyExtension" },{ '@class' : 'java.util.HashMap', 'nodeUri': $scope.currentRepository.nodeUri, 'extensionId': extensionId })
		  		  .$promise.then(function(result) {
		  			  $scope.currentRepositoryExtensions = result;
		  		  }, function(error) {
						$scope.popupApplyUnapplyExtensionAlert = {
								message: error.data.messageResult,
								visible: true,
								danger: true
						};
			 });
	};
	
	/**
	 * Search repository where i am member
	 */
	$scope.searchRepository = function (repositoryName) { 
		$scope.findRepository = [];
		if ($scope.searchType == 'member') {
			for (var index in $scope.repositoriesWhereIAmMember.messageResult) {
				if ($scope.repositoriesWhereIAmMember.messageResult[index].properties.name == repositoryName) {
					$scope.findRepository.push($scope.repositoriesWhereIAmMember.messageResult[index]);
				}
			}
		}
		
		if ($scope.searchType == 'starred') {
			for (var index in $scope.starredRepositories.messageResult) {
				if ($scope.starredRepositories.messageResult[index].properties.name == repositoryName) {
					$scope.findRepository.push($scope.starredRepositories.messageResult[index]);
				}
			}
		}
		
		if ($scope.searchType == 'all') {
			for (var index in $scope.repositories.messageResult) {
				if ($scope.repositories.messageResult[index].properties.name == repositoryName) {
					$scope.findRepository.push($scope.repositories.messageResult[index]);
				}
			}
		}
		
		return $scope.findRepository;
	}
	
	/**
	 * Delete repository
	 */
	$scope.deleteRepository = function(repositoryNodeUri) {
		Repository.action({ path: "deleteRepository" }, repositoryNodeUri)
		  .$promise.then(function(result) {
			  $scope.repositories = result;
			  $('#deleteModal').modal('hide');
			  $('body').removeClass('modal-open');
			  $('.modal-backdrop').remove();
		}, function(error) {
			$scope.userDashboardAlert = {
					message: error.data.messageResult,
					visible: true,
					danger: true
			}
		});
	}
	
	/**
	 * Refresh page
	 */
	$scope.refresh = function() {
		window.location.reload();
	}
	
	/**
	 * Load more repositories
	 */
	$scope.loadMore = function(typeOfRepository) {
		$scope.less = true;
		switch (typeOfRepository) {
			case "all":
				if ($scope.limitAll <= Object.keys($scope.repositories.messageResult).length) {
					$scope.limitAll += limitStep;
				} else {
					$scope.limitAll = Object.keys($scope.repositories.messageResult).length;
				}
				break;
				
			case "member":
				if ($scope.limitWhereIAmMember <= Object.keys($scope.repositoriesWhereIAmMember.messageResult).length) {
					$scope.limitWhereIAmMember += limitStep;
				} else {
					$scope.limitWhereIAmMember = Object.keys($scope.repositoriesWhereIAmMember.messageResult).length;
				}
				break;
				
			case "starred":
				if ($scope.limitStarred <= Object.keys($scope.starredRepositories.messageResult).length) {
					$scope.limitStarred += limitStep;
				} else {
					$scope.limitStarred = Object.keys($scope.starredRepositories.messageResult).length;
				}
				break;
		}
	}
	
	/**
	 * Load less repositories
	 */
	$scope.loadLess = function(typeOfRepository,limit) {
		if (limit > limitStep) {
			limit -= limitStep;
			if (limit <= limitStep) {
				$scope.less = false;
				limit = limitStep;
			}
		}
		
		switch(typeOfRepository) {
			case "all":
				$scope.limitAll = limit;
				break;
			
			case "member":
				$scope.limitWhereIAmMember = limit;
				break;
				
			case "starred":
				$scope.limitStarred = limit;
				break;
		}
	}
	
	/**
	 * Save delete node for popup Delete repository
	 */
	$scope.setDeleteNode = function(node) {
		$scope.deleteNode = node;
		$scope.deleteNodeUri = node.nodeUri;
	}
	
}]);