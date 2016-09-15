$(document).ready( function() {
	var normalRangePicker, appealRangePicker, rangePickers;
	var normalStartDate, appealStartDate, normalDuration, appealDuration;

	var fileSubmitResponded = false;
	$('#importDatabase').steps({
		headerTag: "h3",
		bodyTag: "section",
		transitionEffect: "slideLeft",
		autoFocus: true,
		onInit: function(event, currentIndex)
		{
			normalRangePicker = $('input[name="normal-season-date-range"]');
			appealRangePicker = $('input[name="appeal-season-date-range"]')
			rangePickers = $('input[name="normal-season-date-range"], input[name="appeal-season-date-range"]');
			normalRangePicker.daterangepicker({
				autoUpdateInput: false,
				locale: {
					format: 'DD-MM-YYYY'
				}
				}, function(start, end, label) {
					normalStartDate = new Date( Date.parse( start ));
					appealStartDate = new Date( Date.parse( end ) ); 
					normalDuration = Math.floor((appealStartDate - normalStartDate) / 86400000) + 1;
					console.log(normalStartDate, appealStartDate, normalDuration);
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
					}, function(start, end, label) {
						appealDuration = Math.floor((new Date(end) - appealStartDate) / 86400000) + 1;
					});
					appealRangePicker.on('apply.daterangepicker', function(ev, picker) {
						$(this).val(jsDateToString(appealStartDate) + ' - ' + picker.startDate.format('DD/MM/YYYY'));
					});
					appealRangePicker.on('cancel.daterangepicker', function(ev, picker) {
						$(this).val('');
					});
					appealRangePicker.prop("disabled", false);
			});
			normalRangePicker.on('apply.daterangepicker', function(ev, picker) {
				$(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
			});
			appealRangePicker.on('apply.daterangepicker', function(ev, picker) {
				console.log("AAA");
				$(this).val(jsDateToString(appealStartDate) + ' - ' + picker.startDate.format('DD/MM/YYYY'));
			});
			normalRangePicker.on('cancel.daterangepicker', function(ev, picker) {
				$(this).val('');
			});
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
			return true;
			if (currentIndex == 0 && newIndex == currentIndex + 1) { // Select timespan
				if (!$('#timespan-form').valid())
					return false;
					var tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
					var localISOTime = (new Date(normalStartDate - tzoffset)).toISOString().slice(0,-1);
				$.ajax({
					url:$('#timespan-form').attr('action'),
					type:'post',
					data: {
						normalStartDate: localISOTime,
						normalDuration: normalDuration,
						appealDuration: appealDuration
					},
					success:function(data){
						console.log(data);
					},
					error:function(data) {
						console.error(data.responseText);
					}
				});
				return true;
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