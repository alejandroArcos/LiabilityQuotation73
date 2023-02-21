package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.SimpleResponse;
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
					"mvc.command.name=/RC/EnviarCotizacion" 
				},
		service = MVCResourceCommand.class)

public class EnviarCotizacionResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3 _CMServicesP3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
//		String infoCotiResponse = ParamUtil.getString(resourceRequest, "infoCotiResponse");
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		
//		UbicacionesResponse resUbicaciones = getInfoUbicaciones(infoCotiResponse);
		
		SimpleResponse response = _CMServicesP3.enviarCotizacion( cotizacion , version, user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
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
