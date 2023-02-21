package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.ValoresSubgiroP1_1;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1Empresarial;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
"mvc.command.name=/cotizadores/paso1/gradoRiesgo" }, service = MVCResourceCommand.class)

public class gradoRiesgoResourceCommand extends BaseMVCResourceCommand{

	@Reference
	CotizadorPaso1Empresarial _CMServicesP1Empresarial;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {

		try {
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			String usuario = user.getScreenName();
			int idPerfil = (int) originalRequest.getSession().getAttribute("idPerfil");
			
			int idSubGiro = ParamUtil.getInteger(resourceRequest, "subGiro");
			String codSubgiro = ParamUtil.getString(resourceRequest, "codigo");
			String pantalla = ParamUtil.getString(resourceRequest, "pantalla");
		
		
			ValoresSubgiroP1_1 valoresSubgiro = _CMServicesP1Empresarial.getValoreSubgiroPaso1_1(idPerfil, idSubGiro, codSubgiro, usuario, pantalla);
			
			JsonObject evento = new JsonObject();
	
			evento.addProperty("incendio", valoresSubgiro.getP_GRIncendio());
			evento.addProperty("rc", valoresSubgiro.getP_GRRC());
			evento.addProperty("productos", valoresSubgiro.getP_GRRCProducto());
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(evento);
			PrintWriter writer = resourceResponse.getWriter();
			writer.write(jsonString);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
