app.controller('CalendarController', ['$scope', '$window', '$http', 'calendar', 'modal', function($scope, $window, $http, calendar, modal) {

  calendar.success(function(data) {
    $scope.weeks = data.weeks;
    $scope.unassigneds = data.unassigneds;
    $scope.rooms = data.rooms;
    $scope.normalDays = 2 * Math.trunc(data.normalSeasonDays / 2);
  });

  $scope.modal = function(id) {
     modal.get(id).success(function(data) {
	 
	  if (data.normal)
		data.season = "Normal";
	  else
		data.season = "Appeal";
		
	  switch (data.time) {
		case 0: data.hour = "9:00 - 12:00"; break;
		case 1: data.hour = "13:00 - 16:00"; break;
		case 2: data.hour = "17:00 - 20:00"; break;
		default: data.hour = "";
	  }
		
	  var classrooms = data.classrooms;
	  classrooms.pc = [];
	  classrooms.non_pc = [];
	  for (var i = 0; i < classrooms.length; i++) {
		if (classrooms[i].pc)
			classrooms.pc.push(classrooms[i]);
		else
			classrooms.non_pc.push(classrooms[i]);
	  }
      $scope.exam = data;
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
  
  $scope.updateExamRoom = function ($event, examID, roomID) {
	console.log($event.target.checked, examID, roomID);
	if ($event.target.checked) {
		$http.put('http://localhost:8080/calendar/exams/' + examID + '/rooms/' + roomID,
		{
			examID: examID,
			roomID: roomID
		},
		{
			withCredentials: true
		})
		.success(function(data) {
			console.log("Success!");
		})
		.error(function(err, status) {
			swal("Error", "Could not add room.", "error");
		});
	} else {
		$http.delete('http://localhost:8080/calendar/exams/' + examID + '/rooms/' + roomID,
		{
			examID: examID,
			roomID: roomID
		},
		{
			withCredentials: true
		})
		.success(function(data) {
			console.log("Success!");
		})
		.error(function(err, status) {
			swal("Error", "Could not remove room.", "error");
		});
	}
  };
}]);