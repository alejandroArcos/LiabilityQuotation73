<%@ include file="../init.jsp"%>
<jsp:include page="modalesP3.jsp" />

<portlet:resourceURL id="/RC/SaveComisionesAgente" var="saveComisionesA" cacheability="FULL"/>
<portlet:resourceURL id="/RC/Deducibles" var="saveDeducibles" cacheability="FULL"/>
<portlet:resourceURL id="/RC/saveClausulasA" var="saveClausulasA" cacheability="FULL"/>
<portlet:resourceURL id="/RC/getSlip" var="getSlipURL" cacheability="FULL"/>
<portlet:resourceURL id="/RC/rechazarCotizacion" var="rechazarURL" cacheability="FULL"/>
<portlet:resourceURL id="/RC/EnviarCotizacion" var="enviarURL" cacheability="FULL"/>
<portlet:resourceURL id="/RC/ContinuarJK" var="continuarJkURL" cacheability="FULL"/>
<portlet:resourceURL id="/RC/CalciularCoti" var="calcularURL" cacheability="FULL"/>
<portlet:resourceURL id="/cotizadores/paso3/getComisionesAgente" var="getComisionesAgenteURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/guardaComisionesAgente" var="guardaComisionesAgenteURL" cacheability="FULL" />
<portlet:resourceURL id="/RC/getSeccionComisionUrl" var="getSeccionComisionUrl" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/redirigePasoX" var="redirigeURL"  cacheability="FULL" />
<portlet:resourceURL id="/sendMailSuscriptorAgente" var="sendMailSuscriptorAgenteURL" cacheability="FULL"/>
<portlet:resourceURL id="/paso2/sendMailAgenteSuscriptor" var="sendMailAgenteSuscriptorURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/cargaSlipWordURL" var="cargaSlipWordURL" cacheability="FULL" />
<portlet:resourceURL id="/RC/paso3/getCaratulaURL" var="getCaratulaURL" cacheability="FULL" />
<portlet:resourceURL id="/RC/getEspecificacion" var="getEspecificacionURL" cacheability="FULL" />
<portlet:resourceURL id="/emisionArt492/getemision" var="getEmisionArt492Url" cacheability="FULL" />

<portlet:resourceURL id="/cotizadores/paso3/generaInfoCoaseguro" var="generaInfoCoaseguroURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/recalculoPrimaSuscriptor" var="primaObjetivoSuscriptorURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/validaPrima" var="validaPrimaURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/guardaPrima" var="guardaPrimaURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/getCuotaFinal" var="getCuotaFinalURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/generaInfoReaseguro" var="generaInfoReaseguroURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/paso3/getPermisoEmision" var="getPermisoEmisionURL" cacheability="FULL" />

<portlet:resourceURL id="/cotizadores/validaAgente" var="validaAgenteURL" cacheability="FULL" />

<portlet:resourceURL id="/getDocsEmision" var="getDocsEmision" cacheability="FULL" />

<portlet:resourceURL id="/cotizadores/paso3/generaInfoDesgloseCuotas" var="generaInfoDesgloseCuotasURL" cacheability="FULL" />


<portlet:actionURL var="cotizadorActionPaso2" name="/liability/actionPaso2" />
<portlet:actionURL var="cotizadorActionPaso4" name="/liability/actionPaso4" />

<fmt:setLocale value="es_MX" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=${version}">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css?v=${version}">

<style>
	.site-wrapper .modal-body {
		overflow-y: scroll;
		max-height: 70vh;
	}
</style>

<section id="cotizadores-p3" class="upper-case-all">
	
	<div class="section-heading">
		<div class="container-fluid">
			<h4 class="title text-left"> Cotizador Responsabilidad Civil </h4>
		</div>
	</div>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<ul class="stepper stepper-horizontal container-fluid">
					<li id="step1" class="completed">
						<a href="javascript:void(0)">
							<span class="circle">1</span>
							<span class="label">
								<liferay-ui:message key="LiabilityQuotationPortlet.titPasoUno" />
							</span>
						</a>
					</li>
					<li id="step2" class="completed">
						<a href="javascript:void(0)">
							<span class="circle">2</span>
							<span class="label">
								<liferay-ui:message key="LiabilityQuotationPortlet.titPasoDos" />
							</span>
						</a>
					</li>
					<li id="step3" class="active">
						<a href="javascript:void(0)">
							<span class="circle">3</span>
							<span class="label">
								<liferay-ui:message key="LiabilityQuotationPortlet.titPasoTres" />
							</span>
						</a>
					</li>
					<li id="step4">
						<a href="javascript:void(0)">
							<span class="circle">4</span>
							<span class="label">
								<liferay-ui:message key="LiabilityQuotationPortlet.titPasoCuatro" />
							</span>
						</a>
					</li>
				</ul>

			</div>
		</div>
	</div>
	
	<div style="position: relative;">
		<liferay-ui:error key="errorServicio" message="${caratulaResponse.msg}" />
		<liferay-ui:error key="errorConocido" message="${errorMsg}" />
		<liferay-ui:error key="reaseguroEliminado" message="${reaseguroEliminadoMsg}" />
	</div>
	
	<div class="container-fluid" id="divPaso3">
		
		<div class="section-heading">
			<div class="row divFolio">
				<div class="col-md-9">
					<c:choose>
						<c:when test="${tipoCotizacion == 1}">
							<h5 class="title text-left padding70 mt-4">Industria y Comercio</h5>
						</c:when>
						
						<c:when test="${tipoCotizacion == 2}">
							<h5 class="title text-left padding70 mt-4">Construcción</h5>
						</c:when>
					
						<c:when test="${tipoCotizacion == 3}">
							<h5 class="title text-left padding70 mt-4">Hotelería</h5>
						</c:when>
						
						<c:otherwise>
							<jsp:include page="tab_comercio.jsp" />
						</c:otherwise>
					</c:choose>
				</div>
				<div class="col-md-3" style="text-align: right;">
					<div class="md-form form-group">
						<input id="txtFolioP2" type="text" name="txtFolioP2" class="form-control" value="${infoCoti.folio} - ${infoCoti.version}"  disabled>
						<label class="active" for="txtFolioP1">
							Folio:
						</label>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row" style="padding: 0 70px">
		
			<div id="divTbl" class="col-md-12">
				<table class="customTable" style="width: 100%;">
				<!-- <table class="table simple-data-table table-striped table-bordered" style="width:100%;"> -->
					<thead>
						<tr>
							<c:if test="${perfilAgenteEjecutivo == 1}"> 
								<th>Cobertura</th><th>Suma Asegurada</th><th>Deducible</th>
							</c:if>
							<c:if test="${perfilAgenteEjecutivo != 1}"> 
								<th>Cobertura</th><th>Suma Asegurada</th><th>Prima</th><th>Deducible</th>								
							</c:if>
						</tr>
					</thead>

					<tbody id="tabPaso3">
						<c:set var="bandera" value=""/>
						<c:forEach items="${caratulaResponse.datosCaratula}" var="opc" varStatus="loop">
							<c:if test="${bandera != opc.contenedor}"> 
								<c:set var="bandera" value="${opc.contenedor}"/>
								<c:if test="${perfilAgenteEjecutivo == 1}"> 
									<tr><th>${bandera}</td><td></td><td></th></tr>
								</c:if>
								<c:if test="${perfilAgenteEjecutivo != 1}"> 
							   		<tr><th>${bandera}</td><td></td><td></td><td></th></tr>									
								</c:if>
							</c:if>
							<c:if test="${perfilAgenteEjecutivo == 1}"> 
								<tr><td>${opc.titulo}</td><td class="number">${opc.sa}</td><td>${opc.deducible}</td></tr>
							</c:if>
							<c:if test="${perfilAgenteEjecutivo != 1}"> 
								<tr><td>${opc.titulo}</td><td class="number">${opc.sa}</td><td class="number">${opc.prima}</td><td>${opc.deducible}</td></tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		
		</div>
		
		<div class="row" style="padding: 0 70px;">
			<div class="col-md-3">
				<div class="row" id="art41">
					<div class="col-sm-12">
						<div class="md-form">
							<input type="text" id="txtArt41" class="form-control ">
							<label id="titArt41" for="txtArt41">Art 41</label>
						</div>
					</div>
					<div class="col-sm-12">
						<button type="button" class="btn btn-pink waves-effect waves-light float-right btn-block" id="btnCederComision" name="btnCederComision">
							CEDER COMISIÓN
						</button>
					</div>
				</div>
			</div>
			
			<div class="col-md-6">
				<table class="table-borderless" style="width: 100%;">
					<tbody id="tabPaso3_2">
						<tr><td>Prima Neta:</td><td id="primaNeta" class="number"> <fmt:formatNumber value = "${caratulaResponse.primaNeta}" type = "currency"/> </td></tr>
						<tr><td>Recargo por Pago Fraccionado:</td><td class="number"><input id="recargoPago" class="moneda campoEditable" value="${caratulaResponse.recargo}" disabled="true" /></td><c:if test="${perfilSuscriptor == 1 || perfilJapones == 1}"><td><a id="editarRecargo" onclick="editarCamposPrima('recargoPago')" style="color: #0275d8; text-decoration: underline;">Editar</a></td></c:if></tr>
						<tr><td>Gastos de Expedición:</td><td class="number"><input id="gastos" class="moneda campoEditable" value="${caratulaResponse.gastos}" disabled="true" /></td><c:if test="${perfilSuscriptor == 1 || perfilJapones == 1}"><td><a id="editarGastos" onclick="editarCamposPrima('gastos')" style="color: #0275d8; text-decoration: underline;">Editar</a></td></c:if></tr>
						<tr><td>I.V.A.:</td><td class="number">  <fmt:formatNumber value = "${caratulaResponse.iva}" type = "currency"/> </td></tr>
<%-- 						<tr><td>Prima Neta:</td><td id="primaNeta" class="number moneda"> <fmt:formatNumber value = "${caratulaResponse.primaNeta}" type = "currency"/></td></tr> --%>
<%-- 						<tr><td>Recargo por Pago Fraccionado:</td><td id="recargoPago" class="number moneda"> <fmt:formatNumber value = "${caratulaResponse.recargo}" type = "currency"/> </td></tr> --%>
<%-- 						<tr><td>Gastos de Expediciï¿½n:</td><td class="number">  <fmt:formatNumber value = "${caratulaResponse.gastos}" type = "currency"/></td></tr> --%>
<%-- 						<tr><td>I.V.A.:</td><td class="number"> <fmt:formatNumber value = "${caratulaResponse.iva}" type = "currency"/></td></tr> --%>
					</tbody>
					<tfoot>
						<tr>
							<td>
								<b>Prima total:</b>
							</td>
							<td id="valPrimTot" class="number"><b id="tabPaso3_3"><fmt:formatNumber value = "${caratulaResponse.total}" type = "currency"/></span></b></td>
						</tr>
					</tfoot>
				</table>
			</div>
			
<!-- 			<div class="col-md-1 table-borderless"> -->
<!-- 				<a class="aLink" id="editaPrima" style="color: #1976d2; text-decoration: underline;">Editar</a><br> -->
<!-- 				<a class="aLink" id="editaRecargo" style="color: #1976d2; text-decoration: underline;">Editar</a> -->
<!-- 			</div> -->
			
			<div class="col-md-3" ${ (Leg492 == 'factura') ? 'hidden' : '' }>
				<div class="row ${ perfilSuscriptor == 1 || perfilJapones == 1 ? 'd-none' : '' } " id="divPrimaObj">
					<div class="col-sm-12">
						<div class="md-form">
							<c:set var="txtTipoMon" value="${tipoMoneda == 2 ? '(Dï¿½lares)' : '(Pesos)'}" />
							<input type="text" id="txtPrimaObj" class="form-control ">
							<label id="titPrimaObj" for="txtPrimaObj">Prima Objetivo: ${txtTipoMon}</label>
						</div>
					</div>
					<div class="col-sm-12">
						<!--  button type="button" class="btn btn-pink waves-effect waves-light float-right btn-block" id="btnRecalcularPrima"
							name="btnRecalcularPrima">
							<liferay-ui:message key="LiabilityQuotationPortlet.btnRecalcularPrima" />
						</button -->
						<button class="btn btn-blue text-right" id="paso3_calcula" onclick="">
							<liferay-ui:message key="LiabilityQuotationPortlet.btnRecalcularPrima" />
						</button>
					</div>
				</div>
				<div class="row mt-3 ${ perfilSuscriptor != 1 && perfilJapones != 1 ? 'd-none' : '' }">
					<div class="col-sm-12">
						<button type="button" class="btn btn-pink waves-effect waves-light float-right btn-block" id="btnCalculoPrimaSuscriptor"
							name="btnCalculoPrimaSuscriptor">
							<liferay-ui:message key="LiabilityQuotationPortlet.btnRecalcularPrima" />
						</button>
					</div>
				</div>
				<c:if test="${perfilSuscriptor == 1}">
					<div class="row mt-6">
						<div class="col-sm-12">
							<button type="button" class="btn btn-pink waves-effect waves-light float-right" id="btnDesgloseCuotas">
								<liferay-ui:message key="LiabilityQuotationPortlet.btnDesgloseCuotas" />
							</button>
						</div>
					</div>				
				</c:if>
			</div>		
		</div>
	</div>
	
	<div class="row" style="padding: 0 70px;">
		<div class="col-md-3">
			<div class="row mb-2 mt-5">
				<div class="col-md-12">
					
					<c:if test="${ perfilSuscriptor == 1 || perfilJapones == 1}">
<!-- 						<div class="btn btn-blue waves-effect waves-light float-left btn-block mb-2" id="btnComisionesAgente" name="btnComisionesAgente"> -->
<!-- 							Comisiones del Agente -->
<!-- 						</div> -->
					<button class="btn btn-blue float-left btn-block mb-2" id="btnComisionesAgente" onclick="">Comisiones del Agente</button>
					<button class="btn btn-blue float-left" id="btnCoaseguro" onclick="" ${infoCoti.tipoCoaseguro == 2575 ? 'disabled' : '' }>Coaseguro</button>
					<button class="btn btn-blue float-left" id="btnReaseguro" onclick="">Reaseguro</button>
					</c:if>
					
<!-- 					Se comenta a peticï¿½n de RSanchez, esto no estï¿½ definido para Liability -->
<!-- 					<button class="btn btn-blue float-left btn-block mb-2" id="btnDeducibles" onclick="$('#modalDeducibles').modal('show');">Deducibles</button> -->
<!-- 					<button class="btn btn-blue float-left btn-block mb-2" id="btnClausulas" onclick="$('#modalClausulas').modal('show');">Clï¿½usulas Adicionales</button> -->
				</div>
			</div>
		</div>
		<div class="col-md-9" ${ (Leg492 == 'factura') ? 'hidden' : '' }>
			<div class="row mb-2 mt-5">
				<div class="col-md-12 text-right">
<!-- 					<button class="btn btn-blue text-right" id="paso3_especificacion" onclick="">Especificaciï¿½n</button> -->
					<button class="btn btn-blue d-none" id="paso3_noAceptado" onclick="">No aceptado</button>
					<c:if test="${infoCoti.canalNegocio != 2525}">
						<button class="btn btn-blue text-right" id="paso3_Slip" onclick="fGetSlip();">Generar Slip</button>
					</c:if>
					<c:if test="${ (perfilSuscriptorJr == 1 || perfilSuscriptorMrSr == 1) &&  infoCoti.canalNegocio != 2525}">
						<button class="btn btn-blue text-right" id="paso3_Slip_Word" onclick="fGetSlipWord();">Generar Slip (Word)</button>
					</c:if>
					<c:if test="${infoCoti.canalNegocio == 2525}">
						<button class="btn btn-blue text-right" id="paso3_Slip_JI" onclick="">Slip JI</button>
					</c:if>
					<button class="btn btn-blue text-right d-none" id="paso3_carga_slip_word">Cargar Slip</button>
					<button class="btn btn-pink" id="paso3_emitir" onclick="" disabled>EMITIR</button>
					<button class="btn btn-pink waves-effect waves-light btn-ubicacion  d-none float-right" id="btnsuscontinuar" disabled> A SUSCRIPCIï¿½N </button>
				</div>
				<div class="col-md-12 text-right">
					<c:if test="${ perfilSuscriptorJr == 1 || perfilSuscriptorMrSr == 1 }">
						<button class="btn btn-blue" id="paso3_rechazar" onclick="">Rechazar</button>
					</c:if>
					<button class="btn btn-blue d-none" id="paso3_revire" onclick="">Revire</button>
					<button class="btn btn-blue" id="paso3_back" onclick="">Regresar</button>
					<c:if test="${perfilSuscriptor == 1}">
						<button class="btn btn-pink" id="paso3_enviar" onclick="" disabled>Enviar cotización</button>
					</c:if>
				</div>
			</div>
		</div>
		<div class="col-sm-12" ${ (Leg492 == 'factura') ? '' : 'hidden' }>
			<div class=" col-sm-6"></div>
			<div class="form-check form-check-inline col-sm-2">
				<input type="radio" class="form-check-input" id="chkfactauto" name="rdofactura" value="1" checked="checked">
				<label class="form-check-label" for="chkfactauto">Generar la factura automática</label>
			</div>

			<!-- Material inline 2 -->
			<div class="form-check form-check-inline col-sm-2">
				<input type="radio" class="form-check-input" id="chkfactmanual" name="rdofactura" value="2">
				<label class="form-check-label" for="chkfactmanual">Generar la factura manual</label>
			</div>
			<button type="button" class="btn btn-pink waves-effect waves-light float-right" id="btnFacturaSuscrip"
				name="btnFacturaSuscrip" >Emitir</button>
		</div>
		
		<div class="col-sm-12 text-right">
			<button class="btn btn-pink d-none" id="paso3_nextJK" onclick="">Continuar JK</button>
		</div>
	</div>
	
	<div class="row" style="display:none;">
		<input type="hidden" id="txtEmailAgente" value="${caratulaResponse.email}">
		<div class="col-sm-12 text-right">
			<form class="mb-4" action="${cotizadorActionPaso4}" method="post" id="paso3_form">
				<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="${infoCotizacionURL}" />
				<input type='hidden' id="tipoMoneda" name="tipoMoneda" value="${tipoMoneda}" /> 
				<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}" />
				<input type="hidden" id="auxDG" name="auxDG" class="d-none" value='${datosGen}'>
				<input type='hidden' id="saveUbicaciones" name="saveResponse" value='${saveResponse}' />
				<input type='hidden' id="modoCoti3" name="modoCoti" value="${modoCotizacion}" /> 
			</form>
			<form action="${cotizadorActionPaso2}" method="post" id="paso3-form-back">
				<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="" />
				<input type='hidden' id="tipoMoneda" name="tipoMoneda" value="${tipoMoneda}" /> 
				<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}" />
				<input type="hidden" id="auxDG" name="auxDG" class="d-none" value='${datosGen}'>
				<input type="hidden" id="paso" name="paso" class="d-none" value='paso3' />
			</form>
		</div>
		
		<div class="col-sm-12 text-right">
			<form class="mb-4" action="${cotizadorActionPaso3}" method="post" id="paso2_form">
				<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="" />
				<input type='hidden' id="infoRecalcula" name="infoRecalcula" value="${tipoCotizacion}" />
				<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}" />
				<input type='hidden' id="saveUbicaciones" name="saveResponse" value='${saveResponse}' /> 
				<div class="btn btn-pink" id="paso1_next">Continuar</div>
			</form>
		</div>
	</div>
	
	<div id="divPdf" hidden="true">
		<a id="aPdf" hidden="true"></a>
	</div>
	
</section>

<input type="hidden" id="txtJSGetDocsEmision" value="${getDocsEmision}">

<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/objetos.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/funcionesGenericas.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/mainPaso3.js?v=${version}"></script>

<script>
	var infCotiJson = ${infoCotJson};
	var minPrima = ${minPrima};
	var idPerfil = ${idPerfilUser};
	var tipoMoneda = ${tipoMoneda};
	var tpoCambio = ${tpoCambio};
	var perfilSuscriptor = ${perfilSuscriptor};
	var perfilJapones = ${perfilJapones};
	var perfilAgenteEjecutivo = ${perfilAgenteEjecutivo};
	var datosGen = ${datosGen};
	
	var redirigeURL = '${redirigeURL}';
	var sendMailSuscriptorAgenteURL = '${sendMailSuscriptorAgenteURL}';
	var sendMailAgenteSuscriptorURL = '${sendMailAgenteSuscriptorURL}';
	ligasServicios.saveComisionesA = "${saveComisionesA}";
	ligasServicios.saveDeducibles = "${saveDeducibles}";
	ligasServicios.saveClausulasA = "${saveClausulasA}";
	ligasServicios.getSlip = "${getSlipURL}";
	ligasServicios.rechazaCoti = "${rechazarURL}";
	ligasServicios.enviarCoti = "${enviarURL}";
	ligasServicios.continuarJK = "${continuarJkURL}";
	ligasServicios.recalcularCot = "${calcularURL}";
	ligasServicios.getCaratulaURL = "${getCaratulaURL}";
	ligasServicios.getEspecificacion = "${getEspecificacionURL}";
	
	ligasServicios.secionComisionUrl = "${getSeccionComisionUrl}";
	
	var getEmisionArt492Url = '${getEmisionArt492Url}';
	
	var guardaComisionesAgenteURL = '${guardaComisionesAgenteURL}';
	var comisionesAgenteURL = '${getComisionesAgenteURL}';
	
	var cargaSlipWordURL = '${cargaSlipWordURL}';
	
	var generaInfoCoaseguroURL = '${generaInfoCoaseguroURL}';
	
	var primaObjetivoSuscriptorURL = '${primaObjetivoSuscriptorURL}';
	var validaPrimaURL = '${validaPrimaURL}';
	var guardaPrimaURL = '${guardaPrimaURL}';
	var getCuotaFinalURL = '${getCuotaFinalURL}';
	
	var getPermisoEmisionURL = '${getPermisoEmisionURL}';
	
	var infoCotiResponse = '${saveResponse}';
	
	var generaInfoReaseguroURL = '${generaInfoReaseguroURL}';
	
	var generaInfoDesgloseCuotasURL = '${generaInfoDesgloseCuotasURL}';
	
	var validaAgenteURL = '${validaAgenteURL}';
	
	var suscripcion = ${suscripcion};
	
	var banderaEditar = false;
	
	var boton = 0;
</script>