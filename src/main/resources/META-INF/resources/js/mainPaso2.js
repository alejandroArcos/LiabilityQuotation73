$( document ).ready( function() {
	showLoader();
	window.scrollTo( 0, 0 );
	
	$( "#tabs" ).tabs();
	aplicaFiltros();
	
	seleccionaModo();
	seleccionaUltimaTab();
	validaUltimaUbicacion();
	validaCamposSuscriptor();
	/*validaReglasSuscripcion();*/
	hideLoader();
} );

function validaReglasSuscripcion(){
	if(subGiroRiesgo == '1'){ //Mantis 3368
		if( perfilSuscriptorJr == '0' && perfilSuscriptorMrSr == '0' ){
			$("#saveUbicacion").addClass("d-none");
			$("#btnsuscontinuar").removeClass("d-none");
		}
	}
}

function seleccionaUltimaTab(){
	var ultima = $("#tabs .ui-tabs-nav li").last();

	infoUltimaTab.objeto = ultima;
	infoUltimaTab.numero = ultima.index();
	infoUltimaTab.etiqueta = $(ultima).find(".numUbicacionEndo").text();
	
	$( "#tabs" ).tabs( "option", "active", curPestania );

}

function seleccionaModo() {
	console.log('modo: ' + infCotizacion.modo);
	switch (infCotizacion.modo) {
		case modo.NUEVA :
			validaReglasSuscripcion();
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.EDICION :
			validaReglasSuscripcion();
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.COPIA :
			validaReglasSuscripcion();
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.ALTA_ENDOSO :
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.EDITAR_ALTA_ENDOSO :
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.CONSULTA :
		case modo.CONSULTA_VOBO_REASEGURO :
			$( "#tabs button.close" ).addClass( "d-none" );
			$("#contenPaso2 :input, textarea, select").attr("disabled", true);
			$("#saveUbicacion").removeClass("d-none");
			$("#btnsuscontinuar").addClass("d-none");
			$("#nextUbicacion").addClass("d-none");
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.CONSULTAR_REVISION :
			$( "#tabs button.close" ).addClass( "d-none" );
			$("#contenPaso2 :input, textarea, select").attr("disabled", true);
			$("#saveUbicacion").addClass("d-none");
			$("#btnsuscontinuar").addClass("d-none");
			$("#nextUbicacion").addClass("d-none");
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.BAJA_ENDOSO :
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.EDITAR_BAJA_ENDOSO :
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.COASEGURO:
		case modo.COASEGURO_CONSULTA:
		case modo.REASEGURO:
		case modo.REASEGURO_CONSULTA:
		case modo.VOBO_REASEGURO:
		case modo.FACTURA_492 :
			console.log('modo: ' + infCotizacion.modo);
			showLoader();
			$("#saveUbicacion").click();
			break;
		case modo.EDICION_JAPONES:
			validaReglasSuscripcion();
			console.log('modo: ' + infCotizacion.modo);
			break;
		default:
			showMessageError('.navbar', msj.es.errorInformacion, 0);
			console.error(" Modo default");
			break;
	}
}

$("#paso2_next").click(function(e){
	showLoader();
	
	$("#paso2_form").submit();
});
$(".btn_modal").click(function(e){
	$( '#modalRCArrendatario' ).modal( 'show' );
});

$("#CHECKAPLICACLUCDIV .form-check:checkbox").change(function() {
    if ($(this).is(":checked")) {
    	$( '#modalRCArrendatario' ).modal( 'show' );
    } else {
        
    }
});

$("#modalRCArrendatario #modalCheckAll").change(function() {
    var curCheck = $(this).is(":checked");
    console.log('check: ' + curCheck);
    $.each( $("#modalRCArrendatario .modal-body .checkUbi"), function( key, value ) {
	  $(this).prop("checked", curCheck);
	});
});

$('.acordeon input.form-check[name="RCCHKBOXLUC"]').change(function() {
	var curCheck = $(this).is(":checked");
	if( curCheck ) {
		$('#modalRCArrendatario').modal({
            show: true,
            backdrop: 'static',
            keyboard: false
        });
	}
});

$('#dac_noUbica').on('blur',function(e) {
	if ( $('#dac_noUbica').val() != $('#dac_noUbica').attr('valAnt') ){
		$('#modalConfirmaNumUbica').modal({
			show: true,
			backdrop: 'static',
			keyboard: false
		});
	}
});

$("#nextUbicacion").click(function(e){
	var auxNoUbi = $('#dac_noUbica').val();
	/*var faltaInfo = infoFaltanteUbicaciones();*/
	var faltaInfo = validaRequeridos();
	
	if(erroresCargaMasiva >= 20 && infoCargaMasiva) {
		showMessageError( '.navbar', "Los errores son demasiados para corregir manualmente, favor de regresar al paso 1, corregir el archivo y cargarlo nuevamente", 0 );
		erroresCargaMasiva = 0;
		hideLoader();
	}
	else {
		if (faltaInfo.code == 0) {
			$('#dac_noUbica').attr('valAnt', $('#dac_noUbica').val() );
			generaUbicaciones();
			if( $('#ubicacion-1 .rowAcordeon .acordeon').hasClass('d-none') ){
				saveUbicaciones(1, auxNoUbi, false);					
			}else{
				saveUbicaciones(4, auxNoUbi, false);								
			}
		} else {
			$('#dac_noUbica').val( $('#dac_noUbica').attr('valAnt') );
			showMessageError( '.navbar', faltaInfo.msg, 0 );
		}
	}
});

$('#saveUbicacion').click(function(e){
	var auxNoUbi = $('#dac_noUbica').val();
	var faltaInfo = infoFaltanteUbicaciones();
	if (faltaInfo.code == 0) {
		$('#dac_noUbica').attr('valAnt', $('#dac_noUbica').val() );
		generaUbicaciones();
		if( infCotizacion.modo == modo.CONSULTA || infCotizacion.modo == modo.FACTURA_492 ||
			infCotizacion.modo == modo.REASEGURO || infCotizacion.modo == modo.REASEGURO_CONSULTA ||
			infCotizacion.modo == modo.COASEGURO || infCotizacion.modo == modo.COASEGURO_CONSULTA || 
			infCotizacion.modo == modo.VOBO_REASEGURO || infCotizacion.modo == modo.CONSULTA_VOBO_REASEGURO ){
			saveUbicaciones(1, auxNoUbi, true);			
		}else{
			saveUbicaciones(4, auxNoUbi, true);			
		}
	} else {
		$('#dac_noUbica').val( $('#dac_noUbica').attr('valAnt') );
		showMessageError( '.navbar', faltaInfo.msg, 0 );
	}
});

$("#btnSiNumUbica").click(function(e){
	var auxNoUbi = $('#dac_noUbica').val();
	
	if( parseInt($('#dac_noUbica').val()) > parseInt($('#dac_noUbica').attr('valAnt')) ){
			$('#dac_noUbica').attr('valAnt', $('#dac_noUbica').val() );
			generaUbicaciones();
			saveUbicaciones(2, auxNoUbi, false);
		
		$( '#modalConfirmaNumUbica' ).modal( 'hide' );		
	}else{
		llenaObjUbicacion( tipoGuardar.ELIMINAR_UBICACION, parseInt($('#dac_noUbica').attr('valAnt')), true);
		saveUbicaciones(tipoGuardar.ELIMINAR_VARIAS, parseInt( $('#dac_noUbica').val()) , false);
	}
	
});

$("#btnNoNumUbica").click(function(e){
	$('#dac_noUbica').val( $('#dac_noUbica').attr('valAnt') );
	$( '#modalConfirmaNumUbica' ).modal( 'hide' );
});

$('#dac_noUbica').on('keyup', function() {
    $(event.target).val(function(index, value) {
        var aux = value.replace(/\D/g, "")
        if (parseInt(aux) > 20) {
            showMessageError('.navbar', 'El número de ubicaciones debe ser menor a 20', 0);
            return '20';
        }
        if (parseInt(aux) < 1) {
            showMessageError('.navbar', 'Se debe deter al menos una ubicación', 0);
            return '1';
        }
        return aux;
    });
});

function aplicaFiltros() {
	verificarVisibilidad();
	verificarDisabled();
	setObservableElements();
	/*validaCamposPerfil();*/
	setEstiloSubtitulos();
	
	if($("#dac_noUbica" ).val() < 5){
		$("#btn-carga-masiva").addClass("d-none");
	}
	else {
		$("#btn-carga-masiva").removeClass("d-none");
	}
}

$( ".cpValido" ).blur( function() {
	showLoader();
	cp = $( this ).val();
	var ubicacionPadre = $( this ).closest( ".ubicacion" )
	if ((cp.length < 5) && (cp.length > 0)) {
		$( this ).focus();
		showMessageError( '.navbar', 'Por favor verifique el código postal a 5 dígitos', 0 );
		hideLoader();
	} else if (cp.length == 5) {
		limpiaCamposCp( ubicacionPadre );
		llenaInfoByCP( ubicacionPadre, cp );
		/*getInfoZonaByCp(ubicacionPadre, cp);*/
	} else {
		limpiaCamposCp( ubicacionPadre );
		showMessageSuccess( '.navbar', ' Código postal vacio', 0 );
		hideLoader();
	}
} );

$("#btnRegresarPasoAnt").click(function (){
	regresaPaso1();
});

function regresaPaso1(){
	showLoader();
	/*actualizainfoCot();*/
	$.post( ligasServicios.backP1, {
		infoCot : JSON.stringify( infCotizacion )
		/*
		paso : seleccionaVentana()
		*/
	} ).done( function(data) {
		console.log('data: ' + data);
		if( !valIsNullOrEmpty(data) ){
			jsonResponse = JSON.parse(data);
			if( jsonResponse.code == 0 ){
				if(infCotizacion.modo==modo.NUEVA){
					infCotizacion.modo = modo.EDICION;
				}
				
				$('#generalesCotizacion').val( JSON.stringify( infCotizacion ) );
				$('#infoCotiP1').val(data);
				document.getElementById("back1_form").submit();
			}else{
				showMessageError( '.navbar', jsonResponse.msg, 0 );
				hideLoader();				
			}
		}
		/*
		var response = JSON.parse( data );
		if (response.code == 0) {
			window.location.href = response.msg;
		} else {
			showMessageError( '.navbar', response.msg, 0 );
			hideLoader();
		}
		*/
	} );
}

function limpiaCamposCp(ubicacionPadre) {
	$( ubicacionPadre ).find( "#dr_calle" ).val( "" );
	$( ubicacionPadre ).find( "#dr_numero" ).val( "" );
	$( ubicacionPadre ).find( "#dr_municipio" ).val( "" );
	$( ubicacionPadre ).find( "#dr_estado" ).val( "" );
	$( ubicacionPadre ).find( "#dr_colonia option:not(:first)" ).remove();
	selectDestroy( $( ubicacionPadre ).find( '#dr_colonia' ), false );
}

function llenaInfoByCP(ubicacionPadre, cp) {
	$.post( ligasServicios.getCpURL, {
		cp : cp
		/*pantalla : infoCotJson.pantalla*/
	} ).done( function(data) {
		var response = JSON.parse( data );
		console.log( response );
		if (response.code == 0) {
			llenaInfoCp( ubicacionPadre, response.cpData );
		} else {
			if (response.code == 4) {
				llenaInfoCp( ubicacionPadre, response.cpData );
				/*
				$( "#suscripCpRiesgo" ).text( response.msg );
				$( '#modalSuscripCPRiesgo' ).modal( {
					show : true
				} );
				*/
			}else{
				$( ubicacionPadre ).find( "#dr_cp" ).val("");
				
				showMessageError( '.navbar', response.msg, 0 );				
			}
		}
		hideLoader();
	} );
}

function llenaInfoCp(ubicacionPadre, cpData) {
	$.each( cpData, function(key, value) {
		$( ubicacionPadre ).find( "#dr_cp" ).attr( "idcp", value.idCp );
		$( ubicacionPadre ).find( "#dr_municipio" ).val( value.delegacion );
		$( ubicacionPadre ).find( "#dr_municipio" ).siblings( "label" ).addClass( "active" );
		$( ubicacionPadre ).find( "#dr_estado" ).val( value.estado );
		$( ubicacionPadre ).find( "#dr_estado" ).siblings( "label" ).addClass( "active" );
		$( ubicacionPadre ).find( '#dr_colonia' ).append( new Option( value.colonia, value.idCp ) );
		selectDestroy( $( ubicacionPadre ).find( '#dr_colonia' ), false );
	} );
}

/*
function generaFils(i) {
	var fils = new Object();
	$.each( $( "#ubicacion-" + i + " .acordeon input" ), function(key, value) {
		if ($( value ).is( ":text" )) {
			if ($( value ).hasClass( "select-dropdown" )) {
				var sel = $( value ).siblings( "select" );
				if (valIsNullOrEmpty( $( sel ).val() )) {
					fils[$( sel ).attr( "name" )] = "";
				} else {
					fils[$( sel ).attr( "name" )] = $( sel ).val();
				}
			} else {
				fils[$( value ).attr( "name" )] = quitaTipoMoneda( $( value ).val() );
			}
		} else if ($( value ).is( ":checkbox" )) {
			fils[$( value ).attr( "name" )] = $( value ).is( ":checked" );
		}
	} );
	return fils;
}
*/
function generaFils(i) {
	var fils = new Object();
	$.each( $( "#ubicacion-" + i + " .acordeon input" ), function(key, value) {
		if ($( value ).is( ":text" )) {
			if ($( value ).hasClass( "select-dropdown" )) {
				var sel = $( value ).siblings( "select" );
				if (valIsNullOrEmpty( $( sel ).val() )) {
					fils[$( sel ).attr( "name" )] = "";
				} else {
					fils[$( sel ).attr( "name" )] = $( sel ).val();
				}
			} else {
				if($( value ).hasClass("arrendaluc")) {
					fils[$( value ).attr( "name" )] = $( value ).val();
				}
				else {
					fils[$( value ).attr( "name" )] = quitaTipoMoneda( $( value ).val() );
				}
			}
		} else if ($( value ).is( ":checkbox" )) {
			if($(value).siblings('span').length == 0){
				fils[$( value ).attr( "name" )] = $( value ).is( ":checked" );
			}
			else{
				fils[$( value ).attr( "name" )] = $( value ).is( ":checked" ) ? $( value ).attr('true') : $( value ).attr('false');
			}
		} else {
			fils[$( value ).attr( "name" )] = $( value ).val();
		}
	} );
	return fils;
}

function quitaTipoMoneda(data) {
	return data.replace( /[$,]/g, '' );
}

function generaUbicaciones(){
	$('#canalNegocio').val(infCotizacion.canalNegocio );
	$('#modoCoti').val( infCotizacion.modo );
	$('#modoCoti3').val( infCotizacion.modo );
	ubiArray = [];
	
	$.each($("#tabs .ubicacion"), function(index, value){
			var ubiAux = new Object();
			var cpData = new Object();
			
			ubiAux.idUbicacion = index + 1;
			ubiAux.calle = $(this).find('input#dr_calle').val();
			ubiAux.numero = $(this).find('input#dr_numero').val();
			ubiAux.numInt = $(this).find('input#dr_numero_int').val();

			cpData.idCp = $(this).find('select#dr_colonia').val();
			cpData.cp = $(this).find('input#dr_cp_'+ (index + 1)).val();
			cpData.colonia = $(this).find('.select-wrapper input.select-dropdown').val();
			cpData.delegacion = $(this).find('input#dr_municipio').val();
			cpData.estado = $(this).find('input#dr_estado').val();
			cpData.pais = $(this).find('input#dr_estado').val();
			
			ubiAux.cpData = cpData;
			if( index == 0 ){				
				ubiAux.fields = generaFils( index + 1 );
			}else{
				ubiAux.fields = null;				
			}
			
			ubiArray.push(ubiAux);
	});
	
	console.log('ubiArray: ' + JSON.stringify(ubiArray));
}

function saveUbicaciones(modeSave, noUbi, flagP3){
	showLoader();
	$.post( ligasServicios.saveUbiRC, {
		ubiArray: JSON.stringify(ubiArray),
		infoCot: JSON.stringify(infCotizacion),
		modeSave: modeSave,
		noUbi: noUbi,
		p_folio: infCotizacion.folio,
		p_cotizacion: infCotizacion.cotizacion
	} ).done( function(data) {
		if( !valIsNullOrEmpty(data) ){
			var jsonResponse = JSON.parse(data);
			if(jsonResponse.code == 0){
				$('#saveResponse').val(data);
				$('#responseToP3').val(data);
				$('#suscripcion').val(0);
				
				var auxUbiShow = 1 ;
				if( modeSave == tipoGuardar.GUARDAR_UBICACION ){
					auxUbiShow = parseInt( $( "#tabs" ).tabs( "option", "active") +1 ); 
				}else{
					auxUbiShow = parseInt($('#tabs li.ui-tabs-active span.numUbicacionEndo').text()) - 1;
				}
				$('#curUbicacion').val( auxUbiShow );
				
				if(flagP3){
					showLoader();
					document.getElementById("goToPaso3_form").submit();						
				}else{
					validaUltimaUbicacion();
					showLoader();
					document.getElementById("saveUbicaciones_form").submit();
					/*
					if( !validaUltimaUbicacion() &&  modeSave == tipoGuardar.GUARDAR_UBICACION ){
						document.getElementById("saveUbicaciones_form").submit();
						hideLoader();
					}else{
						document.getElementById("saveUbicaciones_form").submit();			
					}
					*/
				}					
				
			}else{
				if(jsonResponse.code == 5) { //Mantis 3368
					
					if(infCotizacion.canalNegocio == 2525 || infCotizacion.modo == modo.FACTURA_492) {
						$('#saveResponse').val(data);
						$('#responseToP3').val(data);
						$('#suscripcion').val(1);
						
						var auxUbiShow = 1 ;
						if( modeSave == tipoGuardar.GUARDAR_UBICACION ){
							auxUbiShow = parseInt( $( "#tabs" ).tabs( "option", "active") +1 ); 
						}else{
							auxUbiShow = parseInt($('#tabs li.ui-tabs-active span.numUbicacionEndo').text()) - 1;
						}
						$('#curUbicacion').val( auxUbiShow );
						
						if(flagP3){
							showLoader();
							document.getElementById("goToPaso3_form").submit();						
						}else{
							validaUltimaUbicacion();
							showLoader();
							document.getElementById("saveUbicaciones_form").submit();
							/*
							if( !validaUltimaUbicacion() &&  modeSave == tipoGuardar.GUARDAR_UBICACION ){
								document.getElementById("saveUbicaciones_form").submit();
								hideLoader();
							}else{
								document.getElementById("saveUbicaciones_form").submit();			
							}
							*/
						}
					}
					else {
						$("#paso2_next").addClass("d-none");
						$("#infoPrimas").addClass("d-none");
						$("#btnsuscontinuar").addClass("d-none");
						$("#suscripMontoExedeTxt").text(jsonResponse.msg);
						$( '#modalSuscripMontoExede' ).modal( {
							show : true
						} );
					}
				}
				else {
					showMessageError( '.navbar', jsonResponse.msg, 0 );				
				}
				hideLoader();
			}
		}
	} );
}

$("#btnSuscripMontoSi").click(function(){
	$("#btnsuscontinuar").removeClass("d-none");
	$("#saveUbicacion").addClass("d-none");
	$( '#modalSuscripMontoExede' ).modal('hide');
});

function infoFaltanteUbicaciones() {
	$( ".alert-danger" ).remove();
	$( '.invalid' ).removeClass( 'invalid' );
	var totUbicaciones = $( ".numUbicacionEndo" ).length;
	var info = new Object();
	info.code = 0;
	info.msg = "";
	for (var i = 0; i < totUbicaciones; i++) {
		var errorPestania = false;
		$.each( $( "#ubicacion-" + (i + 1) + " input.infReq" ), function(key, value) {
			if (valIsNullOrEmpty( $( value ).val() )) {
				$( value ).addClass( 'invalid' );
				$( value ).parent().append(
						"<div class=\"alert alert-danger\" role=\"alert\"> <span class=\"glyphicon glyphicon-ban-circle\"></span>"
								+ " Hace falta información requerida </div>" );

				errorPestania = true;
			}
		} );

		$.each( $( "#ubicacion-" + (i + 1) + " select.infReq" ), function(key, value) {
			if ($( value ).val() == "-1") {
				$( value ).siblings( "input" ).addClass( 'invalid' );
				$( value ).parent().append(
						"<div class=\"alert alert-danger\"> <span class=\"glyphicon glyphicon-ban-circle\"></span> "
								+ "Hace falta información requerida </div>" );
				errorPestania = true;
			}
		} );

		if (errorPestania) {
			info.code = 2;
			if (valIsNullOrEmpty( info.msg )) {
				info.msg = "Falta información requerida en la pestaña " + $( $( ".numUbicacionEndo" )[i] ).text();
			} else {
				info.msg += "<br/>Falta información requerida en la pestaña " + $( $( ".numUbicacionEndo" )[i] ).text();
			}

		}
	}
	return info;
}

function validaRequeridos(){
	/*
	$("#tabs .ubicacion select.infReq").siblings('input').addClass('infReq');
	var info = new Object();
	info.code = 0;
	info.msg = "";
	*/
	/*var campos = $("#tabs .ubicacion input:visible:enabled:not(:radio)");*/
	/*
	var campos = $("#tabs .ubicacion .infReq:visible:enabled:not(:radio)");
	var completos = false;
	*/
	/*
	if( $(".cn_rdEx .switch:visible #chktoggle").is(":checked")){
		campos = $("#contPaso1 input:visible:enabled:not(:radio)").not("#cn_fismaterno");
	}
	*/
	/*
	$.each(campos, function(key, campo) {
	*/
		/*if( !$(campo).hasClass("noReq") ){*/
		/*
			if($(campo).hasClass("select-dropdown")){
				var select = $(campo).siblings("select");
				completos = noSelect($(select)) ? true : completos;
			}else{
				completos = vaciosInpText($(campo)) ? true : completos;	
			}
		*/
		/*}*/
	/*	
	});
	
	if (completos) {
		info.code = 2;
		info.msg = msj.es.faltaInfo;
	}
	return info;
	*/
	
	$( ".alert-danger" ).remove();
	$( '.invalid' ).removeClass( 'invalid' );
	var totUbicaciones = $( ".numUbicacionEndo" ).length;
	var info = new Object();
	info.code = 0;
	info.msg = "";
	for (var i = 0; i < totUbicaciones; i++) {
		var errorPestania = false;
		$.each( $( "#ubicacion-" + (i + 1) + " input.infReq" ), function(key, value) {
			if (valIsNullOrEmpty( $( value ).val() )) {
				$( value ).addClass( 'invalid' );
				$( value ).parent().append(
						"<div class=\"alert alert-danger\" role=\"alert\"> <span class=\"glyphicon glyphicon-ban-circle\"></span>"
								+ " Hace falta información requerida </div>" );

				errorPestania = true;
				erroresCargaMasiva++;
			}
		} );

		$.each( $( "#ubicacion-" + (i + 1) + " select.infReq" ), function(key, value) {
			if ($( value ).val() == "-1") {
				$( value ).siblings( "input" ).addClass( 'invalid' );
				$( value ).parent().append(
						"<div class=\"alert alert-danger\"> <span class=\"glyphicon glyphicon-ban-circle\"></span> "
								+ "Hace falta información requerida </div>" );
				errorPestania = true;
				erroresCargaMasiva++;
			}
		} );
		
		/*
		if($("#ubicacion-" + (i + 1) + " input:radio.infReq:checked" ).length == 0) {
			
			$("#ubicacion-" + (i + 1) + " input:radio.infReq").parent().parent().append(
						"<div class=\"alert alert-danger\" role=\"alert\"> <span class=\"glyphicon glyphicon-ban-circle\"></span>"
								+ " Hace falta información requerida </div>" );
			
			errorPestania = true;
			erroresCargaMasiva++;
		}
		*/

		if (errorPestania) {
			info.code = 2;
			if (valIsNullOrEmpty( info.msg )) {
				info.msg = "Falta información requerida en la pestaña " + $( $( ".numUbicacionEndo" )[i] ).text();
			} else {
				info.msg += "<br/>Falta información requerida en la pestaña " + $( $( ".numUbicacionEndo" )[i] ).text();
			}

		}
	}
	return info;
}

function validaUltimaUbicacion(){
	$('#tabs button.close:first').hide();
	var campos = $("#tabs .ubicacion .infReq:enabled:not(:radio)");
	var completos = false;
	
	$.each(campos, function(key, campo) {	
		if($(campo).hasClass("select-dropdown")){
			var select = $(campo).siblings("select");
			completos = noSelect2($(select)) ? true : completos;
		}else{
			completos = vaciosInpText2($(campo)) ? true : completos;	
		}
	});
	
	if( !completos ){
		$('#curUbicacion').val( 0 );
		$('.rowAcordeon .acordeon#acordeon1').removeClass('d-none');
		$('#saveUbicacion').attr("disabled", false);
	}
	
	return completos;
}

/*
function getInfoZonaByCp(ubicacionPadre, cp) {
	
	$.post(
		getValoresZonaURL,
		{
			idCodigoPostal: cp,
			cotizacion: infoCotJson.cotizacion,
			version: infoCotJson.version,
			pantalla: infoCotJson.pantalla
		}
	).done(function(data){
		var response = JSON.parse( data );
		console.log(response);
		
		$(ubicacionPadre).find("#informacionZona").empty();
		$.each(response.listaInfoZona, function(key, registro){
			$("#informacionZona").append("<div class='col-md-3'><div class='row justify-content-center bg-light'>" + registro.descripcion +
				"</div><div class='row justify-content-center'>" + registro.valor + "</div></div>");
		});
	});
}
*/

$( "#tabs button.close" ).click( function(e) {
	var liSelect = $( this ).closest( "li" );
	var ubica = $( liSelect ).attr( "aria-controls" );
	var sepUbica = ubica.split( "-" );

	if (sepUbica.length > 1) {
		$('#ubicacionEliminalbl').text( $(this).siblings("a").find(".numUbicacionEndo").text() );
		$( '#modalCerrarTab' ).modal( {
			show : true
		} );
	} else {
		showMessageError( '.navbar', "Error al guardar su información", 0 );
		hideLoader();
	}

});

$( "#btnEliminarPestania" ).click( function(e) {
	showLoader();
	e.preventDefault();
	
	var liElimina = $("#tabs > ul li ").has("a .numUbicacionEndo:contains(" + $('#ubicacionEliminalbl').text() + ")");
	var ubica = $( liElimina ).attr( "aria-controls" );
	var sepUbica = ubica.split( "-" );
	
	/*infoCotJson.noUbicaciones -= 1;*/
	/*infoCotJson.noUbicaciones = parseInt( $('#dac_noUbica').val() );*/
	llenaObjUbicacion( tipoGuardar.ELIMINAR_UBICACION, parseInt(sepUbica[1]), false );
	saveUbicaciones(tipoGuardar.ELIMINAR_UBICACION, parseInt( $('#dac_noUbica').val()) -1, false);
	/*enviaUbicaciones();*/

} );

function llenaObjUbicacion(tipoGuardar, ubicacionBorrar, borrarVarias) {
	$('#canalNegocio').val(infCotizacion.canalNegocio );
	$('#modoCoti').val( infCotizacion.modo );
	$('#modoCoti3').val( infCotizacion.modo );
	/*var infoP1Json = JSON.parse(infoP1);*/
	
	listaUbicaciones.p_cotizacion = infCotizacion.cotizacion;
	listaUbicaciones.p_version = infCotizacion.version;
	listaUbicaciones.p_tipoGuardar = tipoGuardar;
	listaUbicaciones.p_ubicacion = generainfoUbicaciones( ubicacionBorrar, borrarVarias );
	listaUbicaciones.p_folio = infCotizacion.folio + "";
	/*listaUbicaciones.p_agente = infoP1Json.codigoAgente;*/

}

function generainfoUbicaciones(ubicacionBorrar, borrarVarias) {
	/*var ubiArray = new Array();*/
	ubiArray = [];
	var totUbicaciones = 0;
	if (borrarVarias){
		totUbicaciones = parseInt( $('#dac_noUbica').val());
	}else{
		totUbicaciones = $( ".numUbicacionEndo" ).length;		
	}

	for (var i = 0; i < totUbicaciones; i++) {
		if ((i + 1) != ubicacionBorrar) {
			
			var ubiAux = new Object();
			var cpData = new Object();
			
			ubiAux.idUbicacion = i + 1;
			
			ubiAux.calle = $('#ubicacion-'+(i + 1) +' input#dr_calle').val();
			ubiAux.numero = $('#ubicacion-'+(i + 1) +' input#dr_numero').val();
			ubiAux.numInt = $('#ubicacion-'+(i + 1) +' input#dr_numero_int').val();

			cpData.idCp = $('#ubicacion-'+(i + 1) +' select#dr_colonia').val();
			cpData.cp = $('#ubicacion-'+(i + 1) + ' input#dr_cp').val();
			cpData.colonia = $('#ubicacion-'+(i + 1) + ' .select-wrapper input.select-dropdown').val();
			cpData.delegacion = $('#ubicacion-'+(i + 1) + ' input#dr_municipio').val();
			cpData.estado = $('#ubicacion-'+(i + 1) + ' input#dr_estado').val();
			cpData.pais = $('#ubicacion-'+(i + 1) + ' input#dr_estado').val();
			
			ubiAux.cpData = cpData;
			if( i == 0 ){
				ubiAux.fields = generaFils( i + 1 );				
			}else{
				ubiAux.fields = null;								
			}
			
			ubiArray.push(ubiAux);
			
		}
	}

	return ubiArray;
}

function setValueArrendatario(){
	var auxVal = "";
	$.each( $("#modalRCArrendatario .modal-body .checkUbi"), function( key, value ) {
		if ($(this).is(":checked")) {
			if( valIsNullOrEmpty( auxVal) ){
				auxVal = ""+(key+1);
			}else{
				auxVal = auxVal + ","+ (key+1);
			}
	    }
	});
	$('#UBIENLUCidUbicacion0').val(auxVal);
	$( '#modalRCArrendatario' ).modal( 'hide' );
}

function validaCamposSuscriptor() {
	$.each($(".suscriptor"), function(key, index){
	    $(this).parent().addClass('d-none');
	    $('p.alert-primary:contains("para Suscripción")').hide();
	});
	
	if(perfilSuscriptorJr == 1 || perfilSuscriptorMrSr == 1) {
		$.each($(".suscriptor"), function(key, index){
		    $(this).parent().removeClass('d-none');
		});
	}
	
	$.each($(".suscriptormr"), function(key, index){
	    $(this).parent().addClass('d-none');
	});
	
	if( perfilSuscriptorMrSr == 1 ) {
		$.each($(".suscriptormr"), function(key, index){
		    $(this).parent().removeClass('d-none');
		});
	}
	
	if( perfilSuscriptorMr == 1 ) {
		$("input[name='NUMAXCAJONESRC']").attr({
			max: "500"
		});
	}
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
	adjuntaArchivos();
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

    data.append('auxiliarDoc', auxiliarDoc);
    data.append('comentarios', $('#comentariosDosSuscrip').val());
    data.append('infoCot', JSON.stringify(infCotizacion));
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

$("#dac_noUbica" ).on("blur", function(){
	if(this.value < 5){
		$("#btn-carga-masiva").addClass("d-none");
	}else{
		$("#btn-carga-masiva").removeClass("d-none");
	}
});

$("#btn-carga-masiva").click(function(e){
	$("#modalCargaMasiva").modal("show");
});


$("#docCargaMasiva").change(function(evt){
	var listMimetypeValid = ["application/vnd.ms-excel",
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
		"application/vnd.ms-excel.sheet.binary.macroEnabled.12"
    ];
	
	var file = evt.target.files[0];
	var tipoPermitido = (listMimetypeValid.indexOf(file.type) >= 0);
	
	if(tipoPermitido){
		$("#infDocCargaMasiva").val(file.name);
	}else{
		$("#infDocCargaMasiva").val("");
		$(this).val("");
		showMessageError( '#modalCargaMasiva .modal-header', "Archivo no valido", 0 );
	}
	
});

$("#btnCargMasiAcepta").click(function(e){
	
	var dataDoc = new FormData();
	
	dataDoc.append('docAgenSusc', $('#docCargaMasiva')[0].files[0]);
	
	$.ajax( {
		url : ligasServicios.cargaMasiva, 
		data : dataDoc, 
		processData : false, 
		contentType : false, 
		type : 'POST',
		async: false,
		success : function(data) {
			console.log(data);
			cargaMasivaString = data;
			$("#modalCargaMasiva").modal("hide");
			
			$.post(ligasServicios.saveUbiCMRC, 
				{	p_folio: infCotizacion.folio,
					p_cotizacion: infCotizacion.cotizacion,
					p_version: infCotizacion.version,
					cargaMasiva: cargaMasivaString
				})
					.done(function(dataUbi) {
						$('#saveResponse').val(dataUbi);
						showLoader();
						$("#saveUbicaciones_form").submit();
					});
		}
	});
});