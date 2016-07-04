function addUser() {
	var constrain = $('<div class="userEmail"></div>');

	constrain.append('<span class="emailContent">5. emailexample@email.com</span>');
	constrain.append('<button class="btn btn-primary" type="button">Delete</button><br><br><br> ');

	$('.col-sm-8').append(constrain);
}
