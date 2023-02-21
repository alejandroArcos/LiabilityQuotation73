$( document ).ready( function() {
	showLoader();
	window.scrollTo( 0, 0 );
	seleccionaModo();
	validaFormaPago();
	hideLoader();
	setBase64();
	validaTipoMoneda();
} );

function seleccionaModo() {
	console.log('modo: ' + infCotiJson.modo);
	switch (infCotiJson.modo) {
		case modo.NUEVA :
			console.log('modo: ' + infCotiJson.modo);
			break;
		case modo.EDICION :
			console.log('modo: ' + infCotiJson.modo);
			
			$.post(validaAgenteURL, {
        		cotizacion: infCotiJson.cotizacion,
    	    	codigoAgente: ''
    	    }).done(function(data) {
    	    	
    	    	var response = JSON.parse(data);
    	    	
    	    	if(response.code != 0) {
    	    		if(response.code == 3) {
    	    			$("#modalBloqueoAgente").modal('show');
    	    			
    	    			$('#paso3_emitir').attr('disabled', true);
    	    		}
    	    		else {
    	    			showMessageError('.navbar', response.msg, 0);
    	    		}
    	    	}
    	    	else {
    	    		
    	    		$("#paso3_emitir").removeClass('d-none');
    	    		$('#paso3_emitir').attr('disabled', false);
    	    	}
    	    });
			
			break;
		case modo.COPIA :
			console.log('modo: ' + infCotiJson.modo);
			
			$.post(validaAgenteURL, {
        		cotizacion: infCotiJson.cotizacion,
    	    	codigoAgente: ''
    	    }).done(function(data) {
    	    	
    	    	var response = JSON.parse(data);
    	    	
    	    	if(response.code != 0) {
    	    		if(response.code == 3) {
    	    			$("#modalBloqueoAgente").modal('show');
    	    			
    	    			$('#paso3_emitir').attr('disabled', true);
    	    		}
    	    		else {
    	    			showMessageError('.navbar', response.msg, 0);
    	    		}
    	    	}
    	    	else {
    	    		
    	    		$("#paso3_emitir").removeClass('d-none');
    	    		$('#paso3_emitir').attr('disabled', false);
    	    	}
    	    });
			
			break;
		case modo.ALTA_ENDOSO :
			console.log('modo: ' + infCotiJson.modo);
			break;
		case modo.EDITAR_ALTA_ENDOSO :
			console.log('modo: ' + infCotiJson.modo);
			break;
		case modo.CONSULTA :
			$("#paso3_noAceptado").removeClass("d-none");
			$("#paso3_revire").removeClass("d-none");
			
			$("#paso3_emitir").removeClass("d-none");
			
			$("#paso3_calcula").addClass("d-none");
			$("#btnCalculoPrimaSuscriptor").attr('disabled', true);
			
			if(perfilSuscriptor != 1) {
				$("#btnComisionesAgente").attr('disabled', true);
				$("#btnCoaseguro").attr('disabled', true);
				$("#btnReaseguro").attr('disabled', true);
			}
			console.log('modo: ' + infCotiJson.modo);
			
			$.post(validaAgenteURL, {
        		cotizacion: infCotiJson.cotizacion,
    	    	codigoAgente: ''
    	    }).done(function(data) {
    	    	
    	    	var response = JSON.parse(data);
    	    	
    	    	if(response.code != 0) {
    	    		if(response.code == 3) {
    	    			$("#modalBloqueoAgente").modal('show');
    	    			
    	    			$('#paso3_emitir').attr('disabled', true);
    	    		}
    	    		else {
    	    			showMessageError('.navbar', response.msg, 0);
    	    		}
    	    	}
    	    	else {
    	    		
    	    		$("#paso3_emitir").removeClass('d-none');
    	    		$('#paso3_emitir').attr('disabled', false);
    	    	}
    	    });
			
			break;
		case modo.CONSULTA_VOBO_REASEGURO :
			$("#paso3_noAceptado").addClass("d-none");
			$("#paso3_revire").addClass("d-none");
			$("#paso3_emitir").addClass("d-none");
			$("#paso3_calcula").addClass("d-none");
			$("#art41").addClass("d-none");
			$("#btnCalculoPrimaSuscriptor").addClass("d-none");
			$("#paso3_Slip").addClass("d-none");
			$("#paso3_Slip_Word").addClass("d-none");
			$("#paso3_emitir").addClass("d-none");
			$("#btnsuscontinuar").addClass("d-none");
			$("#btnDesgloseCuotas").addClass("d-none");
			$("#paso3_rechazar").addClass("d-none");
			$("#paso3_enviar").addClass("d-none");
			$("#editarRecargo").addClass("d-none");
			$("#editarGastos").addClass("d-none");
			
			$("#btnComisionesAgente").removeClass("d-none");
			$("#btnCoaseguro").removeClass("d-none");
			$("#btnReaseguro").removeClass("d-none");
			break;
		case modo.BAJA_ENDOSO :
			console.log('modo: ' + infCotiJson.modo);
			break;
		case modo.EDITAR_BAJA_ENDOSO :
			console.log('modo: ' + infCotiJson.modo);
			break;
		case modo.FACTURA_492 :
			console.log('modo: ' + infCotiJson.modo);
			$("#tabPaso3_2 a").addClass('d-none');
			break;
		case modo.EDICION_JAPONES:
			console.log('modo: ' + infCotiJson.modo);
			
			$.post(validaAgenteURL, {
        		cotizacion: infCotiJson.cotizacion,
    	    	codigoAgente: ''
    	    }).done(function(data) {
    	    	
    	    	var response = JSON.parse(data);
    	    	
    	    	if(response.code != 0) {
    	    		if(response.code == 3) {
    	    			$("#modalBloqueoAgente").modal('show');
    	    			
    	    			$('#paso3_emitir').attr('disabled', true);
    	    		}
    	    		else {
    	    			showMessageError('.navbar', response.msg, 0);
    	    		}
    	    	}
    	    	else {
    	    		
    	    		$("#paso3_emitir").removeClass('d-none');
    	    		$('#paso3_emitir').attr('disabled', false);
    	    	}
    	    });
			
			break;
		case modo.REASEGURO:
		case modo.COASEGURO:
			break;
		case modo.VOBO_REASEGURO:
			showLoader();
			$("#btnReaseguro").click();
			break;
		default:
			showMessageError('.navbar', msj.es.errorInformacion, 0);
			console.error(" Modo default");
			break;
	}
}

function validaFormaPago() {
	switch(datosGen.p_formaPago) {
	case 5954:
	case 5955:
	case 5956:
		$(".editarCampo").addClass('d-none');
		break;
	default:
		break;
	}
}

function validaTipoMoneda() {
	$.each( $( ".moneda" ), function(key, registro) {
		daFormatoMoneda( registro );
	} );
}

$("#paso3_emitir").click(function() {
	
	$("#paso3_form").submit();
});

var countDeducible = 1
function addDeducibleJsp(){
	if( $('#rowDeduciblesLibre .rowDeducible').length < 5){
		$('#rowDeduciblesLibre').append(`
				<div class="row rowDeducible">
				<div class="col-md-3">
				<div class="md-form">
				<input type="text" id="txtDeducible`+countDeducible+`" class="form-control descripcionDed">
				</div>
				</div>
				<div class="col-md-2">
				<div class="md-form">
				<input type="text" id="mdlPorcenDedu`+countDeducible+`" class="form-control porcenDed">
				</div>
				</div>
				<div class="col-md-3">
				<div class="md-form">
					<input type="text" id="mdlCritDedu`+countDeducible+`" class="form-control criterioDed">
				</div>
				</div>
				<div class="col-md-2">
				<div class="md-form">
				<input type="text" id="mdlMontoDedu`+countDeducible+`" class="form-control montoDed">
				</div>
				</div>
				<div class="col-md-2">
				<div class="md-form form-group">
				<select name="dc_moneda" id="mdlUnidadDedu`+countDeducible+`" class="mdb-select form-control-sel unidadDed">
				<option value="1" selected>opc1</option>
				<option value="2">opc2</option>
				<option value="3">opc3</option>
				</select>
				</div>
				</div>
				</div>
		`);
//		selectDestroy( $('#mdlCritDedu'+countDeducible) , false);
		selectDestroy( $('#mdlUnidadDedu'+countDeducible) , false);
		countDeducible++;
	}else{
		showMessageError('#modalDeducibles .modal-header', 'Máximo 5 deducibles adicionales', 0);
	}
}

/*
$("#btnComisionesAgente").click(function(e){
	$("#modalComAgentes").modal('show');
});
*/

$("#btnComisionesAgente").click(function(){
	$.post(comisionesAgenteURL,{
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version,
		pantalla: infCotiJson.pantalla
	})
		.done(function(data){
			responseComision = JSON.parse(data);
			$("#tableComisionesBody").empty();
			
			var disabled = "";
			
			if(infCotiJson.modo == modo.FACTURA_492 || infCotiJson.modo == modo.CONSULTA || infCotiJson.modo == modo.CONSULTA_VOBO_REASEGURO) {
				disabled = "disabled";
				$("#btnGuardarComisionesAgente").addClass("d-none");
			}
			
			$.each(responseComision.lista, function(index, value){
				/*$("#tableComisionesBody").append('<tr><td>' + value.ramo + '</td><td>' + value.descripcion + "</td><td class='text-right comision' ramo='"+value.ramo+"' valorMin='"+ value.min_valor +"' valorMax='" + value.max_valor + "' contenteditable='true'> <input type='text' class='comision2'> " + value.comision + '</td></tr>')*/
				$("#tableComisionesBody").append('<tr><td>' + value.ramo + '</td><td>' + value.descripcion + '</td><td class="text-right comision" ramo="'+value.ramo+'" valorMin="'+ value.min_valor +'" valorMax="' + value.max_valor + '"> <input type="text" class="auxPorcen" value="' + value.comision + '" ' + disabled + '> </td></tr>')
			});
			$("#modalComisionesAgente").modal('show');
		});
});

/*
$("#paso3_enviar").click(function(e){
	showLoader();
	
	$("#paso3_form").submit();
});

$("#paso3_nextJK").click(function(e){
	showLoader();
	
	$("#paso3_form").submit();
});
 */

function saveComisiones(){
	generaComisiones();
	guardaComisiones();
}

function generaComisiones(){
comisionesAg = [];
	
	$.each($("#bodyModalComisiones .trComision"), function(index, value){
			var comiAux = new Object();
			
			comiAux.ramo = $(this).find('td.tdRamo').text().trim();
			comiAux.cobertura = $(this).find('td.tdCobertura').text();
			comiAux.comisionNueva = $(this).find('td.tdComision').text();
			
			comisionesAg.push(comiAux);

	});
}

function guardaComisiones(){
	
	showLoader();
	$.post( ligasServicios.saveComisionesA, {
		comisionesAg: JSON.stringify(comisionesAg),
		infoCotiResponse: infoCotiResponse
	} ).done( function(data) {
		if( !valIsNullOrEmpty(data) ){
			var jsonResponse = JSON.parse(data);
			if( jsonResponse.code == 0 ){
				showMessageSuccess('.navbar', jsonResponse.msg, 0);				
			}
		}else{
			showMessageError('.navbar', 'Error en servicios', 0);
		}
		$("#modalComAgentes").modal('hide');
		hideLoader();
	} );
}

function saveDeducibles(){
	generaDeducibles();
	guardaDeducibles();
}

function generaDeducibles(){
	p_deduciblesFijo = [];
	
	var deduFijo1 = new Object();
	deduFijo1.descripcion = "Riesgos Ordinarios de Transito";
	deduFijo1.porcentaje = valIsNullOrEmpty( $('#mdlPorcenROT').val() ) ? 0.0 : parseFloat( $('#mdlPorcenROT').val() );
	deduFijo1.idCriterio = valIsNullOrEmpty( $('#mdlCritROT').val() ) ? 0 : parseInt( $('#mdlCritROT').val() );
	deduFijo1.monto = valIsNullOrEmpty( $('#mdlMontoROT').val() ) ? 0 : parseInt( $('#mdlMontoROT').val() );
	deduFijo1.idMoneda = valIsNullOrEmpty( $('#mdlUnidadROT').val() ) ? 0 : parseInt( $('#mdlUnidadROT').val() );
	p_deduciblesFijo.push(deduFijo1);

	var deduFijo2 = new Object();
	deduFijo2.descripcion = "Robo";
	deduFijo2.porcentaje = valIsNullOrEmpty( $('#mdlPorcenRobo').val() ) ? 0.0 : parseFloat( $('#mdlPorcenRobo').val() );
	deduFijo2.idCriterio = valIsNullOrEmpty( $('#mdlCritRobo').val() ) ? 0 : parseInt( $('#mdlCritRobo').val() );
	deduFijo2.monto = valIsNullOrEmpty( $('#mdlMontoRobo').val() ) ? 0 : parseInt( $('#mdlMontoRobo').val() );
	deduFijo2.idMoneda = valIsNullOrEmpty( $('#mdlUnidadRobo').val() ) ? 0 : parseInt( $('#mdlUnidadRobo').val());
	p_deduciblesFijo.push(deduFijo2);

	var deduFijo3 = new Object();
	deduFijo3.descripcion = "Averías en Sistema de Refrigeración";
	deduFijo3.porcentaje = valIsNullOrEmpty( $('#mdlPorcenASF').val() ) ? 0.0 : parseFloat( $('#mdlPorcenASF').val() );
	deduFijo3.idCriterio = valIsNullOrEmpty( $('#mdlCritASF').val() ) ? 0 : parseInt( $('#mdlCritASF').val() );
	deduFijo3.monto = valIsNullOrEmpty( $('#mdlMontoASF').val() ) ? 0 : parseInt( $('#mdlMontoASF').val() );
	deduFijo3.idMoneda = valIsNullOrEmpty( $('#mdlUnidadASF').val() ) ? 0 : parseInt( $('#mdlUnidadASF').val() );
	p_deduciblesFijo.push(deduFijo3);
	
	
	p_deduciblesLibre = [];
	
	$.each($("#rowDeduciblesLibre .rowDeducible"), function(index, value){
		var deduAux = new Object();
		
		deduAux.descripcion = $(this).find('input.descripcionDed').val();
		deduAux.porcentaje = valIsNullOrEmpty( $(this).find('input.porcenDed').val() ) ? 0.0 : parseFloat( $(this).find('input.porcenDed').val() );
		deduAux.criterio = $(this).find('input.criterioDed').val();
		deduAux.monto = valIsNullOrEmpty( $(this).find('input.montoDed').val() ) ? 0 : parseInt( $(this).find('input.montoDed').val() );
		deduAux.idMoneda = valIsNullOrEmpty( $(this).find('select.unidadDed').val() ) ? 0 : parseInt( $(this).find('select.unidadDed').val() );
		
		p_deduciblesLibre.push(deduAux);
		
	});
	
}

function guardaDeducibles(){
	
	showLoader();
	$.post( ligasServicios.saveDeducibles, {
		p_deduciblesFijo: JSON.stringify(p_deduciblesFijo),
		p_deduciblesLibre: JSON.stringify(p_deduciblesLibre),
		infoCotiResponse: infoCotiResponse
	} ).done( function(data) {
		if( !valIsNullOrEmpty(data) ){
			var jsonResponse = JSON.parse(data);
			if( jsonResponse.code == 0 ){
				showMessageSuccess('.navbar', jsonResponse.msg, 0);				
			}
		}else{
			showMessageError('.navbar', 'Error en servicios', 0);
		}
		$("#modalDeducibles").modal('hide');
		hideLoader();
	} );
}

function saveClausulas(){
	generaClausulas();
	guardaClausulas();
}

function generaClausulas(){
	clausulasArr = [];
	
	$.each($("#modalClausulas .rowClausulas .form-check"), function(index, value){
			var clausulaAux = new Object();
			
			clausulaAux.idClausula = $(this).find('input').attr('id');
			clausulaAux.descripcion = $(this).find('label').text();
			clausulaAux.aplica = $(this).find('input').is(':checked');
			clausulaAux.obligatoria = $(this).find('input').is(':disabled');
			clausulaAux.idDiferido = 0;
			
			clausulasArr.push(clausulaAux);
	});
	console.log( JSON.stringify(clausulasArr) );
}

function guardaClausulas(){
	showLoader();
	$.post( ligasServicios.saveClausulasA, {
		clausulasArr: JSON.stringify(clausulasArr),
		infoCotiResponse: infoCotiResponse
	} ).done( function(data) {
		if( !valIsNullOrEmpty(data) ){
			var jsonResponse = JSON.parse(data);
			if( jsonResponse.code == 0 ){
				showMessageSuccess('.navbar', jsonResponse.msg, 0);				
			}
		}else{
			showMessageError('.navbar', 'Error en servicios', 0);
		}
		$("#modalClausulas").modal('hide');
		hideLoader();
	} );
}

function fGetSlip(){
	showLoader();
	$.post( ligasServicios.getSlip, {
		/*infoCotiResponse: infoCotiResponse*/
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version,
		pantalla: infCotiJson.pantalla,
		folio: infCotiJson.folio,
		word: 0
	} ).done( function(data) {
		if( !valIsNullOrEmpty(data) ){
			var respuestaJson = JSON.parse(data);
			if( respuestaJson.code == 0 ){
				showMessageSuccess('.navbar', respuestaJson.msg, 0);
				$('#paso3_enviar').attr('disabled', false);
				
				if(suscripcion == 1) {
					$("#btnsuscontinuar").removeClass('d-none');
					$('#btnsuscontinuar').attr('disabled', false);
				}
				else {
					if(perfilSuscriptor != 1) {
						
						$.post(validaAgenteURL, {
		            		cotizacion: infCotiJson.cotizacion,
		        	    	codigoAgente: ''
		        	    }).done(function(data) {
		        	    	
		        	    	var response = JSON.parse(data);
		        	    	
		        	    	if(response.code != 0) {
		        	    		if(response.code == 3) {
		        	    			$("#modalBloqueoAgente").modal('show');
		        	    		}
		        	    		else {
		        	    			showMessageError('.navbar', response.msg, 0);
		        	    		}
		        	    	}
		        	    	else {
		        	    		
						$("#paso3_emitir").removeClass('d-none');
						$('#paso3_emitir').attr('disabled', false);
		        	    	}
		        	    });
						
						
					}
					else {
						$.post(getPermisoEmisionURL, {})
							.done(function(dataE){
								var response = JSON.parse(dataE);
								
								if(response.code == 0) {
									
									$.post(validaAgenteURL, {
					            		cotizacion: infCotiJson.cotizacion,
					        	    	codigoAgente: ''
					        	    }).done(function(data) {
					        	    	
					        	    	var response = JSON.parse(data);
					        	    	
					        	    	if(response.code != 0) {
					        	    		if(response.code == 3) {
					        	    			$("#modalBloqueoAgente").modal('show');
					        	    		}
					        	    		else {
					        	    			showMessageError('.navbar', response.msg, 0);
					        	    		}
					        	    	}
					        	    	else {
					        	    		
									$("#paso3_emitir").removeClass('d-none');
									$('#paso3_emitir').attr('disabled', false);
								}
							});
						}
							});
						}
				}
				
				var buffer = new Uint8Array(respuestaJson.documento);
	            var blob = new Blob([buffer], { type: "application/pdf" });
	            $("#aPdf").attr("href", window.URL.createObjectURL(blob));
	            var link = document.getElementById("aPdf");
	            link.download = respuestaJson.nombre + respuestaJson.extension;
	            link.click();
				
			}else{
				showMessageError('.navbar', respuestaJson.msg, 0);
			}
		}else{
			showMessageError('.navbar', 'Error en servicios', 0);
		}
		hideLoader();
	} );
}

$("#paso3_carga_slip_word").click(function() {
	$("#modalSlipWord").modal('show');
});

$("#paso3_Slip_JI").click(function() {
	$("#modalSlipJI").modal('show');
});

function fGetSlipWord(){
	showLoader();
	$.post( ligasServicios.getSlip, {
		/*infoCotiResponse: infoCotiResponse*/
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version,
		pantalla: infCotiJson.pantalla,
		folio: infCotiJson.folio,
		word: 1
	} ).done( function(data) {
		if( !valIsNullOrEmpty(data) ){
			var respuestaJson = JSON.parse(data);
			if( respuestaJson.code == 0 ){
				showMessageSuccess('.navbar', respuestaJson.msg, 0);
				
				var buffer = new Uint8Array(respuestaJson.documento);
	            var blob = new Blob([buffer], { type: "application/pdf" });
	            $("#aPdf").attr("href", window.URL.createObjectURL(blob));
	            var link = document.getElementById("aPdf");
	            link.download = respuestaJson.nombre + respuestaJson.extension;
	            link.click();
	            
	            $.post( ligasServicios.getEspecificacion, {
	        		cotizacion: infCotiJson.cotizacion,
	        		version: infCotiJson.version,
	        		pantalla: infCotiJson.pantalla,
	        		folio: infCotiJson.folio
	        	} ).done( function(data) {
	        		
	        		var respuestaJsonEsp = JSON.parse(data);
	        		
	        		if( respuestaJsonEsp.code == 0 ) {
	        			
	        			var buffer = new Uint8Array(respuestaJsonEsp.documento);
	    	            var blob = new Blob([buffer], { type: "application/pdf" });
	    	            $("#aPdf").attr("href", window.URL.createObjectURL(blob));
	    	            var link = document.getElementById("aPdf");
	    	            link.download = respuestaJsonEsp.nombre + respuestaJsonEsp.extension;
	    	            link.click();
				
	        			$("#paso3_carga_slip_word").removeClass('d-none');
	        		}
	        		else {
	        			showMessageError('.navbar', respuestaJsonEsp.msg, 0);
	        		}
	        	});
			}else{
				showMessageError('.navbar', respuestaJson.msg, 0);
			}
		}else{
			showMessageError('.navbar', 'Error en servicios', 0);
		}
		hideLoader();
	} );
}

function guardarSlipWord() {
	
	showLoader();
	
	var dataDoc = new FormData();
	
	var url = new URL(window.location.href);
	var auxiliarDoc = '{';

    $.each($('#archivoSlip')[0].files, function(i, file) {
        dataDoc.append('file-' + i, file);
        var nomAux = file.name.split('.');

		if(nomAux.length > 2) {
			
			var nomAux2 = "";
			
			for(i = 0; i < (nomAux.length - 1); i++) {
				nomAux2 += nomAux[i] + ".";
			}
			
			nomAux2 = nomAux2.slice(0,-1);
			
			auxiliarDoc += '\"plantillaSlip\" : {';
	        auxiliarDoc += '\"nom\" : \"' + nomAux2 + '\",';
	        auxiliarDoc += '\"ext\" : \"' + nomAux[nomAux.length - 1] + '\"}';
		}
		else {
	        auxiliarDoc += '\"plantillaSlip\" : {';
	        auxiliarDoc += '\"nom\" : \"' + nomAux[0] + '\",';
	        auxiliarDoc += '\"ext\" : \"' + nomAux[1] + '\"}';
		}
    });
    auxiliarDoc += '}';

	dataDoc.append('auxiliarDoc', auxiliarDoc);
	dataDoc.append('infoCot', JSON.stringify(infCotiJson));
	dataDoc.append('url', url.origin + url.pathname); 
    dataDoc.append('url2', url.origin);
    dataDoc.append('idcatalogo', 22);
	
	$.ajax( {
		url : cargaSlipWordURL, 
		data : dataDoc, 
		processData : false, 
		contentType : false, 
		type : 'POST',
		async: false,
		success : function(data) {
			console.log(data);
			
			hideLoader();
			
			var response = JSON.parse(data);
			
			if(response.msg == "OK") {
				$("#modalSlipWord").modal('hide');
				
				if(suscripcion == 1) {
					$("#btnsuscontinuar").removeClass('d-none');
					$('#btnsuscontinuar').attr('disabled', false);
				}
				else {
					$.post(getPermisoEmisionURL, {})
					.done(function(dataE){
						var response = JSON.parse(dataE);
						
						if(response.code == 0) {
							$("#paso3_emitir").removeClass('d-none');
							$('#paso3_emitir').attr('disabled', false);
						}
						else {
							$('#paso3_enviar').attr('disabled', false);
						}
					});
				}
			}
			else{
				showMessageError('#modalSlipWord .modal-header', response.msg, 0);
			}
			
		}
	});
}

function guardarSlipJI() {
	showLoader();
	
	var dataDoc = new FormData();
	
	var url = new URL(window.location.href);
	var auxiliarDoc = '{';

    $.each($('#archivoSlipJIEsp')[0].files, function(i, file) {
        dataDoc.append('file-' + i, file);
        var nomAux = file.name.split('.');

		if(nomAux.length > 2) {
			
			var nomAux2 = "";
			
			for(i = 0; i < (nomAux.length - 1); i++) {
				nomAux2 += nomAux[i] + ".";
			}
			
			nomAux2 = nomAux2.slice(0,-1);
			
			auxiliarDoc += '\"plantillaSlip\" : {';
	        auxiliarDoc += '\"nom\" : \"' + nomAux2 + '\",';
	        auxiliarDoc += '\"ext\" : \"' + nomAux[nomAux.length - 1] + '\"}';
		}
		else {
	        auxiliarDoc += '\"plantillaSlip\" : {';
	        auxiliarDoc += '\"nom\" : \"' + nomAux[0] + '\",';
	        auxiliarDoc += '\"ext\" : \"' + nomAux[1] + '\"}';
		}
    });
    auxiliarDoc += '}';

	dataDoc.append('auxiliarDoc', auxiliarDoc);
	dataDoc.append('infoCot', JSON.stringify(infCotiJson));
	dataDoc.append('url', url.origin + url.pathname); 
    dataDoc.append('url2', url.origin);
    dataDoc.append('idcatalogo', 7910);
	
	$.ajax( {
		url : cargaSlipWordURL, 
		data : dataDoc, 
		processData : false, 
		contentType : false, 
		type : 'POST',
		async: false,
		success : function(data) {
			console.log(data);
			
			hideLoader();
			
			var response = JSON.parse(data);
			
			if(response.msg == "OK") {
				$("#modalSlipJI").modal('hide');
				$("#btnsuscontinuar").removeClass('d-none');
				$('#btnsuscontinuar').attr('disabled', false);
			}
			else{
				showMessageError('#modalSlipWord .modal-header', response.msg, 0);
			}
			
		}
	});
}

$( '#editaPrima' ).click( function(e) {
	$( '#primaNeta' ).attr("contenteditable",true);
	$( '#primaNeta' ).focus();
} );
$( '#editaRecargo' ).click( function(e) {
	$( '#recargoPago' ).attr("contenteditable",true);
	$( '#recargoPago' ).focus();
} );
$("#primaNeta").focusout(function(){
	$( '#primaNeta' ).attr("contenteditable",false);
  });
$("#recargoPago").focusout(function(){
	$( '#recargoPago' ).attr("contenteditable",false);
});

$('#paso3_rechazar').click(function() {
	boton=0; // Estatus -> Rechazada
    $('#modalRechazarProp').modal('show');
});
$('#paso3_noAceptado').click(function() {
	boton=1; // No aceptada
	$('#modalRechazarProp').modal('show');
});

$('#btnEnvRecha').click(function(e) {
    showLoader();
    e.preventDefault();
    var errores = false;
    errores = (noSelect($('#mdlRechOp')) ? true : errores);
    errores = (valIsNullOrEmpty($('#comentariosRechazarProp').val().trim()) ? true : errores);
    if (errores) {
        showMessageError('#modalRechazarProp .modal-header', 'Los campos son obligatorios', 0);
        hideLoader();
    } else {
        showLoader();
        var url = new URL(window.location.href);
        var motivo = $('#modalRechazarProp .modal-body select').val();
        $.post(ligasServicios.rechazaCoti, {
            cotizacion: infCotiJson.cotizacion,
            version: infCotiJson.version,
            motivoRechazo: motivo,
            motivo: $('#comentariosRechazarProp').val(),
            boton: boton // Estatus -> De acuerdo al botón presionado
        }).done(function(data) {
        	goToHome();
        });
    }
});

function goToHome(){
	showLoader();
	var urlHome = window.location.origin + '/group/portal-agentes' + seleccionaVentana();
	var url = new URL(window.location.href);
	var aux = url.pathname.replace("/paso3", seleccionaVentana());
	window.location.href = url.origin + aux;
}

function seleccionaVentana(){
	return enlace.FAMILIAR;
}

$('#paso3_enviar').click(function(e) {
	showLoader();
	e.preventDefault();

	$.post(ligasServicios.enviarCoti, {
		/*infoCotiResponse: infoCotiResponse*/
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version
	}).done(function(data) {
		if( !valIsNullOrEmpty(data) ){
			var jsonResponse = JSON.parse(data);
			if( jsonResponse.code == 0 ){
				/*goToHome();*/
				
				/*Envio de correo*/
				
				var url = new URL(window.location.href);
				var auxUrl = url.pathname;
			    
			    $.post( sendMailSuscriptorAgenteURL , {
			        cotizacion: infCotiJson.cotizacion,
			        version: infCotiJson.version,
			        folio: infCotiJson.folio,
			        url: url.origin + auxUrl,
			        tipoCotizacion: infCotiJson.tipoCotizacion.toString(),
			        email: $('#txtEmailAgente').val()
			    }).done(function() {
			    	goToHome();
			    });
				
				/*Envio de correo*/
				
			}else{
				hideLoader();
				showMessageError('.navbar', jsonResponse.msg, 0);
			}
		}else{
			hideLoader();
			showMessageError('.navbar', "Error al enviar", 0);
		}
	});

});
$('#paso3_nextJK').click(function(e) {
	showLoader();
	e.preventDefault();
	
	$.post(ligasServicios.continuarJK, {
		infoCotiResponse: infoCotiResponse
	}).done(function(data) {
		if( !valIsNullOrEmpty(data) ){
			var jsonResponse = JSON.parse(data);
			if( jsonResponse.code == 0 ){
				goToHome();				
			}else{
				hideLoader();
				showMessageError('.navbar', jsonResponse.msg, 0);
			}
		}else{
			hideLoader();
			showMessageError('.navbar', "Error al enviar", 0);
		}
	});
	
});

function quitaTipoMoneda(data) {
	return data.replace( /[$,]/g, '' );
}

$('#paso3_calcula').click(function(e) {
	showLoader();
	e.preventDefault();
	
	 showLoader();
     var primaNeta = parseFloat(valIsNullOrEmpty($('#primaNeta').text()) ?
         0 : $('#primaNeta').text().replace(/[$,]/g, ''));
     var primaObj = parseFloat(valIsNullOrEmpty($('#txtPrimaObj').val()) ?
         0 : $('#txtPrimaObj').val().replace(/[$,]/g, ''));
		var gastos = parseFloat(valIsNullOrEmpty($('#gastos').val()) ?
         0 : $('#gastos').val().replace(/[$,]/g, ''));
		var recargoPago = parseFloat(valIsNullOrEmpty($('#recargoPago').val()) ?
         0 : $('#recargoPago').val().replace(/[$,]/g, ''));
     /*var idPerfil = parseInt($('#txtIdPerfilUser').val());
     var minPrima = parseFloat($('#txtMinPrima').val());*/
     var cambioDoll = parseFloat( tpoCambio );
      
     
     if (tipoMoneda == 1) {
         minPrima = minPrima * cambioDoll;
     }
     

     var nuevaCaratula = true;
     if (idPerfil == 1) {
         if (primaObj < primaNeta) {
             nuevaCaratula = false;
             showMessageError('.navbar', 'La prima capturada no puede ser menor a ' + generaFormatoNumerico(primaNeta, true, true, false), 0);
         }
     }
     if (primaObj < minPrima) {
         nuevaCaratula = false;
         showMessageError('.navbar', 'La prima minima para el Cotizador RC es de $100.00USD o su equivalente en moneda nacional.', 1);
     }
	
     if( nuevaCaratula ){
		$.post(ligasServicios.recalcularCot, {
			cotizacion: infCotiJson.cotizacion,
			version: infCotiJson.version,
			p_primaObjetivo: quitaTipoMoneda( $('#txtPrimaObj').val() ),
			p_recargoPago: quitaTipoMoneda( $('#recargoPago').text() ),
			p_gastos: quitaTipoMoneda( $('#gastos').text() )
		}).done(function(data) {
			var respuestaJson = JSON.parse(data);
	        if (respuestaJson.code == 0) {
	        	var btn1 = "";
	        	var btn2 = "";
	        	if( perfilSuscriptor == 1 || perfilJapones == 1 ){
	        		btn1 = "<td><a onclick=\"editarCamposPrima('recargoPago')\">Editar</a></td>";
	        		btn2 = "<td><a onclick=\"editarCamposPrima('gastos')\">Editar</a></td>";
	        	}
	            $('#txtEmailAgente').val(respuestaJson.email);
	            var band = null;
	            $('#tabPaso3').html("");
	            $.each(respuestaJson.datosCaratula, function(k, valCaratula) {
	                if (!(valCaratula.contenedor == band)) {
	                    band = valCaratula.contenedor;
	                    
	                    if( perfilAgenteEjecutivo == 1 ){	                	
	                    	$('#tabPaso3').append("<tr><th>" + band + "</td><td></td><td></th></tr>");	                		                	
		                }else{
		                	$('#tabPaso3').append("<tr><th>" + band + "</td><td></td><td></td><td></th></tr>");	                	
		                }
	                }
	                if( perfilAgenteEjecutivo == 1 ){	                	
	                	$('#tabPaso3').append("<tr><td>" + valCaratula.titulo + "</td><td class=\"number\">" + valCaratula.sa + "</td><td>" + valCaratula.deducible + "</td></tr>");	                	
	                }else{
	                	$('#tabPaso3').append("<tr><td>" + valCaratula.titulo + "</td><td class=\"number\">" + valCaratula.sa + "</td><td class=\"number\">" + valCaratula.prima + "</td><td>" + valCaratula.deducible + "</td></tr>");	                	
	                }
	            });
	            $('#tabPaso3_2').html("<tr><td>Prima Neta:</td><td id='primaNeta' class=\"number\">" + setCoinFormat('' + respuestaJson.primaNeta) + "</td></tr>");
	            $('#tabPaso3_2').append("<tr><td>Recargo por Pago Fraccionado:</td><td class=\"number\" id=\"recargoPago\">" + setCoinFormat('' + respuestaJson.recargo) + "</td>"+btn1+"</tr>");
	            $('#tabPaso3_2').append("<tr><td>Gastos de Expedición:</td><td class=\"number\" id=\"gastos\">" + setCoinFormat('' + respuestaJson.gastos) + "</td>"+btn2+"</tr>");
	            $('#tabPaso3_2').append("<tr><td>I.V.A.:</td><td class=\"number\">" + setCoinFormat('' + respuestaJson.iva) + "</td></tr>");
	            $('#tabPaso3_3').html(setCoinFormat('' + respuestaJson.total));
	            var tipoMonSelect = parseInt(tipoMoneda);
	            if (tipoMonSelect == 1) {
	                $('#titPrimaObj').text("Prima Objetivo (Pesos):");
	            } else {
	                $('#titPrimaObj').text("Prima Objetivo (Dolares):");
	            }
	            
	            
	            hideLoader();
	            
	            $("#paso3_Slip").attr('disabled', false);
	        	$("#paso3_Slip_Word").attr('disabled', false);
	            
	            showMessageSuccess('.navbar', 'Información actualizada correctamente', 0);
	        } else {
	            agregaAlertError(respuestaJson.msg);
	
	            hideLoader();
	        }
		});
     }
     hideLoader();
	
});

$("#tabPaso3_2" ).on("blur", '.moneda', function(event){
	this.textContent = this.textContent.replace(/[^0-9\.]/g,'');
	daFormatoMonedaText($(this));
});

function daFormatoMonedaText(campo){
	if(!valIsNullOrEmpty($( campo ).text())){
		$( campo ).text(formatter.format($( campo ).text()));
	}
}

$(".moneda" ).on("focus", function(){
	var abc = $(this).text().replace(/[^0-9\.,]/g,'').replace(',', '');
	$(this).text(abc);
});

$( "#tableComisionesBody" ).on( "click", ".colorRed input", function() {
  $(this).parent().removeClass('colorRed');
});

$( "#tableComisionesBody" ).on("keyup", ".auxPorcen",function() {
	validaCampoPorcentaje();
});

function validaCampoPorcentaje(){
	$(event.target).val(function(index, value) {
		 var aux = value.replace(/[$,]/g, '');
		 aux = aux.replace(/\D/g, "")
		 .replace(/([0-9])([0-9]{2})$/, '$1.$2')
		 .replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
	        
		 aux = aux.replace(/\,/g, '');
		 
		 if (parseInt(aux) > 100) {
			 /*showMessageError('.navbar', 'El porcentaje no puede superar el 100% ', 0);*/
			 return '100.00';
		 }
		 var res = aux.split(".");
		 if( res[0].length > 3){
			 /*showMessageError('.navbar', 'El porcentaje mínimo es 00.000001% ', 0);*/
			 return '0.01'
		 }
	        
		 return aux;
	 });
}

$("#btnGuardarComisionesAgente").click(function(){
	generaComosionesAgente();
	guardaComosionesAgente();
});

function generaComosionesAgente(){
	flagValMax = true;
	comsisionesAgArr = [];
	
	$.each($("#modalComisionesAgente .comision"), function(index, value){
			var comisionAux = new Object();
			
			comisionAux.codigo_ramo = $(this).attr('ramo');
			comisionAux.valor =  parseFloat($(this).find('input').val());
			
			if ( comisionAux.valor  > parseFloat($(this).attr('valormax')) ) {
				flagValMax = false;
				$(this).addClass('colorRed')
			}
			
			comsisionesAgArr.push(comisionAux);
	});
	console.log( JSON.stringify(comsisionesAgArr) );
	console.log('flag: ' + flagValMax);
}

function guardaComosionesAgente(){
	if( flagValMax ){
		$.post(guardaComisionesAgenteURL,{
			cotizacion: infCotiJson.cotizacion,
			version: infCotiJson.version,
			pantalla: infCotiJson.pantalla,
			comisiones: JSON.stringify(comsisionesAgArr)
		}).done(function(data){
			console.log(data);
			var jsonResponse = JSON.parse(data);
			if(jsonResponse.code == 0 ){
				$("#modalComisionesAgente").modal('hide');
				/*showMessageSuccess('.navbar', jsonResponse.msg, 0);*/
				/*RecalculaCaratula*/
				fGetCarátula();
				/*RecalculaCaratula*/
			}
		});		
	}else{
		showMessageError( '#modalComisionesAgente .modal-header', "La comisión ingresada no debe ser mayor a la comisión máxima del ramo", 0 );
	}
}

function fGetCarátula(){
	$.post(ligasServicios.getCaratulaURL,{
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version
	}).done(function(data){
		var respuestaJson = JSON.parse(data);
        if (respuestaJson.code == 0) {
        	var btn1 = "";
        	var btn2 = "";
        	if( perfilSuscriptor == 1 || perfilJapones == 1 ){
        		btn1 = "<td><a onclick=\"editarCamposPrima('recargoPago')\">Editar</a></td>";
        		btn2 = "<td><a onclick=\"editarCamposPrima('gastos')\">Editar</a></td>";
        	}
            $('#txtEmailAgente').val(respuestaJson.email);
            var band = null;
            $('#tabPaso3').html("");
            $.each(respuestaJson.datosCaratula, function(k, valCaratula) {
                if (!(valCaratula.contenedor == band)) {
                    band = valCaratula.contenedor;
                    
                    if( perfilAgenteEjecutivo == 1 ){	                	
                    	$('#tabPaso3').append("<tr><th>" + band + "</td><td></td><td></th></tr>");	                		                	
	                }else{
	                	$('#tabPaso3').append("<tr><th>" + band + "</td><td></td><td></td><td></th></tr>");	                	
	                }
                }
                if( perfilAgenteEjecutivo == 1 ){	                	
                	$('#tabPaso3').append("<tr><td>" + valCaratula.titulo + "</td><td class=\"number\">" + valCaratula.sa + "</td><td>" + valCaratula.deducible + "</td></tr>");	                	
                }else{
                	$('#tabPaso3').append("<tr><td>" + valCaratula.titulo + "</td><td class=\"number\">" + valCaratula.sa + "</td><td class=\"number\">" + valCaratula.prima + "</td><td>" + valCaratula.deducible + "</td></tr>");	                	
                }
            });
            $('#tabPaso3_2').html("<tr><td>Prima Neta:</td><td id='primaNeta' class=\"number\">" + setCoinFormat('' + respuestaJson.primaNeta) + "</td></tr>");
            $('#tabPaso3_2').append("<tr><td>Recargo por Pago Fraccionado:</td><td class=\"number\" id=\"recargoPago\">" + setCoinFormat('' + respuestaJson.recargo) + "</td>"+btn1+"</tr>");
            $('#tabPaso3_2').append("<tr><td>Gastos de Expedición:</td><td class=\"number\" id=\"gastos\">" + setCoinFormat('' + respuestaJson.gastos) + "</td>"+btn2+"</tr>");
            $('#tabPaso3_2').append("<tr><td>I.V.A.:</td><td class=\"number\">" + setCoinFormat('' + respuestaJson.iva) + "</td></tr>");
            $('#tabPaso3_3').html(setCoinFormat('' + respuestaJson.total));
            var tipoMonSelect = parseInt(tipoMoneda);
            if (tipoMonSelect == 1) {
                $('#titPrimaObj').text("Prima Objetivo (Pesos):");
            } else {
                $('#titPrimaObj').text("Prima Objetivo (Dolares):");
            }
            
            
            hideLoader();
            showMessageSuccess('.navbar', 'Información actualizada correctamente', 0);
        } else {
            agregaAlertError(respuestaJson.msg);

            hideLoader();
        }
	});
}

$('#txtArt41').on('keyup', function() {
    $(event.target).val(function(index, value) {
        var aux = value.replace(/\D/g, "")
        if (parseInt(aux) > 100) {
            showMessageError('.navbar', 'La comisión no pude superar el 100% ', 0);
            return '100';
        }
        return aux;
    });
});

$('#btnCederComision').click(function(e) {
    try {
        showLoader();
        if (valIsNullOrEmpty($('#txtArt41').val())) {
            showMessageError('.navbar', 'Sin comisión ', 0);
        } else {
            $.post( ligasServicios.secionComisionUrl, {
                seccomi: $('#txtArt41').val(),
                /*tipoCoti : infCotiJson.tipoCotizacion.toString(),*/
                cotizacion: infCotiJson.cotizacion,
                version: infCotiJson.version
            }).done(function(data) {
                sessionExtend();
                var respuestaJson = JSON.parse(data);
                if (respuestaJson.code == 0) {
                    showMessageSuccess('.navbar', 'Información actualizada correctamente', 0);
                } else {
                    showMessageError('.navbar', respuestaJson.msg, 0);
                }
            }).fail(function() {
                showMessageError('.navbar', 'Error al consultar la información', 0);
                hideLoader();
            });
        }
        hideLoader();
    } catch (err) {

        hideLoader();
        showMessageError('.navbar', 'Error al consultar la información', 0);
    }
});

function editarCamposPrima(campo){
	
	showMessageError('.navbar', "Se debe recalcular prima", 0);
	
	$("#"+campo).attr('disabled', false);
	$("#"+campo).focus();
	
	$("#btnEnvCotiSusAgente").attr('disabled', true);
	$("#btnContinuarJK").attr('disabled', true);
	$("#paso3_slip").attr('disabled', true);
	$("#paso3_next").attr('disabled', true);
	$("#paso3_next").addClass('d-none');
	
	$("#paso3_emitir").attr('disabled', true);
	$("#paso3_enviar").attr('disabled', true);
	$("#paso3_emitir").addClass('d-none');
	
	banderaEditar = true;
}

function setCoinFormat(num) {
	num = "" + num;
	if( num ==""){
		return num;
	}
	
	arraySplit = num.split(".");
	izq = arraySplit[0];
	der = "00";
	if ( num.includes(".") ) {
		der = arraySplit[1];
	}
	izq = izq.replace(/ /g, "");
	izq = izq.replace(/\$/g, "");
	izq = izq.replace(/,/g, "");

	var izqAux = "";
	var j = 0;
	for ( i = izq.length - 1; i >= 0; i-- ) {
		if ( j != 0 && j % 3 == 0 ) {
			izqAux += ",";
		}
		j++;
		izqAux += izq[i];
	}
	izq = "";
	for ( i = izqAux.length - 1; i >= 0; i-- ) {
		izq += izqAux[i];
	}
	der = der.substring(0, 2);
	if ( der.length < 2 ) {
		der += "0";
	}
	return "$" + izq + "." + der;
}

$('#txtPrimaObj').on('keyup', function() {
    $(event.target).val(function(index, value) {
        var aux = value.replace(/[$,]/g, '');
        aux = aux.replace(/\D/g, "")
            .replace(/([0-9])([0-9]{2})$/, '$1.$2')
            .replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
        return '$' + aux;
    });
});

$("#paso3_back").click(function(e){
	
	e.preventDefault();
	showLoader();
	actualizainfoCot();
	
	$.post( redirigeURL, {
		infoCot : JSON.stringify( infCotiJson )
	} ).done( function(data) {
		var response = JSON.parse( data );
		if (response.code == 0) {
			//window.location.href = response.msg;
			$("#paso3-form-back #infoCotizacion").val(response.msg);
			$("#paso3-form-back").submit();
		} else {
			showMessageError( '.navbar', response.msg, 0 );
			hideLoader();
		}
	} );
});

function actualizainfoCot(){
	switch (infCotiJson.modo) {
		case modo.NUEVA:
			infCotiJson.modo = modo.EDICION;
			break;
		case modo.COPIA:
			infCotiJson.modo = modo.EDICION;
			break;
		case modo.ALTA_ENDOSO:
			infCotiJson.modo = modo.EDITAR_ALTA_ENDOSO;
			break;
		case modo.BAJA_ENDOSO:
			infCotiJson.modo = modo.EDITAR_BAJA_ENDOSO;
			break;
		default:
			break;
	}
}

$('#paso3_revire').click(function() {
    $('#btnSuscripEnvSus2').removeClass('d-none');
    $('#btnSuscripEnvSus').addClass('d-none');
    $('#fileModal').modal('show');
});
$('#btnSuscripEnvSus2').click(function() {
    showLoader();
    $('#comentariosDosSuscrip').trigger('keyup');

    if ($('#docAgenSusc')[0].files.length == 0 && $('#comentariosDosSuscrip').val().trim().length == 0) {
        showMessageError('#fileModal .modal-header', 'Agregar documentos y/o comentarios', 0);
        hideLoader();
    } else {
        $('#txtAuxEnvDoc').val('1');
        adjuntaArchivos();
    }
});

async function adjuntaArchivos() {

    var url = new URL(window.location.href);
    var data = new FormData();
    var auxiliarDoc = '{';

    $.each($('#docAgenSusc')[0].files, function(i, file) {
        data.append('file-' + i, file);
        var nomAux = file.name.split('.');
        if (i == 0) {
            auxiliarDoc += '\"file-' + i + '\" : {';
        } else {
            auxiliarDoc += ', \"file-' + i + '\" : {';
        }
        auxiliarDoc += '\"nom\" : \"' + nomAux[0] + '\",';
        auxiliarDoc += '\"ext\" : \"' + nomAux[1] + '\"}';
    });
    auxiliarDoc += '}';

    data.append('isRevire', 1);
    data.append('auxiliarDoc', auxiliarDoc);
    data.append('comentarios', $('#comentariosDosSuscrip').val());
    data.append('infoCot', JSON.stringify(infCotiJson));
    data.append('url', url.origin + url.pathname); 
    data.append('url2', url.origin);
    data.append('totArchivos', $('#docAgenSusc')[0].files.length);

    $.ajax({
        url: sendMailAgenteSuscriptorURL,
        data: data,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data) {
            if (data != "") {
                var response = jQuery.parseJSON(data);
                if (response.code > 0) {
                    $('#fileModal').modal('hide');
                    hideLoader();
                    showMessageError('.navbar', response.msg, 0);
                } else {
                    window.location.href = url.origin + url.pathname;
                    
   
                }
            } else {
            	showMessageError('.navbar', "Error al enviar la información", 0);
            }
        }
    });
}

$("#btnCoaseguro").click(function() {
	
	$.post(generaInfoCoaseguroURL,{
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version,
		folio: infCotiJson.folio,
		poliza: infCotiJson.poliza,
		pantalla: infCotiJson.pantalla,
		coaseguro: infCotiJson.tipoCoaseguro,
		modo: infCotiJson.modo
	}).done(function(data){
		console.log(data);
		
		window.location = "rea-coa?infoCotizacion=" + data;
	});
});

$("#btnCalculoPrimaSuscriptor").click(function(){
	$.post(primaObjetivoSuscriptorURL,
		{
			cotizacion: infCotiJson.cotizacion,
			version: infCotiJson.version,
			pantalla: infCotiJson.pantalla
		})
		.done(function(data){
			responseComision = JSON.parse(data);
			$("#tablePrimasBody").empty();
			$.each(responseComision.lista_primas, function(index, value){
				$("#tablePrimasBody").append(
					'<tr id="' + value.ramo + '" class="rowRamo"><td>' + value.descripcion + '</td><td>' + setCoinFormat('' + value.suma_asegurada) + '</td>' + 
					'<td>' + setCoinFormat('' + value.prima) + '</td><td> ' + value.cuota_original + '</td><td id="cuotaFinal">' + value.cuota_final + '</td>' + 
					'<td class="number"><input type="text" id="primaNueva" class="moneda primaNueva" value="' + setCoinFormat('' + value.prima_objetivo) + '">' + 
					'</td><td class="number"><input type="text" id="descuento" class="descuento" value="' + value.descuento + '"></td></tr>');
					
				console.log(value);
			});
			$("#modalPrimaObjetivo").modal('show');
		});
});

$("#tablePrimasBody").on('blur', '.descuento, .primaNueva', function() {
	
	var auxPrimas = [];
	
	var objAux = new Object;
	objAux.p_codigo_ramo = $(this).closest('.rowRamo').attr('id');
	objAux.p_prima_objetiva = parseFloat(valIsNullOrEmpty($(this).closest('.rowRamo').find('#primaNueva').val()) ?
            0 : $(this).closest('.rowRamo').find('#primaNueva').val().replace(/[$,]/g, ''));
	objAux.p_descuento = $(this).closest('.rowRamo').find('#descuento').val();
	
	auxPrimas.push(objAux);
	
	var auxCuotaFinal = $(this).closest('.rowRamo').find('#cuotaFinal');
	
	$.post(getCuotaFinalURL
		,{
			cotizacion: infCotiJson.cotizacion,
			version: infCotiJson.version,
			pantalla: infCotiJson.pantalla,
			lista: JSON.stringify(auxPrimas)
		}
	).done(function(data) {
		var response = JSON.parse(data);
		
		if(response.code == 0) {
			auxCuotaFinal.text(response.p_cuota_final);
		}
		else{
			showMessageError('#modalPrimaObjetivo .modal-header', response.msg, 0);
		}
		
		console.log(response);
	});
});

$("#btnGuardarPrimaObjetivo").click(function() {
	
	var auxPrimas = generaPrimas();
	var gastos = parseFloat(valIsNullOrEmpty($('#gastos').val()) ?
            0 : $('#gastos').val().replace(/[$,]/g, ''));
	var recargoPago = parseFloat(valIsNullOrEmpty($('#recargoPago').val()) ?
        0 : $('#recargoPago').val().replace(/[$,]/g, ''));
	
	
	if((perfilSuscriptor != 1 && perfilJapones != 1) || banderaEditar == false){
		gastos = -1;
		recargoPago = -1;
	}

	$.post(
		validaPrimaURL,
		{
			cotizacion: infCotiJson.cotizacion,
			version: infCotiJson.version,
			pantalla: infCotiJson.pantalla,
			lista: JSON.stringify(auxPrimas)
		}
	).done(function(data){
		var response = JSON.parse(data);
		
		console.log(response);
		
		if(response.code == 0) {
				$.post(
					guardaPrimaURL,
					{
						cotizacion: infCotiJson.cotizacion,
						version: infCotiJson.version,
						pantalla: infCotiJson.pantalla,
						lista: JSON.stringify(auxPrimas),
						gastos: gastos,
						recargoPago: recargoPago
					}
				).done(function(dataG){
					
					var responseGuardar = JSON.parse(dataG);
					
					if(responseGuardar.code == 0) {
						$("#modalPrimaObjetivo").modal('hide');
						
						$('#txtEmailAgente').val(responseGuardar.email);
	                    var band = null;
						var bandInt = null;
	                    $('#tabPaso3').html("");
	                    $('#tabPaso3').html("");
                    	$.each(responseGuardar.datosCaratula, function(k, valCaratula) {
	                        if (!(valCaratula.contenedor == band)) {
	                            band = valCaratula.contenedor;
	
								if(perfilSuscriptor == 1 || perfilJapones == 1) {
									$('#tabPaso3').append("<tr><th>" + band + "</td><td></td><td></td><td></th></tr>");
								}
								else{
									$('#tabPaso3').append("<tr><th>" + band + "</td><td></td><td></th></tr>");
								}
	                            
	                        }
	
							if(perfilSuscriptor == 1 || perfilJapones == 1){
								$('#tabPaso3').append("<tr><td>" + valCaratula.titulo + "</td><td class=\"number\">" + valCaratula.sa + "</td><td class=\"number\">" + valCaratula.prima + "</td><td>" + valCaratula.deducible + "</td></tr>");	
							}
							else{
								$('#tabPaso3').append("<tr><td>" + valCaratula.titulo + "</td><td class=\"number\">" + valCaratula.sa + "</td><td>" + valCaratula.deducible + "</td></tr>");
							}
	                        
	                    });
	
	                    $('#tabPaso3_2').html("<tr><td>Prima Neta:</td><td id='primaNeta' class=\"number\">" + setCoinFormat('' + responseGuardar.primaNeta) + "</td></tr>");                    
	
						if(perfilSuscriptor == 1 || perfilJapones == 1) {
							$('#tabPaso3_2').append("<tr><td>Recargo por Pago Fraccionado:</td><td class=\"number\"><input id=\"recargoPago\" class=\"moneda campoEditable\" value=\"" + setCoinFormat('' + responseGuardar.recargo) + "\" /></td><td><a onclick=\"editarCamposPrima('recargoPago')\" style=\"color: #0275d8; text-decoration: underline;\">Editar</a></td></tr>");
	                		$('#tabPaso3_2').append("<tr><td>Gastos de Expedición:</td><td class=\"number\"><input id=\"gastos\" class=\"moneda campoEditable\" value=\"" + setCoinFormat('' + responseGuardar.gastos) + "\" /></td><td><a onclick=\"editarCamposPrima('gastos')\" style=\"color: #0275d8; text-decoration: underline;\">Editar</a></td></tr>");	
						}
						else {
							$('#tabPaso3_2').append("<tr><td>Recargo por Pago Fraccionado:</td><td class=\"number\"><input id=\"recargoPago\" class=\"moneda campoEditable\" value=\"" + setCoinFormat('' + responseGuardar.recargo) + "\" disabled=\"true\" /></td></tr>");
	                    	$('#tabPaso3_2').append("<tr><td>Gastos de Expedición:</td><td class=\"number\"><input id=\"gastos\" class=\"moneda campoEditable\" value=\"" + setCoinFormat('' + responseGuardar.gastos) + "\" disabled=\"true\" /></td></tr>");
						}
	
	                    $('#tabPaso3_2').append("<tr><td>I.V.A.:</td><td class=\"number\">" + setCoinFormat('' + responseGuardar.iva) + "</td></tr>");
	                    $('#tabPaso3_3').html(setCoinFormat('' + responseGuardar.total));
	                    
						var tipoMonSelect = parseInt($('#dc_moneda').val());
	                    if (tipoMonSelect == 1) {
	                        $('#titPrimaObj').text("Prima Objetivo: (Pesos)");
	                    } else {
	                        $('#titPrimaObj').text("Prima Objetivo: (Dolares)");
	                    }
	
	                    hideLoader();
	                    showMessageSuccess('.navbar', 'Información actualizada correctamente', 0);
	
						$("#paso3_slip").attr('disabled', false);
						$("#paso3_emitir").attr('disabled', true);
						$("#paso3_enviar").attr('disabled', true);
						$("#paso3_emitir").addClass('d-none');
						banderaEditar = false;
						location.reload();
					}
					else { 
						showMessageError('#modalPrimaObjetivo .modal-header', responseGuardar.msg, 0);
					}
				});
		}
		else {
			showMessageError( '#modalPrimaObjetivo .modal-header', response.msg, 0);
			console.log(response.lista_validaciones);
			$.each(response.lista_validaciones, function(key, value) {
				$("#" + value + " input").css('color', 'red');
			});
		}
	});
});

$("#tablePrimasBody").on('click', 'input', function(){
	$(this).removeAttr('style');
});

function generaPrimas() {
	
	var arrayPrimas = [];
	
	$.each($(".rowRamo"), function(key, value){
		
		var auxId = $(this).attr('id');
		
		var auxObj = new Object();
		
		auxObj.p_codigo_ramo = auxId;
		auxObj.p_prima_objetiva = parseFloat(valIsNullOrEmpty($("#" + auxId + " #primaNueva").val()) ?
            0 : $("#" + auxId + " #primaNueva").val().replace(/[$,]/g, ''));
		auxObj.p_descuento = parseFloat(valIsNullOrEmpty($("#" + auxId + " #descuento").val()) ?
            0 : $("#" + auxId + " #descuento").val().replace(/[$,]/g, ''));
		
		arrayPrimas.push(auxObj);
	});
	
	return arrayPrimas;
}

$('#btnFacturaSuscrip').click(function() {
    var idFac = ($('#chkfactauto').is(':checked')) ? 1 : 0;
    $('#txtBtnEmiteFactu').val(idFac);
    showLoader();

    $.post(getEmisionArt492Url, {
        cotizacion: infCotiJson.cotizacion,
        version: infCotiJson.version,
        factura: idFac,
        cotizador: 'LiabilityQuotation'
    }).done(function(data) {
        var response = jQuery.parseJSON(data);
        console.log('aquiiii');
        console.log(response);
        if (response.code == 0) {
            llenaInfoModalPoliza(response);
            $('#modalGenerarPoliza').modal({
                show: true
            });
        } else if (response.code == 4) {
            llenaInfoModalPoliza(response);
            $('#modalGenerarPoliza').modal({
                show: true
            });
            showMessageError('#modalGenerarPoliza', (response.msg), 0);
        } else {
            showMessageError('.navbar', (response.msg), 0);
        }
    }).always(function() {
        hideLoader();
    });
});


function llenaInfoModalPoliza(json) {
    $('.listaCorreos li').remove();
    $('#txtModalPolizaNumeroPoliza').text(validaKeyJson(json, 'numeroPoliza'));
    $('#txtModalPolizaCertificado').text(validaKeyJson(json, 'certificado'));
    $('#txtModalPolizaAsegurado').text(validaKeyJson(json, 'asegurado'));
    $('#txtModalPolizaAgente').val(validaKeyJson(json, 'agente'));

    $('#txtModalPolizaVigenciaDe').text(stringToDate(validaKeyJson(json, 'vigencia.inicio')));
    $('#txtModalPolizaVigenciaAl').text(stringToDate(validaKeyJson(json, 'vigencia.fin')));
    $('#divDescargarArchivos').html();

    $('#txtModalPolizaTotalUbicaciones').text(validaKeyJson(json, 'totalUbicaciones'));
    $('#txtModalPolizaMoneda').text(validaKeyJson(json, 'moneda'));
    $('#txtModalPolizaMoneda').text(validaKeyJson(json, 'moneda'));
    $('#txtModalPolizaFormaPago').text(validaKeyJson(json, 'formaPago'));
    $('#txtModalPolizaPrimaNeta').text(validaKeyJson(json, 'primaNeta'));
    $('#txtModalPolizaRecargoPago').text(validaKeyJson(json, 'recargo'));
    $('#txtModalPolizaGastosExpedicion').text(validaKeyJson(json, 'gastos'));
    $('#txtModalPolizaIva').text(validaKeyJson(json, 'iva'));
    $('#txtModalPolizaTotal').text(validaKeyJson(json, 'total'));
    $('#tablaArchivosPoliza tbody').empty();
    if (!valIsNullOrEmpty($('#txtEmailUser').val())) {
        $('.modal .listaCorreos')
            .append(
                $('<li email="' +
                	Base64.decode( $('#txtEmailUser').val() ) +
                    '" ><button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                    Base64.decode( $('#txtEmailUser').val() ) + '</li>'));
    }

    if (valIsNullOrEmpty(validaKeyJson(json, 'archivos'))) {
        $('.selectCheckImput').prop('checked', false);
        $('.selectCheckImput').prop("disabled", true);
        $('#btnDescargarArchivos').prop("disabled", true);
        $('#polizaBtnEnviar').prop("disabled", true);
    } else {
        $('.selectCheckImput').prop('checked', true);
        $('.selectCheckImput').prop("disabled", false);
        $('#btnDescargarArchivos').prop("disabled", false);
        validaBtnEnviar();
        $.each(json.archivos, function(i, a) {
            var chekbox = '<div class="form-check"> ' + '<input class="form-check-inpu chekArchivos" name="' + a.nombre +
                "-" + a.extension + '" idCarpeta="' + a.idCarpeta + '" idDocumento="' + a.idDocumento +
                '" idCatalogoDetalle="' + a.idCatalogoDetalle + '" type="checkbox" id="' + a.nombre + "-" +
                a.extension + '" checked>' + '<label for="' + a.nombre + "-" + a.extension + '"></label>' +
                '</div>';

            $('#tablaArchivosPoliza tbody').append(
                $('<tr> <td> ' + chekbox + ' </td> <td>  ' + a.nombre + "." + a.extension + ' </td> <td >  ' +
                    a.tipo + ' </td> </tr>'));

            $('#divDescargarArchivos').append($('<a id="' + a.nombre + a.extension + '" />'));

        });
    }
}

function validaKeyJson(json, cadena) {
    var infoJson = '';
    var res = cadena.split(".");
    var ant = null;
    json['name']
    $.each(res, function(key, val) {
        if (key == 0) {
            infoJson = (val in json) ? eval('json.' + val) : "";
            ant = eval('json.' + val);
        } else {
            if (valIsNullOrEmpty(ant)) {
                infoJson = "";
            } else {
                infoJson = (val in ant) ? eval('ant.' + val) : "";
                ant = eval('ant.' + val);
            }
        }
    });
    return infoJson;
}

function validaBtnEnviar() {
    if ($('.listaCorreos li').length > 0) {
        $('#polizaBtnEnviar').prop("disabled", false);
        $('.msjActivarBtnEnviar').prop('hidden', true);
    } else {
        $('#polizaBtnEnviar').prop("disabled", true);
        $('.msjActivarBtnEnviar').prop('hidden', false);
    }
}

$('#polizaBtnEnviar').click(function(e) {
    showLoader();
    var emailsList = $('#listaCorreos li');
    var emailsTot = "";
    $.each(emailsList, function(i, emlis) {
        if (i > 0) {
            emailsTot += ",";
        }
        emailsTot += $(emlis).attr('email');
    });
    recuperaDocumentosEmision(emailsTot);
});

function recuperaDocumentosEmision(emails) {
    $.post($('#txtJSGetDocsEmision').val(), {
        infoDocs: jsonDocumentosEmision(),
        listaEmails: emails,
        cliente: $('#txtModalPolizaAsegurado').text(),
        poliza: $('#txtModalPolizaNumeroPoliza').text(),
        totUbica: $('#txtModalPolizaTotalUbicaciones').text(),
        moneda: $('#txtModalPolizaMoneda').text(),
        certificado: $('#txtModalPolizaCertificado').text(),
        vigencia: $('#txtModalPolizaVigenciaAl').text() + ' al ' + $('#txtModalPolizaVigenciaAl').text(),
        formaPago: $('#txtModalPolizaFormaPago').text(),
        primaNeta: '$' + $('#txtModalPolizaPrimaNeta').text(),
        recargo: '$' + $('#txtModalPolizaRecargoPago').text(),
        gasto: '$' + $('#txtModalPolizaGastosExpedicion').text(),
        iva: '$' + $('#txtModalPolizaIva').text(),
        prima: '$' + $('#txtModalPolizaTotal').text(),
        folio: infCotiJson.folio,
        agente: $('#txtModalPolizaAgente').val()
    }).done(function(data) {
        sessionExtend();
        var respuestaJson = JSON.parse(data);
        if (respuestaJson.code >= 0) {
            if (emails == null) {
                $.each(respuestaJson.archivos, function(i, archivo) {
                	/*
                    fileAux = 'data:application/octet-stream;base64,' + archivo.documento
                    var dlnk = document.getElementById(archivo.nombre + archivo.extension);
                    dlnk.href = fileAux;
                    dlnk.download = archivo.nombre + '.' + archivo.extension;
                    dlnk.click();
                    */
                	if(detectIEEdge()){
    					fileAux = 'data:application/octet-stream;base64,'+archivo.documento
    					var dlnk = document.getElementById('dwnldLnk');
    					dlnk.href = fileAux;
    					dlnk.download = archivo.nombre+'.'+archivo.extension;
    					location.href=document.getElementById("dwnldLnk").href;
    					/*dlnk.click();*/
    				}else{
    					/*
    					 * downloadDocument('archivo base 64' , 'nombre.extension' );
    					 */
    					downloadDocument(archivo.documento, archivo.nombre+'.'+archivo.extension);
    				}
                });
            } else {
                showMessageSuccess('#modalGenerarPoliza', "Correo(s) enviado(s)", 0);
            }
        } else {
            showMessageError('#modalGenerarPoliza', respuestaJson.msg, 0);
        }
    }).fail(function() {
        showMessageError('#modalGenerarPoliza', "Error al consultar la informacion", 0);
    }).always(function() {
        hideLoader();
    });
}

$("#modalPolizaEnviarCorreo").keyup(function(e) {
    if (valIsNullOrEmpty($(this).val())) {
        $('#btnAgregaCorreoPoliza').prop("disabled", true);
    } else {
        $('#btnAgregaCorreoPoliza').prop("disabled", false);
        eliminaErrorEmailEmision();
    }
    var code = (e.keyCode ? e.keyCode : e.which);
    if (code == 13) {
        $('#btnAgregaCorreoPoliza').trigger('click');
    }
});

function eliminaErrorEmailEmision() {
    $("#modalPolizaEnviarCorreo").removeClass('invalid');
    $("#modalPolizaEnviarCorreo").siblings('.alert-danger').remove();
}
$('#btnAgregaCorreoPoliza').click(function(e) {
    var correo = $('#modalPolizaEnviarCorreo').val();
    var error = chekEmail($('#modalPolizaEnviarCorreo'));
    if ((!error) && (!valIsNullOrEmpty(correo))) {
        $('.modal .listaCorreos').append(
            $('<li email="' + correo +
                '"><button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
                correo + '</li>'));
        $('#modalPolizaEnviarCorreo').val('');
        $('#listaCorreos').scrollTo('li:last');
        $('#btnAgregaCorreoPoliza').prop("disabled", true);
    }
    validaBtnEnviar();
    if (!almenosUnArchivoSeleccionado()) {
        $('#polizaBtnEnviar').prop("disabled", true);
    }
});

function chekEmail(campos) {
    var errores = false;
    $.each(campos, function(index, value) {
        if (!valIsNullOrEmpty($(value).val())) {
            if (!validateEmail($(value).val())) {
                errores = true;
                $(value).addClass('invalid');
                $(value).parent().append(
                    "<div class=\"alert alert-danger\" role=\"alert\"> <span class=\"glyphicon glyphicon-ban-circle\"></span>" +
                    " " + $('#txtFormatoEmail').val() + "</div>");
            }
        }
    });
    return errores;
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

$('.modal .listaCorreos').on('click', '.close', function(e) {
    $(this).parent().remove();
    validaBtnEnviar();
});

function jsonDocumentosEmision() {
    var cheks = $('#modalGenerarPoliza .bodyArchivos input[type=checkbox]');
    var listaDocumentos = '';
    if ($('.selectCheckImput').is(':checked')) {
        $.each(cheks, function(i, chek) {
            var ids = $(chek).attr('id');
            if (i > 0) {
                listaDocumentos += ",";
            }
            listaDocumentos += '{"idCarpeta" : ' + $(chek).attr('idCarpeta') + ', "idDocumento" : ' +
                $(chek).attr('idDocumento') + ', "idCatalogoDetalle" : ' +
                $(chek).attr('idCatalogoDetalle') + ', "documento" : "", "nombre" : "", "extension" : "" }';
        });
    } else {
        $.each(cheks, function(i, chek) {
            if ($(chek).is(':checked')) {
                var ids = $(chek).attr('id');
                if (!valIsNullOrEmpty(listaDocumentos)) {
                    listaDocumentos += ",";
                }
                listaDocumentos += '{"idCarpeta" : ' + $(chek).attr('idCarpeta') + ', "idDocumento" : ' +
                    $(chek).attr('idDocumento') + ', "idCatalogoDetalle" : ' +
                    $(chek).attr('idCatalogoDetalle') +
                    ', "documento" : "", "nombre" : "", "extension" : "" }';
            }
        });
    }
    return listaDocumentos;
}

function almenosUnArchivoSeleccionado() {
    var seleccionado = false;
    $.each($('.modal .bodyArchivos .chekArchivos'), function(i, chek) {
        if ($(chek).is(':checked')) {
            seleccionado = true;
            return false;
        }
    });
    return seleccionado;
}

$('#btnDescargarArchivos').click(function(e) {
    showLoader();
    recuperaDocumentosEmision(null);
});

$('#modalGenerarPoliza').on('hidden.bs.modal', function() {
    window.scrollTo(0, 0);
    goToHome();
});

function detectIEEdge() {
    var ua = window.navigator.userAgent;

    var msie = ua.indexOf('MSIE ');
    if (msie > 0) {
        // IE 10 or older => return version number
        console.log(parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10));
        return true;
    }

    var trident = ua.indexOf('Trident/');
    if (trident > 0) {
        // IE 11 => return version number
        var rv = ua.indexOf('rv:');
        console.log(parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10));
        return true;
    }

    var edge = ua.indexOf('Edge/');
    if (edge > 0) {
        // Edge => return version number
        console.log(parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10));
        return true;
    }

    // other browser
    return false;
}

$("#btnsuscontinuar").click(function(){
	$("#chkBjaEnd .form-check-input, #comentariosDosSuscrip ").attr("disabled", false);
	$('#fileModal').modal({
        show: true,
        backdrop: 'static',
        keyboard: false
    });
});

$("#btnSuscripEnvSus").click(function(){
	showLoader();
	adjuntaArchivosSus();
});


async function adjuntaArchivosSus() {

    var url = new URL(window.location.href);
    var data = new FormData();
    var auxiliarDoc = '{';

    $.each($('#docAgenSusc')[0].files, function(i, file) {
        data.append('file-' + i, file);
        var nomAux = file.name.split('.');
        if (i == 0) {
            auxiliarDoc += '\"file-' + i + '\" : {';
        } else {
            auxiliarDoc += ', \"file-' + i + '\" : {';
        }
        auxiliarDoc += '\"nom\" : \"' + nomAux[0] + '\",';
        auxiliarDoc += '\"ext\" : \"' + nomAux[1] + '\"}';
    });
    auxiliarDoc += '}';

    data.append('auxiliarDoc', auxiliarDoc);
    data.append('comentarios', $('#comentariosDosSuscrip').val());
    data.append('infoCot', JSON.stringify(infCotiJson));
    data.append('url', url.origin + url.pathname); 
    data.append('url2', url.origin);
    data.append('totArchivos', $('#docAgenSusc')[0].files.length);

    $.ajax({
        url: sendMailAgenteSuscriptorURL,
        data: data,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data) {
            if (data != "") {
                var response = jQuery.parseJSON(data);
                if (response.code > 0) {
                    $('#fileModal').modal('hide');
                    hideLoader();
                    showMessageError('.navbar', response.msg, 0);
                } else {
                    window.location.href = url.origin + url.pathname;
                    
   
                }
            } else {
            	showMessageError('.navbar', "Error al enviar la información", 0);
            }
        }
    });
}

$("#btnReaseguro").click(function() {
	
	$.post(generaInfoReaseguroURL,{
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version,
		folio: infCotiJson.folio,
		poliza: infCotiJson.poliza,
		pantalla: infCotiJson.pantalla,
		modo: infCotiJson.modo
	}).done(function(data){
		console.log(data);
		
		window.location = "rea-coa?infoCotizacion=" + data;
	});
});

$("#btnDesgloseCuotas").click(function() {
	
	$.post(generaInfoDesgloseCuotasURL,{
		cotizacion: infCotiJson.cotizacion,
		version: infCotiJson.version,
		folio: infCotiJson.folio,
		pantalla: infCotiJson.pantalla
	}).done(function(data){
		console.log(data);
		
		window.open(window.origin + "/group/portal-agentes/desglose-cuotas?infoCotizacion=" + data);
	});
});

function setBase64(){
	Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9\+\/\=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/\r\n/g,"\n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}};
}