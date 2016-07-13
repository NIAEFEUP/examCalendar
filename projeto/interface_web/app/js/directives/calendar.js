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

app.directive('modal', function($timeout) {
  return {
    restrict : 'E',
    templateUrl: 'directives/calendar/modal.html'
  };
});
