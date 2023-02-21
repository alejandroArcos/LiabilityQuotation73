package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.DeducibleResponse;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
					"mvc.command.name=/RC/Deducibles" 
				},
		service = MVCResourceCommand.class)

public class SaveDeduciblesResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3 _CMServicesP3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		String p_deduciblesFijo = ParamUtil.getString(resourceRequest, "p_deduciblesFijo");
		String p_deduciblesLibre = ParamUtil.getString(resourceRequest, "p_deduciblesLibre");
		String infoCotiResponse = ParamUtil.getString(resourceRequest, "infoCotiResponse");
		
		JsonArray deduciblesFijo = gson.fromJson(p_deduciblesFijo, JsonArray.class);
		JsonArray deduciblesLibre = gson.fromJson(p_deduciblesLibre, JsonArray.class);
		UbicacionesResponse resUbicaciones = getInfoUbicaciones(infoCotiResponse);
		
//		_CMServicesP3.guardaDeducibles(p_cotizacion, p_version, p_usuario, p_pantalla, p_deduciblesFijo, p_deduciblesLibre);
		DeducibleResponse response = _CMServicesP3.guardaDeducibles(resUbicaciones.getCotizacion(), resUbicaciones.getVersion(), user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation, deduciblesFijo, deduciblesLibre);
		
		PrintWriter writer = resourceResponse.getWriter();
		String responseString = gson.toJson(response);
		writer.write(responseString);
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
}
