$(document).ready( function() {
	var fileSubmitResponded = false;
	$('#importDatabase').steps({
		headerTag: "h3",
		bodyTag: "section",
		transitionEffect: "slideLeft",
		autoFocus: true,
		onContentLoaded: function(event, currentIndex)
		{
			$('#timespan-form').validate();
		},
		onStepChanging: function (event, currentIndex, newIndex)
		{
			if (currentIndex == 0 && newIndex == currentIndex + 1) { // Select timespan
				return $('#timespan-form').valid();
			} else if (currentIndex == 1 && newIndex == currentIndex + 1) { // Import database
			
				if (fileSubmitResponded) {
					fileSubmitResponded = false;
					return true; // This way it won't enter in an infinite form submission loop
				}
				
				var data = new FormData($('#file-import-form')[0]);
				$('#pleaseWaitDialog').modal();
				$.ajax({
					url:$('#file-import-form').attr('action'),
					type:'post',
					cache: false,
					contentType: false,
					processData: false,
					data: data,
					success:function(data){
						fileSubmitResponded = true;
						console.log(data);
						$('#pleaseWaitDialog').modal('hide');
						$('#importDatabase').steps('next');
					},
					error:function(data) {
						console.error(data.responseText);
						$('#pleaseWaitDialog').modal('hide');
					}
				});
				return false;
			}
			return true;
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
	
	// Date range picker
	var normalRangePicker = $('input[name="normal-season-date-range"]');
	var appealRangePicker = $('input[name="appeal-season-date-range"]')
	var rangePickers = $('input[name="normal-season-date-range"], input[name="appeal-season-date-range"]');
	normalRangePicker.daterangepicker({
		autoUpdateInput: false,
		locale: {
			format: 'DD-MM-YYYY'
		}
		}, function(start, end, label) {
		
			var date = new Date( Date.parse( end ) ); 
			date.setDate( date.getDate() + 1 );
			var newDate = date.toDateString(); 
			newDate = new Date( Date.parse( newDate ) );
			
			appealRangePicker.daterangepicker({
				autoUpdateInput: false,
				minDate: newDate,
				locale: {
					format: 'DD-MM-YYYY'
				}
			});
			appealRangePicker.prop("disabled", false);
	});
	rangePickers.on('apply.daterangepicker', function(ev, picker) {
		$(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	});
	rangePickers.on('cancel.daterangepicker', function(ev, picker) {
		$(this).val('');
	});
});

function updateImportProgress(progress) {
	$('.progress-bar').css('width', progress+'%').attr('aria-valuenow', progress).text(progress+'%');
}