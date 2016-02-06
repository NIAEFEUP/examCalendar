function addConstraint() {
	var constraint = $('<div class="constraint"></div>');

	constraint.append('<h3>Nova Restrição</h3> ');
	constraint.append('<button class="btn btn-primary" type="button">Editar</button> ');
	constraint.append('<button class="btn btn-primary" type="button">Apagar</button> ');

	$('.constraints').append(constraint);
}