/**
 * Agrega clase al campo espesifico para ocultarlo
 * mandar el selector tipo jquery
 * id -> #id
 * class -> .class
 * @param selector
 * @returns
 */
function ocultaCampos(selector){
	$(selector).addClass("d-none");
	$(selector).removeClass("d-block");
}

/**
 * Agrega clase al campo espesifico para mostrarlo
 * mandar el selector tipo jquery
 * id -> #id
 * class -> .class
 * @param selector
 * @returns
 */
function muestraCampos(selector){
	$(selector).addClass("d-block");
	$(selector).removeClass("d-none");
}

function activaCampos(campo){
	if(valIsNullOrEmpty($(campo).val())){
		$(campo).siblings('label').removeClass('active');
	}else{
		$(campo).siblings('label').addClass('active');
	}
}

/**
 * Destrulle y regenera los material select
 * @param objeto
 * @param enabled
 * @returns
 */
function selectDestroy(objeto, enabled) {
    $(objeto).prop("disabled", enabled);
    $(objeto).materialSelect('destroy');
    $(objeto).materialSelect();
}


function noSelect(campo) {
    var errores = false;
    if ($(campo).val() == "-1") {
        errores = true;
        $(campo).siblings("input").addClass('invalid');
        $(campo).parent().append(
            "<div class=\"alert alert-danger\"> <span class=\"glyphicon glyphicon-ban-circle\"></span> " + " " +
            msj.es.campoRequerido + "</div>");
    }
    
    return errores;
}

function noSelect2(campo) {
    var errores = false;
    if ($(campo).val() == "-1") {
        errores = true;
    }
    
    return errores;
}

function vaciosInpText(value) {	
	var errores = false;	
	if($(value).is(":visible")){
		if (valIsNullOrEmpty($(value).val())) {
			errores = true;
			$(value).addClass('invalid');
			$(value).parent().append(
					"<div class=\"alert alert-danger\" role=\"alert\"> <span class=\"glyphicon glyphicon-ban-circle\"></span>" + " "
					+ msj.es.campoRequerido + "</div>");
		}			
	}
	return errores;
}

function vaciosInpText2(value) {	
	var errores = false;	
	/*if($(value).is(":visible")){*/
		if (valIsNullOrEmpty($(value).val())) {
			errores = true;
		}			
	/*}*/
	return errores;
}


$("#contPaso1 :input").change(function(){
	removeClassInvalid();
});

$("#contPaso1 :input").click(function(){
	removeClassInvalid();
});

$("#contenPaso2 :input").change(function(){
	removeClassInvalid();
});

$("#contenPaso2 :input").click(function(){
	removeClassInvalid();
});

function removeClassInvalid(){
	$(".alert-danger").remove();
    $('.invalid').removeClass('invalid');
}

/**
 * llena input tex 
 * @param campo
 * @param valor
 * @param disabled
 * @returns
 */
function llenaCampoText(campo, valor, disabled){
	$(campo).val(valor);
	activaCampos(campo)
	$(campo).prop("disabled", disabled);
}


function valIsNullOrEmpty(value) {
	if (value === undefined) {
		return true;
	}
	value = value.trim();
	return (value == null || value == "null" || value === "");
}


function deshabilitaRadio(selector, disabled){
	$(selector).find(".form-check-input").prop("disabled", disabled);
}

function seleccionaOpcionSelect(campo, value, disabled){
	$(campo + " option[value = '"+ value +"' ]").attr("selected", true);
	selectDestroy(campo, disabled);
}

function chkRdodtsContr(rdoBtons) {
    var res = null;
    $.each(rdoBtons, function(index, value) {
        if ($(value).is(':checked')) {
            res = $(value).val();
            return false;
        }
    });
    return res;
}

/* Funciones Paso 2 */

function daFormatoMoneda(campo){
	if(!valIsNullOrEmpty($( campo ).val())){
		$( campo ).val(formatter.format($( campo ).val()));
	}
}

$(".acordeon .moneda" ).on("blur", function(){
	this.value = this.value.replace(/[^0-9\.]/g,'');
	daFormatoMoneda($(this));
});

$(".acordeon .moneda" ).on("keyup", function(event){
	var aux = $(event.target).val().split('.');
	$(event.target).val(aux[0]);
	 $(event.target).val(function (index, value ) {
	        return value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
	    });
});

$(".acordeon .moneda" ).on("focus", function(){
	/*this.value = this.value.replace(/[^0-9\.,]/g,''); */
	var abc = $(this).val().replace(/[^0-9\.,]/g,'');
	$(this).val(abc.split('.')[0]);
});

$('.monedaDec').on('keyup', function() {
    $(event.target).val(function(index, value) {
        var aux = value.replace(/[$,]/g, '');
        aux = aux.replace(/\D/g, "")
            .replace(/([0-9])([0-9]{2})$/, '$1.$2')
            .replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
        return '$' + aux;
    });
});

$("#tabs .cpValido" ).on("keyup", function(){
	this.value = this.value.replace(/[^0-9\.]/g,'');
});

$("#tabs .nvTot" ).on("keyup", function(){
	this.value = this.value.replace(/[^0-9]/g,'');
	if(parseInt(this.value) < 1 || parseInt(this.value) > 50){
		this.value = this.value.slice(0, -1);
		showMessageError( '.navbar', "Los niveles deben estar en un rango de 1 a 50 ", 0 );
	}
});


$("#tabs .nvTot" ).blur(function(){
	hideLoader();
	var ubicacionPadre = $( this ).closest( ".ubicacion" );
	var tot = $(this).val();
	if(!valIsNullOrEmpty(tot)){
		var selector = $( ubicacionPadre).find('#dr_nivelReal');
		$(selector).find('option').not(':first').remove();
		var maxSelect = parseInt(this.value);
		for(var i = 1; i <= maxSelect; i++){
			$(selector).append($('<option>', { 
				value: i,
				text : i 
			}));	
		}		
		selectDestroy($(selector), false);
	}
});

$(".acordeon .numero" ).on("keyup", function(event){
	if( parseInt($(this).val()) > parseInt( $(this).attr('max') ) ){
		$(this).val( $(this).attr('max') );
	}
});
