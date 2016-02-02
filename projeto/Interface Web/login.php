<?php
include 'Templates/header.html';
?>

<link rel="stylesheet" href="loginFilesAux/login.css">

<form name="login" method="post" action="">
	<div class="input-group input-group-lg">
	  <span class="input-group-addon" id="sizing-addon1">@</span>
	  <input name="username" type="text" class="form-control" placeholder="Username" aria-describedby="sizing-addon1">
	</div>

	<br>

	<div class="input-group input-group-lg">
	  <span class="input-group-addon" id="sizing-addon1">*</span>
	  <input name="password" type="password" class="form-control" placeholder="Password" aria-describedby="sizing-addon1">
	</div>

	<br><br>

	<input id="submitButton" class="btn btn-primary" type="submit" value="Login">
</form>

<?php
include 'Templates/footer.html';
?>