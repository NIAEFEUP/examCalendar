app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider
  .when('/login', {
    controller: 'LoginController',
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
  .when('/faq', {
    templateUrl: 'views/faq/'
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
