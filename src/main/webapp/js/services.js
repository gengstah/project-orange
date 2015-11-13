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
  			registerTalent: { 
  				method: 'POST', 
  				params: { 
  					action: 'registerTalent'
  				}
  			},
  			registerAgency: { 
  				method: 'POST', 
  				params: { 
  					action: 'registerAgency'
  				}
  			},
  			updateTalent: { 
  				method: 'POST', 
  				params: { 
  					action: 'updateTalent'
  				}
  			},
  			updateAgency: { 
  				method: 'POST', 
  				params: { 
  					action: 'updateAgency'
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

services.factory('TalentApplyEvent', ['$resource', 
	function($resource) {
		return $resource('/api/service/talent/apply/:id', {}, {
			apply: {
				method: 'POST',
				headers : { 
  					'Content-Type': 'application/x-www-form-urlencoded' 
  				}
			}
		});
	}
]);

services.factory('TalentWithdrawEvent', ['$resource', 
  	function($resource) {
  		return $resource('/api/service/talent/withdraw/:id', {}, {
  			withdraw: {
				method: 'POST',
				headers : { 
  					'Content-Type': 'application/x-www-form-urlencoded' 
  				}
			}
  		});
  	}
]);

services.factory('Agency', ['$resource', 
	function($resource) {
		return $resource('/api/service/agency/:action', {}, {
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
					action: 'countApprovedAgencies'
				}
			},
			countForApprovalTalents: {
				method: 'GET',
				params: { 
					action: 'countForApprovalAgencies'
				}
			},
			countDeniedTalents: {
				method: 'GET',
				params: { 
					action: 'countDeniedAgencies'
				}
			},
			addTalentToEvent: {
				method: 'POST',
				params: { 
					action: 'addTalentToEvent'
				},
				headers : { 
					'Content-Type': 'application/x-www-form-urlencoded' 
				}
			},
			removeTalentFromEvent: {
				method: 'POST',
				params: { 
					action: 'removeTalentFromEvent'
				},
				headers : { 
					'Content-Type': 'application/x-www-form-urlencoded' 
				}
			}
		});
	}
]);

services.factory('Event', ['$resource', 
	function($resource) {
		return $resource('/api/service/event/:id');
	}
]);

services.factory('EventCount', ['$resource', 
   	function($resource) {
   		return $resource('/api/service/event/:countType/:id', {}, {
   			countApprovedEvents: {
   				method: 'GET',
				params: { 
					countType: 'countApprovedEvents'
				}
   			},
   			countForApprovalEvents: {
   				method: 'GET',
				params: { 
					countType: 'countForApprovalEvents'
				}
   			},
   			countDeniedEvents: {
   				method: 'GET',
				params: { 
					countType: 'countDeniedEvents'
				}
   			},
   			countClosedEvents: {
   				method: 'GET',
				params: { 
					countType: 'countClosedEvents'
				}
   			},
   			countApprovedEventsByAgency : {
   				method: 'GET',
				params: { 
					countType: 'countApprovedEventsByAgency'
				}
   			}
   		});
   	}
]);

services.factory('ApprovedEvent', ['$resource', 
 	function($resource) {
 		return $resource('/api/service/event/approved');
 	}
]);

services.factory('AgencyEvent', ['$resource', 
	function($resource) {
		return $resource('/api/service/event/agency/:id');
	}
]);

services.factory('ApprovedEventsOfAgencyWithTalentApplication', ['$resource', 
 	function($resource) {
 		return $resource('/api/service/event/:action/:agencyId/:talentId', {}, {
 			findApprovedEventsOfAgencyNotAppliedByTalent: {
 				method: 'GET',
  				params: { 
  					action: 'findApprovedEventsOfAgencyNotAppliedByTalent'
				},
				isArray: true
 			},
 			findApprovedEventsOfAgencyAppliedByTalent: {
 				method: 'GET',
  				params: { 
  					action: 'findApprovedEventsOfAgencyAppliedByTalent'
				},
				isArray: true
 			}
 		});
 	}
]);

services.factory('ApprovedAgencyEvent', ['$resource', 
 	function($resource) {
 		return $resource('/api/service/event/agency/approved/:id');
 	}
]);

services.factory('TalentEvent', ['$resource', 
 	function($resource) {
 		return $resource('/api/service/event/talent/:id');
 	}
]);

services.factory('TalentEventQuery', ['$resource', 
  	function($resource) {
  		return $resource('/api/service/event/talentEvent/:type/:id', {}, {
  			event: {
  				method: 'GET',
  				params: { 
  					type: 'event'
				},
				isArray: true
  			},
  			talent: {
  				method: 'GET',
  				params: { 
  					type: 'talent'
				},
				isArray: true
  			}
  		});
  	}
]);

services.factory('EventAction', ['$resource', 
	function($resource) {
		return $resource('/api/service/event/:action', {}, {
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

services.service('EventDetail',
	function() {
		this.create = function(event) {
			this.event = event;
		};

		this.destroy = function() {
			this.event = null;
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