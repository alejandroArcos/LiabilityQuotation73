package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso2RC;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
					"mvc.command.name=/RC/SaveUbiCMRC" 
				},
		service = MVCResourceCommand.class)

public class GuardaCargaMasivaResourceCommand extends BaseMVCResourceCommand {

	
	@Reference
	CotizadorPaso2RC _CMServicesP2RC;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		String p_cotizacion = ParamUtil.getString(resourceRequest, "p_cotizacion");
		String p_version = ParamUtil.getString(resourceRequest, "p_version");
		String p_folio = ParamUtil.getString(resourceRequest, "p_folio");
		String cargaMasiva = ParamUtil.getString(resourceRequest, "cargaMasiva");
		
		JsonObject cargaMasivaObj = new JsonObject(); 
		
		JsonArray axuArrayCargaMasiva = gson.fromJson(cargaMasiva, JsonArray.class);
		
		cargaMasivaObj.addProperty("p_cotizacion", p_cotizacion);
		cargaMasivaObj.addProperty("p_version", p_version);
		cargaMasivaObj.addProperty("p_ubicaciones", axuArrayCargaMasiva.size());
		cargaMasivaObj.addProperty("p_usuario", user.getScreenName());
		cargaMasivaObj.addProperty("p_pantalla", LiabilityQuotation73PortletKeys.LiabilityQuotation);
		cargaMasivaObj.add("cargaMasiva", axuArrayCargaMasiva);
		
		UbicacionesResponse ubicacionResponseCargaMasiva = _CMServicesP2RC.cargaMasivaRC(cargaMasivaObj);
	
		generaVarSesion(resourceRequest, ubicacionResponseCargaMasiva);
		
		String jsonString = gson.toJson(ubicacionResponseCargaMasiva);
		System.err.println(jsonString);
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
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
			
			String nombreCargaMasiva = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
					+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
					+ "_CARGAMASIVA";

			System.out.println(".........................>2");
			System.out.println(ubicacionString);
			psession.setAttribute(auxNombre, ubicacionString, PortletSession.APPLICATION_SCOPE);
			psession.setAttribute(nombreCargaMasiva, true, PortletSession.APPLICATION_SCOPE);

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
