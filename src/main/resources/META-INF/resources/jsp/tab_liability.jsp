<%@ include file="../init.jsp"%>

<c:set var="oculto" value="${ perfilMayorEjecutivo ? '' : 'd-none' }"></c:set>

<h6 id="tit_tab">
	<liferay-ui:message key="LiabilityQuotationPortlet.titDatsRiesgP2" />
</h6>
<div class="datosRiesgo">
	<div class="row">
		<div class="col-md-2">
			<div class="md-form">
				<input type="text" name="dr_cp" id="dr_cp_${count+1}" idCp="${ ubicacion.ubicaciones[count].cpData.idCp}" maxlength="5" class="form-control cpValido infReq" value="${ ubicacion.ubicaciones[count].cpData.cp}">
				<label for="dr_cp_${count+1}">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblCodPosP2" />
				</label>
			</div>
		</div>
		<div class="col-md-2">
			<div class="md-form">
				<%-- <input type="text" name="dr_calle" id="dr_calle" class="form-control infReq" value="${ ubicacion.ubicaciones[count].calle}"> --%>
				<input type="text" name="dr_calle" id="dr_calle" class="form-control infReq" value="${fn:escapeXml( ubicacion.ubicaciones[count].calle )}">
				<label for="dr_calle">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblCalleP2" />
				</label>
			</div>
		</div>
		<div class="col-md-2">
			<div class="md-form form-group">
				<%-- <input type="text" name="dr_numero" id="dr_numero" class="form-control infReq" value="${ ubicacion.ubicaciones[count].numero}"> --%>
				<input type="text" name="dr_numero" id="dr_numero" class="form-control infReq" value="${fn:escapeXml( ubicacion.ubicaciones[count].numero )}">
				<label for="dr_numero">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblNumeroP2" />
				</label>
			</div>
		</div>
		<div class="col-md-2">
			<div class="md-form form-group">
				<%-- <input type="text" name="dr_numero_int" id="dr_numero_int" class="form-control noReq" value="${ ubicacion.ubicaciones[count].numInt}"> --%>
				<input type="text" name="dr_numero_int" id="dr_numero_int" class="form-control noReq" value="${fn:escapeXml( ubicacion.ubicaciones[count].numInt )}">
				<label for="dr_numero_int">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblNumeroIntP2" />
				</label>
			</div>
		</div>
		<div class="col-md-2">
			<div class="md-form form-group">
				<select name="dr_colonia" id="dr_colonia" class="mdb-select form-control-sel  infReq">
					<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
					<c:forEach items="${colonias[count]}" var="option">
						<c:set var="seleccionado" 
						value="${ option.idCp == ubicacion.ubicaciones[count].cpData.idCp ? 'selected' : '' }"></c:set>
						<option ${ seleccionado } value="${option.idCp}">${option.colonia}</option>
					</c:forEach>
				</select>
				<label for="dr_colonia">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblColoniaP2" />
				</label>
			</div>
		</div>
		<div class="col-md-2">
			<div class="md-form form-group">
				<input type="text" name="dr_municipio" id="dr_municipio" class="form-control infReq" value="${ ubicacion.ubicaciones[count].cpData.delegacion}">
				<label for="dr_municipio">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblDelMuniP2" />
				</label>
			</div>
		</div>
	</div>



	<div class="row">
		<div class="col-md-2">
			<div class="md-form form-group">
				<input type="text" name="dr_estado" id="dr_estado" class="form-control infReq" value="${ ubicacion.ubicaciones[count].cpData.estado}">
				<label for="dr_estado">
					<liferay-ui:message key="LiabilityQuotationPortlet.lblEdoP2" />
				</label>
			</div>
		</div>
		
	</div>
</div>

<!-- <h6 class="p-4"> -->
<%-- 	<liferay-ui:message key="LiabilityQuotationPortlet.titSumaAseguraCoberP2" /> --%>
<!-- </h6> -->