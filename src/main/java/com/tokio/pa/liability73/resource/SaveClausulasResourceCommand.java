package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.ClausulaResponse;
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
					"mvc.command.name=/RC/saveClausulasA" 
				},
		service = MVCResourceCommand.class)

public class SaveClausulasResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3 _CMServicesP3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		String clausulasA = ParamUtil.getString(resourceRequest, "clausulasArr");
		String infoCotiResponse = ParamUtil.getString(resourceRequest, "infoCotiResponse");
		JsonArray p_clausulas = gson.fromJson(clausulasA, JsonArray.class);

		UbicacionesResponse resUbicaciones = getInfoUbicaciones(infoCotiResponse);
		
//		_CMServicesP3.saveClausulasAdicionales(p_cotizacion, p_version, p_usuario, p_pantalla, p_clausulas);
		ClausulaResponse response = _CMServicesP3.saveClausulasAdicionales(resUbicaciones.getCotizacion(), resUbicaciones.getVersion(), user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation, p_clausulas);
		
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
