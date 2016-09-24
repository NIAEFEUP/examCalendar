app.controller('LogoutController', ['$window', '$http', '$scope', function($window, $http, $scope) {
  
  $http.get('http://localhost:8080/logout')
  .success(function(data) {
    $window.location.href = '#/login';
  })
  .error(function(err, status) {
    $window.location.href = '#/login';
  });

}]);
