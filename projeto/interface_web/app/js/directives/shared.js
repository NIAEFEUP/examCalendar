app.directive('usersSidebar', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/shared/usersSidebar.html'
  };
});

app.directive('usersHeader', function() {
  return {
    restrict : 'E',
    templateUrl: 'directives/shared/usersHeader.html'
  };
});
