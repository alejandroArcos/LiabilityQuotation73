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
					"mvc.command.name=/RC/ContinuarJK" 
				},
		service = MVCResourceCommand.class)

public class ContinuarJKResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3 _CMServicesP3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		String infoCotiResponse = ParamUtil.getString(resourceRequest, "infoCotiResponse");
		
		UbicacionesResponse resUbicaciones = getInfoUbicaciones(infoCotiResponse);
		
		SimpleResponse response = _CMServicesP3.continuarJK(resUbicaciones.getCotizacion(), resUbicaciones.getVersion(), user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
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
