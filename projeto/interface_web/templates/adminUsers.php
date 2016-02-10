<?php
	include 'header.html';
	include 'sidebar.html'
?>

<link rel="stylesheet" href="css/adminUsers.css">
<script src="js/adminUsers.js" async></script>

<div align="right">
	<input class="glyphicon glyphicon-plus-sign" aria-hidden="true" type="email" name="email"  class="form-control" id="email" required="true" placeholder="inserir novo email utilizador">
	<button type="button" class="btn btn-default btn-lg" onclick="addUser()">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"> Adicionar Utilizador</span>
	</button>
</div>

<div class="page-header">
  <h1>Lista de Utilizadores</h1>
</div>

<!--
Vai ser preciso tornar dinâmica a parte dos utilizadores.
Em baixo está apenas um exemplo.
-->
<div class="restricoes">
	<div class="restricao">
		<h3>1. Exemplo email utilizador</h3>
		<button class="btn btn-primary" type="button">Apagar</button>
	</div>
	<div class="restricao">
		<h3>2. Exemplo email utilizador</h3>
		<button class="btn btn-primary" type="button">Apagar</button>
	</div>
</div>

<?php
	include 'footer.html';
?>