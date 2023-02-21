package com.tokio.pa.liability73.resource;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Enum.TipoCotizacion;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
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
		"mvc.command.name=/cotizadores/paso3/generaInfoCoaseguro"
	},
	service = MVCResourceCommand.class
)

public class GeneraInfoCoaseguroResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorPaso3 _CMSP3;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		int p_cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int p_version = ParamUtil.getInteger(resourceRequest, "version");
		int p_folio = ParamUtil.getInteger(resourceRequest, "folio");
		int p_coaseguro = ParamUtil.getInteger(resourceRequest, "coaseguro");
		String p_poliza = ParamUtil.getString(resourceRequest, "poliza");
		String p_pantalla = ParamUtil.getString(resourceRequest, "pantalla");
		String modo = ParamUtil.getString(resourceRequest, "modo");
		
		InfoCotizacion infCotizacion = new InfoCotizacion();
		infCotizacion.setCotizacion(p_cotizacion);
		infCotizacion.setVersion(p_version);
		infCotizacion.setFolio(p_folio);
		infCotizacion.setPantalla(p_pantalla);
		infCotizacion.setTipoCotizacion(TipoCotizacion.RC);
		if(modo.equals("CONSULTA") || modo.contentEquals("CONSULTA_VOBO_REASEGURO")) {
			infCotizacion.setModo(ModoCotizacion.COASEGURO_CONSULTA);
		}else {
			infCotizacion.setModo(ModoCotizacion.COASEGURO);
		}
		infCotizacion.setPoliza(p_poliza);
		infCotizacion.setRc(1);
		infCotizacion.setTipoCoaseguro(p_coaseguro);
		
		String responseString = CotizadorModularUtil.encodeURL(infCotizacion);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(responseString);
		
		
	}

}
