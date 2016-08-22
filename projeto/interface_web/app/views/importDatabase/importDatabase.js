$(document).ready( function() {
	 

	var fileSubmitResponded = false;
	$('#importDatabase').steps({
		headerTag: "h3",
		bodyTag: "section",
		transitionEffect: "slideLeft",
		autoFocus: true,
		onInit: function(event, currentIndex)
		{
			$.validator.addMethod("daterange", function( value, element ) {
				if (value.length != 23)
					return false;
				
				var dates = $.map(value.split("-"), $.trim);
				
				if (dates.length != 2 || !isValidDate(dates[0]) || !isValidDate(dates[1]))
					return false;
					
				return true;
			}, 'Date range must be in the format "DD/MM/YYYY - DD/MM/YYYY"');
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
	var appealStartDate;
	normalRangePicker.daterangepicker({
		autoUpdateInput: false,
		locale: {
			format: 'DD-MM-YYYY'
		}
		}, function(start, end, label) {
		
			appealStartDate = new Date( Date.parse( end ) ); 
			appealStartDate.setDate( appealStartDate.getDate() + 1 );
			var newDate = appealStartDate.toDateString(); 
			newDate = new Date( Date.parse( newDate ) );
			appealRangePicker.val(jsDateToString(appealStartDate) + " - ");
			appealRangePicker.daterangepicker({
				autoUpdateInput: false,
				singleDatePicker: true,
				minDate: newDate,
				locale: {
					format: 'DD-MM-YYYY'
				}
			});
			appealRangePicker.prop("disabled", false);
	});
	normalRangePicker.on('apply.daterangepicker', function(ev, picker) {
		$(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
	});
	appealRangePicker.on('apply.daterangepicker', function(ev, picker) {
		$(this).val(jsDateToString(appealStartDate) + ' - ' + picker.startDate.format('DD/MM/YYYY'));
	});
	rangePickers.on('cancel.daterangepicker', function(ev, picker) {
		$(this).val('');
	});
});

function updateImportProgress(progress) {
	$('.progress-bar').css('width', progress+'%').attr('aria-valuenow', progress).text(progress+'%');
}

function jsDateToString(date) {
	return ('0' + date.getDate()).slice(-2) + '/'
             + ('0' + (date.getMonth() + 1)).slice(-2) + '/'
             + date.getFullYear();
}

function isValidDate(date)
{
    var matches = /^(\d{1,2})[-\/](\d{1,2})[-\/](\d{4})$/.exec(date);
    if (matches == null) return false;
    var d = matches[1];
    var m = matches[2]-1;
    var y = matches[3];
    var composedDate = new Date(y, m, d);
    return composedDate.getDate() == d &&
           composedDate.getMonth() == m &&
           composedDate.getFullYear() == y;
}