'use strict';

// Declare app level module which depends on filters, and services
angular.module('TalentManagement', [
	'ui.router',
	'ngCookies',
	'ui.utils',
	'TalentManagementFilters',
	'TalentManagementServices',
	'TalentManagementDirectives',
	'TalentManagementControllers'
]).
config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', 
	function($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
		$urlRouterProvider.otherwise("/");

		$stateProvider
			.state('home', {
				url: '/', 
				templateUrl: 'templates/home.html'
			});
		
		$locationProvider.hashPrefix('!');
		
		$httpProvider.interceptors.push(['$injector', function ($injector) {
			return $injector.get('AuthInterceptor');
		}]);
	}
]).
run(['$rootScope',
	function($rootScope) {
		
	}
]).
constant('USER_ROLES', {
	all: '*',
	admin: 'ROLE_ADMIN',
	agency: 'ROLE_AGENCY',
	user: 'ROLE_USER'
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