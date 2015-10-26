'use strict';

/* Filters */

var filters = angular.module('TalentManagementFilters', []);

filters.filter('slice', function() {
	return function(arr, start, end) {
		return arr.slice(start, end);
	}
});