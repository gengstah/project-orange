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
			uploadUrl: "/api/service/user/uploadImage",
		    uploadAsync: true,
			minFileCount: 1,
		    maxFileCount: 5,
		    validateInitialCount: true,
		    overwriteInitial: false,
		    initialPreviewShowDelete: true,
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
			
			user.talent.workExperiences = workExperiences;
			
			var exp = user.talent.exp;
			var birthDateStandardFormat = user.talent.birthDateStandardFormat;
			var password2 = user.password2;
			
			delete user.talent.exp;
			delete user.talent.birthDateStandardFormat;
			delete user.password2;
			var response = vcRecaptchaService.getResponse($scope.widgetId);
			user.reCaptchaResponse = response;
			Auth.register(user, function(userResponse) {
				Session.create(userResponse);
				$signUpButton.button("reset");
				vcRecaptchaService.reload();
				$rootScope.$broadcast(events.loginSuccess);
				delete $scope.user;
				delete $scope.userSignUpErrors;
			}, function(error) {
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
			
			$("input#birthDateStandardFormat").datepicker({
				autoclose: true,
				toggleActive: true
			});
			var dateParts = user.talent.birthDate.split("-");
			$("input#birthDateStandardFormat").datepicker('setDate', new Date(dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0]));
			
			var initialPreviewList = [];
			var initialPreviewConfigList = [];
			
			var images = user.talent.images;
			for(var imageIndex in images) {
				var image = images[imageIndex];
				var fileNameParts = image.fileLocation.split("/");
				var fileName = fileNameParts[fileNameParts.length - 1];
				
				initialPreviewList.push("<img style='height:160px' src='" + image.fileLocation + "'>");
				initialPreviewConfigList.push({ url: "/api/service/user/deleteSavedImage/" + fileName });
			}
			
			$("#fileInput").fileinput({
				uploadUrl: "/api/service/user/uploadImage",
				uploadAsync: true,
				minFileCount: 1,
			    maxFileCount: 5,
			    validateInitialCount: true,
			    overwriteInitial: false,
			    initialPreviewShowDelete: true,
		        allowedFileExtensions : ['jpg', 'jpeg', 'png','gif'],
		        previewFileType: "image",
		        browseClass: "btn btn-success",
		        browseLabel: "Pick Image",
		        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
		        removeClass: "btn btn-danger",
		        removeLabel: "Delete",
		        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> ",
		        initialPreview: initialPreviewList,
		        initialPreviewConfig: initialPreviewConfigList
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
		
		$scope.updateProfile = function updateProfile(user) {
			var $updateButton = $("#updateButton").button("loading");
			user.talent.birthDate = new Date(user.talent.birthDateStandardFormat);
			
			var workExperiences = [];
			var exps = user.talent.exp.split(",");
			for(var exp in exps) {
				workExperiences.push({ name: exps[exp] });
			}
			
			user.talent.workExperiences = workExperiences;
			
			var exp = user.talent.exp;
			var birthDateStandardFormat = user.talent.birthDateStandardFormat;
			
			delete user.talent.exp;
			delete user.talent.birthDateStandardFormat;
			Auth.update(user, function(userResponse) {
				$updateButton.button("reset");
				vcRecaptchaService.reload();
				delete $scope.user;
				delete $scope.userUpdateErrors;
			}, function(error) {
				user.talent.exp = exp;
				user.talent.birthDateStandardFormat = birthDateStandardFormat;
				$updateButton.button("reset");
				$scope.userUpdateErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
			});
		};
		
	}
]);

controllers.controller('EventsController', ['$scope', '$rootScope', '$state',
	function($scope, $rootScope, $state) {
		
	}
]);

controllers.controller('PasswordController', ['$scope', 'Session', 'Auth', '$state',
  	function($scope, Session, Auth, $state) {
	
  		$scope.changePassword = function changePassword(oldPassword, newPassword) {
  			var username = Session.user.username;
  			
  			Auth.changePassword($.param({username: username, oldPassword: oldPassword, newPassword: newPassword}), function(user) {
  				$scope.oldPassword = '';
  				$scope.newPassword = '';
  				$scope.newPassword2 = '';
  				$state.go('home');
  			}, function(error) {
  				$scope.changePasswordErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
  			});
  		}
  		
  		$scope.oldPassword = '';
  		$scope.newPassword = '';
  		$scope.newPassword2 = '';
  		
  	}
]);