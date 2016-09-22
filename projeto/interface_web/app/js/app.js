var app = angular.module('ExamCalendar', ['ngRoute'])
.config(['$httpProvider', '$sceDelegateProvider', function($httpProvider, $sceDelegateProvider) {
	$httpProvider.defaults.withCredentials = true;
	$sceDelegateProvider.resourceUrlWhitelist(['self', 'http://localhost:8080/**', 'http://localhost/**']);
}]);