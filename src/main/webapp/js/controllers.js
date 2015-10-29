'use strict';

/* Controllers */

var controllers = angular.module('TalentManagementControllers', []);

controllers.controller('ApplicationController', ['$scope', '$state',
	function($scope, $state) {
		
	}
]);

controllers.controller('HeaderController', ['$scope', '$rootScope', 'AUTH_EVENTS',
	function($scope, $rootScope, events) {
		$scope.logout = function logout() {
			$rootScope.$broadcast(events.logoutSuccess);
		};
  	}
]);

controllers.controller('HomeController', ['$scope', '$rootScope', '$state',
    function($scope, $rootScope, $state) {
		
	}
]);

controllers.controller('RegisterTalentController', ['$scope', '$rootScope', '$state', 'Auth', 'WorkExperience', 'TalentRegistrationService',
	function($scope, $rootScope, $state, Auth, WorkExperience, TalentRegistrationService) {
		// reCAPTCHA
		/*$scope.response = null;
	    $scope.widgetId = null;
	    
	    $scope.model = {
	    	key: '6LejxQ8TAAAAADY4bvpWWTHpF3IP0VWqiKYj6weM'
	    }
	    
	    $scope.setResponse = function (response) {
	    	$scope.response = response;
	    }
	    
	    $scope.setWidgetId = function (widgetId) {
	    	$scope.widgetId = widgetId;
	    }
	    
	    $scope.cbExpiration = function() {
	    	$scope.response = null;
	    }*/
	    // reCAPTCHA
	    
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
			user.talent.birthDate = new Date(user.talent.birthDateStandardFormat);
			if(user.password != user.password2) return false;
			
			var workExperiences = [];
			var exps = user.talent.exp.split(",");
			for(var exp in exps) {
				workExperiences.push({ name: exps[exp] });
			}
			
			user.talent.workExperiences = workExperiences;
			
			var images = user.talent.images;
			
			delete user.talent.exp;
			delete user.talent.images;
			delete user.talent.birthDateStandardFormat;
			delete user.password2;
			console.log({ user: user, images: images });
			
			TalentRegistrationService.registerUser(user, images, "/api/service/user/register");
			
		};
	}
]);

controllers.controller('EventsController', ['$scope', '$rootScope', '$state',
	function($scope, $rootScope, $state) {
		
	}
]);