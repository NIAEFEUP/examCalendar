<?php
include 'Templates/header.html';
?>

<link rel="stylesheet" href="ConstrainsFilesAux/constrains.css">
<script src="ConstrainsFilesAux/constrains.js" async></script>

<div align="right">
	<button type="button" class="btn btn-default btn-lg" onclick="addConstrain()">
		<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"> Adicionar Restrição</span>
	</button>
</div>

<div class="page-header">
  <h1>Lista de Restrições</h1>
</div>

<!--
Vai ser preciso tornar dinâmica a parte das restrições.
Em baixo está apenas um exemplo.
-->
<div class="restricoes">
	<div class="restricao">
		<h3>1. Exemplo de uma Restrição</h3>
		<button class="btn btn-primary" type="button">Editar</button>
		<button class="btn btn-primary" type="button">Apagar</button>
	</div>
	<div class="restricao">
		<h3>2. Exemplo de outra Restrição</h3>
		<button class="btn btn-primary" type="button">Editar</button>
		<button class="btn btn-primary" type="button">Apagar</button>
	</div>
</div>

<?php
include 'Templates/footer.html';
?>