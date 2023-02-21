<%@ include file="../init.jsp"%>

<portlet:resourceURL id="/liability/listaPersonas" var="listaPersonasURL" cacheability="FULL"/>
<portlet:resourceURL id="/cotizadores/paso1/getSubGiro" var="getSubGiroURL" cacheability="FULL"/>
<portlet:resourceURL id="/CotizadorRC/GuardaPaso1" var="guardaPaso1URL" cacheability="FULL"/>
<portlet:resourceURL id="/cotizadores/paso1/canalNegocio" var="canalNegocio" cacheability="FULL"/>
<portlet:resourceURL id="/cotizadores/paso1/gradoRiesgo" var="gradoRiesgo" cacheability="FULL"/>
<portlet:resourceURL id="/CotizadorRC/getGiroRC" var="getGiroRC" cacheability="FULL"/>
<portlet:resourceURL id="/cotizadores/validaAgente" var="validaAgenteURL" cacheability="FULL" />

<portlet:actionURL var="cotizadorActionPaso2" name="/liability/actionPaso2"/>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css?v=${version}">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=${version}">

<section id="cotizadores-p1" class="upper-case-all">

	<div class="section-heading">
		<div class="container-fluid">
			<h4 class="title text-left">Cotizador Responsabilidad Civil</h4>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<ul class="stepper stepper-horizontal container-fluid">
					<li id="step1" class="active ">
						<a href="javascript:void(0)">
							<span class="circle">1</span>
							<span class="label">
								Datos cotización
							</span>
						</a>
					</li>
					<li id="step2">
						<a href="javascript:void(0)">
							<span class="circle">2</span>
							<span class="label">
								Datos de riesgo y modalidad
							</span>
						</a>
					</li>
					<li id="step3">
						<a href="javascript:void(0)">
							<span class="circle">3</span>
							<span class="label">
								Propuesta de Cotización
							</span>
						</a>
					</li>
					<li id="step4">
						<a href="javascript:void(0)">
							<span class="circle">4</span>
							<span class="label">
								Emisión
							</span>
						</a>
					</li>
				</ul>

			</div>
		</div>
	</div>


	<div class="container-fluid" id="divPaso1">
		<div class="row">
			<div class="col-md-6"></div>
			<c:if test="${numeroSolicitud}">
				<div class="col-md-3" style="text-align: right;">
					<div class="md-form form-group">
						<input id="txtSolicitud" type="text" name="txtSolicitud" class="form-control" value="${ infCot.solicitud }" disabled>
						<label class="active" for="txtSolicitud">
							<liferay-ui:message key="LiabilityQuotationPortlet.titSolicitud" />
						</label>
					</div>
				</div>
			</c:if>
			<div class="col-md-3 divFolio" style="text-align: right;">
				<div class="md-form form-group">
					<input id="txtFolioP1" type="text" name="txtFolioP1" class="form-control" value="${ infoCotiP1.folio } - ${ infoCotiP1.version }" disabled>
					<label class="active" for="txtFolioP1">
						<liferay-ui:message key="LiabilityQuotationPortlet.titFolio" />
					</label>
				</div>
			</div>
		</div>
	</div>

	<div class="padding70" id="contPaso1">

		<div class="col-md-12">
			<!--Accordion wrapper-->
			<div class="accordion md-accordion" id="accordionEx" role="tablist" aria-multiselectable="true">


				<!-- Accordion card -->
				<div class="card ">


					<!-- Card header -->
					<div class="card-header btn-blue modificado" role="tab" id="headingDatosContratante">
						<a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseDatosContratante" aria-expanded="false" aria-controls="collapseDatosContratante">
							<h5 class="mb-0">
								Datos del Contratante
								<i class="fas fa-angle-down rotate-icon"></i>
							</h5>
						</a>
					</div>

					<div id="collapseDatosContratante" class="collapse in" role="tabpanel" aria-labelledby="headingDatosContratante" data-parent="#accordionEx">
						<div class="card-body">

							<div class="row">
								<div class="col-md-12">
									<div class="form-inline divRdoTpClient">
										<div class="form-check">
											<input class="form-check-input form-control" name="group1" type="radio" id="radio_ce" value="0" checked="checked">
											<label class="form-check-label" for="radio_ce">
												Cliente Existente
											</label>
										</div>
										<div class="form-check">
											<input class="form-check-input form-control" name="group1" type="radio" id="radio_cn" value="1">
											<label class="form-check-label" for="radio_cn">
												Cliente Nuevo
											</label>
										</div>
									</div>
								</div>
							</div>

							<div class="row data_cteext">
								<div class="col-md-3">
									<div class="md-form">
										<input type="text" name="ce_rfc" id="ce_rfc" class="form-control " maxlength="13" pattern="^[a-zA-Z0-9]{4,10}$" value="${ infoCotiP1.datosCliente.rfc }">
										<label for="ce_rfc">RFC del contratante</label>
									</div>
								</div>
								<div class="col-md-6">
									<div class="md-form">
										<input type="text" name="ce_nombre" id="ce_nombre" class="form-control "
											value="${ infoCotiP1.datosCliente.nombre } ${ infoCotiP1.datosCliente.appPaterno} ${ infoCotiP1.datosCliente.appMaterno }">
										<label for="ce_nombre">
											<liferay-ui:message key="LiabilityQuotationPortlet.lblNomComExP1" />
										</label>
									</div>
								</div>
								<div class="col-md-3">
									<div class="md-form">
										<input type="text" name="ce_codigo" id="ce_codigo" class="form-control" value="${ infoCotiP1.datosCliente.codigo }" disabled>
										<label for="ce_codigo">
											<liferay-ui:message key="LiabilityQuotationPortlet.lblCodClieExP1" />
										</label>
									</div>
								</div>
							</div>


							<div class="row data_ctenvo d-none">
								<div class="col-sm-12">
									<div class="row data_nuevotip">
										<div class="col-md-8 cn_ncEx">
											<div class="md-form form-group">
												<input type="text" id="cn_nombrecompleto" name="cn_nombrecompleto" class="form-control" disabled>
												<label for="cn_nombrecompleto">
													<liferay-ui:message key="LiabilityQuotationPortlet.lblNomComExP1" />
												</label>
											</div>
										</div>
										<div class="col-md-4 cn_tpEx">
											<div class="form-inline tipo_persona">
												<div class="form-check">
													<input class="form-check-input form-control" name="group2" type="radio" id="cn_personamoral" checked="checked" value="2">
													<label class="form-check-label" for="cn_personamoral">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblTipPerNvMorP1" />
													</label>
												</div>
												<div class="form-check">
													<input class="form-check-input form-control" name="group2" type="radio" id="cn_personafisica" value="1">
													<label class="form-check-label" for="cn_personafisica">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblTipPerNvFisP1" />
													</label>
												</div>
											</div>
										</div>

										<div class="col-md-3 cn_rdEx d-none">
											<div class="row row justify-content-md-center">
												<label class="pb-2"> Extranjera:</label>
											</div>
											<div class="row row justify-content-md-center">
												<div class="switch">
													<label>
														No
														<input id="chktoggle" type="checkbox">
														<span class="lever"></span>
														Si
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-3">
											<div class="md-form">
												<input type="text" id="cn_rfc" name="cn_rfc " class="form-control " maxlength="13" pattern="^[a-zA-Z0-9]{4,10}$">
												<label for="cn_rfc">
													<liferay-ui:message key="LiabilityQuotationPortlet.lblRfcExP1" />
												</label>
											</div>
										</div>

										<div class="col-md-9 px-0 tip_moral divPerMor">
											<div class="col-md-6">
												<div class="md-form">
													<input type="text" id="cn_nombrecontratante" name="cn_nombrecontratante" class="form-control">
													<label for="cn_nombrecontratante">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblNomConNvMoP1" />
													</label>
												</div>
											</div>
											<div class="col-md-6">
												<div class="md-form form-group">
													<select name="cn_denominacion" id="cn_denominacion" class="mdb-select form-control-sel colorful-select dropdown-primary"
														searchable='<liferay-ui:message key="LiabilityQuotationPortlet.buscar" />'>
														<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
														<c:forEach items="${listaCatDenominacion}" var="option">
															<option value="${option.idCatalogoDetalle}">${option.valor}</option>
														</c:forEach>
													</select>
													<label for="cn_denominacion">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblDenominaNvMoP1" />
													</label>
												</div>
											</div>
										</div>



										<div class="col-md-9 px-0 tip_fisica" style="display: none">

											<div class="col-md-4">
												<div class="md-form">
													<input type="text" id="cn_fisnombre" name="cn_fisnombre" class="form-control">
													<label for="cn_fisnombre">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblNomFisicaP1" />
													</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="md-form">
													<input type="text" id="cn_fispaterno" name="cn_fispaterno" class="form-control ">
													<label for="cn_fispaterno">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblApPatFisicaP1" />
													</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="md-form">
													<input type="text" id="cn_fismaterno" name="cn_fismaterno" class="form-control ">
													<label for="cn_fismaterno">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblApMatFisicaP1" />
													</label>
												</div>
											</div>
										</div>
									</div>


								</div>
							</div>

							<div class="data_cotizacion">
								<br />
								<h5>
									<liferay-ui:message key="LiabilityQuotationPortlet.titDatosCotizacion" />
								</h5>
								<br />
								<div class="row">
									<div class="col-md-12">
										<div class="form-inline form-left float-left divRdoTipoCotiza">
											<div class="form-check">
												<input class="form-check-input form-control" name="group3" type="radio" id="dc_comerIndus" ${ cotizadorData.tipoComercio == 1 ? 'checked' : '' } value="1">
												<label class="form-check-label" for="dc_comerIndus">
													<liferay-ui:message key="LiabilityQuotationPortlet.rdbComercio" />
												</label>
											</div>
											<div class="form-check">
												<input class="form-check-input form-control" name="group3" type="radio" id="dc_contruccion" ${ cotizadorData.tipoComercio == 2 ? 'checked' : '' } value="2">
												<label class="form-check-label" for="dc_contruccion">
													<liferay-ui:message key="LiabilityQuotationPortlet.rdbConstruccion" />
												</label>
											</div>
											<div class="form-check">
												<input class="form-check-input form-control" name="group3" type="radio" id="dc_hoteleria" ${ cotizadorData.tipoComercio == 3 ? 'checked' : '' } value="3">
												<label class="form-check-label" for="dc_hoteleria">
													<liferay-ui:message key="LiabilityQuotationPortlet.rdbHoteleria" />
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-sm-3">
										<div class="md-form form-group">
											<select name="dc_movimientos" id="dc_movimientos" class="mdb-select form-control-sel colorful-select dropdown-primary" disabled>
												<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
												<c:forEach items="${listaMovimiento}" var="option">
													<option value="${option.idCatalogoDetalle}" ${ 207 ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
												</c:forEach>
											</select>
											<label for="dc_movimientos">
												<liferay-ui:message key="LiabilityQuotationPortlet.lblTipoMovimientoDtsCotizaP1" />
											</label>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="md-form form-group">
											<select name="dc_moneda" id="dc_moneda" class="mdb-select form-control-sel">
												<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
												<c:forEach items="${listaCatMoneda}" var="option">
													<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.moneda ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
												</c:forEach>
											</select>
											<label for="dc_moneda">
												<liferay-ui:message key="LiabilityQuotationPortlet.lblMonedaDtsCotizaP1" />
											</label>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="md-form form-group">
											<div class="row">
												<div class="col">
													<input placeholder="Fecha Desde" type="date" id="dc_dateDesde" name="dc_dateDesde" class="form-control datepicker " value="${ fechaHoy }">
													<label for="dc_dateDesde">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblDesdeDtsCotizaP1" />
													</label>
												</div>
												<div class="col">
<%-- 													<input placeholder="Fecha Hasta" type="date" id="dc_dateHasta" name="dc_dateHasta" class="form-control datepicker" value="${ fechaMasAnio }" ${auxDisabledPerfil}> --%>
													<input placeholder="Fecha Hasta" type="date" id="dc_dateHasta" name="dc_dateHasta" class="form-control datepicker" value="${ fechaMasAnio }">
													<label for="dc_dateHasta">
														<liferay-ui:message key="LiabilityQuotationPortlet.lblHastaDtsCotizaP1" />
													</label>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-sm-3">
										<div class="md-form form-group">
											<select name="dc_agentes" id="dc_agentes" class="mdb-select form-control-sel colorful-select dropdown-primary" searchable='<liferay-ui:message key="LiabilityQuotationPortlet.buscar" />'>
												<c:if test="${fn:length(listaAgentes) gt 0}">
													<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
												</c:if>
												<c:forEach items="${listaAgentes}" var="option">
													<option value="${option.idPersona}" ${ infoCotiP1.agente ==  option.idPersona ? 'selected' : ''}>${option.nombre}${option.appPaterno}${option.appMaterno}</option>
												</c:forEach>
											</select>
											<label for="dc_agentes"><liferay-ui:message key="LiabilityQuotationPortlet.lblAgentesDtsCotizaP1" /></label>
										</div>
									</div>

									<div class="col-sm-3">
										<div class="md-form form-group">
											<select name="dc_formpago" id="dc_formpago" class="mdb-select form-control-sel ">
												<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
												<c:forEach items="${listaCatFormaPago}" var="option">
													<c:if test="${flagPagoSusJ}">
														<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.formaPago ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
													</c:if>
													<c:if test="${!flagPagoSusJ && option.codigo != 'S'}">
														<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.formaPago ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
													</c:if>
												</c:forEach>
											</select>
											<label for="dc_formpago">
												<liferay-ui:message key="LiabilityQuotationPortlet.lblPagoDtsCotizaP1" />
											</label>
										</div>
									</div>
									<div class="col-6 px-0 empresarial_giros">
										<div class="col-sm-6">
											<div class="md-form form-group">
<!-- 												<select name="dc_giro" id="dc_giro" class="mdb-select form-control-sel "> -->
												<select name="dc_giro" id="dc_giro" class="mdb-select form-control-sel" searchable='<liferay-ui:message key="LiabilityQuotationPortlet.buscar" />'>
													<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
													<c:forEach items="${listaGiros}" var="option">
														<c:if test="${option.codigo != '2' }">
															<c:if test="${ option.codigo == '1'}">
																<c:set var = "auxDisabled" value = "disabled"/>
															</c:if>
															<c:if test="${ perfilSuscriptor == 1 || perfilJapones == 1}">
																<c:set var = "auxDisabled" value = ""/>
															</c:if>
															<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.giro ==  option.idCatalogoDetalle ? 'selected' : ''} ${auxDisabled}>${option.valor}</option>
															
															<c:set var = "auxDisabled" value = ""/>
														</c:if>
													</c:forEach>
												</select>
												<label for="dc_giro">
													<liferay-ui:message key="LiabilityQuotationPortlet.lblGiroDtsCotizaP1" />
												</label>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="md-form form-group">

<!-- 												<select name="dc_subgiro" id="dc_subgiro" class="mdb-select form-control-sel" disabled> -->
												<select name="dc_subgiro" id="dc_subgiro" class="mdb-select form-control-sel" disabled searchable='<liferay-ui:message key="LiabilityQuotationPortlet.buscar" />'>
													<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
													<c:forEach items="${listaSubGiro}" var="option">
														<%-- Modificacion subgiros de riesgo agentes --%>
														<%--														
															<c:if test="${ option.otro == 1}">
																<c:set var = "auxDisabled" value = "disabled"/>
															</c:if>
															<c:if test="${ perfilSuscriptor == 1 || perfilJapones == 1}">
																<c:set var = "auxDisabled" value = ""/>
															</c:if>
														--%>
															<option codigo="${option.codigo}" suscripcion="${option.otro}" value="${option.idCatalogoDetalle}" ${ infoCotiP1.subGiro ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
													</c:forEach>
												</select>
												<label for="dc_subgiro">
													<liferay-ui:message key="LiabilityQuotationPortlet.lblSubGiroDtsCotizaP1" />
												</label>
											</div>
										</div>
									</div>
									<div class="col-sm-3 ${idPerfilUser == 4 || idPerfilUser == 5 || idPerfilUser == 6 ? '': 'd-none'}">
										<div class="md-form form-group">
											<select name="dc_sector" id="dc_sector" class="mdb-select form-control-sel ">
												<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
												<c:forEach items="${listaCatSector}" var="option">
													<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.sector ==  option.idCatalogoDetalle ? 'selected' : ''} 
																								${ infoCotiP1==null && option.valor=='PRIVADO' ? 'selected' : ''} >${option.valor}</option>
												</c:forEach>
											</select>
											<label for="dc_sector">
												<liferay-ui:message key="LiabilityQuotationPortlet.lblSectorDtsCotizaP1" />
											</label>
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>
				</div>


				<!-- Accordion card -->
				<div class="card ${dNonePerfil}">


					<!-- Card header -->
					<div class="card-header btn-blue modificado" role="tab" id="headingDatosAdicionales">
						<a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseDatosAdicionales" aria-expanded="false" aria-controls="collapseDatosAdicionales">
							<h5 class="mb-0">
								<liferay-ui:message key="LiabilityQuotationPortlet.dac.titulo" />
								<i class="fas fa-angle-down rotate-icon"></i>
							</h5>
						</a>
					</div>

					<div id="collapseDatosAdicionales" class="collapse in" role="tabpanel" aria-labelledby="headingDatosAdicionales" data-parent="#accordionEx">
						<div class="card-body">
							<div class="row">
								<div class="col-md-12">
									<div class="row">
										<div class="col-md-3">
											<div class="md-form">
												<input type="text" id="dac_tpoCambio" name="dac_tpoCambio" class="form-control" value="${tpoCambio}" readonly="readonly">
												<label for="dac_tpoCambio">
													<liferay-ui:message key="LiabilityQuotationPortlet.dac.tipCamb" />
												</label>
											</div>
										</div>
										<div class="col-md-3">
											<div class="md-form form-group">
												<select name="dac_cnlNegocio" id="dac_cnlNegocio" class="mdb-select form-control-sel" disabled>
													<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
													<c:forEach items="${listaCatCanalNegocio}" var="option">
														<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.canalNegocio ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
													</c:forEach>
												</select>
												<label for="dac_cnlNegocio">
													<liferay-ui:message key="LiabilityQuotationPortlet.dac.cnlNegocio" />
												</label>
											</div>
										</div>
										<div class="col-md-3">
											<div class="md-form form-group ${dNonePerfil}">
												<select name="dac_coaseguro" id="dac_coaseguro" class="mdb-select form-control-sel">
													<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
													<c:forEach items="${listaCoaseguro}" var="option">
<%-- 														<option value="${option.idCatalogoDetalle}" ${ 2575 ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option> --%>
														<c:if test="${option.codigo != '4'}">
															<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.tipoCao ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
														</c:if>
													</c:forEach>
												</select>
												<label for="dac_coaseguro">
													<liferay-ui:message key="LiabilityQuotationPortlet.dac.coaseguro" />
												</label>
											</div>
										</div>
										<div class="col-md-3">
											<div class="md-form form-group">
<!-- 												<select name="dac_grdoRiesRC" id="dac_grdoRiesRC" class="mdb-select form-control-sel"> -->
<%-- 													<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option> --%>
<%-- 													<c:forEach items="${listaCatGrdoRiesRC}" var="option"> --%>
<%-- 														<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.gradoRiesgo ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option> --%>
<%-- 													</c:forEach> --%>
<!-- 												</select> -->
												<input type="text" id="dac_grdoRiesRC" name="dac_grdoRiesRC" class="form-control numerPosi" value="${infoCotiP1.gradoRiesgo}" readonly>
												<label for="dac_grdoRiesRC">
													<liferay-ui:message key="LiabilityQuotationPortlet.dac.grdoRiesgoRC" />
												</label>
											</div>
										</div>
									</div>
									
									<div class="row">
										
										<div class="col-md-3">
											<div class="md-form form-group">
												<select name="dac_esqAsegura" id="dac_esqAsegura" class="mdb-select form-control-sel">
													<option value="-1" selected disabled><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
													<c:forEach items="${listaCatEsquemaRC}" var="option">
<%-- 														<option value="${option.idCatalogoDetalle}" ${ P1_1.p_esquemaRC ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option> --%>
														<option value="${option.idCatalogoDetalle}" ${ infoCotiP1.esquemaAseguramiento ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.valor}</option>
													</c:forEach>
												</select>
												<label for="dac_esqAsegura">
													<liferay-ui:message key="LiabilityQuotationPortlet.dac.esqAseguramiento" />
												</label>
											</div>
										</div>
										
										<div id ="divFechaConven" class="col-sm-3" >
											<div class="md-form form-group">
												<div class="row">
													<div class="col">
														<input placeholder="Fecha" type="date" id="dc_dateConven" name="dc_dateConven" class="form-control datepicker-conven" value="${ fechaConvencional }">
														<label for="dc_dateConven">
															<liferay-ui:message key="LiabilityQuotationPortlet.dac.fechaConven" />
														</label>
													</div>
												</div>
											</div>
										</div>
									</div>
									
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>

			<!-- hata aqui los acordeones -->


			<div class="row">
				<div class="col-sm-12 text-right">
					<form class="mb-4" action="${cotizadorActionPaso2}" method="post" id="paso1_form">
						<input type='hidden' id="canalNegocio" name="canalNegocio" value="" />
						<input type='hidden' id="tipoCoaseguro" name="tipoCoaseguro" value="" />
						<input type='hidden' id="subGiroRiesgo" name="subGiroRiesgo" value="" /> 
						<input type='hidden' id="tipoMoneda" name="tipoMoneda" value="" /> 
						<input type='hidden' id="infoCotizacion" name="infoCotizacion" value="" /> 
						<input type='hidden' id="tipoCoti" name="tipoCoti" value="1"/> 
						<input type="hidden" id="folioCoti" name="folioCoti" class="d-none" value="">
						<input type="hidden" id="versionCoti" name="versionCoti" class="d-none" value="">
						<input type="hidden" id="saveResponse" name="saveResponse" class="d-none" value="">
						<input type="hidden" id="auxDG" name="auxDG" class="d-none" value="">
						<div class="btn btn-pink" id="paso1_next">Continuar</div>
					</form>
					<form class="mb-4" action="${cotizadorActionPaso3}" method="post" id="goToPaso3_form">
						<input type='hidden' id="modoCoti3" name="modoCoti" value="" /> 
						<input type='hidden' id="tipoMoneda" name="tipoMoneda" value="${tipoMoneda}" /> 
						<input type='hidden' id="tipoCoti" name="tipoCoti" value="${tipoCotizacion}"/> 
						<input type="hidden" id="responseToP3" name="saveResponse" class="d-none" value="">
						<input type="hidden" id="auxDG" name="auxDG" class="d-none" value='${datosGen}'>
					</form>
				</div>
			</div>
		</div>




	</div>
</section>

<!-- Modal usuario existente -->
<div class="modal" id="modalClienteExistente" tabindex="-1" role="dialog" aria-labelledby="clienteExistenttLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title" id="clienteExistenttLabel">
					<liferay-ui:message key="LiabilityQuotationPortlet.titModClientExistt" />
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<h4 class="font-weight-bold">
							<samp id="nombreClienteExistt"></samp>
						</h4>
						<liferay-ui:message key="LiabilityQuotationPortlet.infoModClientExistt" />
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnClienttExisttSi">Si</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal">No</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal usuario existente -->

<!-- Modal Giro Sub-giro -->
<div class="modal" id="modalGiroSubgiro" tabindex="-1" role="dialog" aria-labelledby="modalGiroSubgiroLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title text-black-50" id="modalGiroSubgiroLabel">
					<i class="fas fa-exclamation-triangle"></i> Alerta de suscripciï¿½n
				</h5>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						El Sub Giro seleccionado sï¿½lo puede ser cotizado por el ï¿½rea de suscripciï¿½n <br>
						<h5>ï¿½Desea Continuar?</h5>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" data-dismiss="modal" id="btnSuscripGiroSi">Si</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" id="btnSuscripGiroNo">No</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal Giro Sub-giro -->

<!-- Modal Bloqueo Agentes -->
<div class="modal" id="modalBloqueoAgente" tabindex="-1" role="dialog" aria-labelledby="modalBloqueoAgenteLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title text-black-50" id="modalBloqueoAgenteLabel">
					<i class="fas fa-exclamation-triangle"></i> Restricción para Agente
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>

			<!--Body-->
			<div class="modal-body">
				<div class="row">
					<div class="col-12">
						La clave de agente seleccionada debe actualizar su expediente de Agente para poder emitir pï¿½lizas.   Por el momento, solo podrï¿½ generar cotizaciones.   Dudas o comentarios, favor de escribir a enterate@tokiomarine.com.mx.
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" data-dismiss="modal" id="btncpsusc">Entendido</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal Bloqueo Agentes -->

<script>
	var infoCliente = '${infoCliente}';
</script>

<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/mainPaso1.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/objetos.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/funcionesGenericas.js?v=${version}"></script>

<script>
	ligasServicios.listaPersonas = '${listaPersonasURL}';
	ligasServicios.listaSubgiros = "${getSubGiroURL}";
	ligasServicios.guardaInfo = "${guardaPaso1URL}";
	ligasServicios.canalNegocio = "${canalNegocio}";
	ligasServicios.getGiroRC = "${getGiroRC}";
	ligasServicios.gradoRiesgo = "${gradoRiesgo}";
	
	
	var datosCliente = '${datosCliente}';
	var diasRetro = ${retroactividad};
	var infCotizacion = ${infCotizacionJson};
	var perfilSuscriptor = '${perfilSuscriptor}';
	
	var validaAgenteURL = '${validaAgenteURL}';
	var listaCatFechaConv = ${listaCatFechaConv};
	var idPerfilUser = '${idPerfilUser}';
	
</script>
