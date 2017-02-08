app.controller('LoginController', ['$window', '$http', '$scope', 'backendURL', function($window, $http, $scope, backendURL) {

  $scope.login = function() {
    var email = angular.element('#email').val();
    var password = angular.element('#password').val();
    $http.post(backendURL + '/login',
		{"email": email, "password": password},
		{
		headers:
			{'Content-Type': 'application/json'},
		withCredentials: true
		})
    .success(function(data) {
      $window.location.href = 'calendar';
    })
    .error(function(err, status) {
      window.alert("TODO: Authentication error.");
    });
  };

}]);
