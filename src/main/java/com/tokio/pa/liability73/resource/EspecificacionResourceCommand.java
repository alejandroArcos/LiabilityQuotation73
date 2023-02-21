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
import com.tokio.pa.cotizadorModularServices.Bean.SlipResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3RC;
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
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
					"mvc.command.name=/RC/getEspecificacion" 
				},
		service = MVCResourceCommand.class
)

public class EspecificacionResourceCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso3RC _ServicePaso3RC;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		System.err.println("Entré getslipRC");
		String usuario = "";
		String pantalla = LiabilityQuotation73PortletKeys.LiabilityQuotation;

		String cotizacion = ParamUtil.getString(resourceRequest, "cotizacion");
		String folio = ParamUtil.getString(resourceRequest, "folio");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		
		try{
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			usuario = user.getScreenName();
		} catch(Exception e){
			SessionErrors.add(resourceRequest, "errorUsuario" );
			SessionMessages.add(resourceRequest, PortalUtil.getPortletId(resourceRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
		
		SlipResponse response = fGetSlip(cotizacion, version, usuario, pantalla);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(response);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
	}
	
	private SlipResponse fGetSlip(String cotizacion, int version, String usuario,
			String pantalla) {
		try {
			return _ServicePaso3RC.getEspecificacion(cotizacion, version, usuario, pantalla);
			/*return null;*/
		} catch (Exception e) {
			/* TODO Auto-generated catch block	*/
			return null;
		}
	}

}
