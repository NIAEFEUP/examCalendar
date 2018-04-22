var app = angular.module('ExamCalendar', ['ngRoute'])
	.constant('backendURL', 'http://localhost:8082')
	.constant('whiteList', ['self', 'http://localhost:8082/**', 'http://localhost/**'])
	.config(['$httpProvider', '$sceDelegateProvider', 'backendURL', 'whiteList', function($httpProvider, $sceDelegateProvider, backendURL, whiteList) {
		$httpProvider.defaults.withCredentials = true;
		$sceDelegateProvider.resourceUrlWhitelist(whiteList);
	}])
