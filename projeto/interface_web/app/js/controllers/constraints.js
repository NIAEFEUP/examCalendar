var rooms, professors, years, period;

app.controller('ConstraintsController', ['$scope', '$compile', 'constraints', function($scope, $compile, constraints) {
  constraints.success(function(data) {
    rooms = data.rooms;
    professors = data.professors;
    years = data.years;
    period = data.period;
  });

  var constraintId = 0;
  $scope.addConstraint = function(type) {
    //TODO create constraint on the server
    var options = '';
    switch(type) {
      case 'Room':
        options = html(rooms);
        break;
      case 'Professor':
        options = html(professors);
        break;
      default: //'Year'
        options = html(years);
        break;
    }

    options += datesHTML();

    var btnhtml = '<constraint id="constraint' + constraintId + '">'+
    '<h3>'+type+' Constraint '+
      '<button class="btn btn-primary" type="button" onclick="removeConstraint('+constraintId+')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'+
      options+
      '</h3></constraint>';
    $('#constraints').append(btnhtml);
    constraintId++;
  };
}]);

function html(array) {
  var html = '<select>';
  array.forEach(function (item, index) {
    if (item.id != null)
      html += ('<option opt="' + item.id + '">'+item.name+'</option>');
    else
      html += ('<option opt="' + item + '">'+item+'</option>');
  });
  html += '</select> ';
  return html;
};

function datesHTML() {
  //TODO 2 calendars should be display here (begin and end)
  // check period variable
  console.log(2);
  return '';
};

function removeConstraint(id) {
  //TODO remove constraint from the server
  $('#constraint'+id).remove();
};
