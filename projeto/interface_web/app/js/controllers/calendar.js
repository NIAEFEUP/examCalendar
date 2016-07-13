app.controller('CalendarController', ['$scope', 'calendar', 'modal', function($scope, calendar, modal) {
  calendar.success(function(data) {
    $scope.weeks = data.weeks;
  });

  $scope.modal = function(id) {
    //TODO pass id as argument
     modal.get('lecture').success(function(data) {
      $scope.lecture = data;
      $('#examInformation').modal('show');
    });
  };
}]);
