app.controller('CalendarController', ['$scope', 'calendar', 'modal', function($scope, calendar, modal) {
  calendar.success(function(data) {
    $scope.weeks = data.weeks;
    $scope.unassigneds = data.unassigneds;
    $scope.classrooms = data.classrooms;
    $scope.normalDays = 2 * Math.trunc(data.normalSeasonDays / 2);
    console.log($scope.normalDays);
  });

  $scope.modal = function(id) {
    //TODO pass id as argument
     modal.get('lecture').success(function(data) {
      $scope.lecture = data;
      $('#examInformation').modal('show');
    });
  };

  $scope.isActive = function(array, id) {
    return array != null && array.indexOf(id) != -1;
  };

}]);
