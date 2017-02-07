app.factory('auth',['$http', function ($http) {
    return {
        getAuth: function (success, error) {
            $http.get('http://localhost:8080/currentUser').success(success).error(error);
        }
    };
}]);
