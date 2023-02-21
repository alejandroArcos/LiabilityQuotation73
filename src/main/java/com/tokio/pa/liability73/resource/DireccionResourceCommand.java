package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.cotizador.jsonformservice.JsonFormService;
import com.tokio.pa.cotizadorModularServices.Bean.EmisionDataResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso4;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	    immediate = true,
	    property = {
		        "javax.portlet.name="+ LiabilityQuotation73PortletKeys.LiabilityQuotation,
		        "mvc.command.name=/getDireccion"
	    },
	    service = MVCResourceCommand.class
	)

public class DireccionResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso4 _ServicePaso4;
	@Reference
	JsonFormService _JsonFormService;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		String pantalla = LiabilityQuotation73PortletKeys.LiabilityQuotation;
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		String usuario = user.getScreenName();
		int version = ParamUtil.getInteger(resourceRequest, "version");
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int ubicacion = ParamUtil.getInteger(resourceRequest, "ubicacion");
		
		EmisionDataResponse ubicacionResp = fGetDireccion(cotizacion, version, ubicacion, usuario, pantalla);
		
		PrintWriter writer = resourceResponse.getWriter();
		if (ubicacionResp.getCode() == 0){
			Gson gson = new Gson();
			String jsonString = gson.toJson(ubicacionResp);
			writer.write(jsonString);
		}else{
			String jsonString = "{\"code\" : \" " + ubicacionResp.getCode() + "\", \"msg\" : \"" + ubicacionResp.getMsg()  + "\" }";
			writer.write(jsonString);
		}
	}
	
	private EmisionDataResponse fGetDireccion (int cotizacion, int version, int ubicacion, String usuario,String pantalla){
		try {
			return _ServicePaso4.getDireccion(cotizacion, version, ubicacion, usuario, pantalla);
		} catch (Exception e) {
			return null;
		}
	}
}
