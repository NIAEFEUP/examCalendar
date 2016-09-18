app.directive('calendar', function($timeout) {
  return {
    restrict : 'E',
    templateUrl: 'directives/calendar/calendar.html',
    link: function (scope, element, attrs) {
      $timeout(function() {
        REDIPS.drag.init();
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