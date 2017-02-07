app.controller('LogoutController', ['$window', '$http', '$scope', function($window, $http, $scope) {
  $http.get('http://localhost:8080/logout');
}]);
