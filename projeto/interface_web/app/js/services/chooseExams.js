app.factory('chooseExams', ['$http', function($http) {
  return $http.get('http://localhost:8080/importDatabase/topics')
}]);