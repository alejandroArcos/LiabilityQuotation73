package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1RC;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;
import java.util.Comparator;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		 property = {
		 "javax.portlet.name="+ LiabilityQuotation73PortletKeys.LiabilityQuotation,
		 "mvc.command.name=/CotizadorRC/getGiroRC"
		 },
		 service = MVCResourceCommand.class
		 )

public class GetGiroResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso1RC _CMPaso1RC;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		int p_tipoPoliza = ParamUtil.getInteger(resourceRequest, "tipoCoti");
		
		ListaRegistro listaGiros = fGetCatGiro(
				CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
				p_tipoPoliza, user.getScreenName(),
				LiabilityQuotation73PortletKeys.LiabilityQuotation, resourceRequest);
		
		String jsonString = gson.toJson(listaGiros);
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
		
	}
	
	private ListaRegistro fGetCatGiro(int p_rownum, String p_tiptransaccion, String p_codigo,
			int p_activo, int p_tipoPoliza, String p_usuario, String p_pantalla, ResourceRequest resourceRequest) {
		try {
			ListaRegistro lr = _CMPaso1RC.getCatalogoGiroRC(p_rownum, p_tiptransaccion, p_codigo, p_activo,
					p_tipoPoliza, p_usuario, p_pantalla);

			lr.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return lr;
		} catch (Exception e) {
			System.err.print("----------------- error en traer los catalogos");
			e.printStackTrace();
			SessionErrors.add(resourceRequest, "errorConocido");
			resourceRequest.setAttribute("errorMsg", "Error en catalogos");
			SessionMessages.add(resourceRequest, PortalUtil.getPortletId(resourceRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			return null;
		}
	}
}
