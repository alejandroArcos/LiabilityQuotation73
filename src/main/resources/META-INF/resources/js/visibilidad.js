function verificarVisibilidad() {
	$("[visibilityExpression]").each(function() {
		var classList = $(this).attr('class').split(/\s+/);
		var idUbicacion = "";
		$.each(classList, function(index, item) {
			/*if ( item.startsWith("idUbicacion") ) {
				idUbicacion = item;
			}*/
			if ( item.indexOf("idUbicacion") == 0 ) {
				idUbicacion = item;
			}
		});
		var strParametrizada = $(this).attr('visibilityExpression').replace(/\$\{/g, "Boolean($('#");
		strParametrizada = strParametrizada.replace(/\}/g, "" + idUbicacion + "').val())");
		
		if(strParametrizada.includes('CHECK')){
			strParametrizada = strParametrizada.replace(".val()", ".is(':checked')");
		}
		
		if ( eval(strParametrizada) ) {
			
			if($(this).hasClass("mdb-select")){
				$("#" + $(this).attr("id") ).closest(".md-form").css('display', '');
			}else if ( $("#"+$( this ).attr("id")).attr('type') == 'checkbox'){
				if( $("#" + $(this).attr("id") ).closest('.switch').length > 0 ){
					$("#" + $(this).attr("id") ).closest('.switch').css('display', '');					
				}else{
					$("#" + $(this).attr("id") ).parent().css('display', '');
				}
			}
			else{
				$("#" + $(this).attr("id") ).parent().css('display', '');
			}
		} else {
			if($(this).hasClass("mdb-select")){
				$("#" + $(this).attr("id") ).closest(".md-form").css('display', 'none');
				$("#"+$( this ).attr("id")).val("");
			}else if ( $("#"+$( this ).attr("id")).attr('type') == 'checkbox'){
				if( $("#" + $(this).attr("id") ).closest('.switch').length > 0 ){
					$("#" + $(this).attr("id") ).closest('.switch').css('display', 'none');					
				}else{
					$("#" + $(this).attr("id") ).parent().css('display', 'none');
				}
				$("#"+$( this ).attr("id")).val("");
			}
			else{
				$("#" + $(this).attr("id") ).parent().css('display', 'none');
				$("#"+$( this ).attr("id")).val("");
			}
		}
	});
}

function verificarDisabled() {
	$("[disableExpression]").each(function() {
		var classList = $(this).attr('class').split(/\s+/);
		var idUbicacion = "";
		$.each(classList, function(index, item) {
			/*if ( item.startsWith("idUbicacion") ) {
				idUbicacion = "." + item;
			}*/
			if ( item.indexOf("idUbicacion") == 0 ) {

				idUbicacion = "." + item;
			}
		});
		var strParametrizada = $(this).attr('disableExpression').replace(/\$\{/g, "Boolean($('#");
		strParametrizada = strParametrizada.replace(/\}/g, "'+idUbicacion).val())");
		if ( eval(strParametrizada) ) {
			$(this).attr('disabled', 'disabled');
			$(this).val("");
		} else {
			$(this).removeAttr('disabled');
		}
	});
}

function setObservableElements() {
	$("[observable]").each(function(index) {
		if ( $(this).prop('type') === 'checkbox' ) {
			$(this).prop('value', $(this).prop('checked') ? "true" : "");
		}
	});
}

function validaCamposPerfil(){
	
	$(".suscriptor").each(function(index){
		
		if(perfilSuscriptor != "1"){
			$(this).parent().css('display', 'none');
			$('p.alert-primary:contains("para Suscripción")').hide();
		}
	});
}

function setEstiloSubtitulos(){
	$(".rowAcordeon .subtitulo").parent().addClass("alert alert-primary");
	$(".rowAcordeon p").addClass("alert alert-primary");
}


$(".ubicacion").on( "change","[observable]",function (){
	
	if($(this).prop('type') === 'checkbox'){
		$(this).prop('value', $(this).prop('checked')?"true":"");
	}
	aplicaFiltros();
	
});