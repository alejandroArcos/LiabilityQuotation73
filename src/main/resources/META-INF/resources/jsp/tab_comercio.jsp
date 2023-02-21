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
						Cobertura Básica
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
							<label for="cb_baseCoti">Base de cotización</label>
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
							<select id="ca_rcCruzada" name="ca_rcCruzada" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcCruzada">RC Cruzada</label>
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
							<label for="ca_sublimite">Sublímite</label>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<input type="text" name="ca_subVehiculo" id="ca_subVehiculo" class="form-control">
							<label for="ca_subVehiculo">Sublímite por vehículo</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input type="text" name="ca_maxCajones" id="ca_maxCajones" class="form-control">
							<label for="ca_maxCajones">Número máximo de cajones</label>
						</div>
					</div>
					<div class="col-md-3">
						
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_gradoRcProductos" name="ca_gradoRcProductos" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_gradoRcProductos">Grado de riesgo RC Productos</label>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcBienesCustodia" name="ca_rcBienesCustodia" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcBienesCustodia">RC Bienes bajo custodia</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcContamina" name="ca_rcContamina" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcContamina">RC Contaminación</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcExplosivos" name="ca_rcExplosivos" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcExplosivos">RC Explosivos</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcDaniosExtran" name="ca_rcDaniosExtran" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcDaniosExtran">RC Daños en el Extranjero</label>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcProfColegios" name="ca_rcProfColegios" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcProfColegios">RC Profesional Colegios</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_acosoColegio" name="ca_acosoColegio" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_acosoColegio">Acoso Sexual Colegios</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcProfGim" name="ca_rcProfGim" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcProfGim">RC Profesional Gimnasios</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_gradoRcProductos" name="ca_gradoRcProductos" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_gradoRcProductos">Acoso Sexual Gimnasios</label>
						</div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-3">
						<div class="md-form form-group">
							<select id="ca_rcProductosExporta" name="ca_rcProductosExporta" class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
							</select>
							<label for="ca_rcProductosExporta">RC Productos de Exportación</label>
						</div>
					</div>
				</div>
				
				</div>
			</div>
			
		</div>
		
	</div>
</div>