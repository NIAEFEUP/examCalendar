app.factory('calendar', ['$http', function($http) {
  return $http.get('http://localhost:8080/calendar')
  .success(function(data) {
    return data;
  })
  .error(function(err) {
    return err;
  });
}]);

app.factory('modal', ['$http', function($http) {
  var lecture = {};
  lecture.get = function(id) {
    //TODO replace link
    return $http.get('http://localhost/examCalendar/app/' + id + '.json')
    .success(function(data) {
      return data;
    })
    .error(function(err) {
      return err;
    });
  };

  return lecture;
}]);
