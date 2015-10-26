'use strict';

// Declare app level module which depends on filters, and services
angular.module('KCVLending', [
	'ui.router',
	'ngCookies',
	'ui.utils',
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
			});
		
		$locationProvider.hashPrefix('!');
		
		$httpProvider.interceptors.push(['$injector', function ($injector) {
			return $injector.get('AuthInterceptor');
		}]);
	}
]).
run(['$rootScope', 'AUTH_EVENTS', 'AuthService', 'USER_ROLES', 'Auth', 'Session',
		function($rootScope, events, AuthService, roles, Auth, Session) {
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