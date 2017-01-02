app.controller('CalendarController', ['$scope', '$window', '$http', 'calendar', 'modal', function($scope, $window, $http, calendar, modal) {

  calendar.success(function(data) {
    $scope.weeks = data.weeks;
    $scope.unassigneds = data.unassigneds;
    $scope.rooms = data.rooms;
    $scope.normalDays = 2 * Math.trunc(data.normalSeasonDays / 2);
  });

  $scope.modal = function(id) {
     modal.get(id).success(function(data) {
	  var classrooms = data.classrooms;
	  classrooms.pc = [];
	  classrooms.non_pc = [];
	  for (var i = 0; i < classrooms.length; i++) {
		if (classrooms[i].pc)
			classrooms.pc.push(classrooms[i]);
		else
			classrooms.non_pc.push(classrooms[i]);
	  }
      $scope.lecture = data;
      $('#examInformation').modal('show');
    });
  };

  $scope.setExamPeriod = function (examID, day, time) {
	$http.post('http://localhost:8080/calendar/exams',
		{
			id: examID,
			day: day,
			time: time
		},
		{
			withCredentials: true
		})
    .success(function(data) {
		console.log("Success!");
    })
    .error(function(err, status) {
      window.alert("TODO: Error." + err);
    });
  }
}]);