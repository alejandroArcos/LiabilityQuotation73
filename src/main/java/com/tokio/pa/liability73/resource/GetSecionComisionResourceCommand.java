package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.SimpleResponse;
import com.tokio.pa.cotizadorModularServices.Exception.CotizadorModularException;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
"mvc.command.name=/RC/getSeccionComisionUrl" }, service = MVCResourceCommand.class)

public class GetSecionComisionResourceCommand extends BaseMVCResourceCommand{
	
	@Reference
	CotizadorPaso3 _ServicePaso3;
	
	@Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
		try {
			double sc = ParamUtil.getDouble(resourceRequest, "seccomi");
			int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
			String version = ParamUtil.getString(resourceRequest, "version");
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			String p_usuario = user.getScreenName();

			SimpleResponse respuesta = _ServicePaso3.getSecionComision(cotizacion, version, sc, p_usuario, LiabilityQuotation73PortletKeys.LiabilityQuotation);

			Gson gson = new Gson();
			String jsonString = gson.toJson(respuesta);
			PrintWriter writer = resourceResponse.getWriter();
			writer.write(jsonString);

		} catch (CotizadorModularException e) {
			// TODO Auto-generated catch block
			PrintWriter writer = resourceResponse.getWriter();
			String jsonString = "{\"code\" : \"5\", \"msg\" : \"Error al consultar la información\" }";
			writer.write(jsonString);
		}
	}
}