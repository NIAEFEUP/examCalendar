app.controller('CalendarController', ['$scope', 'calendar', function($scope, calendar) {
  calendar.success(function(data) {
    $scope.weeks = data.weeks;
  });
}]);
