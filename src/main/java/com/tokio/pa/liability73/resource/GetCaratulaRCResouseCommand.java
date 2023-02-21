package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CaratulaResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3RC;
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
					"mvc.command.name=/RC/paso3/getCaratulaURL" 
				},
		service = MVCResourceCommand.class)

public class GetCaratulaRCResouseCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3RC _ServicePaso3RC;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		String version = ParamUtil.getString(resourceRequest, "version");

        CaratulaResponse response = _ServicePaso3RC.getCaratulaRC(cotizacion, version, user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
		PrintWriter writer = resourceResponse.getWriter();
		String responseString = gson.toJson(response);
		writer.write(responseString);
	}
}
