package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.InfoAuxPaso1;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1RC;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.beans.DatosGenerales;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		 property = {
		 "javax.portlet.name="+ LiabilityQuotation73PortletKeys.LiabilityQuotation,
		 "mvc.command.name=/CotizadorRC/GuardaPaso1"
		 },
		 service = MVCResourceCommand.class
		 )

public class GuardaPaso1ResourseCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorPaso1RC _CMPaso1RC;

	InfoCotizacion infCot;
	int idPerfilUser;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws Exception {
		Gson gson = new Gson();
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		String datos = ParamUtil.getString(resourceRequest, "datos");
		String infoCot = ParamUtil.getString(resourceRequest, "infoCot");
		
		DatosGenerales dg = gson.fromJson(datos, DatosGenerales.class);
		infCot = gson.fromJson(infoCot, InfoCotizacion.class);
		
		System.out.println("DatosGenerales:" +dg);
		System.out.println("InfoCotizacion:" + infCot);
		
		switch (infCot.getModo()) {
		case CONSULTA:
			infCot.setModo(ModoCotizacion.EDICION);
			break;
			
		case EDICION_JAPONES:
			infCot.setModo(ModoCotizacion.EDICION);
			
		default:
			break;
		}
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		String usuario = user.getScreenName();
//		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		UbicacionesResponse abc = guardaPaso1RC(dg, usuario);
		generaVarSesion(abc, dg, resourceRequest);
		
		String jsonString = gson.toJson(abc);
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
		
	}
	
	private UbicacionesResponse guardaPaso1RC(DatosGenerales dg, String usuario) {
		try {
			
			int modo = (infCot.getModo().getModoCotizacion() >= 3) ? 1
					: infCot.getModo().getModoCotizacion();
			
			UbicacionesResponse abc = _CMPaso1RC.guardarCotizacionRC(dg.getP_tipoComercio(), dg.getP_tipoMov(), dg.getP_moneda(), dg.getP_fecInicio(),
					dg.getP_fecFin(), dg.getP_agente(), dg.getP_formaPago(), dg.getP_giro(), dg.getP_subGiro(), dg.getP_tipoCambio(),
					dg.getP_canalNegocio(), dg.getP_tipoCao(), dg.getP_gradoRiesgo(), dg.getP_esquemaAseguramiento(), dg.getP_fechaConvencional(),
					dg.getIdPersona(), dg.getTipoPer(), dg.getExtranjero(), dg.getRfc(), dg.getNombreCompleto(), dg.getNombre(), dg.getAppPaterno(),
					dg.getAppMaterno(), dg.getIdDenominacion(), dg.getCodigo(), (int)infCot.getCotizacion(), infCot.getVersion(), modo, ""+infCot.getFolio(),
					usuario, LiabilityQuotation73PortletKeys.LiabilityQuotation, idPerfilUser, dg.getSector());
			return abc;
			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error al guardar Info Paso 1");
			return null;
		}
	}
	
	private void generaVarSesion(UbicacionesResponse ubicacionResponse, DatosGenerales dg,
			ResourceRequest resourceRequest) {
		final PortletSession psession = resourceRequest.getPortletSession();

		String ubicacionString = CotizadorModularUtil.objtoJson(ubicacionResponse);
		String nombreUbicaciones = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
				+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
				+ "_UBICACIONRESPONSE";

		String nombreDatosGenerales = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
				+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
				+ "_DATOSP1";

//		String nombreSubGiroRiesgo = "LIFERAY_SHARED_F=" + ubicacionResponse.getFolio() + "_C="
//				+ ubicacionResponse.getCotizacion() + "_V=" + ubicacionResponse.getVersion()
//				+ "_SUBGIRORIESGO";

		InfoAuxPaso1 p1 = new InfoAuxPaso1();
		p1.setMonedaSeleccionada(dg.getMoneda() + "");
//		p1.setSubgiroRiesgo(dg.getP_permisoSubgiro() > 0);
//		p1.setSubEstado(dg.getSubEstado());
		String paso1 = CotizadorModularUtil.objtoJson(p1);

		psession.setAttribute(nombreUbicaciones, ubicacionString, PortletSession.APPLICATION_SCOPE);
		psession.setAttribute(nombreDatosGenerales, paso1, PortletSession.APPLICATION_SCOPE);
//		psession.setAttribute(nombreSubGiroRiesgo, dg.getP_permisoSubgiro(), PortletSession.APPLICATION_SCOPE);

		System.out.println("info paso 1 :" + paso1);
	}
}
