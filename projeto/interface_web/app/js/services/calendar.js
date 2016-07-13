app.factory('calendar', ['$http', function($http) {
  //TODO replace link
  return $http.get('http://localhost/examCalendar/app/calendar.json')
  .success(function(data) {
    return data;
  })
  .error(function(err) {
    return err;
  });
}]);
