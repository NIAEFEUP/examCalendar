app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
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


app.run(['$rootScope', '$location', 'auth', function ($rootScope, $location, auth) {

    // register listener to watch route changes
    $rootScope.$on("$routeChangeStart", function (event, next, current) {

        auth.getAuth(
            //success
            function (data) {
                //TODO armazenar info do utilizador presente em "data"

                if (next.templateUrl === "views/login/") {
                    $location.path("/calendar");
                }
            },
            //error
            function (data) {
                if (!(next.templateUrl === "views/login/" || next.templateUrl === undefined)) {
                    swal("Não se encontra autenticado. Por favor proceda à autenticação.");
                    $location.path("/login");
                }
            }
        )
    })
}]);
