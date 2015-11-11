'use strict';

/* Controllers */

var controllers = angular.module('TalentManagementControllers', []);

controllers.controller('ApplicationController', ['$scope', '$state', 'USER_ROLES', 'AuthService', 'AUTH_EVENTS', 'Session', 'Auth', 'Talent',
	function($scope, $state, roles, AuthService, events, Session, Auth, Talent) {
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
			
			$scope.countTalents();
		});
		
		$scope.countTalents = function countTalents() {
			Talent.countApprovedTalents(function(approvedTalentsCount) {
				$scope.approvedTalentsCount = approvedTalentsCount
			});
			
			if(Session.user.userRole == roles.admin) {
				Talent.countForApprovalTalents(function(forApprovalTalentsCount) {
					$scope.forApprovalTalentsCount = forApprovalTalentsCount;
				});
				
				Talent.countDeniedTalents(function(deniedTalentsCount) {
					$scope.deniedTalentsCount = deniedTalentsCount;
				});
			}
		};

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
	function($scope, $rootScope, events, Talent, Session, roles) {
		$scope.logout = function logout() {
			$rootScope.$broadcast(events.logoutSuccess);
		};
  	}
]);

controllers.controller('HomeController', ['$scope', '$rootScope', '$state', 'Auth', 'Session', 'AUTH_EVENTS',
    function($scope, $rootScope, $state, Auth, Session, events) {
		
	}
]);

controllers.controller('LoginController', ['$scope', '$rootScope', '$state', 'Auth', 'Session', 'AUTH_EVENTS',
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
			Auth.registerTalent(user, function(userResponse) {
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

controllers.controller('RegisterAgencyController', ['$scope', '$rootScope', '$state', 'vcRecaptchaService', 'Auth', 'Session', 'AUTH_EVENTS',
	function($scope, $rootScope, $state, vcRecaptchaService, Auth, Session, events) {
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
		
		$scope.agencySignUp = function agencySignUp(user) {			
			var $signUpButton = $("#signUpButton").button("loading");
			if(user.password != user.password2) return false;
			
			var password2 = user.password2;
			delete user.password2;
			
			var response = vcRecaptchaService.getResponse($scope.widgetId);
			user.reCaptchaResponse = response;
			Auth.registerAgency(user, function(userResponse) {
				Session.create(userResponse);
				$signUpButton.button("reset");
				vcRecaptchaService.reload();
				$rootScope.$broadcast(events.loginSuccess);
				delete $scope.user;
				delete $scope.userSignUpErrors;
			}, function(error) {
				user.password2 = password2;
				$signUpButton.button("reset");
				vcRecaptchaService.reload();
				$scope.userSignUpErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
			});
			
		};
	}
]);

controllers.controller('TalentProfileController', ['$scope', '$rootScope', '$state', 'Auth', 'UserProfile', 'USER_ROLES', 'Talent',
	function($scope, $rootScope, $state, Auth, UserProfile, roles, Talent) {
		if(UserProfile.user && ($scope.user.userRole == roles.admin || $scope.user.userRole == roles.agency)) {
			Auth.viewProfile({ email : UserProfile.user.email }, function(user) {
				$scope.userToView = user;
			});
		} else {
			Auth.viewProfile(function(user) {
				$scope.userToView = user;
			});
		}
		
		$('#profileTabs a').click(function (e) {
			e.preventDefault();
			$(this).tab('show');
		});
		
		$scope.approve = function approve(id, clazz) {
			Talent.approve($.param({id: id, talentClass: clazz}), function() {
				$scope.successMessageApprove = "Updated";
				$scope.countTalents();
				
				delete $scope.successMessageDeny
				delete $scope.errorMessageApprove;
				delete $scope.errorMessageDeny;
			}, function(error) {
				$scope.errorMessageApprove = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
				
				delete $scope.successMessageDeny;
				delete $scope.successMessageApprove;
				delete $scope.errorMessageDeny;
			});
		};
		
		$scope.deny = function deny(id, note) {
			Talent.deny($.param({id: id, adminNote: note}), function() {
				$scope.successMessageDeny = "Updated";
				$scope.countTalents();
				
				delete $scope.successMessageApprove;
				delete $scope.errorMessageApprove;
				delete $scope.errorMessageDeny;
			}, function(error) {
				$scope.errorMessageDeny = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
				
				delete $scope.successMessageApprove;
				delete $scope.errorMessageApprove;
				delete $scope.successMessageDeny;
			});
		};
		
		$scope.setForApproval = function setForApproval(id, note) {
			Talent.setForApproval($.param({id: id, adminNote: note}), function() {
				$scope.successMessageDeny = "Updated";
				$scope.countTalents();
				
				delete $scope.successMessageApprove;
				delete $scope.errorMessageApprove;
				delete $scope.errorMessageDeny;
			}, function(error) {
				$scope.errorMessageDeny = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
				
				delete $scope.successMessageApprove;
				delete $scope.errorMessageApprove;
				delete $scope.successMessageDeny;
			});
		};
	}
]);

controllers.controller('AgencyProfileController', ['$scope', '$rootScope', '$state', 'Auth', 'UserProfile', 'USER_ROLES', 'Agency',
   	function($scope, $rootScope, $state, Auth, UserProfile, roles, Agency) {
   		if(UserProfile.user && ($scope.user.userRole == roles.admin || $scope.user.userRole == roles.agency)) {
   			Auth.viewProfile({ email : UserProfile.user.email }, function(user) {
   				$scope.userToView = user;
   			});
   		} else {
   			Auth.viewProfile(function(user) {
   				$scope.userToView = user;
   			});
   		}
   		
   		$scope.approve = function approve(id, clazz) {
   			Agency.approve($.param({id: id}), function() {
   				$scope.successMessageApprove = "Updated";
   				
   				delete $scope.successMessageDeny
   				delete $scope.errorMessageApprove;
   				delete $scope.errorMessageDeny;
   			}, function(error) {
   				$scope.errorMessageApprove = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
   				
   				delete $scope.successMessageDeny;
   				delete $scope.successMessageApprove;
   				delete $scope.errorMessageDeny;
   			});
   		};
   		
   		$scope.deny = function deny(id, note) {
   			Agency.deny($.param({id: id, adminNote: note}), function() {
   				$scope.successMessageDeny = "Updated";
   				
   				delete $scope.successMessageApprove;
   				delete $scope.errorMessageApprove;
   				delete $scope.errorMessageDeny;
   			}, function(error) {
   				$scope.errorMessageDeny = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
   				
   				delete $scope.successMessageApprove;
   				delete $scope.errorMessageApprove;
   				delete $scope.successMessageDeny;
   			});
   		};
   		
   		$scope.setForApproval = function setForApproval(id, note) {
   			Agency.setForApproval($.param({id: id, adminNote: note}), function() {
   				$scope.successMessageDeny = "Updated";
   				
   				delete $scope.successMessageApprove;
   				delete $scope.errorMessageApprove;
   				delete $scope.errorMessageDeny;
   			}, function(error) {
   				$scope.errorMessageDeny = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
   				
   				delete $scope.successMessageApprove;
   				delete $scope.errorMessageApprove;
   				delete $scope.successMessageDeny;
   			});
   		};
   	}
]);

controllers.controller('TalentProfileUpdateController', ['$scope', '$rootScope', '$state', '$filter', 'Auth', 'WorkExperience',
	function($scope, $rootScope, $state, $filter, Auth, WorkExperience) {
		Auth.viewProfile(function(user) {
			$scope.userToUpdate = user;
			
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
				var stringValues = [];
				
				for(var expIndex = 0;expIndex < exps.length;expIndex++) {
					var exp = exps[expIndex];
					workExperiences.push({ value: exp.name });
					stringValues.push(exp.name);
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
				
				$('#exp').tokenfield({
					tokens: expsStringArray,
					typeahead: [null, { source: engine.ttAdapter() }]
				});
				
				user.talent.exp = stringValues.join(',');
				
			});
		});
		
		$scope.updateTalentProfile = function updateTalentProfile(user) {
			user.password = '        ';
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
			Auth.updateTalent(user, function(userResponse) {
				$updateButton.button("reset");
				$state.go("home");
				delete $scope.userToUpdate;
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

controllers.controller('AgencyProfileUpdateController', ['$scope', '$rootScope', '$state', '$filter', 'Auth', 'WorkExperience',
 	function($scope, $rootScope, $state, $filter, Auth, WorkExperience) {
 		Auth.viewProfile(function(user) {
 			$scope.userToUpdate = user;
 		});
 		
 		$scope.updateAgencyProfile = function updateAgencyProfile(user) {
 			user.password = '        ';
 			var $updateButton = $("#updateButton").button("loading");
 			
 			Auth.updateAgency(user, function(userResponse) {
 				$updateButton.button("reset");
 				$state.go("home");
 				delete $scope.userToUpdate;
 				delete $scope.userUpdateErrors;
 			}, function(error) {
 				$updateButton.button("reset");
 				$scope.userUpdateErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
 			});
 		};
 		
 	}
]);

controllers.controller('EventController', ['$scope', '$rootScope', '$state', 'Session', 'USER_ROLES', 'Event', 'AgencyEvent', 'ApprovedEvent',
	function($scope, $rootScope, $state, Session, roles, Event, AgencyEvent, ApprovedEvent) {
		if($scope.isAuthorized(roles.admin)) {
			Event.query(function(events) {
				$scope.events = events;
			});
		}
		
		if($scope.isAuthorized(roles.agency)) {
			AgencyEvent.query({ id: Session.user.agency.id }, function(events) {
				$scope.events = events;
			});
		}
		
		if($scope.isAuthorized(roles.user)) {
			ApprovedEvent.query(function(events) {
				$scope.events = events;
			});
		}
		
		$scope.viewEvent = function viewEvent(id) {
			console.log("clicked");
		};
	}
]);

controllers.controller('ApprovedTalentController', ['$scope', '$rootScope', '$state', 'Talent', 'DATA', 'UserProfile', 'Session', 'USER_ROLES',
	function($scope, $rootScope, $state, Talent, DATA, UserProfile, Session, roles) {
		var columnCount = 4;
		
		Talent.approved({ page: 1, size: DATA.pageSize }, function(approvedTalents) {
			var talentsPerRow = Math.ceil(approvedTalents.length / columnCount);
			var talentRows = [];
			
			for(var talentIndex = 0;talentIndex < approvedTalents.length;talentIndex += talentsPerRow) {
				var talentRow = approvedTalents.slice(talentIndex, Math.min(talentIndex + talentsPerRow, approvedTalents.length));
				talentRows.push(talentRow);
			}
			
			$scope.approvedTalents = talentRows;
		});
		
		$scope.viewProfile = function viewProfile(user) {
			UserProfile.setUser(user);
			$state.go("talentProfile");
		};
	
	}
]);

controllers.controller('ForApprovalTalentController', ['$scope', '$rootScope', '$state', 'Talent', 'DATA', 'UserProfile', 'Session', 'USER_ROLES',
	function($scope, $rootScope, $state, Talent, DATA, UserProfile, Session, roles) {
		var columnCount = 4;
		
		if(Session.user.userRole == roles.admin) {
			Talent.forApproval({ page: 1, size: DATA.pageSize }, function(forApprovalTalents){
				var talentsPerRow = Math.ceil(forApprovalTalents.length / columnCount);
				var talentRows = [];
				
				for(var talentIndex = 0;talentIndex < forApprovalTalents.length;talentIndex += talentsPerRow) {
					var talentRow = forApprovalTalents.slice(talentIndex, Math.min(talentIndex + talentsPerRow, forApprovalTalents.length));
					talentRows.push(talentRow);
				}
				
				$scope.forApprovalTalents = talentRows;
			});
		}
		
		$scope.viewProfile = function viewProfile(user) {
			UserProfile.setUser(user);
			$state.go("talentProfile");
		};
	
	}
]);

controllers.controller('DeniedTalentController', ['$scope', '$rootScope', '$state', 'Talent', 'DATA', 'UserProfile', 'Session', 'USER_ROLES',
   	function($scope, $rootScope, $state, Talent, DATA, UserProfile, Session, roles) {
   		var columnCount = 4;
   		
   		if(Session.user.userRole == roles.admin) {
   			Talent.denied({ page: 1, size: DATA.pageSize }, function(deniedTalents){
   				var talentsPerRow = Math.ceil(deniedTalents.length / columnCount);
   				var talentRows = [];
   				
   				for(var talentIndex = 0;talentIndex < deniedTalents.length;talentIndex += talentsPerRow) {
   					var talentRow = deniedTalents.slice(talentIndex, Math.min(talentIndex + talentsPerRow, deniedTalents.length));
   					talentRows.push(talentRow);
   				}
   				
   				$scope.deniedTalents = talentRows;
   			});
   		}
   		
   		$scope.viewProfile = function viewProfile(user) {
   			UserProfile.setUser(user);
   			$state.go("talentProfile");
   		};
   	
   	}
]);

controllers.controller('AddEventController', ['$scope', '$state', 'Event', 
	function($scope, $state, Event) {

		$scope.addEvent = function addEvent(event) {
			var $addEventButton = $("#addEventButton").button("loading");
			
			event.runDateFrom = new Date(event.runDateFromStandardFormat);
			event.runDateTo = new Date(event.runDateToStandardFormat);
			
			var runDateFromStandardFormat = event.runDateFromStandardFormat;
			var runDateToStandardFormat = event.runDateToStandardFormat;
			
			delete event.runDateFromStandardFormat;
			delete event.runDateToStandardFormat;
			
			Event.save(event, function(savedEvent) {
				$state.go('events');
				$addEventButton.button("reset");
			}, function(error) {
				event.runDateFromStandardFormat = runDateFromStandardFormat;
				event.runDateToStandardFormat = runDateToStandardFormat;
				$addEventButton.button("reset");
				$scope.addEventErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
			});
		};
		
		$("input#runDateFromStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("input#runDateToStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
	}
]);

controllers.controller('UpdateEventController', ['$scope', '$state', 'Event',
  	function($scope, $state, Event) {
		
		$scope.updateEvent = function updateEvent(event) {
			Event.save(event, function(savedEvent) {
				$state.go('events');
			});
		};
		
		$("input#runDateFromStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("input#runDateToStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
  		
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