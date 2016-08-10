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

    //var btnhtml = '<constraint id="' + constraintId + '"></constraint>';
    var btnhtml = '<constraint id="' + constraintId + '">'+
    '<h3>'+type+' Constraint '+
      '<button class="btn btn-primary" type="button" onclick="removeConstraint('+constraintId+')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'+
      options+
      //'<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button>'+
    '</h3></constraint>';
    //var temp = $compile(btnhtml)($scope);
    //angular.element(document.getElementById('constraints')).append(temp);
    $('#constraints').append(btnhtml);
    constraintId++;
  };
}]);

function html(array) {
  var html = '<select>';
  array.forEach(function (item, index) {
    if (item.id != null)
      html += ('<option id="' + item.id + '">'+item.name+'</option>');
    else
      html += ('<option id="' + item + '">'+item+'</option>');
  });
  html += '</select> ';
  return html;
};

function datesHTML() {
  console.log(2);
  return "2";
};

function removeConstraint(id) {
  var index = 1;
  $('#constraints > constraint').each(function () {
    if ($(this).attr('id') == id) {
      $('#constraints > constraint:nth-child('+index+')').remove();
      return;
    } else {
      index++;
    }
  });
};
