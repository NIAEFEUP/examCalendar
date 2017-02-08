app.controller('LogoutController', ['$window', '$http', '$scope', 'backendURL', function($window, $http, $scope, backendURL) {
  $http.get(backendURL + '/logout');
}]);
