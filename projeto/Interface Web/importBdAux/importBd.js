

$(document).on('ready', function() {
	
	$("#teachers").fileinput({
		showUpload: true,
		layoutTemplates: {
			main1: "{preview}\n" +
			"<div class=\'input-group {class}\'>\n" +
			"   <div class=\'input-group-btn\'>\n" +
			"       {browse}\n" +
			"       {upload}\n" +
			"       {remove}\n" +
			"   </div>\n" +
			"   {caption}\n" +
			"</div>"
		}
	});

	$("#students").fileinput({
		showUpload: true,
		layoutTemplates: {
			main1: "{preview}\n" +
			"<div class=\'input-group {class}\'>\n" +
			"   <div class=\'input-group-btn\'>\n" +
			"       {browse}\n" +
			"       {upload}\n" +
			"       {remove}\n" +
			"   </div>\n" +
			"   {caption}\n" +
			"</div>"
		}
	});

	$("#exam").fileinput({
		showUpload: true,
		layoutTemplates: {
			main1: "{preview}\n" +
			"<div class=\'input-group {class}\'>\n" +
			"   <div class=\'input-group-btn\'>\n" +
			"       {browse}\n" +
			"       {upload}\n" +
			"       {remove}\n" +
			"   </div>\n" +
			"   {caption}\n" +
			"</div>"
		}
	});
	
});