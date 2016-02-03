<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
<link href="updateFileLib/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
<script src="updateFileLib/js/plugins/canvas-to-blob.min.js" type="text/javascript"></script>
<script src="updateFileLib/js/fileinput.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" type="text/javascript"></script>

<?php
include 'Templates/header.html';
?>


<div class="row">
	<div class="col-lg-offset-3 col-lg-6">
		<input id="input-fcount-4" name="inputfcount4[]" multiple type="file" class="file-loading" accept="image/*">
		<script>
			$("#input-fcount-4").fileinput({
				minFileCount: 2,
				validateInitialCount: true,
				overwriteInitial: false,
				initialPreview: [
				"<img style='height:160px' src='http://loremflickr.com/200/150/city?random=1'>",
				"<img style='height:160px' src='http://loremflickr.com/200/150/city?random=2'>",
				],
				initialPreviewConfig: [
				{caption: "City-1.jpg", width: "120px", url: "/site/file-delete", key: 1},
				{caption: "City-2.jpg", width: "120px", url: "/site/file-delete", key: 2}, 
				],
				allowedFileExtensions: ["jpg", "png", "gif"]
			});
		</script>
	</div>
</div>

<?php
include 'Templates/footer.html';
?>
