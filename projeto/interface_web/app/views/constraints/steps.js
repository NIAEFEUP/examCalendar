$(document).ready(function() {
  $("#steps").steps({
    headerTag: "h3",
    bodyTag: "section",
    transitionEffect: "slideLeft",
    autoFocus: true,
		onFinished: function (event, currentIndex)
		{
      swal({
        title: "Success",
        text: "A new calendar will be generated. This action will take some time.",
        type: "success",
        closeOnConfirm: false
      }, function(){
        location.replace("#/calendar");
      });
		}
  });

  // Date range picker
	var normalRangePicker = $('input[name="normal-season-date-range"]');
	var appealRangePicker = $('input[name="appeal-season-date-range"]');
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
