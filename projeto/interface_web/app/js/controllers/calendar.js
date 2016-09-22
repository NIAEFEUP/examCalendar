app.controller('CalendarController', ['$scope', '$window', 'calendar', 'modal', function($scope, $window, calendar, modal) {
  calendar.success(function(data) {
    $scope.weeks = data.weeks;
    $scope.unassigneds = data.unassigneds;
    $scope.rooms = data.rooms;
    $scope.normalDays = 2 * Math.trunc(data.normalSeasonDays / 2);
  });

  $scope.modal = function(id) {
     modal.get('lecture').success(function(data) {
      $scope.lecture = data;
      $('#examInformation').modal('show');
    });
  };

  $scope.isActive = function(array, id) {
    return array != null && array.indexOf(id) != -1;
  };

}]);
