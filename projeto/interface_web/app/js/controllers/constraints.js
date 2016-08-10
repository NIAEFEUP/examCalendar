app.controller('ConstraintsController', ['$scope', '$compile', function($scope, $compile) {
  var constraintId = 1;
  $scope.ola = "cenas";
  $scope.addConstraint = function() {
    //var btnhtml = '<constraint id="' + constraintId + '"></constraint>';
    var btnhtml = '<constraint id="' + constraintId + '">'+
    '<h3>Constraint Example'+
      '<button class="btn btn-primary" type="button" onclick="removeConstraint('+constraintId+')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'+
      '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button>'+
      '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>'+
    '</h3></constraint>';
    //var temp = $compile(btnhtml)($scope);
    //angular.element(document.getElementById('constraints')).append(temp);
    $('#constraints').append(btnhtml);
    constraintId++;
  };
}]);

function removeConstraint(id) {
  var index = 1;
  $('#constraints > constraint').each(function () {
    console.log(id);
    if ($(this).attr('id') == id) {
      console.log(index);
      $('#constraints > constraint:nth-child('+index+')').remove();
    } else {
      index++;
    }
  });
};
