app.controller('LogoutController', ['$window', '$http', '$scope', 'backendURL', function($window, $http, $scope, backendURL) {
  
  $http.get(backendURL + '/logout')
  .success(function(data) {
    $window.location.href = '#/login';
  })
  .error(function(err, status) {
    $window.location.href = '#/login';
  });

}]);
