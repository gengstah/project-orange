'use strict';

/* Controllers */

var controllers = angular.module('TalentManagementControllers', []);

controllers.controller('ApplicationController', ['$scope', '$state', 'USER_ROLES', 'AuthService', 'AUTH_EVENTS', 'Session', 'Auth', 'Talent', 'EventCount',
	function($scope, $state, roles, AuthService, events, Session, Auth, Talent, EventCount) {
		$scope.user = Session.user;
		$scope.userRoles = roles;
		$scope.isAuthenticated = AuthService.isAuthenticated;
		$scope.isAuthorized = AuthService.isAuthorized;
		
		$scope.setCurrentUser = function (user) {
			$scope.user = user;
		};
		
		$scope.$on(events.notAuthenticated, function() {
			destroySession();
			
			$state.go('home');
		});
		
		$scope.$on(events.notAuthorized, function() {
			destroySession();
			
			$state.go('home');
		});
		
		$scope.$on(events.sessionTimeout, function() {
			destroySession();
			
			$state.go('home');
		});

		$scope.$on(events.loginSuccess, function() {
			$scope.setCurrentUser(Session.user);
			$state.go('home');
			
			$scope.countTalents();
			$scope.countEvents();
		});
		
		$scope.countTalents = function countTalents() {
			if(Session.user.userRole == roles.admin || Session.user.userRole == roles.agency) {
				Talent.countApprovedTalents(function(approvedTalentsCount) {
					$scope.approvedTalentsCount = approvedTalentsCount
				});
			}
			
			if(Session.user.userRole == roles.admin) {
				Talent.countForApprovalTalents(function(forApprovalTalentsCount) {
					$scope.forApprovalTalentsCount = forApprovalTalentsCount;
				});
				
				Talent.countDeniedTalents(function(deniedTalentsCount) {
					$scope.deniedTalentsCount = deniedTalentsCount;
				});
			}
		};
		
		$scope.countEvents = function countEvents() {
			if(Session.user.userRole == roles.admin || Session.user.userRole == roles.user) {
				EventCount.countApprovedEvents(function(approvedEventsCount) {
					$scope.approvedEventsCount = approvedEventsCount;
				});
			}
			
			if(Session.user.userRole == roles.agency) {
				EventCount.countApprovedEventsByAgency({ id: Session.user.agency.id }, function(approvedAgencyEventsCount) {
					$scope.approvedEventsCount = approvedAgencyEventsCount;
				});
				
				EventCount.countForApprovalEventsByAgency({ id: Session.user.agency.id }, function(forApprovalAgencyEventsCount) {
					$scope.forApprovalEventsCount = forApprovalAgencyEventsCount;
				});
				
				EventCount.countDeniedEventsByAgency({ id: Session.user.agency.id }, function(deniedAgencyEventsCount) {
					$scope.deniedEventsCount = deniedAgencyEventsCount;
				});
				
				EventCount.countClosedEventsByAgency({ id: Session.user.agency.id }, function(closedAgencyEventsCount) {
					$scope.closedEventsCount = closedAgencyEventsCount;
				});
			}
			
			if(Session.user.userRole == roles.admin) {
				EventCount.countForApprovalEvents(function(forApprovalEventsCount) {
					$scope.forApprovalEventsCount = forApprovalEventsCount;
				});
				
				EventCount.countDeniedEvents(function(deniedEventsCount) {
					$scope.deniedEventsCount = deniedEventsCount;
				});
				
				EventCount.countClosedEvents(function(closedEventsCount) {
					$scope.closedEventsCount = closedEventsCount;
				});
			}
		};

		$scope.$on(events.loginFailed, function() {
			
		});

		$scope.$on(events.logoutSuccess, function() {
			Auth.logout(function() {
				destroySession();
				
				$state.go('home');
			});
		});
		
		function destroySession() {
			Session.destroy();
			
			delete $scope.user;
			
			delete $scope.approvedTalentsCount;
			delete $scope.forApprovalTalentsCount;
			delete $scope.deniedTalentsCount;
			
			delete $scope.approvedEventsCount;
			delete $scope.forApprovalEventsCount;
			delete $scope.deniedEventsCount;
			delete $scope.closedEventsCount;
		}
	}
]);

controllers.controller('HeaderController', ['$scope', '$rootScope', 'AUTH_EVENTS',
	function($scope, $rootScope, events, Talent, Session, roles) {
		$scope.logout = function logout() {
			$rootScope.$broadcast(events.logoutSuccess);
		};
  	}
]);

controllers.controller('HomeController', ['$scope', '$rootScope', '$state',
    function($scope, $rootScope, $state) {
		
	}
]);

controllers.controller('RegistrationController', ['$scope', '$rootScope', '$state',
    function($scope, $rootScope, $state) {
		
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

controllers.controller('TalentProfileController', ['$scope', '$rootScope', '$state', 'Auth', 'UserProfile', 'Agency', 'Talent', 'ApprovedEventsOfAgencyWithTalentApplication',
	function($scope, $rootScope, $state, Auth, UserProfile, Agency, Talent, ApprovedEventsOfAgencyWithTalentApplication) {
		if(UserProfile.user) {
			Auth.viewProfile({ email : UserProfile.user.email }, function(user) {
				$scope.userToView = user;
				
				if($scope.isAuthorized($scope.userRoles.agency)) {
					retrieveAgencyEvents();
					
					$scope.addTalentToEvent = function addTalentToEvent(talentId, eventId) {
						Agency.addTalentToEvent($.param({ talentId: talentId, eventId: eventId }), function() {
							$scope.successMessageAddTalent = "Talent has been added to your event!";
							retrieveAgencyEvents();
							$scope.userToView.talent.eventSize++;
							
							delete $scope.errorMessageAddTalent;
							delete $scope.errorMessageRemoveTalent;
							delete $scope.successMessageRemoveTalent;
						}, function(error) {
							$scope.errorMessageAddTalent = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
							
							delete $scope.successMessageAddTalent;
						});
					};
					
					$scope.removeTalentFromEvent = function removeTalentFromEvent(talentId, eventId) {
						Agency.removeTalentFromEvent($.param({ talentId: talentId, eventId: eventId }), function() {
							$scope.successMessageRemoveTalent = "Talent has been removed from your event!";
							retrieveAgencyEvents();
							$scope.userToView.talent.eventSize--;
							
							delete $scope.errorMessageRemoveTalent;
							delete $scope.errorMessageAddTalent;
							delete $scope.successMessageAddTalent;
						}, function(error) {
							$scope.errorMessageRemoveTalent = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
							
							delete $scope.successMessageRemoveTalent;
						});
					};
				}
			});
		} else {
			Auth.viewProfile(function(user) {
				$scope.userToView = user;
			});
		}
		
		function retrieveAgencyEvents() {
			ApprovedEventsOfAgencyWithTalentApplication.findApprovedEventsOfAgencyNotAppliedByTalent({ agencyId: $scope.user.agency.id, talentId: $scope.userToView.talent.id }, function(eventsNotApplied) {
				$scope.eventsNotApplied = eventsNotApplied;
			});
			
			ApprovedEventsOfAgencyWithTalentApplication.findApprovedEventsOfAgencyAppliedByTalent({ agencyId: $scope.user.agency.id, talentId: $scope.userToView.talent.id }, function(eventsApplied) {
				$scope.eventsApplied = eventsApplied;
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
				$state.go("talentProfile");
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
 				$state.go("agencyProfile");
 				delete $scope.userToUpdate;
 				delete $scope.userUpdateErrors;
 			}, function(error) {
 				$updateButton.button("reset");
 				$scope.userUpdateErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
 			});
 		};
 		
 	}
]);

controllers.controller('ApprovedEventController', ['$scope', '$rootScope', '$state', 'USER_ROLES', 'Event', 'AgencyEvent', 'EventDetail',
	function($scope, $rootScope, $state, roles, Event, AgencyEvent, EventDetail) {
		
		$("input#runDateStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("input#dateCreatedFromStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("input#dateCreatedToStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
	
		$scope.eventCriteria = {};
	
		function searchEvents(eventCriteria) {
			if(eventCriteria.runDateStandardFormat) eventCriteria.runDate = new Date(eventCriteria.runDateStandardFormat);
			if(eventCriteria.dateCreatedFromStandardFormat) eventCriteria.dateCreatedFrom = new Date(eventCriteria.dateCreatedFromStandardFormat);
			if(eventCriteria.dateCreatedToStandardFormat) eventCriteria.dateCreatedTo = new Date(eventCriteria.dateCreatedToStandardFormat);
			
			if($scope.isAuthorized(roles.admin) || $scope.isAuthorized(roles.user)) {
				Event.approved(eventCriteria, function(approvedEvents) {
					approvedEvents = processIdentities(approvedEvents);
					
					$scope.approvedEvents = approvedEvents;
				});
			}
			
			if($scope.isAuthorized(roles.agency)) {
				AgencyEvent.approved(eventCriteria, function(approvedEvents) {
					approvedEvents = processIdentities(approvedEvents);
					
					$scope.approvedEvents = approvedEvents;
				});
			}
		}
		
		searchEvents($scope.eventCriteria);
		
		$scope.searchEvent = function searchEvent(eventCriteria) {
			searchEvents(eventCriteria);
		};
		
		$scope.viewEvent = function viewEvent(event) {
			EventDetail.create(event);
			$state.go('event-page');
		};
		
		$scope.reset = function reset() {
			$scope.eventCriteria = {};
		};
	}
]);

controllers.controller('ForApprovalEventController', ['$scope', '$rootScope', '$state', 'USER_ROLES', 'Event', 'AgencyEvent', 'EventDetail',
   	function($scope, $rootScope, $state, roles, Event, AgencyEvent, EventDetail) {
   		
   		$scope.eventCriteria = {};
   	
   		function searchEvents(eventCriteria) {
			if($scope.isAuthorized(roles.admin)) {
				Event.forApproval(eventCriteria, function(forApprovalEvents) {
					forApprovalEvents = processIdentities(forApprovalEvents);
					
					$scope.forApprovalEvents = forApprovalEvents;
				});
			}
			
			if($scope.isAuthorized(roles.agency)) {
				AgencyEvent.forApproval(eventCriteria, function(forApprovalEvents) {
					forApprovalEvents = processIdentities(forApprovalEvents);
					
					$scope.forApprovalEvents = forApprovalEvents;
				});
			}
   		}
   		
   		searchEvents($scope.eventCriteria);
		
		$scope.searchEvent = function searchEvent(eventCriteria) {
			searchEvents(eventCriteria);
		};
   		
   		$scope.viewEvent = function viewEvent(event) {
   			EventDetail.create(event);
   			$state.go('event-page');
   		};
   		
   		$scope.reset = function reset() {
   			$scope.eventCriteria = {};
   		};
   	}
]);

controllers.controller('DeniedEventController', ['$scope', '$rootScope', '$state', 'USER_ROLES', 'Event', 'AgencyEvent', 'EventDetail',
	function($scope, $rootScope, $state, roles, Event, AgencyEvent, EventDetail) {
		
		$scope.eventCriteria = {};
		
		function searchEvents(eventCriteria) {
			if($scope.isAuthorized(roles.admin)) {
				Event.denied(eventCriteria, function(deniedEvents) {
					deniedEvents = processIdentities(deniedEvents);
					
					$scope.deniedEvents = deniedEvents;
				});
			}
			
			if($scope.isAuthorized(roles.agency)) {
				AgencyEvent.denied(eventCriteria, function(deniedEvents) {
					deniedEvents = processIdentities(deniedEvents);
					
					$scope.deniedEvents = deniedEvents;
				});
			}
		}
		
		searchEvents($scope.eventCriteria);
		
		$scope.searchEvent = function searchEvent(eventCriteria) {
			searchEvents(eventCriteria);
		};
		
		$scope.viewEvent = function viewEvent(event) {
			EventDetail.create(event);
			$state.go('event-page');
		};
		
		$scope.reset = function reset() {
			$scope.eventCriteria = {};
		};
	}
]);

controllers.controller('ClosedEventController', ['$scope', '$rootScope', '$state', 'USER_ROLES', 'Event', 'AgencyEvent', 'EventDetail',
 	function($scope, $rootScope, $state, roles, Event, AgencyEvent, EventDetail) {
 		
 		$scope.eventCriteria = {};
 		
 		function searchEvents(eventCriteria) {
			if($scope.isAuthorized(roles.admin)) {
				Event.closed(eventCriteria, function(closedEvents) {
					closedEvents = processIdentities(closedEvents);
					
					$scope.closedEvents = closedEvents;
				});
			}
			
			if($scope.isAuthorized(roles.agency)) {
				AgencyEvent.closed(eventCriteria, function(closedEvents) {
					closedEvents = processIdentities(closedEvents);
					
					$scope.closedEvents = closedEvents;
				});
			}
 		}
 		
 		searchEvents($scope.eventCriteria);
		
		$scope.searchEvent = function searchEvent(eventCriteria) {
			searchEvents(eventCriteria);
		};
 		
 		$scope.viewEvent = function viewEvent(event) {
 			EventDetail.create(event);
 			$state.go('event-page');
 		};
 		
 		$scope.reset = function reset() {
 			$scope.eventCriteria = {};
 		};
 	}
]);

function processIdentities(events) {
	var agencies = [];
	
	for(var eventIndex = 0;eventIndex < events.length;eventIndex++) {
		var event = events[eventIndex];
		var id = event.agency["@identity"];
		if(typeof event.agency === 'object') {
			agencies.push({id: id, value: event.agency});
		} else {
			var index = -1;
			for(var agencyIndex in agencies) {
				if(agencies[agencyIndex].id == event.agency) {
					index = agencyIndex;
					break;
				}
			}
			event.agency = agencies[index].value;
		}
	}
	
	return events;
}

controllers.controller('EventPageController', ['$scope', '$rootScope', '$state', 'EventDetail', 'EventAction', 'TalentApplyEvent', 'TalentWithdrawEvent', 'TalentEventQuery', 'UserProfile',
   	function($scope, $rootScope, $state, EventDetail, EventAction, TalentApplyEvent, TalentWithdrawEvent, TalentEventQuery, UserProfile) {
		retrieveTalentEvents();
		
		function retrieveTalentEvents() {
			TalentEventQuery.event({ id: EventDetail.event.id }, function(talentEvents) {
				$scope.event = EventDetail.event;
				$scope.talentEvents = talentEvents;
				
				if($scope.isAuthorized($scope.userRoles.user)) {
					$scope.applied = false;
					
					for(var talentEventIndex = 0;talentEventIndex < talentEvents.length;talentEventIndex++) {
						var talentEvent = talentEvents[talentEventIndex];
						if(talentEvent.talent.user.email == $scope.user.email) {
							$scope.applied = true;
							break;
						}
					}
				}
			});
		}
   		
   		$scope.apply = function apply(eventId) {
   			TalentApplyEvent.apply($.param({ eventId: eventId }), function() {
   				retrieveTalentEvents();
   				$scope.applied = true;
   			}, function(error) {
   				$scope.errorMessageApply = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
				
				delete $scope.successMessageApply;
   			});
   		};
   		
   		$scope.withdraw = function withdraw(eventId) {
   			TalentWithdrawEvent.withdraw($.param({ eventId: eventId }), function() {
   				retrieveTalentEvents();
   				$scope.applied = false;
   			}, function(error) {
   				$scope.errorMessageApply = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
				
				delete $scope.successMessageApply;
   			});
   		};
   		
   		$scope.approve = function approve(id, actualTalentFee) {
   			EventAction.approve($.param({id: id, actualTalentFee: actualTalentFee}), function() {
				$scope.successMessageApprove = "Updated";
				$scope.event.actualTalentFee = actualTalentFee;
				
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
			EventAction.deny($.param({id: id, adminNote: note}), function() {
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
			EventAction.setForApproval($.param({id: id, adminNote: note}), function() {
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
		
		$scope.viewProfile = function viewProfile(user) {
			UserProfile.setUser(user);
			$state.go("talentProfile");
		};
   	}
]);

controllers.controller('ApprovedTalentController', ['$scope', '$rootScope', '$state', 'Talent', 'DATA', 'UserProfile', 'Session', 'USER_ROLES',
	function($scope, $rootScope, $state, Talent, DATA, UserProfile, Session, roles) {
		Talent.approved({}, function(approvedTalents) {
			$scope.approvedTalents = approvedTalents;
		});
		
		$scope.viewProfile = function viewProfile(user) {
			UserProfile.setUser(user);
			$state.go("talentProfile");
		};
		
		$scope.talentCriteria = {};
		
		$scope.searchTalent = function searchTalent(talentCriteria) {
			
			talentCriteria.talentClasses = [];
			if(talentCriteria.talentClassAA) talentCriteria.talentClasses.push("AA");
			if(talentCriteria.talentClassA) talentCriteria.talentClasses.push("A");
			if(talentCriteria.talentClassB) talentCriteria.talentClasses.push("B");
			if(talentCriteria.talentClassC) talentCriteria.talentClasses.push("C");
			
			Talent.approved(talentCriteria, function(approvedTalents) {
				$scope.approvedTalents = approvedTalents;
			});
			
		};
		
		$scope.reset = function reset() {
			$scope.talentCriteria = {};
		};
	
	}
]);

controllers.controller('ForApprovalTalentController', ['$scope', '$rootScope', '$state', 'Talent', 'DATA', 'UserProfile', 'Session', 'USER_ROLES',
	function($scope, $rootScope, $state, Talent, DATA, UserProfile, Session, roles) {
		if(Session.user.userRole == roles.admin) {
			Talent.forApproval({ page: 1, size: DATA.pageSize }, function(forApprovalTalents){
				$scope.forApprovalTalents = forApprovalTalents;
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
   		if(Session.user.userRole == roles.admin) {
   			Talent.denied({ page: 1, size: DATA.pageSize }, function(deniedTalents){
   				$scope.deniedTalents = deniedTalents;
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

controllers.controller('UpdateEventController', ['$scope', '$state', 'Event', 'EventDetail',
  	function($scope, $state, Event, EventDetail) {
		
		$scope.event = EventDetail.event;
	
		$scope.updateEvent = function updateEvent(event) {
			var $updateEventButton = $("#updateEventButton").button("loading");
			
			event.runDateFrom = new Date(event.runDateFromStandardFormat);
			event.runDateTo = new Date(event.runDateToStandardFormat);
			
			var runDateFromStandardFormat = event.runDateFromStandardFormat;
			var runDateToStandardFormat = event.runDateToStandardFormat;
			
			delete event.runDateFromStandardFormat;
			delete event.runDateToStandardFormat;
			
			Event.save(event, function(savedEvent) {
				$state.go('events');
				$updateEventButton.button("reset");
			}, function(error) {
				event.runDateFromStandardFormat = runDateFromStandardFormat;
				event.runDateToStandardFormat = runDateToStandardFormat;
				$updateEventButton.button("reset");
				$scope.updateEventErrors = JSON.parse(error.headers('X-TalentManagementServiceApi-Exception'));
			});
		};
		
		$("input#runDateFromStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		if($scope.event.runDateFrom) {
			var dateParts = $scope.event.runDateFrom.split("-");
			var dateFormat = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
			$("input#runDateFromStandardFormat").datepicker('setDate', new Date(dateFormat));
			$scope.event.runDateFromStandardFormat = dateFormat;
		}
		
		$("input#runDateToStandardFormat").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		if($scope.event.runDateTo) {
			var dateParts = $scope.event.runDateTo.split("-");
			var dateFormat = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
			$("input#runDateToStandardFormat").datepicker('setDate', new Date(dateFormat));
			$scope.event.runDateToStandardFormat = dateFormat;
		}
  		
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