$(document).ready( function() {

	$('#importDatabase').steps({
		headerTag: "h3",
		bodyTag: "section",
		transitionEffect: "slideLeft",
		autoFocus: true,
		onStepChanging: function (event, currentIndex, newIndex)
		{
			if (currentIndex == 0) { // Import database
				// TODO submit files
				$('#pleaseWaitDialog').modal();
			}
			return false;
		},
		onFinishing: function (event, currentIndex)
		{
			return true;
		},
		onFinished: function (event, currentIndex)
		{
			alert("Submitted!");
		}
	});

	var ucmapSubmitted = false;
	var roomsSubmitted = false;
	var professorsSubmitted = false;

	// We can attach the `fileselect` event to all file inputs on the page
	$(document).on('change', ':file', function() {
	var input = $(this);
	console.log($(this).parent().parent().prev().html());
	var label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
	$(this).parent().parent().prev().val(label); // Fill label with the name of the uploaded file.
	});
});

function updateImportProgress(progress) {
	$('.progress-bar').css('width', progress+'%').attr('aria-valuenow', progress).text(progress+'%');
}