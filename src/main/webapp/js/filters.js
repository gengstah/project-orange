'use strict';

/* Filters */

var filters = angular.module('TalentManagementFilters', []);

filters.filter('slice', function() {
	return function(arr, start, end) {
		return arr.slice(start, end);
	}
});

filters.filter('ageFilter', function() {
	function calculateAge(birthday) {
    	var ageDifMs = Date.now() - new Date(birthday).getTime();
        var ageDate = new Date(ageDifMs);
        return Math.abs(ageDate.getUTCFullYear() - 1970);
    }

    return function(birthdate) { 
    	return calculateAge(birthdate);
    }; 
});