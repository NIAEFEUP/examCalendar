<?php
	include 'header.html';
	include 'sidebar.html';
?>

<link rel="stylesheet" href="css/calendarOverview.css">
<meta name="viewport" content="width=device-width, user-scalable=no"/><!-- "position: fixed" fix for Android 2.2+ -->
<script type="text/javascript" src="js/calendarOverview-header.js"></script>
<script type="text/javascript" src="js/redips-drag-min.js"></script>
<script type="text/javascript" src="js/calendarOverview-script.js"></script>

<div align="right">
	<input type="checkbox" name="request-checkbox" checked>
	<h4>Pedir/Cancelar Gerador Calendário</h4>
</div>
<div align="right">
	<button type="button">
	<h4>Verificar Calendário</h4></button>
</div>

<div class="page-header">
	<h1>Lista de Calendários</h1>
</div>

<!-- tables inside this DIV could have draggable content -->
<div id="redips-drag">
	<table id="table1">
		<colgroup><col width="100"/><col width="100"/><col width="100"/><col width="100"/><col width="100"/><col width="100"/><col width="100"/></colgroup>
		<tr class="row-head">
			<td class="elem-empty redips-mark"></td>
			<td class="redips-mark">Segunda-feira</td>
			<td class="redips-mark">Terça-feira</td>
			<td class="redips-mark">Quarta-feira</td>
			<td class="redips-mark">Quinta-feira</td>
			<td class="redips-mark">Sexta-feira</td>
			<td class="redips-mark">Sábado</td>
		</tr>
		<tr class="row-dates">
			<td class="elem-empty redips-mark"></td>
			<td class="redips-mark"><b>1 de Julho</b></td>
			<td class="redips-mark"><b>2 de Julho</b></td>
			<td class="redips-mark"><b>3 de Julho</b></td>
			<td class="redips-mark"><b>4 de Julho</b></td>
			<td class="redips-mark"><b>5 de Julho</b></td>
			<td class="redips-mark"><b>6 de Julho</b></td>
		</tr>
		<tr class="row-type1">
			<td class="elem-time-interval redips-mark">09h00 - 12h00</td>
			<td><div id="d1" class="redips-drag t1">IART</div></td>
			<td></td>
			<td><div id="d2" class="redips-drag t1">COMP</div></td>
			<td><div id="d3" class="redips-drag t1">SDIS</div></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row-type2">
			<td class="elem-time-interval redips-mark">13h30 - 16h30</td>
			<td><div id="d4" class="redips-drag t1">LBAW</div></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row-type1">
			<td class="elem-time-interval redips-mark">17h00 - 20h00</td>
			<td></td>
			<td><div id="d6" class="redips-drag t1">PPIN</div></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>

		<!--Just for show-->
		<tr class="row-dates">
			<td class="elem-empty redips-mark"></td>
			<td class="redips-mark"><b>8 de Julho</b></td>
			<td class="redips-mark"><b>9 de Julho</b></td>
			<td class="redips-mark"><b>10 de Julho</b></td>
			<td class="redips-mark"><b>11 de Julho</b></td>
			<td class="redips-mark"><b>12 de Julho</b></td>
			<td class="redips-mark"><b>13 de Julho</b></td>
		</tr>
		<tr class="row-type1">
			<td class="elem-time-interval redips-mark">09h00 - 12h00</td>
			<td><div id="d1" class="redips-drag t1">IART</div></td>
			<td></td>
			<td><div id="d2" class="redips-drag t1">COMP</div></td>
			<td><div id="d3" class="redips-drag t1">SDIS</div></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row-type2">
			<td class="elem-time-interval redips-mark">13h30 - 16h30</td>
			<td><div id="d4" class="redips-drag t1">LBAW</div></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr class="row-type1">
			<td class="elem-time-interval redips-mark">17h00 - 20h00</td>
			<td></td>
			<td><div id="d6" class="redips-drag t1">PPIN</div></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</table>
</div>

<script src="js/bootstrap-switch.js"></script>
<script src="js/calendarOverview.js"></script>

<?php
	include 'footer.html';
?>
