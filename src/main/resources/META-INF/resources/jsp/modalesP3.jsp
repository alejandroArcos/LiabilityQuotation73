<%@ include file="../init.jsp"%>

<div class="modal" id="modalComAgentes" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
	<!--Content-->
		<div class="modal-content">
			<!--Header-->
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">Comisiones del Agente</h3>
				
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>

			<!--Body-->
			<div id="bodyModalComisiones" class="modal-body">
				<div class="container">
					
					<table class="table table-striped">
					  <thead>
					    <tr style="background-color: #3d8ad1; color: white;">
					      <th scope="col">Ramo</th>
					      <th scope="col">Cobertura</th>
					      <th scope="col">Nueva Comisión</th>
					    </tr>
					  </thead>
					  <tbody>

					  	<c:forEach items="${comAgente.comisiones}" var="option">
					  		<tr class="trComision">
						      <td class="tdRamo">${fn:trim(option.ramo)}</th> 
						      <td class="tdCobertura">${option.cobertura}</td>
						      <td class="tdComision" contenteditable="true">${option.comisionNueva}</td>
						    </tr>
						</c:forEach>
						
						
					</table>
					
					
				</div>

			</div><!--  -->
			<!--Footer-->
			<div class="modal-footer justify-content-center blue-gradient">
				<button type="button" class="btn btn-pink" style="display: none;">Cancelar</button>
				<button onclick="" type="button" class="btn btn-pink" data-dismiss="modal">Cancelar</button>
				<button onclick="saveComisiones()" type="button" class="btn btn-blue" >Aceptar</button>
			</div>
		</div>
	<!--/.Content-->
	</div>
</div>

<!-- Modal Comisiones Agente -->
<div class="modal" id="modalComisionesAgente" tabindex="-1" role="dialog" aria-labelledby="modalComisionesAgenteLabel"
	aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">Comisiones del Agente</h3>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">
				<div class="row">
					<div class="col-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Ramo</th>
									<th>Cobertura</th>
									<th>Nueva Comisión</th>
								</tr>
							</thead>
							<tbody id="tableComisionesBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<div class="btn btn-pink waves-effect waves-light" id="btnGuardarComisionesAgente">Guardar</div>
					</div>
					<div class="col-md-6">
						<div class="btn btn-blue waves-effect waves-light" id="btnCancelarComisionesAgente" data-dismiss="modal">Cancelar</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- MODAL COMISIONES DEL AGENTE -->

<!-- MODAL DEDUCIBLES -->
<div class="modal" id="modalDeducibles" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document" style="max-width: 80%;">
	<!--Content-->
		<div class="modal-content">
			<!--Header-->
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">DEDUCIBLES</h3>
				
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>

			<!--Body-->
			<div id="bodyModalDeducibles" class="modal-body">
				<div class="row">
					<div class="col-md-3"></div>
					<div class="col-md-2">
						<p>Porcentaje</p>
					</div>
					<div class="col-md-3">
						<p>Criterio</p>
					</div>
					<div class="col-md-2">
						<p>Monto</p>
					</div>
					<div class="col-md-2">
						<p>Unidad</p>
					</div>
				</div>
					
				<div class="row rowROT">
					<div class="col-md-3">
						<p class="mt-3 labelROT">Riesgos Ordiniarios de Tránsito (ROT)</p>
					</div>
					<div class="col-md-2">
						<div class="md-form">
							<input type="text" id="mdlPorcenROT" class="form-control porcenROT">
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select name="dc_moneda" id="mdlCritROT" class="mdb-select form-control-sel criterioROT">
								<option value="-1" selected disabled>Seleccionar</option>
								<option value="1">opc1</option>
								<option value="2">opc2</option>
								<option value="3">opc3</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="md-form">
							<input type="text" id="mdlMontoROT" class="form-control">
						</div>
					</div>
					<div class="col-md-2">
						<div class="md-form form-group">
							<select name="dc_moneda" id="mdlUnidadROT" class="mdb-select form-control-sel">
								<option value="-1" selected disabled>Seleccionar</option>
								<option value="1">opc1</option>
								<option value="2">opc2</option>
								<option value="3">opc3</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3">
						<p class="mt-3">Robo</p>
					</div>
					<div class="col-md-2">
						<div class="md-form">
							<input type="text" id="mdlPorcenRobo" class="form-control">
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select name="dc_moneda" id="mdlCritRobo" class="mdb-select form-control-sel">
								<option value="1" selected>opc1</option>
								<option value="2">opc2</option>
								<option value="3">opc3</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="md-form">
							<input type="text" id="mdlMontoRobo" class="form-control">
						</div>
					</div>
					<div class="col-md-2">
						<div class="md-form form-group">
							<select name="dc_moneda" id="mdlUnidadRobo" class="mdb-select form-control-sel">
								<option value="1" selected>opc1</option>
								<option value="2">opc2</option>
								<option value="3">opc3</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3">
						<p class="mt-3">Averías en el sistema de refrigeración</p>
					</div>
					<div class="col-md-2">
						<div class="md-form">
							<input type="text" id="mdlPorcenASF" class="form-control">
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<select name="dc_moneda" id="mdlCritASF" class="mdb-select form-control-sel">
								<option value="1" selected>opc1</option>
								<option value="2">opc2</option>
								<option value="3">opc3</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="md-form">
							<input type="text" id="mdlMontoASF" class="form-control">
						</div>
					</div>
					<div class="col-md-2">
						<div class="md-form form-group">
							<select name="dc_moneda" id="mdlUnidadASF" class="mdb-select form-control-sel">
								<option value="1" selected>opc1</option>
								<option value="2">opc2</option>
								<option value="3">opc3</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row mt-4">
					<div class="col-sm-12 text-right">
						<a onclick="addDeducibleJsp();">Agregar Deducible <i class="fa fa-plus-circle" aria-hidden="true"></i></a>
					</div>
				</div>
				<div id="rowDeduciblesLibre">
					<div class="row rowDeducible">
						<div class="col-md-3">
							<div class="md-form">
								<input type="text" id="txtDeducible0" class="form-control descripcionDed">
							</div>
						</div>
						<div class="col-md-2">
							<div class="md-form">
								<input type="text" id="mdlPorcenDedu0" class="form-control porcenDed">
							</div>
						</div>
						<div class="col-md-3">
							<div class="md-form">
								<input type="text" id="mdlCritDedu0" class="form-control criterioDed">
							</div>
<!-- 							<div class="md-form form-group"> -->
<!-- 								<select name="dc_moneda" id="mdlCritDedu0" class="mdb-select form-control-sel criterioDed"> -->
<!-- 									<option value="1" selected>opc1</option> -->
<!-- 									<option value="2">opc2</option> -->
<!-- 									<option value="3">opc3</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
						</div>
						<div class="col-md-2">
							<div class="md-form">
								<input type="text" id="mdlMontoDedu0" class="form-control montoDed">
							</div>
						</div>
						<div class="col-md-2">
							<div class="md-form form-group">
								<select name="dc_moneda" id="mdlUnidadDedu0" class="mdb-select form-control-sel unidadDed">
									<option value="1" selected>opc1</option>
									<option value="2">opc2</option>
									<option value="3">opc3</option>
								</select>
							</div>
						</div>
					</div>
				</div>

			</div><!--  -->
			<!--Footer-->
			<div class="modal-footer justify-content-center blue-gradient">
				<button type="button" class="btn btn-pink" style="display: none;">Cancelar</button>
				<button onclick="" type="button" class="btn btn-pink" data-dismiss="modal">Cancelar</button>
				<button onclick="saveDeducibles();" type="button" class="btn btn-blue" >Continuar</button>
			</div>
		</div>
	<!--/.Content-->
	</div>
</div>
<!-- MODAL DEDUCIBLES -->

<!-- MODAL CLAUSULAS ADICIONALES -->
<div class="modal" id="modalClausulas" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
	<!--Content-->
		<div class="modal-content">
			<!--Header-->
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">CLAUSULAS ADICIONALES</h3>
				
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>

			<!--Body-->
			<div id="bodyModalClausulas" class="modal-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-check mt-4">
						    <input type="checkbox" class="form-check-input" id="checkALL" name="checkALL">
						    <label class="form-check-label" for="checkALL">Marcar / Desmarcar todas</label>
						</div>
					</div>
				</div>
				<hr>
				<div class="row rowClausulas">
					<c:forEach items="${respClauAdi.clausulas}" var="option">
				  		<div class="col-sm-12">
							<div class="form-check mt-4">
							    <input type="checkbox" class="form-check-input" id="${option.idClausula}" name="${option.idClausula}" ${option.obligatoria ? 'checked disabled' : ''}>
							    <label class="form-check-label" for="${option.idClausula}">${option.descripcion}</label>
							</div>
						</div>
					</c:forEach>
				</div>

			</div><!--  -->
			<!--Footer-->
			<div class="modal-footer justify-content-center blue-gradient">
				<button type="button" class="btn btn-pink" style="display: none;">Cancelar</button>
				<button onclick="" type="button" class="btn btn-pink" data-dismiss="modal">Cancelar</button>
				<button id="saveClausulas" onclick="saveClausulas();" type="button" class="btn btn-blue" >Continuar</button>
			</div>
		</div>
	<!--/.Content-->
	</div>
</div>
<!-- MODAL CLAUSULAS ADICIONALES -->

<!-- Modal Rechazar propuesta -->
<div class="modal" id="modalRechazarProp" tabindex="-1" role="dialog"
	aria-labelledby="modalRechazarPropLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered"
		role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="modalRechazarPropLabel">
					<liferay-ui:message key="LiabilityQuotationPortlet.modalRechazaPropuesta" />
				</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-md-12" style="text-align: center;">
						<span id="textRechazo">
							<liferay-ui:message key="LiabilityQuotationPortlet.modalTxtRechazo" />
						</span>
					</div>

					<div class="col-md-12">
						<div class="md-form form-group">
							<select name="selectMotRechazo" id="selectMotRechazo "
								class="mdb-select form-control-sel">
								<option value="-1" selected><liferay-ui:message key="LiabilityQuotationPortlet.selectOpDefoult" /></option>
								<c:forEach items="${motivoRechazo}" var="option">
									<option value="${option.idCatalogoDetalle}">${option.valor}</option>
								</c:forEach>
							</select> <label for="selectMotRechazo"> <liferay-ui:message key="LiabilityQuotationPortlet.modalMotivo" /> </label>
						</div>
					</div>
					<div class="col-12">
						<div class="md-form">
							<textarea id="comentariosRechazarProp"
								name="comentariosRechazarProp" class="md-textarea form-control"
								rows="3" maxlength="1000" style="text-transform: uppercase;"></textarea>
							<label for="comentariosDosSuscrip"> <liferay-ui:message key="LiabilityQuotationPortlet.modalComentarios" /> </label>
						</div>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light"
							data-dismiss="modal"> <liferay-ui:message key="LiabilityQuotationPortlet.cancelar" /> </button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light"
							id="btnEnvRecha"> <liferay-ui:message key="LiabilityQuotationPortlet.enviar" /> </button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END  Modal Rechazar propuesta -->

<!-- Modal subir archivos paso 2  -->
<!-- <div class="modal" id="fileModal" tabindex="-1" role="dialog" -->
<!-- 	aria-labelledby="fileModalLabel" aria-hidden="true"> -->
<!-- 	<div class="modal-dialog modal-lg" role="document"> -->
<!-- 		<div class="modal-content"> -->
<!-- 			<div class="fileErrorPopup"></div> -->
<!-- 			<div class="modal-header"> -->
<!-- 				<h5 class="modal-title" id="fileModalLabel"> -->
<%-- 					<liferay-ui:message key="LiabilityQuotationPortlet.modalRequiereInfoAdicional" /> --%>
<!-- 				</h5> -->
<!-- 			</div> -->
<!-- 			<div class="modal-body"> -->

<!-- 				<div class="row"> -->
<!-- 					<div class="col-12"> -->
<%-- 						<span> <liferay-ui:message key="LiabilityQuotationPortlet.modalArchivosPermitidos" /> </span> --%>
<!-- 						<div class="md-form"> -->
<!-- 							<div class="file-field"> -->
<!-- 								<div class="btn btn-blue btn-rounded btn-sm float-left"> -->
<!-- 									<span> -->
<!-- 										<i class="fas fa-upload mr-2" aria-hidden="true"></i> -->
<%-- 										<liferay-ui:message key="LiabilityQuotationPortlet.modalSelArchivo" /> --%>
<!-- 									</span> -->
<!-- 									<input id="docAgenSusc" type="file" multiple="multiple" data-file_types="pdf|doc|docx|xls|xlsx"> -->
<!-- 								</div> -->
<!-- 								<input id="infDocSuc" class="form-control" type="text" -->
<!-- 									placeholder="Archivos" disabled> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="row"> -->
<!-- 					<div class="col-12"> -->
<!-- 						<div class="md-form"> -->
<!-- 							<textarea id="comentariosDosSuscrip" name="comentariosDosSuscrip" -->
<!-- 								class="md-textarea form-control" rows="3" maxlength="1000" -->
<!-- 								style="text-transform: uppercase;"></textarea> -->
<%-- 							<label for="comentariosDosSuscrip"> <liferay-ui:message key="LiabilityQuotationPortlet.modalComentarios" /> </label> --%>
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="modal-footer"> -->
<!-- 				<button class="btn btn-pink waves-effect waves-light" id="btnSuscripEnvSus"> -->
<%-- 					<liferay-ui:message key="LiabilityQuotationPortlet.modalEnviarSuscriptor" /> --%>
<!-- 				</button> -->
<!-- 				<button class="btn btn-pink waves-effect waves-light" id="btnSuscripEnvSus2" hidden> -->
<%-- 					<liferay-ui:message key="LiabilityQuotationPortlet.modalEnviarSuscriptor" /> --%>
<!-- 				</button> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->
<!-- end Modal subir archivos paso 2  -->


<!-- Modal subir archivos paso 2  -->
<div class="modal" id="fileModal" tabindex="-1" role="dialog" aria-labelledby="fileModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="fileErrorPopup"></div>
			<div class="modal-header green">
				<h5 class="modal-title text-white" id="fileModalLabel">
				<i class="far fa-edit"></i>
				¿Requiere incluir información adicional?</h5>
			</div>
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<span>Archivos permitidos, PDF, Excel, Word</span>
						<div class="md-form">
							<div class="file-field">
								<div class="btn btn-blue btn-rounded btn-sm float-left">
									<span><i class="fas fa-upload mr-2" aria-hidden="true"></i>Seleccionar Archivo</span>
									<input id="docAgenSusc" type="file" multiple="multiple" data-file_types="pdf|doc|docx|xls|xlsx">
								</div>
								<input id="infDocSuc" class="form-control" type="text" placeholder="Archivos" disabled>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-12">
						<div class="md-form">
							<textarea id="comentariosDosSuscrip" name="comentariosDosSuscrip" class="md-textarea form-control" rows="3"
								maxlength="1000" style="text-transform: uppercase;"></textarea>
							<label for="comentariosDosSuscrip"> Comentarios adicionales. </label>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-pink waves-effect waves-light" id="btnSuscripEnvSus">Enviar a suscriptor</button>
				<button class="btn btn-pink waves-effect waves-light d-none" id="btnSuscripEnvSus2" >Enviar a suscriptor</button>
			</div>
		</div>
	</div>
</div>
<!-- end Modal subir archivos paso 2  -->

<!-- MODAL SLIP WORD -->
<div class="modal" id="modalSlipWord" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">Carga Slip Word</h3>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>
			<div id="bodyModalSlipWord" class="modal-body">
				<hr>
				<div class="row">
					<div class="col-md-12 d-flex justify-content-center section-heading md-form">
						<h5 class="title text-left padding70 mt-4">Carga de Slip en PDF</h5>
					</div>
					<div class="col-md-12 d-flex justify-content-center md-form">
						<div class="file-field">
							<a class="btn-floating blue-gradient mt-0 float-left">
								<i class="fas fa-paperclip" aria-hidden="true"></i>
								<input type="file" id="archivoSlip" data-file_types="pdf" accept="application/pdf">
							</a>
							<div class="file-path-wrapper">
								<input id="infDocSuc" class="file-path" type="text" placeholder="Adjuntar" readonly>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer justify-content-center blue-gradient">
				<button type="button" class="btn btn-pink" style="display: none;">Cancelar</button>
				<button onclick="" type="button" class="btn btn-pink" data-dismiss="modal">Cancelar</button>
				<button id="guardarSlipWord" onclick="guardarSlipWord();" type="button" class="btn btn-blue" >Continuar</button>
			</div>
		</div>
	</div>
</div>
<!-- MODAL SLIP WORD -->


<!-- Modal Prima Objetivp -->
<div class="modal" id="modalPrimaObjetivo" tabindex="-1" role="dialog" aria-labelledby="modalPrimaObjetivoLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">Prima Objetivo</h3>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">
				<div class="row">
					<div class="col-12">
						<table class="table">
							<thead>
								<tr>
									<th>Ramo o Sección</th>
									<th>Suma Asegurada</th>
									<th>Prima</th>
									<th>Cuota original</th>
									<th>Cuota final</th>
									<th>Prima Objetivo</th>
									<th>Descuento</th>
								</tr>
							</thead>
							<tbody id="tablePrimasBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<div class="btn btn-pink waves-effect waves-light" id="btnGuardarPrimaObjetivo">Guardar</div>
					</div>
					<div class="col-md-6">
						<div class="btn btn-blue waves-effect waves-light" data-dismiss="modal">Cancelar</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- MODAL SLIP JI -->
<div class="modal" id="modalSlipJI" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header blue-gradient text-white">
				<h3 class="heading lead">Carga Slip JI</h3>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true" class="white-text">&times;</span>
				</button>
			</div>
			<div id="bodyModalSlipJI" class="modal-body">
				<hr>
				<div class="row">
					<div class="col-md-12 d-flex justify-content-center section-heading md-form">
						<h5 class="title text-left padding70 mt-4">Carga de Slip en PDF</h5>
					</div>
					<div class="col-md-12 d-flex justify-content-center md-form">
						<div class="file-field">
							<a class="btn-floating blue-gradient mt-0 float-left">
								<i class="fas fa-paperclip" aria-hidden="true"></i>
								<input type="file" id="archivoSlipJIEsp" data-file_types="pdf" accept="application/pdf">
							</a>
							<div class="file-path-wrapper">
								<input id="infDocSuc" class="file-path" type="text" placeholder="Slip en Español" readonly>
							</div>
						</div>
						<div class="file-field">
							<a class="btn-floating blue-gradient mt-0 float-left">
								<i class="fas fa-paperclip" aria-hidden="true"></i>
								<input type="file" id="archivoSlipJIEng" data-file_types="pdf" accept="application/pdf">
							</a>
							<div class="file-path-wrapper">
								<input id="infDocSuc" class="file-path" type="text" placeholder="Slip en Inglés" readonly>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer justify-content-center blue-gradient">
				<button type="button" class="btn btn-pink" style="display: none;">Cancelar</button>
				<button onclick="" type="button" class="btn btn-pink" data-dismiss="modal">Cancelar</button>
				<button id="guardarSlipJI" onclick="guardarSlipJI();" type="button" class="btn btn-blue" >Continuar</button>
			</div>
		</div>
	</div>
</div>
<!-- MODAL SLIP JI -->

<div class="modal" id="modalGenerarPoliza" tabindex="-1" role="dialog" aria-labelledby="cerrarPolizaLabel"
	aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered anchoModal" role="document">
		<div class="modal-content">
			<div class="modal-header green">
				<h5 class="modal-title text-white" id="cerrarPolizaLabel">
					<span id="titModalEmisionp3"></span>
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body -->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<table class="table altoTbPoliza tablaPoliza">
							<tbody>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaNumeroPoliza" /></b> <span
											id="txtModalPolizaNumeroPoliza"> </span>
									</td>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaCertificado" /></b> <span
											id="txtModalPolizaCertificado"> </span>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaAsegurado" /></b> <span id="txtModalPolizaAsegurado">
										</span>
									</td>
								</tr>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaVigencia" /></b> <span id="txtModalPolizaVigenciaDe">
										</span> <b> al </b> <span id="txtModalPolizaVigenciaAl"> </span>
									</td>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaTotalUbicaciones" /></b> <span
											id="txtModalPolizaTotalUbicaciones"> </span>
									</td>
								</tr>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaMoneda" /></b> <span id="txtModalPolizaMoneda">
										</span>
									</td>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaFormaPago" /></b> <span id="txtModalPolizaFormaPago">
										</span>
									</td>
								</tr>
							</tbody>
						</table>
						<table class="table altoTbPoliza table-borderless colPoliza20 tablaPoliza">
							<tbody>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaPrimaNeta" /></b>
									</td>
									<td class="moneda celdaPoliza">$</td>
									<td class="colPoliza10">
										<span id="txtModalPolizaPrimaNeta"> </span>
									</td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaRecargoPago" /></b>
									</td>
									<td class="moneda celdaPoliza">$</td>
									<td class="colPoliza10">
										<span id="txtModalPolizaRecargoPago"> </span>
									</td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaGastosExpedicion" /></b>
									</td>
									<td class="moneda celdaPoliza">$</td>
									<td class="colPoliza10">
										<span id="txtModalPolizaGastosExpedicion"> </span>
									</td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaIva" /></b>
									</td>
									<td class="moneda celdaPoliza">$</td>
									<td class="colPoliza10">
										<span id="txtModalPolizaIva"> </span>
									</td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<b><liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaTotal" /></b>
									</td>
									<td class="moneda celdaPoliza">$</td>
									<td class="colPoliza10">
										<span id="txtModalPolizaTotal"> </span>
									</td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
				<input type="hidden" id="txtModalPolizaAgente" class="d-none">
				</div>
				<div class="row">
					<div class="col-7">
						<div class="row">
							<div class="col-12">
								<div class="md-form">
									<input type="email" name="modalPolizaEnviarCorreo" id="modalPolizaEnviarCorreo" class="form-control">
									<label for="modalPolizaEnviarCorreo">
										<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaEnviarCorreo" />
									</label>
								</div>
							</div>
							<div class="col-12">
								<button class="btn btn-pink waves-effect waves-light float-right" id="btnAgregaCorreoPoliza" disabled>
									<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaBtnAgregarCorreo" />
								</button>
							</div>
							<div class="col-12 table-wrapper-scroll-y">
								<table class="altoTbArchivos table table-striped" id="tablaArchivosPoliza" style="width: 100%;">
									<thead>
										<tr>
											<th scope="col" class="th-sm">
												<div class="form-check">
													<input type="checkbox" class="form-check-input selectCheckImput" id="seleccionarTodosArchivos" checked>
													<label class="form-check-label" for="seleccionarTodosArchivos">
														<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaChekTodos" />
													</label>
												</div>
											</th>
											<th scope="col" class="th-sm">
												<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaArchivoTit" />
											</th>
											<th scope="col" class="th-sm">
												<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaTipoTit" />
											</th>
										</tr>
									</thead>
									<tbody class="bodyArchivos">

									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="col-5">
						<div class="alert alert-warning msjActivarBtnEnviar" role="alert" hidden="true">
							<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaMsjEnviar" />
						</div>
						<ul id="listaCorreos" class="listaCorreos scrollbarLiMod scrollbarLiMod-primary">
						</ul>
					</div>
				</div>
				<div class="row">
					<div class="col-6">
						<button class="btn btn-pink waves-effect waves-light " id="btnDescargarArchivos">
							<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaBtnDescargarArchivos" />
						</button>
						<button class="btn btn-pink waves-effect waves-light" id="polizaBtnEnviar">
							<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaBtnEnviar" />
						</button>
					</div>
					<div class="col-6">
						<button class="btn btn-blue waves-effect waves-light float-right" id="polizaBtnAceptar" data-dismiss="modal">
							<liferay-ui:message key="LiabilityQuotationPortlet.modalPolizaBtnAceptar" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal Poliza Generada -->

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
						El agente seleccionado para esta cotización debe actualizar su expediente de Agente por lo que no es posible emitir esta cotización.   Dudas o comentarios, favor de escribir a enterate@tokiomarine.com.mx.					</div>
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