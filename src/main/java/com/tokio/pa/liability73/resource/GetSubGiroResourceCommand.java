package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;
import java.util.Comparator;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = { "javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
"mvc.command.name=/cotizadores/paso1/getSubGiro" }, service = MVCResourceCommand.class)

public class GetSubGiroResourceCommand extends BaseMVCResourceCommand {
	@Reference
	CotizadorPaso1 _CMServicesP1;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {

		int giroSeleccionado = ParamUtil.getInteger(resourceRequest, "giro");
		String pantalla = ParamUtil.getString(resourceRequest, "pantalla");

		if (giroSeleccionado > 0) {

			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			String p_usuario = user.getScreenName();

			try {
				 ListaRegistro catalogo = _CMServicesP1.wsCatalogosDetallePadre(
						 CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
						 CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
						 giroSeleccionado, 
						 CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
						 p_usuario, pantalla);
				 
				 catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
		
				Gson gson = new Gson();
				String jsonString = gson.toJson(catalogo);
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(jsonString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PrintWriter writer = resourceResponse.getWriter();
				writer.write("{\"codigo\" : \"3\", \"error\" : \"sin informacion\" }");
			}
		} else {
			PrintWriter writer = resourceResponse.getWriter();
			writer.write("{\"codigo\" : \"3\", \"error\" : \"sin informacion\" }");
		}

	}
}
