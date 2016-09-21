var app = angular.module('ExamCalendar', ['ngRoute'])
.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.withCredentials = true;
}]);