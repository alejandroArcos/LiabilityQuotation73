package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CpResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso2;
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
					"mvc.command.name=/cotizadores/paso2/getCP" 
				},
		service = MVCResourceCommand.class)

public class GetCpResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso2 _CMServicesP2;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		PrintWriter writer = resourceResponse.getWriter();
		String cp = ParamUtil.getString(resourceRequest, "cp");
//		String pantalla = ParamUtil.getString(resourceRequest, "pantalla");
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		String p_usuario = user.getScreenName();
		CpResponse cpResponse = _CMServicesP2.getCP(cp, p_usuario, LiabilityQuotation73PortletKeys.LiabilityQuotation);
		String responseString = gson.toJson(cpResponse);
		writer.write(responseString);
	}
}
