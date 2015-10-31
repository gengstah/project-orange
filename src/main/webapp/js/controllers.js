'use strict';

/* Controllers */

var controllers = angular.module('TalentManagementControllers', []);

controllers.controller('ApplicationController', ['$scope', '$state', 'USER_ROLES', 'AuthService', 'AUTH_EVENTS', 'Session', 'Auth',
	function($scope, $state, roles, AuthService, events, Session, Auth) {
		$scope.user = Session.user;
		$scope.userRoles = roles;
		$scope.isAuthenticated = AuthService.isAuthenticated;
		$scope.isAuthorized = AuthService.isAuthorized;
		
		$scope.setCurrentUser = function (user) {
			$scope.user = user;
		};
		
		$scope.$on(events.notAuthenticated, function() {
			$state.go('home');
		});

		$scope.$on(events.loginSuccess, function() {
			$scope.setCurrentUser(Session.user);
			$state.go('home');
		});

		$scope.$on(events.loginFailed, function() {
			
		});

		$scope.$on(events.logoutSuccess, function() {
			Auth.logout(function() {
				Session.destroy();
				$state.go('home');
			});
		});
	}
]);

controllers.controller('HeaderController', ['$scope', '$rootScope', 'AUTH_EVENTS',
	function($scope, $rootScope, events) {
		$scope.logout = function logout() {
			$rootScope.$broadcast(events.logoutSuccess);
		};
  	}
]);

controllers.controller('HomeController', ['$scope', '$rootScope', '$state', 'Auth', 'Session', 'AUTH_EVENTS',
    function($scope, $rootScope, $state, Auth, Session, events) {
		$scope.credentials = {};
		
		$scope.login = function login(credentials) {
			var $loginButton = $("#loginButton").button("loading");
			Auth.authenticate($.param({email: $scope.credentials.email, password: $scope.credentials.password}), function(user) {
				delete $scope.loginErrors;
				$scope.credentials = {};
				Session.create(user);
				$loginButton.button("reset");
				$rootScope.$broadcast(events.loginSuccess);
			}, function(error) {
				$scope.loginErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
				console.log($scope.loginErrors);
				$loginButton.button("reset");
				$rootScope.$broadcast(events.loginFailed);
			});
		};
		
	}
]);

controllers.controller('RegisterTalentController', ['$scope', '$rootScope', '$state', 'vcRecaptchaService', 'Auth', 'WorkExperience', 'Session', 'AUTH_EVENTS',
	function($scope, $rootScope, $state, vcRecaptchaService, Auth, WorkExperience, Session, events) {
		// reCAPTCHA
		$scope.response = null;
	    $scope.widgetId = null;
	    
	    $scope.model = {
	    	key: '6LcIBBATAAAAADjwWGkqfRDhKudYMUnsCVcks090'
	    }
	    
	    $scope.setResponse = function (response) {
	    	$scope.response = response;
	    }
	    
	    $scope.setWidgetId = function (widgetId) {
	    	$scope.widgetId = widgetId;
	    }
	    
	    $scope.cbExpiration = function() {
	    	$scope.response = null;
	    }
	    // reCAPTCHA
	    
		if(!!Session.user) $state.go("home");
	
		$scope.user = {
			talent: {
				gender: 'F'
			}
		};
	
		$("input#birthDateStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("#fileInput").fileinput({
	        allowedFileExtensions : ['jpg', 'jpeg', 'png','gif'],
	        previewFileType: "image",
	        browseClass: "btn btn-success",
	        browseLabel: "Pick Image",
	        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
	        removeClass: "btn btn-danger",
	        removeLabel: "Delete",
	        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> "
	    });
		
		WorkExperience.query(function(exps) {
			
			var workExperiences = [];
			
			for(var expIndex = 0;expIndex < exps.length;expIndex++) {
				var exp = exps[expIndex];
				workExperiences.push({ value: exp.name });
			}
			
			var engine = new Bloodhound({
				local: workExperiences,
				datumTokenizer: function(d) {
					return Bloodhound.tokenizers.whitespace(d.value);
				},
				queryTokenizer: Bloodhound.tokenizers.whitespace
			});

			engine.initialize();

			$('#exp').tokenfield({
				typeahead: [null, { source: engine.ttAdapter() }]
			});
			
		});
		
		$scope.talentSignUp = function talentSignUp(user) {
			var $signUpButton = $("#signUpButton").button("loading");
			user.talent.birthDate = new Date(user.talent.birthDateStandardFormat);
			if(user.password != user.password2) return false;
			
			var workExperiences = [];
			var exps = user.talent.exp.split(",");
			for(var exp in exps) {
				workExperiences.push({ name: exps[exp] });
			}
			
			user.talent.images = [];
			for(var imageFileName in user.talent.imageFileNames) {
				user.talent.images.push({ fileLocation: user.talent.imageFileNames[imageFileName] });
			}
			
			user.talent.workExperiences = workExperiences;
			
			var imageFileNames = user.talent.imageFileNames;
			var exp = user.talent.exp;
			var birthDateStandardFormat = user.talent.birthDateStandardFormat;
			var password2 = user.password2;
			
			delete user.talent.imageFileNames;
			delete user.talent.exp;
			delete user.talent.birthDateStandardFormat;
			delete user.password2;
			var response = vcRecaptchaService.getResponse();
			user.reCaptchaResponse = response;
			Auth.register(user, function(userResponse) {
				Session.create(userResponse);
				$signUpButton.button("reset");
				$rootScope.$broadcast(events.loginSuccess);
				delete $scope.user;
				delete $scope.userSignUpErrors;
			}, function(error) {
				user.talent.imageFileNames = imageFileNames;
				user.talent.exp = exp;
				user.talent.birthDateStandardFormat = birthDateStandardFormat;
				user.password2 = password2;
				$signUpButton.button("reset");
				vcRecaptchaService.reload();
				$scope.userSignUpErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
			});
			
		};
	}
]);

controllers.controller('ProfileController', ['$scope', '$rootScope', '$state', 'Auth',
	function($scope, $rootScope, $state, Auth) {
		Auth.viewProfile(function(user) {
			$scope.user = user;
		});
		
		$('#profileTabs a').click(function (e) {
			e.preventDefault();
			$(this).tab('show');
		});
	}
]);

controllers.controller('ProfileUpdateController', ['$scope', '$rootScope', '$state', '$filter', 'Auth', 'WorkExperience',
	function($scope, $rootScope, $state, $filter, Auth, WorkExperience) {
		Auth.viewProfile(function(user) {
			$scope.user = user;
			var dateParts = user.talent.birthDate.split("-");
			$("input#birthDateStandardFormat").datepicker('setDate', new Date(dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0]));
			
			WorkExperience.query(function(exps) {
				
				var workExperiences = [];
				
				for(var expIndex = 0;expIndex < exps.length;expIndex++) {
					var exp = exps[expIndex];
					workExperiences.push({ value: exp.name });
				}
				
				var engine = new Bloodhound({
					local: workExperiences,
					datumTokenizer: function(d) {
						return Bloodhound.tokenizers.whitespace(d.value);
					},
					queryTokenizer: Bloodhound.tokenizers.whitespace
				});

				engine.initialize();
				
				var expsStringArray = [];
				for(var workExperienceIndex in user.talent.workExperiences) {
					expsStringArray.push(user.talent.workExperiences[workExperienceIndex].name);
				}
				
				user.talent.exp = exps.join(',');
				
				$('#exp').tokenfield({
					tokens: expsStringArray,
					typeahead: [null, { source: engine.ttAdapter() }]
				});
				
			});
		});
		
		$("input#birthDateStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("#fileInput").fileinput({
	        allowedFileExtensions : ['jpg', 'jpeg', 'png','gif'],
	        previewFileType: "image",
	        browseClass: "btn btn-success",
	        browseLabel: "Pick Image",
	        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
	        removeClass: "btn btn-danger",
	        removeLabel: "Delete",
	        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> "
	    });
	}
]);

controllers.controller('EventsController', ['$scope', '$rootScope', '$state',
	function($scope, $rootScope, $state) {
		
	}
]);