

<?php
include 'Templates/header.html';
?>


<div class="row">
	<div class="col-lg-offset-3 col-lg-6">
		<label class="control-label">Ficheiro de estudantes inscritos na UC</label>
		<input class="uploadFile" id="students" name="input23[]" type="file" multiple class="file-loading">
		<br>
		<label class="control-label">Ficheiro de professores que lecionam a UC</label>
		<input class="uploadFile" id="teachers" name="input23[]" type="file" multiple class="file-loading">
		<br>
		<label class="control-label">Ficheiro de salas de exame</label>
		<input class="uploadFile" id="exam" name="input23[]" type="file" multiple class="file-loading">
		<br>
	</div>
</div>


<?php
include 'Templates/footer.html';
?>


<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
<link href="updateFileLib/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
<script src="updateFileLib/js/plugins/canvas-to-blob.min.js" type="text/javascript"></script>
<script src="updateFileLib/js/fileinput.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" type="text/javascript"></script>
<script src="importBdAux/importBd.js" type="text/javascript"></script>