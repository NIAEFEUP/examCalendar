app.factory('calendar', ['$http', 'backendURL', function($http, backendURL) {
  return $http.get(backendURL + '/calendar')
  .success(function(data) {
    return data;
  })
  .error(function(err) {
    return err;
  });
}]);

app.factory('modal', ['$http', 'backendURL', function($http, backendURL) {
  var lecture = {};
  lecture.get = function(id) {
    return $http.get(backendURL + '/exams/' + id)
    .success(function(data) {
      return data;
    })
    .error(function(err) {
      return err;
    });
  };

  return lecture;
}]);
