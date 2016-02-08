function addConstrain() {
	var constrain = $('<div class="restricao"></div>');

	constrain.append('<h3>Nova Restrição</h3> ');
	constrain.append('<button class="btn btn-primary" type="button">Editar</button> ');
	constrain.append('<button class="btn btn-primary" type="button">Apagar</button> ');

	$('.restricoes').append(constrain);
}