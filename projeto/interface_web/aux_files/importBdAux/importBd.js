
/* Plugin utilizado: http://plugins.krajee.com/file-input */

$(document).on('ready', function() {
	
	$('.uploadFile').fileinput({
		showUpload: true,
		uploadUrl: 'upload.php',
		dropZoneEnabled: false
	});
	
});