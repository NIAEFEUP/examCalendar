var app = angular.module('ExamCalendar', ['ngRoute']);
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider
  .when('/login', {
    templateUrl: 'views/login/'
  })
  .when('/calendar', {
    templateUrl: 'views/calendarOverview/'
  })
  .when('/constrains', {
    templateUrl: 'views/constrains/'
  })
  .when('/adminHome', {
    templateUrl: 'views/adminHome/'
  })
  .when('/adminUsers', {
    templateUrl: 'views/adminUsers/'
  })
  .otherwise({
    redirectTo: '/login'
  });

  $locationProvider.html5Mode(false);

}]);
