/**
 * Modo para el flujo de cotizaciones
 */
const modo = {
    NUEVA: "NUEVA",
    EDICION: "EDICION",
    COPIA: "COPIA",
    AUX_PASO4: "AUX_PASO4",
    ALTA_ENDOSO: "ALTA_ENDOSO",
    BAJA_ENDOSO: "BAJA_ENDOSO",
    EDITAR_ALTA_ENDOSO : "EDITAR_ALTA_ENDOSO",
    EDITAR_BAJA_ENDOSO : "EDITAR_BAJA_ENDOSO",
    CONSULTA : "CONSULTA",
    FACTURA_492 : "FACTURA_492",
    ERROR : "ERROR",
    RENOVACION_AUTOMATICA : "RENOVACION_AUTOMATICA",
    CONSULTAR_RENOVACION_AUTOMATICA : "CONSULTAR_RENOVACION_AUTOMATICA",
	EDICION_JAPONES : "EDICION_JAPONES",
	CONSULTAR_REVISION: "CONSULTAR_REVISION",
	COASEGURO: "COASEGURO",
	REASEGURO: "REASEGURO",
	VOBO_REASEGURO: "VOBO_REASEGURO",
	REASEGURO_CONSULTA: "REASEGURO_CONSULTA",
	CONSULTA_VOBO_REASEGURO: "CONSULTA_VOBO_REASEGURO",
	COASEGURO_CONSULTA: "COASEGURO_CONSULTA",
};


const tipoCotizacion = {
	ERROR : "ERROR",
	FAMILIAR : "FAMILIAR",
	EMPRESARIAL : "EMPRESARIAL"
};

const tipoPersona = {
		FISICA : "FISICA",
		MORAL : "MORAL"
};

const formatter = new Intl.NumberFormat('en-US', {
	  style: 'currency',
	  currency: 'USD',
	  minimumFractionDigits: 2
});

const diasRetroactividad = 14;

const msj = {
		es : {
			errorInformacion : "Error al  cargar la información",
			catSinInfo: "Catalogo sin información",
	        campoRequerido: "El campo es requerido",
	        faltaInfo: "Hace falta información requerida",
	        errorGuardar: "Error al guardar su información",
	        ubMinimas: "El minimo de ubicaciones es 1"
		}
};

/**
 * objeto para Url de Resources Command 
 */
var ligasServicios = {
	listaPersonas : "",
	listaSubgiros : "",
	guardaInfo : "",
	redirige : "",
	gradoRiesgo : "",
	canalNegocio : "",
	getGiroRC : "",
	getCpURL : "",
	saveUbiRC : "",
	backP1: "",
	saveComisionesA : "",
	saveDeducibles: "",
	saveClausulasA: "",
	getSlip: "",
	rechazaCoti: "",
	enviarCoti: "",
	continuarJK: "",
	recalcularCot: "",
	cargaMasiva: "",
	saveUbiCMRC: "",
	getEspecificacion: ""
};

/**
 * rfc genericos que se descartan
 */
var rfcGenerico = ["XAXX010101000", "XEXX010101000"];

/**
 * Variables globales auxiliares j
 */
var auxP1 = {
	infoClientExistenttEncontrado : null
};

/**
 * informacion necesaria para guardar
 */
var DatosGenerales = {
	p_tipoComercio: 0,
	p_tipoMov :  0,
	p_moneda : 0,
	p_fecInicio : "",
	p_fecFin : "",
	p_formaPago : 0,
	p_giro : 0,
	p_subGiro: 0,
	p_tipoCambio : 0,
	p_canalNegocio : 0,
	p_tipoCao : 0,
	p_gradoRiesgo : 0,
	p_esquemaAseguramiento : 0,
	p_fechaConvencional : "",
	paso : 0,
	
		 vigencia :  0,
		 fecinicio :  "",
		 fecfin :  "",
		 moneda :  0,
		 formapago :  0,
		 agente :  0,
		 idPersona :  0,
		 tipoPer :  0,
		 rfc : ""  ,
		 nombre :  "",
		 appPaterno :  "",
		 appMaterno :  "",
		 idDenominacion :  0,
		 codigo :  "",
		 modo :  "",
		 tipoCot : "",
		 cotizacion :  "",
		 version :  0,
		 giro :  0,
		 subGiro :  0,
		 noUbicaciones: 0,
		 folio :  "",
		 detalleSubGiro :  "",
		 pantalla : "",
		 p_permisoSubgiro :  0,
		 subEstado : ""
};


/* Objetos Paso 2 */

const tipoGuardar = {
		GUARDAR_UBICACION : 1,
		AGREGAR_UBICACION : 2,
		ELIMINAR_UBICACION : 3,
		GUARDAR_FILEDS : 4,
		ELIMINAR_VARIAS : 5
};

var infoUltimaTab = {
		objeto : "",
		numero : "",
		etiqueta : ""
}

var pantallaString = "LiabilityQuotation";

/* es el objeto donde se setea la informacion de la cotizacion */
var infoCotJson =  null;

/* Es el objeto donde se setea la informacion de las ubicaciones */
var listaUbicaciones = new Object();

const enlace = {
		FAMILIAR : "/paquete-familiar",
		EMPRESARIAL : "/paquete-empresarial",
		PASO2 : "/paso2",
		PASO3 : "/paso3",
		PASO4 : "/paso4"
	};