package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CuotaFinalResponse;
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
		        "javax.portlet.name="+ LiabilityQuotation73PortletKeys.LiabilityQuotation,
		        "mvc.command.name=/cotizadores/paso3/getCuotaFinal"
	    },
	    service = MVCResourceCommand.class
	)

public class GetCuotaFinalResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorPaso3 _ServicePaso3;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		String pantalla = ParamUtil.getString(resourceRequest, "pantalla");
		String lista = ParamUtil.getString(resourceRequest, "lista");
		String usuario = "";
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		usuario = user.getScreenName();
		
		Gson gson = new Gson();
		
		JsonArray p_lista = gson.fromJson(lista, JsonArray.class);
		
		CuotaFinalResponse response = _ServicePaso3.getCuotaFinal(cotizacion, version, idPerfilUser, p_lista, usuario, pantalla);
		
		
		String jsonString = gson.toJson(response);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
	}

}
