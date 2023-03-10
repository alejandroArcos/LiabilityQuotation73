<%@ include file="../init.jsp"%>

<div class="col-md-12 mt-5">
	<!--Accordion wrapper-->
	<div class="accordion md-accordion" id="accordionEx" role="tablist" aria-multiselectable="true">

		<!-- Accordion card -->
		<div class="card ">
			
			<!-- Card header -->
			<div class="card-header btn-blue modificado" role="tab" id="headingCoberturaBasica">
				<a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseCoberturaBasica" aria-expanded="false" aria-controls="collapseCoberturaBasica">
					<h5 class="mb-0">
						Cobertura B?sica
						<i class="fas fa-angle-down rotate-icon"></i>
					</h5>
				</a>
			</div>
			
			<div id="collapseCoberturaBasica" class="collapse in" role="tabpanel" aria-labelledby="headingCoberturaBasica" data-parent="#accordionEx">
				<div class="card-body">
				
				<div class="row">
					<div class="col-md-4">
						<div class="md-form form-group">
							<input type="text" id="cb_actInmueble" name="cb_actInmueble" class="form-control" value="">
							<label for="cb_actInmueble">Actividades e Inmuebles</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="md-form form-group">
							<select id="cb_baseCoti" name="cb_baseCoti" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="cb_baseCoti">Base de cotizaci?n</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="md-form form-group">
							<select id="cb_riesgoRC" name="cb_riesgoRC" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="cb_riesgoRC">Grado de riesgo RC</label>
						</div>
					</div>
				</div>
				
				</div>
			</div>
			
		</div>
		
		<!-- Accordion card -->
		<div class="card ">
			
			<!-- Card header -->
			<div class="card-header btn-blue modificado" role="tab" id="headingCoberturaAdicional">
				<a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseCoberturaAdicional" aria-expanded="false" aria-controls="collapseCoberturaAdicional">
					<h5 class="mb-0">
						Coberturas Adicionales
						<i class="fas fa-angle-down rotate-icon"></i>
					</h5>
				</a>
			</div>
			
			<div id="collapseCoberturaAdicional" class="collapse in" role="tabpanel" aria-labelledby="headingCoberturaAdicional" data-parent="#accordionEx">
				<div class="card-body">
				
				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_cargaDescarga" name="ca_cargaDescarga" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_cargaDescarga">Carga y Descarga</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input type="text" name="ca_contraIndepen" id="ca_contraIndepen" class="form-control">
							<label for="ca_contraIndepen">Contratistas Independientes</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcAsumida" name="ca_rcAsumida" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcAsumida">RC Asumida</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcProductos" name="ca_rcProductos" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcProductos">RC Productos</label>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcArrendamiento" name="ca_rcArrendamiento" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcArrendamiento">RC Arrendamiento</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcContamina" name="ca_rcContamina" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcContamina">RC Contaminaci?n</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcEstacionamiento" name="ca_rcEstacionamiento" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcEstacionamiento">RC Estacionamiento</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input type="text" name="ca_sublimite" id="ca_sublimite" class="form-control">
							<label for="ca_sublimite">Subl?mite</label>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<input type="text" name="ca_subVehiculo" id="ca_subVehiculo" class="form-control">
							<label for="ca_subVehiculo">Subl?mite por veh?culo</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input type="text" name="ca_maxCajones" id="ca_maxCajones" class="form-control">
							<label for="ca_maxCajones">N?mero m?ximo de cajones</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_gradoRcProductos" name="ca_gradoRcProductos" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_gradoRcProductos">Grado de riesgo RC Productos</label>
						</div>						
					</div>
					<div class="col-md-3">
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_guardaropa" name="ca_guardaropa" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_guardaropa">xii) Guardaropa</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_lavPlanchado" name="ca_lavPlanchado" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_lavPlanchado">xiii) Lavado y Planchado</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_equiHuespedes" name="ca_equiHuespedes" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_equiHuespedes">xiv) Equipaje y Efectos de hu?spedes</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_dineroValores" name="ca_dineroValores" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_dineroValores">xv) Recepci?n de Dinero y Valores</label>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcCruzada" name="ca_rcCruzada" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcCruzada">RC Cruzada</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_demandaExtranjero" name="ca_demandaExtranjero" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_demandaExtranjero">RC Demandas procedentes del Extranjero</label>
						</div>
					</div>
					<div class="col-md-3">
						
					</div>
					<div class="col-md-3">
						
					</div>
				</div>
				
				</div>
			</div>
			
		</div>
		
	</div>
</div>