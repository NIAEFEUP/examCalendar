<?php
include 'Templates/header.html';
?>

<link rel="stylesheet" href="AdminUsersAux/adminUsers.css">
<script src="AdminUsersAux/adminUsers.js" async></script>


<div align="right">
	<button type="button" class="btn btn-default btn-lg" onclick="">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"> Exportar Log File</span>
	</button>
</div>

<div class="page-header">
  <h1>Atividade Recente</h1>
</div>

<!--
Vai ser preciso tornar dinâmica a parte das ações realizadas pelos utilizadores
Em baixo está apenas um exemplo.
-->
<div class="restricoes">
	<div class="restricao">
		<h3>1. Ativdade</h3>
	</div>
	<div class="restricao">
		<h3>2. Atividade</h3>
	</div>
</div>

<?php
include 'Templates/footer.html';
?>