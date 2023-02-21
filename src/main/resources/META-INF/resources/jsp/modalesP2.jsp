<%@ include file="../init.jsp"%>

<!-- Modal regresar paso anterior -->
<div class="modal" id="modalRegresarP1" tabindex="-1" role="dialog" aria-labelledby="helpLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title text-black-50" id="helpLabel">
					<i class="fas fa-exclamation-triangle"></i>
					<liferay-ui:message key="LiabilityQuotationPortlet.mdlTitRegresar" />
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<h2>
							<liferay-ui:message key="LiabilityQuotationPortlet.mdlbody1" />
						</h2>
						<h3>
							<liferay-ui:message key="LiabilityQuotationPortlet.mdlBody2" />
						</h3>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div id="divRegPas2" class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnRegresarPasoAnt">Regresar a la página anterior</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal">Continuar en esta página</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Modal regresar paso anterior -->

<!-- Modal Arrendatario -->
<div class="modal" id="modalRCArrendatario" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm modal-dialog-scrollable" role="document">
		<div class="modal-content">
			<div class="modal-header blue">
				<h5 class="modal-title text-white">
					<liferay-ui:message key="LiabilityQuotationPortlet.titRFCArrendatario" />
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<div class="form-check">
						    <input type="checkbox" class="form-check-input" id="modalCheckAll">
						    <label class="form-check-label" for="modalCheckAll">Marcar / Desmarcar Todas</label>
						</div>
						<hr>
						<c:forEach var="i" begin="1" end="${ infCotizacion.noUbicaciones }">
							<div class="form-check">
							    <input type="checkbox" class="form-check-input checkUbi" id="modalCheck${ i }">
							    <label class="form-check-label" for="modalCheck${ i }">${ i }</label>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-sm btn-pink waves-effect waves-light" onclick="setValueArrendatario();">Aceptar</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-sm btn-blue waves-effect waves-light" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal Arrendatario -->

<!-- Modal Cerrar tab -->
<div class="modal" id="modalCerrarTab" tabindex="-1" role="dialog" aria-labelledby="cerrarLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title text-black-50" id="cerrarLabel">
					<i class="fas fa-exclamation-triangle"></i>
					<liferay-ui:message key="LiabilityQuotationPortlet.mplTabsTituloCerrar" />
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body -->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<h3>
							<liferay-ui:message key="LiabilityQuotationPortlet.mplTabsMsj1" />
							<b><label id="ubicacionEliminalbl"></label></b>
							<liferay-ui:message key="LiabilityQuotationPortlet.mplTabsMsj2" />
						</h3>
					</div>
				</div>
			</div>

			<!--Footer -->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal">Cancelar</button>
					</div>
					<div class="col-md-6" id="elimTabBtn">
						<button class="btn btn-pink waves-effect waves-light" id="btnEliminarPestania" data-dismiss="modal">Eliminar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal Cerrar tab -->

<!-- Modal monto excedido -->
<div class="modal" id="modalSuscripMontoExede" tabindex="-1" role="dialog" aria-labelledby="modalSuscripMontoExedeLabel"
	aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title text-black-50" id="modalSuscripMontoExedeLabel">
				<i class="fas fa-exclamation-triangle"></i> Alerta de limites</h5>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<span id="suscripMontoExedeTxt"></span> <br>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnSuscripMontoSi" >Si</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal" >No</button>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal monto excedido -->

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

<!-- Modal Carga Masiva -->
<div class="modal" id="modalCargaMasiva" tabindex="-1" role="dialog" aria-labelledby="modalCargaMasivaLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header green">
				<h5 class="modal-title text-black-50" id="modalCargaMasivaLabel">
					<i class="fa fa-building"></i> Carga masiva de Ubicaciones
				</h5>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
					<div class="col-12">
						<div class="row">
							<div class="col-6"></div>
							<div class="col-4 pt-2">Descargar Archivo</div>
							<div class="col-2">
								<a id="btnDescargaXLS_CM" href="${urlDoc}" class="btn-floating btn-sm teal pt-2 pl-1 text-dark">XLS</a>
							</div>
						</div>
					</div>
				</div>
				<hr />
				<div class="row">
					<div class="col-12">
						<span>Archivo permitido Excel</span>
						<div class="md-form">
							<div class="file-field">
								<div class="btn btn-blue btn-rounded btn-sm float-left">
									<span>
										<i class="fas fa-upload mr-2" aria-hidden="true"></i>Seleccionar Archivo
									</span>
									<input id="docCargaMasiva" type="file" data-file_types="xls|xlsx">
								</div>
								<input id="infDocCargaMasiva" class="form-control" type="text" placeholder="Archivo" disabled>
							</div>
						</div>
					</div>
				</div>

			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnCargMasiAcepta">Aceptar</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal" id="btnCargMasiCancel">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- END Modal carga masiva -->