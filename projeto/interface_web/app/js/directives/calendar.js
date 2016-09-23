var redipsInit;

app.directive('calendar', function($timeout) {
  return {
    restrict : 'E',
    templateUrl: 'directives/calendar/calendar.html',
    link: function (scope, element, attrs) {
      $timeout(function() {
		redipsInit();
      });
    }
  };
});

app.directive('unassigned', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/calendar/unassigned.html'
  };
});

app.directive('modal', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/calendar/modal.html'
  };
});

// redips initialization
redipsInit = function () {
	// reference to the REDIPS.drag library and message line
	var	rd = REDIPS.drag;
	// how to display disabled elements
	rd.style.borderDisabled = 'solid';	// border style for disabled element will not be changed (default is dotted)
	rd.style.opacityDisabled = 60;		// disabled elements will have opacity effect
	// initialization
	rd.init();
	rd.event.dropped = function (targetCell) {
		var examID = $(rd.obj).data("id");
		var slot = $(targetCell);
		var date = slot.data("date") || null;
		var time = slot.data("time");
		if (typeof time == 'undefined')
			time = null;
		console.log(date, time);
	};
};