app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider
  .when('/logout', {
    controller: 'LogoutController',
    templateUrl: 'views/login/'
  })
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
	controller: 'ImportDatabaseController',
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

  $locationProvider.html5Mode({
                 enabled: true,
                 requireBase: false
          });
}]);

app.run( function($rootScope, $location, $http) {
  /*// register listener to watch route changes
  $rootScope.$on("$routeChangeStart", function(event, next, current) {
    $http.post('http://localhost:8080/login')
    .success(function (data) {
      if ( next.templateUrl == "views/login" ) {
        $location.path( "/calendar" );
      }
    })
    .error(function(err, status) {
      if ( next.templateUrl == "views/login" ) {
        // already going to #login, no redirect needed
      } else {
        // not going to #login, we should redirect now
        $location.path( "/login" );
      }
    });
  });*/
});
