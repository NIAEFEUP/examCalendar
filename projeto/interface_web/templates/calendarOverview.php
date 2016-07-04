<link rel="stylesheet" href="./templates/styles/template.css">
<?php
	include 'header.html';
	include 'sidebar.html';
?>


<link rel="stylesheet" href="css/calendarOverview.css">
<link href="css/bootstrap-switch.css" rel="stylesheet">

<div align="right">
	<input type="checkbox" name="request-checkbox" checked>
	<h4>Pedir/Cancelar Gerador Calendário</h4>
</div>

<div class="page-header">
	<h1>Lista de Calendários</h1>
</div>

<div class="calendars">
	<div class="calendar">
		<h3>1. Exemplo de um Calendário</h3>
		<button class="btn btn-primary" type="button">Consultar/Editar</button>
	</div>
	<div class="calendar">
		<h3>2. Exemplo de outro Calendário</h3>
		<button class="btn btn-primary" type="button">Consultar/Editar</button>
	</div>
</div>

<script src="js/bootstrap-switch.js"></script>
<script src="js/calendarOverview.js"></script>

<?php
	include 'footer.html';
?>