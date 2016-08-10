app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider
  .when('/login', {
    templateUrl: 'views/login/'
  })
  .when('/calendar', {
    controller: 'CalendarController',
    templateUrl: 'views/calendar/'
  })
  .when('/constraints', {
    controller: 'ConstraintsController',
    templateUrl: 'views/constraints/'
  })
  .when('/importDatabase', {
    templateUrl: 'views/importDatabase/'
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
