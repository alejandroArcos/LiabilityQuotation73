package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso2RC;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
					"mvc.command.name=/RC/SaveUbiRC" 
				},
		service = MVCResourceCommand.class)

public class GuardaUbicacionRCResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso2RC _CMPaso2RC;
	
	InfoCotizacion infCot = null;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		generaInfoCot(resourceRequest);
		
		String ubiArray = ParamUtil.getString(resourceRequest, "ubiArray");
//		String p_folio = ParamUtil.getString(resourceRequest, "p_folio");
		int modeSave = ParamUtil.getInteger(resourceRequest, "modeSave");/* 1- Guardar ----- 2-Agregar ---------- 3-Eliminar */
		int noUbi = ParamUtil.getInteger(resourceRequest, "noUbi");
//		int p_cotizacion = ParamUtil.getInteger(resourceRequest, "p_cotizacion");
		
		String usuario = themeDisplay.getUser().getScreenName();
		
		JsonArray ubicaciones = gson.fromJson(ubiArray, JsonArray.class);
//		int p_numeroUbicaciones = ubicaciones.size() +1;
		
		
//		                               _CMPaso2RC.guardaUbicacionRC(p_cotizacion,                p_folio             , p_tipoGuardar, p_version            , p_numeroUbicaciones, p_modo                                   ,   p_usuario ,idPerfilUser,  p_pantalla                                      , p_ubicacion)
		UbicacionesResponse response = _CMPaso2RC.guardaUbicacionRC((int)infCot.getCotizacion(), ""+infCot.getFolio(), modeSave      , infCot.getVersion() , noUbi              , infCot.getModo().getModoCotizacion()     , usuario     , idPerfilUser, LiabilityQuotation73PortletKeys.LiabilityQuotation, ubicaciones);
		generaVarSesion(resourceRequest, response);
		
		System.err.println("RESPONSE SAVE UBICACION");
		System.err.println(response);

		String jsonString = gson.toJson(response);
		System.err.println(jsonString);
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
		
	}
	
	private void generaInfoCot(ResourceRequest resourceRequest){
		Gson gson = new Gson();
		
		String infoCot = ParamUtil.getString(resourceRequest, "infoCot");
		System.out.println("infoCot String :" + infoCot);
		infCot = gson.fromJson(infoCot, InfoCotizacion.class);
		
		System.out.println("infCot objeto: " + infCot);
		
	}
	
	private void generaVarSesion(ResourceRequest resourceRequest, UbicacionesResponse ubicacionResponse ){
		JsonObject respuesta = new JsonObject();
		
		respuesta.addProperty("code", ubicacionResponse.getCode());
		respuesta.addProperty("msg", ubicacionResponse.getMsg());
		
		final PortletSession psession = resourceRequest.getPortletSession();
		if (ubicacionResponse.getCode() == 0) {


			String ubicacionString = CotizadorModularUtil.objtoJson(ubicacionResponse);
			String auxNombre = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
					+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
					+ "_UBICACIONRESPONSE";

			System.out.println(".........................>2");
			System.out.println(ubicacionString);
			psession.setAttribute(auxNombre, ubicacionString, PortletSession.APPLICATION_SCOPE);

		}
		if (ubicacionResponse.getCode() == 5) {

			System.out.println("entre en codigo 5");

			String ubicacionString = CotizadorModularUtil.objtoJson(ubicacionResponse);
			String auxNombre = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
					+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
					+ "_UBICACIONRESPONSE";

			String auxNombre2 = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
					+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
					+ "_EXCEDELIMITES";

			psession.setAttribute(auxNombre2, 1, PortletSession.APPLICATION_SCOPE);
			psession.setAttribute(auxNombre, ubicacionString, PortletSession.APPLICATION_SCOPE);

			System.out.println("los nombres de variables son:");
			System.out.println(auxNombre);
			System.out.println(auxNombre2);
		}
	}
}
