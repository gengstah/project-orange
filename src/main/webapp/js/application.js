'use strict';

// Declare app level module which depends on filters, and services
angular.module('TalentManagement', [
	'ui.router',
	'ngCookies',
	'ui.utils',
	'vcRecaptcha',
	'TalentManagementFilters',
	'TalentManagementServices',
	'TalentManagementDirectives',
	'TalentManagementControllers'
]).
config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 'USER_ROLES', 
	function($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, roles) {
		$urlRouterProvider.otherwise("/");

		$stateProvider
			.state('home', {
				url: '/', 
				templateUrl: 'templates/home.html',
				data: {
					authorizedRoles: [roles.all]
				}
			})
			.state('events', {
				url: '/events', 
				templateUrl: 'templates/events.html',
				data: {
					authorizedRoles: [roles.all]
				}
			})
			.state('registerTalent', {
				url: '/talent/register', 
				templateUrl: 'templates/talent/register.html',
				data: {
					authorizedRoles: [roles.all]
				}
			})
			.state('login', {
				url: '/login', 
				templateUrl: 'templates/login.html',
				data: {
					authorizedRoles: [roles.all]
				}
			})
			.state('profile', {
				url: '/profile', 
				templateUrl: 'templates/profile-page.html',
				data: {
					authorizedRoles: [roles.user, roles.agency, roles.admin]
				}
			})
			.state('updateProfile', {
				url: '/talent/profile/update', 
				templateUrl: 'templates/talent/profile-update.html',
				data: {
					authorizedRoles: [roles.user, roles.agency, roles.admin]
				}
			})
			.state('changePassword', {
				url: '/changePassword', 
				templateUrl: 'templates/change-password.html',
				data: {
					authorizedRoles: [roles.user, roles.agency, roles.admin]
				}
			})
			.state('talents', {
				url: '/talents', 
				templateUrl: 'templates/admin/talents.html',
				data: {
					authorizedRoles: [roles.admin]
				}
			});
		
		$locationProvider.hashPrefix('!');
		
		$httpProvider.interceptors.push(['$injector', function ($injector) {
			return $injector.get('AuthInterceptor');
		}]);
	}
]).
run(['$rootScope', '$state', 'AUTH_EVENTS', 'AuthService', 'USER_ROLES', 'Auth', 'Session', 'UserProfile',
		function($rootScope, $state, events, AuthService, roles, Auth, Session, UserProfile) {
	$rootScope.$state = $state;
	Auth.get(function(user) {
		if(user != null) {
			Session.create(user);
			$rootScope.$broadcast(events.loginSuccess);
		}
	});

	$rootScope.$on('$stateChangeStart', function (event, next) {
		var authorizedRoles = next.data.authorizedRoles;
		if (authorizedRoles.indexOf(roles.all) === -1 && !AuthService.isAuthorized(authorizedRoles)) {
			event.preventDefault();
			if (AuthService.isAuthenticated()) {
				$rootScope.$broadcast(events.notAuthorized);
			} else {
				$rootScope.$broadcast(events.notAuthenticated);
			}
		}
	});
	
	$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
		$('html, body').stop().animate({
            scrollTop: ($("#page-top").offset().top - 50)
        }, 1250, 'easeInOutExpo');
		
		if(toState.name != 'profile') UserProfile.destroy();
	});
}]).
constant('USER_ROLES', {
	all: '*',
	agency: 'ROLE_AGENCY',
	user: 'ROLE_USER',
	admin: 'ROLE_ADMIN'
}).
constant('AUTH_EVENTS', {
	loginSuccess: 'auth-login-success',
  	loginFailed: 'auth-login-failed',
  	logoutSuccess: 'auth-logout-success',
  	sessionTimeout: 'auth-session-timeout',
  	notAuthenticated: 'auth-not-authenticated',
  	notAuthorized: 'auth-not-authorized'
}).
constant('DATA', {
	pageSize: 20
});