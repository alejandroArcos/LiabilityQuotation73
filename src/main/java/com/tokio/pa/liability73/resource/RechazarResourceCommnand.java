package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.SimpleResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
					"mvc.command.name=/RC/rechazarCotizacion" 
				},
		service = MVCResourceCommand.class)

public class RechazarResourceCommnand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3 _ServicePaso3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		String cotizacion = ParamUtil.getString(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		int motivoRechazo = ParamUtil.getInteger(resourceRequest, "motivoRechazo");
		int boton = ParamUtil.getInteger(resourceRequest, "boton");
		String comentario = ParamUtil.getString(resourceRequest, "motivo");
		String usuario = themeDisplay.getUser().getScreenName();
		
//		_ServicePaso3.rechazaCotizacion(cotizacion, version, motivo, comentario, usuario, pantalla, perfil, boton);
		SimpleResponse simpleResponse = _ServicePaso3.rechazaCotizacion(cotizacion, version, motivoRechazo, comentario, usuario, LiabilityQuotation73PortletKeys.LiabilityQuotation, idPerfilUser, boton);
		Gson gson = new Gson();
		String stringJson = gson.toJson(simpleResponse);
		resourceResponse.getWriter().write(stringJson);
	}
}