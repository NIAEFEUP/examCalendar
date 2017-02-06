app.controller('LoginController', ['$window', '$http', '$scope', function ($window, $http, $scope) {

    $scope.login = function () {
        var email = angular.element('#email').val();
        var password = angular.element('#password').val();
        $http.post('http://localhost:8080/login',
            {"email": email, "password": password},
            {
                headers: {'Content-Type': 'application/json'},
                withCredentials: true
            })
            .success(function (data) {
                $window.location.href = '#/calendar';
            })
            .error(function (err, status) {
                window.alert("TODO: Authentication error.");
            });
    };

}]);
