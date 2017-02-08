var app = angular.module('ExamCalendar', ['ngRoute'])
	.constant('backendURL', 'http://localhost:8080')
	.constant('whiteList', ['self', 'http://localhost:8080/**', 'http://localhost/**'])
	.config(['$httpProvider', '$sceDelegateProvider', 'backendURL', 'whiteList', function($httpProvider, $sceDelegateProvider, backendURL, whiteList) {
		$httpProvider.defaults.withCredentials = true;
		$sceDelegateProvider.resourceUrlWhitelist(whiteList);
	}])