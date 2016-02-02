function addUser() {
	var constrain = $('<div class="restricao"></div>');

	constrain.append('<h3>Exemplo email utilizador</h3> ');
	constrain.append('<button class="btn btn-primary" type="button">Apagar</button> ');

	$('.restricoes').append(constrain);
}