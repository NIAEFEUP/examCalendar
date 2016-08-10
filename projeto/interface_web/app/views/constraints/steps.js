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
});
