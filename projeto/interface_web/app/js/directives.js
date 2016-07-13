app.directive('usersSidebar', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/usersSidebar.html'
  };
});

app.directive('usersHeader', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/usersHeader.html'
  };
});

app.directive('calendar', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/calendar.html',
    link: function (scope, element, attrs) {
      REDIPS.drag.init();
    }
  };
});
