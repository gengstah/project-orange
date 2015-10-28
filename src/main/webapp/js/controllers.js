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

controllers.controller('RegisterTalentController', ['$scope', '$rootScope', '$state',
	function($scope, $rootScope, $state) {
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
	    
		$("input#birthDate").datepicker({
			autoclose: true,
			toggleActive: true
		});
		
		$("#fileInput").fileinput({
	        allowedFileExtensions : ['jpg', 'jpeg', 'png','gif']
	    });
		
		$scope.talentSignUp = function talentSignUp(talent) {
			
			
		};
	}
]);

controllers.controller('EventsController', ['$scope', '$rootScope', '$state',
	function($scope, $rootScope, $state) {
		
	}
]);