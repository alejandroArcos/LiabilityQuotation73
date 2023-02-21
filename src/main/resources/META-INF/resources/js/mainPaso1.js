$( document ).ready(function() {
	showLoader();	
	window.scrollTo(0, 0);
	
	validaInfoCliente();
	
	var retroactivo;
	
	if(diasRetro > 0) {
		if(diasRetro == 90) {
			retroactivo = new Date();
			retroactivo.setMonth(retroactivo.getMonth()-3);
		}
		else {
			retroactivo = -diasRetro;
		}
	}
	else{
		retroactivo = new Date();
	}
	
	$( '.datepicker' ).pickadate( {
		format : 'yyyy-mm-dd',
		formatSubmit : 'yyyy-mm-dd',
		min : retroactivo,
		max : 365
	} );
	$( '.datepicker-conven' ).pickadate( {
		format : 'yyyy-mm-dd',
		formatSubmit : 'yyyy-mm-dd',
		min : new Date()
	} );

	$( '.datepicker-custom' ).pickadate( {
		format : 'yyyy-mm-dd',
		formatSubmit : 'yyyy-mm-dd'
	} );
	
	var pick_ini = $( '#dc_dateDesde' ).pickadate( 'picker' );
	var pick_fin = $( '#dc_dateHasta' ).pickadate( 'picker' );
	var pick_convencional = $( '#dac_dateConvencional' ).pickadate( 'picker' );
	
	
	
	/**********************AUTOCOMPLETE***************************************/
	$( "#ce_nombre" ).autocomplete( {
		minLength : 3,
		source : function(request, response) {
			$.getJSON( ligasServicios.listaPersonas, {
				term : request.term,
				tipo : 1,
				pantalla : pantallaString
			}, function(data, status, xhr) {
				sessionExtend();
				if (data.codigo == '0') {
					showMessageError( '.navbar', msj.es.errorInformacion, 0 );
					console.error("autocomplete nombre");
					response( null );
				} else {
					response( data );
				}
			} );
		},
		focus : function(event, ui) {
			$( "#ce_nombre" ).val( ui.item.nombrepersona );
			return false;
		},
		select : function(event, ui) {
			$( "#ce_nombre" ).val( ui.item.nombre + " " + ui.item.appPaterno + " " + ui.item.appMaterno );
			$( "#ce_rfc" ).val( ui.item.rfc );
			$( "#ce_codigo" ).val( ui.item.codigo );
			$( "#ce_idPersona" ).val( ui.item.idPersona );
			$( "#tipoPer" ).val( ui.item.tipoPer );
			$( "#idDenominacion" ).val( ui.item.idDenominacion );
			activaCampos("#ce_rfc");
			activaCampos("#ce_codigo");
			auxP1.infoClientExistenttEncontrado = ui.item;
			seleccionaTipoPer();
			return false;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			showMessageError( '.navbar', msj.es.errorInformacion, 0 );
			console.error("autocomplete nombre");
		}
	} ).autocomplete( "instance" )._renderItem = function(ul, item) {
		if (item.idDenominacion == 0) {
			return $( "<li>" ).append(
					"<div>" + item.nombre + " " + item.appPaterno + " " + item.appMaterno + "</div>" )
					.appendTo( ul );
		} else {
			return $( "<li>" ).append(
					"<div>" + item.nombre + " " + item.appPaterno + " " + item.appMaterno + "</div>" )
					.appendTo( ul );
		}
	};

	/* autocomplite por rfc */
	$( "#ce_rfc" ).autocomplete( {
		minLength : 3,
		source : function(request, response) {
			$.getJSON( ligasServicios.listaPersonas, {
				term : request.term,
				tipo : 3,
				pantalla : pantallaString
			}, function(data, status, xhr) {
				sessionExtend();
				if (data.codigo == '0') {
					showMessageError( '.navbar', msj.es.errorInformacion, 0 );
					console.error("autocomplete rfc");
					response( null );
				} else {
					response( data );
				}
			} );
		},
		focus : function(event, ui) {
			$( "#ce_rfc" ).val( ui.item.nombrepersona );
			return false;
		},
		select : function(event, ui) {
			$( "#ce_nombre" ).val( ui.item.nombre + " " + ui.item.appPaterno + " " + ui.item.appMaterno );
			$( "#ce_rfc" ).val( ui.item.rfc );
			$( "#ce_codigo" ).val( ui.item.codigo );
			$( "#ce_idPersona" ).val( ui.item.idPersona );
			$( "#tipoPer" ).val( ui.item.tipoPer );
			$( "#idDenominacion" ).val( ui.item.idDenominacion );
			activaCampos("#ce_nombre");
			activaCampos("#ce_codigo");
			auxP1.infoClientExistenttEncontrado = ui.item;
			seleccionaTipoPer();
			return false;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			showMessageError( '.navbar', msj.es.errorInformacion, 0 );
			console.error("autocomplete rfc");
		}
	} ).autocomplete( "instance" )._renderItem = function(ul, item) {
		if (item.idDenominacion == 0) {
			return $( "<li>" ).append(
					"<div>" + item.rfc + " - " + item.nombre + " " + item.appPaterno + " " + item.appMaterno
							+ "</div>" ).appendTo( ul );
		} else {
			return $( "<li>" ).append(
					"<div>" + item.rfc + " - " + item.nombre + " " + item.appPaterno + " " + item.appMaterno
							+ "</div>" ).appendTo( ul );
		}
	};
	/**********************AUTOCOMPLETE***************************************/
	hideShowFechaConven();
	aplicaReglas();
	cambiaFecha();
	hideLoader();
});

function aplicaReglas() {
	seleccionaModo();
}

function seleccionaModo() {
	switch (infCotizacion.modo) {
		case modo.NUEVA :
			$('.divRdoTipoCotiza .form-check-input[value=1]').attr('checked', true);
			/*$('#dac_coaseguro option[value=2575]').attr("selected",true);
			$('#dac_esqAsegura option[value=2688]').attr("selected",true);*/
			$("#dac_coaseguro option:contains('DIRECTO')").attr("selected",true);
			$("#dac_esqAsegura option:contains('OCURRENCE')").attr("selected",true);
			$('#divFechaConven').hide();
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.EDICION :
			bloqueaCampoEdicion();
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			
			$.post(validaAgenteURL, {
				cotizacion: 0,
		    	codigoAgente: $("#dc_agentes option:selected").text()
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
		    });
			
			break;
		case modo.COPIA :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			
			$.post(validaAgenteURL, {
				cotizacion: 0,
		    	codigoAgente: $("#dc_agentes option:selected").text()
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
		    });
			
			break;
		case modo.ALTA_ENDOSO :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.EDITAR_ALTA_ENDOSO :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.CONSULTA :
		case modo.CONSULTA_VOBO_REASEGURO :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			bloqueaCampoConsulta();
			console.log('modo: ' + infCotizacion.modo);
			
			$.post(validaAgenteURL, {
				cotizacion: 0,
		    	codigoAgente: $("#dc_agentes option:selected").text()
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
		    });
			
			break;
		case modo.CONSULTAR_REVISION :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			bloqueaCampoConsulta();
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.BAJA_ENDOSO :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.EDITAR_BAJA_ENDOSO :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			break;
		case modo.COASEGURO:
		case modo.COASEGURO_CONSULTA:
		case modo.REASEGURO:
		case modo.REASEGURO_CONSULTA:
		case modo.VOBO_REASEGURO:
		case modo.FACTURA_492 :
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			showLoader();
			$("#paso1_next").click();
			break;
		case modo.EDICION_JAPONES:
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			
			$.post(validaAgenteURL, {
				cotizacion: 0,
		    	codigoAgente: $("#dc_agentes option:selected").text()
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
		    });
			
			break;
		/*
		case modo.COASEGURO:
			bloqueaCampoEdicion();
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			console.log('modo: ' + infCotizacion.modo);
			break;
		*/
		default:
			auxP1.infoClientExistenttEncontrado = JSON.parse(datosCliente);
			showMessageError('.navbar', msj.es.errorInformacion, 0);
			console.error(" Modo default");
			break;
	}
}

function bloqueaCampoEdicion(){
	deshabilitaRadio(".divRdoTpClient", true);
	deshabilitaRadio(".divRdoTipoCotiza", true);
	$("#ce_rfc").prop("disabled", true);
	$("#ce_nombre").prop("disabled", true);
	$("#ce_codigo").prop("disabled", true);
	$("#dc_movimientos").prop("disabled", true);
	$("#dc_detalleSubgiro").prop("disabled", true);
	if( perfilSuscriptor == '0'){
		$("#dc_dateDesde").prop("disabled", true);		
		$("#dc_agentes").prop("disabled", true);
		$("#dc_giro").prop("disabled", true);
		$("#dc_subgiro").prop("disabled", true);
		$("#dac_cnlNegocio").prop("disabled", true);
	}else{
		$("#dac_cnlNegocio").prop("disabled", false);
		$("#dc_subgiro").prop("disabled", false);		
	}
}
function bloqueaCampoConsulta(){
	deshabilitaRadio(".divRdoTpClient", true);
	deshabilitaRadio(".divRdoTipoCotiza", true);
	$("#ce_rfc").prop("disabled", true);
	$("#ce_nombre").prop("disabled", true);
	$("#ce_codigo").prop("disabled", true);
	$("#dc_movimientos").prop("disabled", true);
	$("#dc_moneda").prop("disabled", true);
	$("#dc_detalleSubgiro").prop("disabled", true);
	$("#dc_dateDesde").prop("disabled", true);		
	$("#dc_dateHasta").prop("disabled", true);		
	$("#dc_agentes").prop("disabled", true);
	$("#dc_formpago").prop("disabled", true);
	$("#dc_giro").prop("disabled", true);
	$("#dc_subgiro").prop("disabled", true);
	$("#dc_sector").prop("disabled", true);
	$("#dac_cnlNegocio").prop("disabled", true);
	$("#dac_coaseguro").prop("disabled", true);
	$("#dac_grdoRiesRC").prop("disabled", true);
	$("#dac_esqAsegura").prop("disabled", true);
	$("#dc_dateConven").prop("disabled", true);
}

/**
 * @description tipo 0 = Cliente Existente, tipo 1 = Cliente Nuevo
 */
$( '.divRdoTpClient .form-check-input' ).click( function(e) {
	if ($( this ).val() == "1") {
		muestraCampos( ".data_ctenvo" );
		ocultaCampos( ".data_cteext" );
		$(".data_cteext input:text").val("");
	} else {
		muestraCampos( ".data_cteext" );
		ocultaCampos( ".data_ctenvo" );
	}
} );

/**
 * @description tipo 2 = persona moral, tipo 1 = persona fisica
 */
$( '.tipo_persona .form-check-input' ).click( function() {
	if ($( this ).val() == "1") {
		muestraCampos( ".tip_fisica" );
		ocultaCampos( ".tip_moral" );
		fLlenaNombreFisica();
		actualisaFisica();
	} else {
		muestraCampos( ".tip_moral" );
		ocultaCampos( ".tip_fisica" );
		fLlenaNombreMoral();
		actualizaMoral();
	}
} );


function fLlenaNombreFisica() {
	var apPat = $( "#cn_fispaterno" ).val();
	var apMat = $( "#cn_fismaterno" ).val();
	var nom = $( "#cn_fisnombre" ).val();
	$( "#cn_nombrecompleto" ).val( nom + " " + apPat + " " + apMat );
	activaCampos( "#cn_nombrecompleto" );
}

function actualisaFisica(){
	$(".cn_ncEx").addClass("col-md-6");
	$(".cn_ncEx").removeClass("col-md-8");
	$(".cn_tpEx").addClass("col-md-3");
	$(".cn_tpEx").removeClass("col-md-4");
	$(".cn_rdEx").removeClass("d-none");
}

function fLlenaNombreMoral() {
	var nom = ($( "#cn_nombrecontratante" ).val().length > 0) ? ($( "#cn_nombrecontratante" ).val() + ", ") : "";
	var tip = ($( "#cn_denominacion :selected" ).val() != '-1') ? $( "#cn_denominacion :selected" ).text() : "";
	$( "#cn_nombrecompleto" ).val( nom + tip );
	activaCampos( "#cn_nombrecompleto" );
}

function actualizaMoral(){
	$(".cn_ncEx").addClass("col-md-8");
	$(".cn_ncEx").removeClass("col-md-6");
	$(".cn_tpEx").addClass("col-md-4");
	$(".cn_tpEx").removeClass("col-md-3");
	$(".cn_rdEx").addClass("d-none");
}

function bloqueaRadio(){
	$(".divRdoVigencia .form-check-input[value = " + infVigencia +"]").trigger("click")
	deshabilitaRadio(".divRdoTpClient", true);
	deshabilitaRadio(".divRdoVigencia", true);
	
}

function seleccionaTipoPer(){
	if (auxP1.infoClientExistenttEncontrado.tipoPer == 217){
		infCotizacion.tipoPersona = tipoPersona.FISICA;
	}else if(auxP1.infoClientExistenttEncontrado.tipoPer == 218){
		infCotizacion.tipoPersona = tipoPersona.MORAL;
	}
}

$( '#cn_rfc' ).on('blur',function(e) {
	if(!valIsNullOrEmpty($(this).val())){
		showLoader();
		auxP1.infoClientExistenttEncontrado = null;
		var rfc = $( "#cn_rfc" ).val().toUpperCase();
		if (rfcGenerico.indexOf( rfc ) < 0) {
			$.post( ligasServicios.listaPersonas, {
				term : $( this ).val(),
				tipo : 3,
				pantalla : infCotizacion.pantalla
			}, function(data) {
				var response = jQuery.parseJSON( data );
				if (response.length > 0) {
					$.each( response, function(key, registro) {
						if (registro.rfc === rfc) {
							auxP1.infoClientExistenttEncontrado = registro;
							return false;
						}
					} );
					if (auxP1.infoClientExistenttEncontrado != null) {
						$( '#nombreClienteExistt' ).text(
								auxP1.infoClientExistenttEncontrado.rfc + ' - ' + auxP1.infoClientExistenttEncontrado.nombre
										+ ' ' + auxP1.infoClientExistenttEncontrado.appPaterno + ' '
										+ auxP1.infoClientExistenttEncontrado.appMaterno );
						$( '#modalClienteExistente' ).modal( 'show' );
					}
				}else{
					auxP1.infoClientExistenttEncontrado = null;
				}
				hideLoader();
			} );
		}else{
			hideLoader();
		}
	}
} );

$( '#btnClienttExisttSi' ).click(function() {
	clickButton = true;
	$( '#radio_ce' ).trigger( 'click' );	
	llenaCampoText($( "#ce_nombre" ), auxP1.infoClientExistenttEncontrado.nombre + " " + auxP1.infoClientExistenttEncontrado.appPaterno + " "
			+ auxP1.infoClientExistenttEncontrado.appMaterno, false);	
	llenaCampoText($( "#ce_rfc" ), auxP1.infoClientExistenttEncontrado.rfc, false);
	llenaCampoText($( "#ce_codigo" ), auxP1.infoClientExistenttEncontrado.codigo, false);
	$(".data_ctenvo .form-control:input:text").val("");
	$("#modalClienteExistente").modal("hide");
} );

/**
 * @descripcion val = 0 con vigencia anual,
 * val = 1 --> a partir de emision
 */
$(".divRdoTipoCotiza .form-check-input").click(function() {
	/*$('#dc_dateHasta').datepicker('destroy');*/
	cambiaFecha();
	var auxTipoCoti = $(this).val();
	$('#tipoCoti').val( auxTipoCoti );
	
	changeGiroTipoPoliza(auxTipoCoti);
});


function changeGiroTipoPoliza( tipoCoti ){
	showLoader();
	$.post( ligasServicios.getGiroRC, {
		tipoCoti : tipoCoti
	}, function(data) {
		console.log(data);
		if( !valIsNullOrEmpty(data) ){
			var response = JSON.parse(data);
			if( response.msg = 'OK' ){
				$('#dc_giro option:not(:first)').remove();
				$.each(response.lista, function(key, registro) {
					
					var auxDidabledTxt = "";
					
					if(registro.codigo != "2") {
                		if ( perfilSuscriptor == "0" && registro.codigo == "1" ){
                			auxDidabledTxt = "disabled";
                		}
                		if( perfilSuscriptor == "0" ){
                			if( registro.codigo == "0" ){
                				$("#dc_giro").append(
                                        "<option value=\"" + registro.idCatalogoDetalle + "\" "+ auxDidabledTxt +">" + registro.valor + "</option>");
                			}
                		} else {
                			$("#dc_giro").append(
                                    "<option value=\"" + registro.idCatalogoDetalle + "\" "+ auxDidabledTxt +">" + registro.valor + "</option>");
                		}
					}
					/*
					$("#dc_giro").append('<option value="'+campo.idCatalogoDetalle+'">'+campo.valor+'</option>');
					*/
				});
				selectDestroy( $('#dc_giro'), false);
				resetSelectSubgiro();
			}else{
				resetSelectGiro()
				showMessageError('.navbar', response.msg, 0);
			}
			
		}else{
			resetSelectGiro();
			showMessageError('.navbar', 'Error al recuperar GIROS', 0);
		}
		hideLoader();
	});
}

function resetSelectGiro(){
	$('#dc_giro option:not(:first)').remove();
	selectDestroy( $('#dc_giro'), false);
	resetSelectSubgiro();
}

function resetSelectSubgiro(){
	$('#dc_subgiro option:not(:first)').remove();
	selectDestroy( $('#dc_subgiro'), true);
}

/*
$("#paso1_next").click(function(e){
	showLoader()
	
	guardaPaso1();
});
*/

$("#paso1_next").click(function(e){
	removeClassInvalid();
	showLoader();
	
	var completos = validaRequeridos();
	if(completos){
		llenaDatos();
		guardaPaso1();
		
		/*document.getElementById("formPaso1").submit();*/
		
	}else{
		hideLoader();
		showMessageError('.navbar', msj.es.faltaInfo, 0);
		$("#paso1_form").submit(function(e){
	        e.preventDefault();
	    });
	}
});


function guardaPaso1(){
	console.log('infoCoti: ' + JSON.stringify(infCotizacion));
	DatosGenerales.paso = 2;
	$('#auxDG').val(JSON.stringify(DatosGenerales));
	$.post( ligasServicios.guardaInfo, {
		datos : JSON.stringify(DatosGenerales),
		infoCot : JSON.stringify(infCotizacion)
	}, function(data) {
		console.log(data);
		var response = JSON.parse(data);

		if(response.code == 0){
			
			$('#folioCoti').val( response.folio );
			$('#versionCoti').val( response.version );
			$('#saveResponse').val( data );
			$('#infoCotizacion').val( JSON.stringify(infCotizacion) );
			
			/*
			$('#paso1_form').submit(function(){ 
		        $('<input />').attr('type', 'hidden')
		            .attr('name', 'saveResponse')
		            .attr('value', data)
		            .appendTo('#paso1_form');

			    return true;
			});
			*/
			showLoader();
			
			document.getElementById("paso1_form").submit();
			
		}else{
			showMessageError( '.navbar', response.msg, 0 );
			hideLoader();
		}
		
		
	});
	
}

function validaRequeridos(){
	var campos = $("#contPaso1 input:visible:enabled:not(:radio)");
	var completos = true;
	if( $(".cn_rdEx .switch:visible #chktoggle").is(":checked")){
		campos = $("#contPaso1 input:visible:enabled:not(:radio)").not("#cn_fismaterno");
	}
	$.each(campos, function(key, campo) {
		if($(campo).hasClass("select-dropdown")){
			var select = $(campo).siblings("select");
			completos = noSelect($(select)) ? false : completos;
		}else{
			completos = vaciosInpText($(campo)) ? false : completos;	
		}	
	});
	return completos;
}

function llenaDatos(){
	if($("#radio_ce").is(":checked")){
		llenaClienteExistente();
	}else{
		llenaClienteNuevo();
	}
	llenaDatsGene();
}

function llenaClienteExistente(){
	DatosGenerales.idPersona = auxP1.infoClientExistenttEncontrado.idPersona;
	DatosGenerales.tipoPer = auxP1.infoClientExistenttEncontrado.tipoPer;
	DatosGenerales.rfc = auxP1.infoClientExistenttEncontrado.rfc;
	DatosGenerales.nombre = auxP1.infoClientExistenttEncontrado.nombre;
	DatosGenerales.appPaterno = auxP1.infoClientExistenttEncontrado.appPaterno;
	DatosGenerales.appMaterno = auxP1.infoClientExistenttEncontrado.appMaterno;
	DatosGenerales.idDenominacion = auxP1.infoClientExistenttEncontrado.idDenominacion;
	DatosGenerales.codigo = auxP1.infoClientExistenttEncontrado.codigo;
}

function llenaClienteNuevo(){
	DatosGenerales.idPersona = 0;
	DatosGenerales.tipoPer = parseInt($(".tipo_persona .form-check-input:checked").val(), 10);
	DatosGenerales.rfc = $("#cn_rfc").val();
	llenaNombreCN();
	DatosGenerales.codigo = "";
}

function llenaDatsGene(){
	var auxtipoComercio = parseInt($(".divRdoTipoCotiza .form-check-input:checked").val(), 10);
	$("#tipoCoti").val(auxtipoComercio);
	DatosGenerales.p_tipoComercio = auxtipoComercio;
	DatosGenerales.p_tipoMov = parseInt($("#dc_movimientos").val(), 10);
	DatosGenerales.p_moneda = parseInt($("#dc_moneda").val(), 10);
	$('#tipoMoneda').val( $("#dc_moneda").val( ) );
	DatosGenerales.p_fecInicio = $("#dc_dateDesde").val();
	DatosGenerales.p_fecFin = $("#dc_dateHasta").val();
	DatosGenerales.p_agente = parseInt($("#dc_agentes").val(), 10);
	DatosGenerales.p_formaPago = parseInt($("#dc_formpago").val(), 10);
	DatosGenerales.p_giro = $("#dc_giro").val();
	DatosGenerales.p_subGiro = $("#dc_subgiro").val();
	DatosGenerales.sector = $("#dc_sector").val();
	
	$('#subGiroRiesgo').val( $('#dc_subgiro').find('option:selected').attr('suscripcion') );
	DatosGenerales.p_tipoCambio = parseFloat(  $('#dac_tpoCambio').val().replace('$','') );
	DatosGenerales.p_canalNegocio = parseInt($("#dac_cnlNegocio").val(), 10);
	$('#canalNegocio').val( $("#dac_cnlNegocio").val( ) );
	DatosGenerales.p_tipoCao = parseInt($("#dac_coaseguro").val(), 10);
	$('#tipoCoaseguro').val( $("#dac_coaseguro").val( ) );
	DatosGenerales.p_gradoRiesgo = parseInt($("#dac_grdoRiesRC").val(), 10);

	DatosGenerales.p_esquemaAseguramiento = parseInt($("#dac_esqAsegura").val(), 10);
	DatosGenerales.p_fechaConvencional = $("#dc_dateConven").val();
	
	
	DatosGenerales.modo = infCotizacion.modo;
	DatosGenerales.tipoCot = 'LIABILITY';
	DatosGenerales.folio = infCotizacion.folio;
	DatosGenerales.cotizacion = infCotizacion.cotizacion;
	DatosGenerales.version = infCotizacion.version;
	DatosGenerales.pantalla = 'LIABILITY';	
}

function llenaNombreCN(){
	if($("#cn_personamoral").is(":checked")){
		DatosGenerales.nombre = $("#cn_nombrecontratante").val();
		DatosGenerales.appPaterno ="";
		DatosGenerales.appMaterno = "";
		DatosGenerales.idDenominacion = parseInt($("#cn_denominacion").val(), 10);
		DatosGenerales.extranjero = 0;
	}else{
		DatosGenerales.nombre = $("#cn_fisnombre").val();
		DatosGenerales.appPaterno = $("#cn_fispaterno").val();
		DatosGenerales.appMaterno = $("#cn_fismaterno").val();
		DatosGenerales.idDenominacion = 0;
		DatosGenerales.extranjero = $(".cn_rdEx .switch:visible #chktoggle").is(":checked") ? 1: 0;
	}
}

$('#dc_dateDesde').on("change", function() {
	cambiaFecha();
});

function cambiaFecha(){
	var pick_ini = $( '#dc_dateDesde' ).pickadate( 'picker' );
	var pick_fin = $( '#dc_dateHasta' ).pickadate( 'picker' );
	var pick_convencional = $( '#dc_dateConven' ).pickadate( 'picker' );
	var iniSelec = pick_ini.get("select");
	var convenSelec= pick_convencional.get("select");
	if(valIsNullOrEmpty(iniSelec)){
		pick_fin.set('clear');
	}else{
		/*var anioSig = new Date((iniSelec.year +1), iniSelec.month, iniSelec.date);*/

		var auxMin = new Date(iniSelec.year, iniSelec.month, iniSelec.date);
		
		
		if($('#dc_contruccion').is(":checked")){
			var anioSigMax = new Date((iniSelec.year +2), iniSelec.month, iniSelec.date);
			var anioSigSel = new Date((iniSelec.year +1), iniSelec.month, iniSelec.date);

		}else{
			var anioSigMax = new Date((iniSelec.year +1), iniSelec.month, iniSelec.date);
			var anioSigSel = new Date((iniSelec.year +1), iniSelec.month, iniSelec.date);
			auxMin = new Date(iniSelec.year + 1, iniSelec.month, iniSelec.date);
		}
		pick_fin.set('min', auxMin);
		pick_fin.set('max', anioSigMax);
		pick_fin.set('select', anioSigSel);
		pick_fin.set('view', anioSigSel);	
		
		actualizaFechaConvencional();
	}
	
}

function actualizaFechaConvencional(){
	var iniSelec = $( '#dc_dateDesde' ).pickadate( 'picker' ).get("select");
	var auxMinConv = new Date(iniSelec.year - retroactividadFechaConv(), iniSelec.month, iniSelec.date);
	var pick_convencional = $( '#dc_dateConven' ).pickadate( 'picker' );
	var convenSelec= pick_convencional.get("select");
	
	pick_convencional.set('min', auxMinConv);
	if(!valIsNullOrEmpty(convenSelec) && $("#dc_dateConven").is(":visible")){
		var auxConven = new Date(convenSelec.year, convenSelec.month, convenSelec.date);
		if(auxConven < auxMinConv){
			pick_convencional.set('clear');
		}
	}
}

function retroactividadFechaConv(){
	let anios=0;
	let canal=$('#dac_cnlNegocio').find('option:selected').text().substring(0,3).trim();
	
	$.each(listaCatFechaConv.lista, function(key, registro) {
		if(registro.valor==idPerfilUser && (registro.codigo=='*' || registro.codigo==canal)){
			if(parseInt(registro.otro) > anios){
				anios=parseInt(registro.otro);
			}
		}
    });
	
	return anios;
}


$('#dac_cnlNegocio').change(function() {
	actualizaFechaConvencional();
});



$('#dc_giro').change(function() {
    showLoader();
    $("#dc_subgiro option:not(:first)").remove();
    
    if ($(this).val() === '-1') {
    	 selectDestroy($("#dc_subgiro"), true);
         hideLoader();
    }else{
    	var seleccionado = $(this).val();
        $.post(ligasServicios.listaSubgiros, {
            giro: seleccionado,
            pantalla : pantallaString
        }).done(function(data) {
            sessionExtend();
            if (valIsNullOrEmpty(data)) {
                showMessageError('.navbar', msj.es.errorInformacion, 0);
                console.error("giro change");
                selectDestroy($("#dc_subgiro"), true);
            } else {
                var response = jQuery.parseJSON(data);
                if(response.totalRow > 0){
                	$.each(response.lista, function(key, registro) {
                		var auxDidabledTxt = "";
                		
                		if(registro.otro != "2") {
	                		if ( perfilSuscriptor == "0" && registro.otro == "1" ){
	                			auxDidabledTxt = "disabled";
	                		}
	                		if( perfilSuscriptor == "0" ){
	                			if( registro.otro == "0" ){
	                				$("#dc_subgiro").append(
	                                        "<option value=\"" + registro.idCatalogoDetalle + "\" suscripcion=\"" +
	                                        registro.otro + "\" codigo=\"" + registro.codigo + "\" "+ auxDidabledTxt +">" + registro.valor + "</option>");
	                			}
	                		}else{
	                			$("#dc_subgiro").append(
	                                    "<option value=\"" + registro.idCatalogoDetalle + "\" suscripcion=\"" +
	                                    registro.otro + "\" codigo=\"" + registro.codigo + "\" "+ auxDidabledTxt +">" + registro.valor + "</option>");
	                		}
                		}
                        /*$("#dc_subgiro").append(
                            "<option value=\"" + registro.idCatalogoDetalle + "\" suscripcion=\"" +
                            registro.otro + "\" codigo=\"" + registro.codigo + "\" "+ auxDidabledTxt +">" + registro.valor + "</option>");*/
                    });
                    selectDestroy($("#dc_subgiro"), false);
                }else{
                	$("#dc_subgiro option:not(:first)").remove();
                	selectDestroy($("#dc_subgiro"), true);
                	showMessageError('.navbar', msj.es.errorInformacion, 0);
                	console.error("giro change");
                }
            }
            hideLoader();
        }).fail(function() {
        	$("#dc_subgiro option:not(:first)").remove();
        	selectDestroy($("#dc_subgiro"), true);
        	showMessageError('.navbar', msj.es.errorInformacion, 0);
        	console.error("giro change");
        	 hideLoader();
        });
    }
});

$('#dc_subgiro').change(function() {
	/*
	$('#dac_grdoRiesRC option').attr('selected', false);
	if($('option:selected', this).attr('suscripcion') == '1'){
		$('#dac_grdoRiesRC option[value=2509]').attr('selected', true);
	}else{
		$('#dac_grdoRiesRC option[value=-1]').attr('selected', true);
	}
	selectDestroy( $('#dac_grdoRiesRC'), false );
	*/
	$('#dac_grdoRiesRC option').prop('selected', false);
	if(!valIsNullOrEmpty(perfilSuscriptor)){
		if(perfilSuscriptor == '0'){
			if($('option:selected', this).attr('suscripcion') == '1'){
				$('#modalGiroSubgiro').modal({
					show: true,
					backdrop: 'static',
					keyboard: false
				});
			}			
		}
	}
	
	var seleccionado = $(this).val();
    $.post(ligasServicios.gradoRiesgo, {
        subGiro: seleccionado,
		codigo: $(this).find(':selected').attr('codigo'),
		pantalla : infCotizacion.pantalla
    }).done(function(data) {
		console.log(data);
		var response = jQuery.parseJSON(data);
		
		$('#dac_grdoRiesRC').val(response.rc).siblings('label').addClass('active');
    });
	
});

$("#btnSuscripGiroNo").click(function(){
	$('#dc_subgiro option[value=-1]').prop('selected', true);
    selectDestroy($('#dc_subgiro'), false);
    $('#modalGiroSubgiro').modal('hide');
});

$('#dc_agentes').change(function() {
	if ($( this ).val() == "0000000000001"){
		$('#dc_fcomicion').hide();
		$('#dc_fcomicion').val() = 0;
	}else{
		$('#dc_fcomicion').show();
	}
	
	showLoader();
	
    $("#dac_cnlNegocio option:not(:first)").remove();
    
    if ($(this).val() === '-1') {
    	 selectDestroy($("#dac_cnlNegocio"), true);
    	 actualizaFechaConvencional();
         hideLoader();
    }else{
		$.post(ligasServicios.canalNegocio, {
	    	codigoAgente: $("#dc_agentes option:selected").text(),
	        pantalla : pantallaString
	    }).done(function(data) {
	        sessionExtend();
	        if (valIsNullOrEmpty(data)) {
	            showMessageError('.navbar', msj.es.errorInformacion, 0);
	            console.error("giro change");
	            selectDestroy($("#dac_cnlNegocio"), true);
	        } else {
	            var response = jQuery.parseJSON(data);
	            if(response.totalRow > 0){
	            	$.each(response.lista, function(key, registro) {
						let seleccionado = registro.otro == "1" ? 'selected' : '';
	                    $("#dac_cnlNegocio").append(
	                        "<option value=\"" + registro.idCatalogoDetalle + "\" suscripcion=\"" +
	                        registro.otro + "\" codigo=\"" + registro.codigo + "\" " + seleccionado + " >" + registro.valor + "</option>");
	                });
	                selectDestroy($("#dac_cnlNegocio"), false);
	            }else{
	            	$("#dac_cnlNegocio option:not(:first)").remove();
	            	selectDestroy($("#dac_cnlNegocio"), true);
	            	showMessageError('.navbar', msj.es.errorInformacion, 0);
	            	console.error("Agente Change");
	            }
	        }
	        actualizaFechaConvencional();
	        hideLoader();
	    }).fail(function() {
	    	$("#dac_cnlNegocio option:not(:first)").remove();
	    	selectDestroy($("#dac_cnlNegocio"), true);
	    	showMessageError('.navbar', msj.es.errorInformacion, 0);
	    	console.error("Agente Change");
	    	actualizaFechaConvencional();
	    	hideLoader();
	    });
	
		$.post(validaAgenteURL, {
			cotizacion: 0,
	    	codigoAgente: $("#dc_agentes option:selected").text()
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
	    });
	
	}
} );

$( '.tip_fisica input:text' ).keyup( function() {
	fLlenaNombreFisica();
} );

$( '.divPerMor #cn_nombrecontratante' ).keyup( function() {
	fLlenaNombreMoral();
} );

$( '.divPerMor #cn_denominacion' ).on( 'change', function() {
	fLlenaNombreMoral();
} );

function fLlenaNombreFisica() {
	var apPat = $( "#cn_fispaterno" ).val();
	var apMat = $( "#cn_fismaterno" ).val();
	var nom = $( "#cn_fisnombre" ).val();
	$( "#cn_nombrecompleto" ).val( nom + " " + apPat + " " + apMat );
	activaCampos( "#cn_nombrecompleto" );
}

function fLlenaNombreMoral() {
	var nom = ($( "#cn_nombrecontratante" ).val().length > 0) ? ($( "#cn_nombrecontratante" ).val() + ", ") : "";
	var tip = ($( "#cn_denominacion :selected" ).val() != '-1') ? $( "#cn_denominacion :selected" ).text() : "";
	$( "#cn_nombrecompleto" ).val( nom + tip );
	activaCampos( "#cn_nombrecompleto" );
}

$('#dac_esqAsegura').on("change", function() {
	hideShowFechaConven();
});

function hideShowFechaConven(){
	if($( "#dac_esqAsegura :selected" ).text() == 'OCURRENCE' ){
		$('#divFechaConven').hide();
	}else{
		$('#divFechaConven').show();		
	}
}

function validaInfoCliente(){
	if( !valIsNullOrEmpty(infoCliente) ){
		
		auxP1.infoClientExistenttEncontrado = JSON.parse(infoCliente);
	}
}

$(".numerPosi").keyup(function(e){
	this.value = this.value.replace(/[^0-9]/g,'');
});