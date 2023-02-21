<%@ include file="../init.jsp"%>
<jsp:include page="modalesP2.jsp" />

<portlet:resourceURL id="/RC/SaveUbiRC" var="saveUbiRC" cacheability="FULL"/>
<portlet:resourceURL id="/RC/backP1" var="backP1" cacheability="FULL"/>
<portlet:resourceURL id="/cotizadores/paso2/getCP" var="getCpURL" cacheability="FULL"/>
<portlet:resourceURL id="/paso2/sendMailAgenteSuscriptor" var="sendMailAgenteSuscriptorURL" cacheability="FULL" />

<portlet:resourceURL id="/cotizadores/paso2/cargaMasiva" var="cargaMasiva" cacheability="FULL" />
<portlet:resourceURL id="/RC/SaveUbiCMRC" var="saveUbiCMRC" cacheability="FULL"/>

<portlet:actionURL var="cotizadorActionPaso1" name="/liability/actionPaso1" />
<portlet:actionURL var="cotizadorActionPaso2" name="/liability/actionPaso2" />
<portlet:actionURL var="cotizadorActionPaso3" name="/liability/actionPaso3" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=${version}">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css?v=${version}">

<fmt:setLocale value="es_MX" />

<section id="cotizadores-p2" class="upper-case-all">

	<div class="section-heading">
		<div class="container-fluid">
			<h4 class="title text-left">Cotizador Responsabilidad Civil</h4>
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
					<li id="step2" class="active">
						<a href="javascript:void(0)">
							<span class="circle">2</span>
							<span class="label">
								<liferay-ui:message key="LiabilityQuotationPortlet.titPasoDos" />
							</span>
						</a>
					</li>
					<li id="step3">
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
		<liferay-ui:success key="consultaExitosa" message="LiabilityQuotationPortlet.exito" />
		<liferay-ui:error key="errorConocido" message="${errorMsg}" />
		<liferay-ui:error key="errorDesconocido" message="LiabilityQuotationPortlet.erorDesconocido" />
	</div>

	<div class="padding70" id="contenPaso2">


		<div class="container-fluid paso2Datos">
			<div class="row">
				<div class="col-md-9">
					<c:choose>
						<c:when test="${tipoCotizacion == 1}">
							<h4 class="title text-left">Industria y Comercio</h4>
						</c:when>
						
						<c:when test="${tipoCotizacion == 2}">
							<h4 class="title text-left">Construcción</h4>
						</c:when>
					
						<c:when test="${tipoCotizacion == 3}">
							<h4 class="title text-left">Hotelería</h4>
						</c:when>
						
						<c:otherwise>
							
						</c:otherwise>
					</c:choose>
				</div>
				<div class="col-md-3" style="text-align: right;">
					<div class="md-form form-group">
						<input id="txtFolioP2_2" type="text" name="txtFolioP2_2" class="form-control" disabled value="${infCotizacion.folio} - ${infCotizacion.version}">
						<label for="txtFolioP2_2">
							<liferay-ui:message key="LiabilityQuotationPortlet.titFolio" />
						</label>
					</div>
				</div>
			</div>
			<div id="chkBjaEnd" class="row d-none">
				<div class="col-md-12">
					<div class="text-center">
						<h2>
							Seleccione las ubicaciones que quiere dar de baja
						</h2>
						<br>
						<c:forEach var="j" begin="1" end="${ infCotizacion.noUbicaciones }">
							<div class="form-check form-check-inline">
							  <input type="checkbox" class="form-check-input"
							   id="chk-${ j }" ${ j == 1 ? 'disabled' : '' } >
							  <label class="form-check-label" for="chk-${ j }">${relacionUbicaciones[j]}</label>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2">
					<div class="md-form">
						<input type="text" id="dac_noUbica" name="dac_noUbica" class="form-control" value="${infCotizacion.noUbicaciones}" valAnt="${infCotizacion.noUbicaciones}">
						<label for="dac_noUbica">
							<liferay-ui:message key="LiabilityQuotationPortlet.dac.noUbicaciones" />
						</label>
					</div>
				</div>
				<div class="col-md-10">
					<button id="btn-add-tab-endosos" type="button" class="btn btn-blue btn-sm waves-effect waves-light float-right d-none">Agregar Ubicación</button>
					<button id="btn-carga-masiva" type="button" class="btn btn-pink btn-sm waves-effect waves-light float-right d-none">Carga Masiva</button>
				</div>
				<div class="col-md-12">
					<div id="tabs">

						<ul>
							<c:forEach var="i" begin="1" end="${ infCotizacion.noUbicaciones }">
								<li id="tab-${i}">
									<a href="#ubicacion-${i}">
										<i class="fa fa-map-marker" aria-hidden="true"></i>
<%-- 										<span class="numUbicacionEndo">${relacionUbicaciones[i]}</span> --%>
										<span class="numUbicacionEndo">${ i }</span>
									</a>
									<button id="btnCls${i}" type="button" class="close my-1 mr-2" aria-label="Close">
										<span aria-hidden="true">×</span>
									</button>
								</li>
							</c:forEach>
						</ul>

						<c:forEach var="i" begin="1" end="${ infCotizacion.noUbicaciones }">
							<div id="ubicacion-${i}" style="padding: 50px;" class="ubicacion">
								<div class="row">
									<div class="col-md-12">
										<c:set var="count" value="${ i - 1 }" scope="request"></c:set>
										
										<jsp:include page="tab_liability.jsp" />
											
										
										<div class="rowAcordeon">
											<div class="col-md-12 acordeon d-none" id="acordeon${ i }">
												${jsonFiels[i]}
											</div>
										</div>

									</div>
								</div>
							</div>
						</c:forEach>

					</div>
				</div>
			</div>


			<div class="row mt-4">
				<div  class="col-md-8">
					<div id="infoPrimas" class="row d-none">
						<table class="table tablaTit" id="tabla_Tittotal">
							<thead class="blue-grey lighten-4">
								<tr class="triTabTot">
									<th>
										<liferay-ui:message key="LiabilityQuotationPortlet.lblUbicacionP2" />
									</th>
								</tr>
							</thead>
							<tbody>
								<tr class="trsTabParcial">
									<td scope="row">
										<liferay-ui:message key="LiabilityQuotationPortlet.lblPrimaNetaP2" />
									</td>
								</tr>
							</tbody>
						</table>
						<div class="table-wrapper-scroll-table">
							<table class="table tablaTotal" id="tabla_total" >
								<thead class="blue-grey lighten-4">
									<tr class="triTabTot">

										<c:forEach items="${ ubicacion.ubicaciones }" var="u">
											<th>${ u.idubicacion }</th>
										</c:forEach>
									</tr>
								</thead>
								<tbody>
									<tr class="trsTabParcial">
										<c:forEach items="${ ubicacion.ubicaciones }" var="u">
											<td><fmt:formatNumber value="${u.primaNeta}" type="currency"  /> </td>
										</c:forEach>
									</tr>
								</tbody>
							</table>
						</div>
						<table class="table col-md-4 tablaTotal2" id="tabla_total_2">
							<thead class="blue-grey lighten-4">
								<tr>
									<th id="tit_table">
										<liferay-ui:message key="LiabilityQuotationPortlet.lblPrimaTotalP2" />
									</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td id="tol_table" class="totPrimaTab"> 
										<fmt:formatNumber value="${ubicacion.primaTotal}" type="currency" />
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>




				<div class="col-md-4">
					<div class="row">
						<div class="col-md-12">
<!-- 							<a class="btn btn-blue waves-effect waves-light float-right btn-ubicacion btn_modal" id="save_tot2"> -->
							<a class="btn btn-blue waves-effect waves-light float-right btn-ubicacion" id="nextUbicacion">Guardar ubicación</a>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<button class="btn btn-blue waves-effect waves-light float-right btn-ubicacion" id="saveUbicacion" disabled="true">continuar</button>
							<button class="btn btn-pink waves-effect waves-light btn-ubicacion  d-none float-right" id="btnsuscontinuar"> A SUSCRIPCIÓN </button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<!-- 							<a class="btn btn-pink waves-effect waves-light btn-ubicacion d-none" id="paso2_suscripcion_next" > -->
							<%-- 								<liferay-ui:message key="LiabilityQuotationPortlet.btnContinuarP1" /> --%>
							<!-- 							</a> -->
							<a class="btn btn-pink waves-effect waves-light btn-ubicacion float-right d-none" id="paso2_next">
								<liferay-ui:message key="LiabilityQuotationPortlet.btnContinuarP3" />
							</a>
							<a class="btn btn-blue waves-effect waves-light btn-ubicacion float-right" data-toggle="modal" href="#modalRegresarP1" id="paso1_back2">
								<liferay-ui:message key="LiabilityQuotationPortlet.btnRegresarP2" />
							</a>
							<a class="btn btn-blue waves-effect waves-light btn-ubicacion float-right d-none" id="regresarEndoso" onclick="regresaPaso1();">
								<liferay-ui:message key="LiabilityQuotationPortlet.btnRegresarP2" />
							</a>

							<a class="btn btn-pink waves-effect waves-light btn-ubicacion d-none float-right" id="con_baja">
								<liferay-ui:message key="LiabilityQuotationPortlet.btnContinuarP3" />
							</a>
							<a class="btn btn-pink waves-effect waves-light btn-ubicacion  d-none float-right" id="btnsuscontinuar"> A SUSCRIPCIÓN </a>
						</div>
					</div>
				</div>
			</div>
					
			
		</div>
	</div>
	
	<div class="row" style="display:none;">
		<div class="col-sm-12 text-right">
			<form class="mb-4" action="${cotizadorActionPaso3}" method="post" id="paso2_form">
				<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="" />
				<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}" /> 
				<input type='hidden' id="saveUbicaciones" name="saveUbicaciones" value="" /> 
				<div class="btn btn-pink" id="paso1_next">Continuar</div>
			</form>
		</div>
		<div class="col-sm-12 text-right">
			<form class="mb-4" action="${cotizadorActionPaso1}" method="post" id="back1_form">
				<input type='hidden' id="infoCotiP1" name="infoCotiP1" value="" />
				<input type='hidden' id="generalesCotizacion" name="infoCotizacion" value="" />
				<div class="btn btn-pink" id="paso2_back">Continuar</div>
			</form>
		</div>
		
		
		<div class="col-sm-12 text-right">
			<form class="mb-4" action="${cotizadorActionPaso2}" method="post" id="saveUbicaciones_form">
<!-- 				<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="" />  -->
				<input type='hidden' id="modoCoti" name="modoCoti" value="" /> 
				<input type='hidden' id="canalNegocio" name="canalNegocio" value="" /> 
				<input type='hidden' id="subGiroRiesgo" name="subGiroRiesgo" value="${subGiroRiesgo}" /> 
				<input type='hidden' id="tipoMoneda" name="tipoMoneda" value="${tipoMoneda}" />
				<input type='hidden' id="curUbicacion" name="curUbicacion" value="${curUbicacion}"/> 
				<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}"/> 
				<input type="hidden" id="saveResponse" name="saveResponse" class="d-none" value="">
				<input type="hidden" id="auxDG" name="auxDG" class="d-none" value='${datosGen}'>
				<input type='hidden' id="tipoCoaseguro" name="tipoCoaseguro" value="${infCotizacion.tipoCoaseguro}" />
				<div class="btn btn-pink" id="paso1_next">Continuar</div>
			</form>
		</div>
		<div class="col-sm-12 text-right">
			<form class="mb-4" action="${cotizadorActionPaso3}" method="post" id="goToPaso3_form">
<%-- 				<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="${infoCoti}" />  --%>
				<input type='hidden' id="modoCoti3" name="modoCoti" value="" /> 
				<input type='hidden' id="tipoMoneda" name="tipoMoneda" value="${tipoMoneda}" /> 
				<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}"/>
				<input type='hidden' id="canalNegocio" name="canalNegocio" value="${infCotizacion.canalNegocio}" />
				<input type='hidden' id="coaseguro" name="coaseguro" value="${infCotizacion.tipoCoaseguro}" /> 
				<input type="hidden" id="responseToP3" name="saveResponse" class="d-none" value="" />
				<input type="hidden" id="auxDG" name="auxDG" class="d-none" value='${datosGen}' />
				<input type="hidden" id="suscripcion" name="suscripcion" class="d-none" value="" />
				<div class="btn btn-pink" id="paso1_next">Continuar</div>
			</form>
		</div>
			
	</div>
	
</section>

<!-- Modal Numero Ubicaciones -->
<div class="modal" id="modalConfirmaNumUbica" tabindex="-1" role="dialog" aria-labelledby="clienteExistenttLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header blue">
				<h5 class="modal-title text-white" id="clienteExistenttLabel">CONFIRMAR</h5>
<!-- 				<button type="button" class="close" data-dismiss="modal" aria-label="Close"> -->
<!-- 					<span aria-hidden="true">&times;</span> -->
<!-- 				</button> -->
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12 text-center">
						<h4>¿Desea cambiar el número de ubicaciones?</h4>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnSiNumUbica">Si</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" id="btnNoNumUbica">No</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal Numero Ubicaciones -->



<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/visibilidad.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/objetos.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/funcionesGenericas.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/mainPaso2.js?v=${version}"></script>

<script>
	var infCotizacion = ${infoCoti};
	var curPestania = ${curUbicacion};
	var perfilSuscriptorJr = ${perfilSuscriptorJr};
	var perfilSuscriptorMrSr = ${perfilSuscriptorMrSr};
	var perfilSuscriptorMr = ${perfilSuscriptorMr};
	var subGiroRiesgo = ${subGiroRiesgo};
	var sendMailAgenteSuscriptorURL = '${sendMailAgenteSuscriptorURL}';
	
	ligasServicios.getCpURL = "${getCpURL}";
	ligasServicios.saveUbiRC = "${saveUbiRC}";
	ligasServicios.backP1 = "${backP1}";
	ligasServicios.cargaMasiva = "${cargaMasiva}";
	ligasServicios.saveUbiCMRC = "${saveUbiCMRC}";
	
	var cargaMasivaString = "";
	var infoCargaMasiva = ${infoCargaMasiva};
	var erroresCargaMasiva = 0;
</script>