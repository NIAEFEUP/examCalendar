app.factory('constraints', ['$http', function($http) {
  //TODO replace link
  return $http.get('http://localhost/examCalendar/app/resources/constraints.json')
  .success(function(data) {
    return data;
  })
  .error(function(err) {
    return err;
  });
}]);
