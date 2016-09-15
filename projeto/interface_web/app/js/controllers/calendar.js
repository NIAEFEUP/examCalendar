app.controller('CalendarController', ['$scope', '$window', 'calendar', 'modal', function($scope, $window, calendar, modal) {
  calendar.success(function(data) {
    $scope.weeks = data.weeks;
    $scope.unassigneds = data.unassigneds;
    $scope.classrooms = data.classrooms;
    $scope.normalDays = 2 * Math.trunc(data.normalSeasonDays / 2);
  });

  calendar.error(function(err) {
    $window.location.href = '#/login';
  });

  $scope.modal = function(id) {
     modal.get('lecture').success(function(data) {
      $scope.lecture = data;
      $('#examInformation').modal('show');
    })
    .error(function(err) {
      $window.location.href = '#/login';
    });
  };

  $scope.isActive = function(array, id) {
    return array != null && array.indexOf(id) != -1;
  };

}]);
