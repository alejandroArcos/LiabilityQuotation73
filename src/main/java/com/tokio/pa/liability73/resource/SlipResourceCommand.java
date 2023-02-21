package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.EnviaCorreoVisitaInspeccionResponse;
import com.tokio.pa.cotizadorModularServices.Bean.SlipResponse;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3RC;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;
import com.tokio.pa.liability73.util.SendMail;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
					"mvc.command.name=/RC/getSlip" 
				},
		service = MVCResourceCommand.class)

public class SlipResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3RC _ServicePaso3RC;
	
	@Reference
	CotizadorPaso3 _ServicePaso3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		System.err.println("Entré getslipRC");
		String usuario = "";
		String pantalla = LiabilityQuotation73PortletKeys.LiabilityQuotation;

//		String infoCotiResponse = ParamUtil.getString(resourceRequest, "infoCotiResponse");
//		System.err.println("infoCotiResponse: " + infoCotiResponse);
//		UbicacionesResponse resUbicaciones = getInfoUbicaciones(infoCotiResponse);
//		int p_cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
//		int p_version = ParamUtil.getInteger(resourceRequest, "version");
		String cotizacion = ParamUtil.getString(resourceRequest, "cotizacion");
		String folio = ParamUtil.getString(resourceRequest, "folio");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		int word = ParamUtil.getInteger(resourceRequest, "word");
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		try{
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			usuario = user.getScreenName();
		} catch(Exception e){
			SessionErrors.add(resourceRequest, "errorUsuario" );
			SessionMessages.add(resourceRequest, PortalUtil.getPortletId(resourceRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
		
		enviaCorreoIngenieria(Integer.parseInt(cotizacion), version, Integer.parseInt(folio), usuario, pantalla, idPerfilUser, resourceResponse);
		
		try {
			/*
			SimpleResponse responseValidaciones = _ServicePaso3.validacionesPaso3(Integer
					.parseInt(cotizacion), version, idPerfilUser, usuario, pantalla);
			
			if(responseValidaciones.getCode() == 0) {
			*/
				SlipResponse response = fGetSlip(cotizacion, version, usuario, pantalla, word);
				
				Gson gson = new Gson();
				String jsonString = gson.toJson(response);
				
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(jsonString);
			/*
			}
			else {
				Gson gson = new Gson();
				String jsonString = gson.toJson(responseValidaciones);
				
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(jsonString);
			}
			*/
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	private SlipResponse fGetSlip(String cotizacion, int version, String usuario,
			String pantalla, int word) {
		try {
			return _ServicePaso3RC.getSlip(cotizacion, version, usuario, pantalla, word);
			/*return null;*/
		} catch (Exception e) {
			/* TODO Auto-generated catch block	*/
			return null;
		}
	}
	
	private UbicacionesResponse getInfoUbicaciones(String saveResponse){
		Gson gson = new Gson();
		if( Validator.isNotNull(saveResponse) ){
			UbicacionesResponse response = gson.fromJson(saveResponse, UbicacionesResponse.class);
			return response;
		}else{
			return new UbicacionesResponse();
		}
	}
	
	private void enviaCorreoIngenieria(int p_cotizacion, int p_version, int p_folio, String p_usuario, String p_pantalla, int idPerfil, ResourceResponse resourceResponse) {
		
		try {
			/*
			InfoP_1_1 responseRiesgo = _ServicePaso1.consultaInfoAdicionalPaso1(p_cotizacion, p_version, p_folio, p_usuario, p_pantalla, "" + idPerfil);
			Paso1_1 gradoRiesgo = responseRiesgo.getResultado().get(0);
			*/
			
			//if(gradoRiesgo.getP_gradoRiesgoInc() >= 3 || gradoRiesgo.getP_gradoRiesgoRC() >= 3 || gradoRiesgo.getP_gradoRiesgoRCProd() >= 3) {
				JsonObject request = new JsonObject();
				request.addProperty("p_cotizacion", p_cotizacion);
				request.addProperty("p_version", p_version);
				request.addProperty("idPerfil", idPerfil);
				request.addProperty("p_usuario", p_usuario);
				request.addProperty("p_pantalla", p_pantalla);
				
				EnviaCorreoVisitaInspeccionResponse response = _ServicePaso3RC.getInformacionCorreo(request);
				
				if(response.getCode() == 0) {
					
					String[] listMails = response.getEmails_envio().split(",");
					System.out.println(listMails.length);
					//String[] listMails = {"uriel.flores@globalquark.com.mx"};
					String subject = "Visita de Inspección";
					
					String body = "<!DOCTYPE html> <html>   <head>  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />  <title>Aviso de folio asignado ${folio}</title>   </head>   <body>     <header><h3>Fecha: Ciudad de México a ${fecha}</h3></header><section><p>Equipo de ingeniería,</p><p>Se tiene en consideración una posible visita de inspección para una cotización de paquete empresarial con el Folio ${folio} " + 
							", con ${ubicaciones} ubicación(es) cotizada por el usuario ${usuario}, con correo ${email}, con ejecutivo asignado ${nombre}.</p><p>Esto con la finalidad de que se tenga contemplado en la asignación de una fecha para la posible visita de inspección y actualización de riesgo.</p><p>Así como la asignación de un miembro del equipo para esta visita</p><p>Saludos cordiales</p></section><footer><img         src='https://preview.ibb.co/i3vFWp/Firma_Correo_Tokio_Marine.png'         alt='Firma Correo Tokio Marine'         width='35%'       /></footer></body></html>";
					body = StringUtil.replace(body,
							new String[] { "${fecha}", "${folio}", "${ubicaciones}", "${usuario}", "${email}", "${nombre}"},
							new String[] { response.getP_fecha() , response.getP_folio(), response.getP_ubicacion(), response.getP_usuario(),
									response.getP_email(), response.getP_ejecutivo() });
					
					new SendMail().SendMAil(listMails, body, subject);
				}
				else {
					
				}
				/*
				//Temporal
				Gson gson = new Gson();
				String jsonString = gson.toJson(response);
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(jsonString);
				//Temporal
				 * 
				 */
			//}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
