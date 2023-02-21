package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CaratulaResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

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
					"mvc.command.name=/RC/CalciularCoti" 
				},
		service = MVCResourceCommand.class)

public class CalcularResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3 _CMServicesP3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));

		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
//		String infoCotiResponse = ParamUtil.getString(resourceRequest, "infoCotiResponse");
		
//		UbicacionesResponse resUbicaciones = getInfoUbicaciones(infoCotiResponse);
		
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		
		double p_primaObjetivo = ParamUtil.getDouble(resourceRequest, "p_primaObjetivo");
		double p_gastos = ParamUtil.getDouble(resourceRequest, "p_gastos");
		double p_recargoPago = ParamUtil.getDouble(resourceRequest, "p_recargoPago");
        JsonArray p_lista = new JsonArray();
        JsonObject ramo = new JsonObject();
        ramo.addProperty("p_codigo_ramo", "41A6");
        ramo.addProperty("p_prima_objetiva", p_primaObjetivo);
        p_lista.add(ramo);
		//		CaratulaResponse response = _CMServicesP3.getCaratulaPrimaRecalculo(cotizacion                    , version                    , p_primaObjetivo, p_gastos, p_recargoPago, usuario             , pantalla)
//		CaratulaResponse response = _CMServicesP3.getCaratulaPrimaRecalculo(cotizacion, version, p_primaObjetivo, p_gastos, p_recargoPago, user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		CaratulaResponse response = _CMServicesP3.calculaPrimaObjetivo(cotizacion, version, idPerfilUser, p_recargoPago, p_gastos, p_lista, user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
		PrintWriter writer = resourceResponse.getWriter();
		String responseString = gson.toJson(response);
		writer.write(responseString);
	}
	
//	private UbicacionesResponse getInfoUbicaciones(String saveResponse){
//		Gson gson = new Gson();
//		if( Validator.isNotNull(saveResponse) ){
//			UbicacionesResponse response = gson.fromJson(saveResponse, UbicacionesResponse.class);
//			return response;
//		}else{
//			return new UbicacionesResponse();
//		}
//	}
}
