package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CotizadorDataResponse;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		 property = {
		 "javax.portlet.name="+ LiabilityQuotation73PortletKeys.LiabilityQuotation,
		 "mvc.command.name=/RC/backP1"
		 },
		 service = MVCResourceCommand.class
		 )

public class GetCotizadorDataResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso1 _CMPaso1;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		String usuario = user.getScreenName();
		String pantalla = LiabilityQuotation73PortletKeys.LiabilityQuotation;
		
		String infoCotStr = ParamUtil.getString(resourceRequest, "infoCot");
		InfoCotizacion infoCoti = gson.fromJson(infoCotStr, InfoCotizacion.class);
		
//		_CMPaso1.getCotizadorData(folio, cotizacion, version, usuario, pantalla);
		CotizadorDataResponse response = _CMPaso1.getCotizadorData(infoCoti.getFolio(), infoCoti.getCotizacion(), infoCoti.getVersion(), usuario, pantalla);
		
		String jsonStringResponse = gson.toJson(response);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonStringResponse);
	}
}
