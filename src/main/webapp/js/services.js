'use strict';

/* Services */

var services = angular.module('TalentManagementServices', ['ngResource']);

services.factory('Auth', ['$resource', 
  	function($resource) {
  		return $resource('/api/service/user/:action', {}, {
  			authenticate: { 
  				method: 'POST', 
  				params: { 
  					action: 'authenticate'
  				}, 
  				headers : { 
  					'Content-Type': 'application/x-www-form-urlencoded' 
  				}
  			},
  			logout: { 
  				method: 'GET', 
  				params: { 
  					action: 'logout'
  				}
  			},
  			register: { 
  				method: 'POST', 
  				params: { 
  					action: 'register'
  				}
  			},
  			update: { 
  				method: 'POST', 
  				params: { 
  					action: 'update'
  				}
  			},
  			viewProfile: {
  				method: 'GET', 
  				params: { 
  					action: 'profile'
  				}
  			},
			changePassword: {
				method: 'POST',
				params: { 
					action: 'changepassword'
				},
				headers : { 
					'Content-Type': 'application/x-www-form-urlencoded' 
				}
			}
  		});
  	}
]);

services.factory('Talent', ['$resource', 
	function($resource) {
		return $resource('/api/service/talent/:action', {}, {
			approved: { 
				method: 'GET', 
				params: { 
					action: 'approved'
				},
				isArray: true
			},
			forApproval: { 
				method: 'GET', 
				params: { 
					action: 'forApproval'
				},
				isArray: true
			},
			denied: { 
				method: 'GET', 
				params: { 
					action: 'denied'
				},
				isArray: true
			},
			approve: { 
				method: 'POST', 
				params: { 
					action: 'approve'
				}, 
  				headers : { 
  					'Content-Type': 'application/x-www-form-urlencoded' 
  				}
			},
			deny: {
				method: 'POST', 
				params: { 
					action: 'deny'
				}, 
  				headers : { 
  					'Content-Type': 'application/x-www-form-urlencoded' 
  				}
			},
			setForApproval: {
				method: 'POST',
				params: { 
					action: 'setForApproval'
				},
				headers : { 
					'Content-Type': 'application/x-www-form-urlencoded' 
				}
			},
			countApprovedTalents: {
				method: 'GET',
				params: { 
					action: 'countApprovedTalents'
				}
			},
			countForApprovalTalents: {
				method: 'GET',
				params: { 
					action: 'countForApprovalTalents'
				}
			},
			countDeniedTalents: {
				method: 'GET',
				params: { 
					action: 'countDeniedTalents'
				}
			}
		});
	}
]);

services.factory('WorkExperience', ['$resource', 
	function($resource) {
		return $resource('/api/service/exp');
 	}
]);

services.factory('AuthService', ['Session',
	function (Session) {
		var authService = {};

		authService.isAuthenticated = function () {
			return !!Session.user;
		};

		authService.isAuthorized = function (authorizedRoles) {
			if(!angular.isArray(authorizedRoles)) authorizedRoles = [authorizedRoles];

			return (authService.isAuthenticated() && 
				authorizedRoles.indexOf(Session.user.userRole) !== -1);
		};

		return authService;
	}
]);

services.service('Session',
	function() {
		this.create = function(user) {
			this.user = user;
		};

		this.destroy = function() {
			this.user = null;
		};

		return this;
	}
);

services.service('UserProfile',
	function() {
		this.setUser = function(user) {
			this.user = user;
		};

		this.destroy = function() {
			this.user = null;
		};

		return this;
	}
);

services.factory('AuthInterceptor', ['$rootScope', '$q', 'AUTH_EVENTS',
	function($rootScope, $q, events) {
		return {
			responseError: function(response) {
			
				$rootScope.$broadcast({
					401: events.notAuthenticated,
					403: events.notAuthorized,
        			419: events.sessionTimeout,
        			440: events.sessionTimeout
				}[response.status], response);
				
				return $q.reject(response);
				
			}
		};
	}
]);