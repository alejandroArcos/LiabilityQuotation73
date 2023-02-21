package com.tokio.pa.liability73.portlet;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CotizadorDataResponse;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.Paso1_1;
import com.tokio.pa.cotizadorModularServices.Bean.Persona;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Bean.SimpleResponse;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Enum.TipoCotizacion;
import com.tokio.pa.cotizadorModularServices.Exception.CotizadorModularException;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1RC;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.beans.DatosGenerales;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author urielfloresvaldovinos
 */
@Component(
		immediate = true,
		property = {
			"javax.portlet.version=3.0",
			"com.liferay.portlet.display-category=category.sample",
			"com.liferay.portlet.instanceable=true",
			"javax.portlet.display-name=LiabilityQuotationPortlet Portlet",
			"javax.portlet.init-param.template-path=/",
			"javax.portlet.init-param.view-template=/jsp/paso1.jsp",
			"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user",
			"com.liferay.portlet.private-session-attributes=false",
			"com.liferay.portlet.requires-namespaced-parameters=false",
			"com.liferay.portlet.private-request-attributes=false"
		},
		service = Portlet.class
	)


public class LiabilityQuotation73Portlet extends MVCPortlet {
	
	@Reference
	CotizadorGenerico _CMServicesGenerico;
	
	@Reference
	CotizadorPaso1RC _CMPaso1RC;
	
	@Reference
	CotizadorPaso1 _CMServicesP1;
	
	@Reference
	CotizadorPaso3 _ServicePaso3;
	
	InfoCotizacion infCotizacion;
	User user;
	int idPerfilUser;
	
	private static final Log _log = LogFactoryUtil.getLog(LiabilityQuotation73Portlet.class);
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {
		
		/*
		Enumeration<String> en = renderRequest.getParameterNames();
		while (en.hasMoreElements()) {
			Object objOri = en.nextElement();
			String param = (String) objOri;
			String value = renderRequest.getParameter(param);
			System.out.println("[ ---> " +param + " : " + value );
		}
		*/
		HttpServletRequest originalRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
		idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");

//		viewOnlySubcriptor( renderResponse );
		
		validaPantalla ( renderRequest, renderResponse );
		renderRequest.setAttribute("perfilSuscriptor", perfilSuscriptor());
		renderRequest.setAttribute("perfilJapones", perfilJapones());
		renderRequest.setAttribute("perfilSuscriptorJr", perfilSuscriptorJr());
		renderRequest.setAttribute("perfilSuscriptorMrSr", perfilSuscriptorSrMr());
		renderRequest.setAttribute("perfilSuscriptorMr", perfilSuscriptorMr());
		renderRequest.setAttribute("perfilAgenteEjecutivo", perfilAgenteEjecutivo() );
		renderRequest.setAttribute("idPerfilUser", idPerfilUser);
		
		
		
		super.render(renderRequest, renderResponse);
	}
	
	private void viewOnlySubcriptor( RenderResponse renderResponse ){
		if( !perfilSuscriptorGeneral() && (perfilAgente() != 1)) {
			String location = "/group/portal-agentes/";
			try {
				PortalUtil.getHttpServletResponse(renderResponse).sendRedirect(location );
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error al redireccionar");
			}
		}
	}
	
	private void validaPantalla ( RenderRequest renderRequest, RenderResponse renderResponse){
		
		Gson gson = new Gson();
//		getIdPerfil(renderRequest);
		System.err.println("setidPerfilUser: " + idPerfilUser);
		String datosGen = ParamUtil.getString(renderRequest, "auxDG");
		
		/*
		 * If para validar si es pantalla 1
		 * */
		if(Validator.isNull(datosGen)){
			llenaInfoCotizacion(renderRequest, renderResponse);
			cargaCatalogos(renderRequest);
			getTpoCambio(renderRequest);
//			generaFechas(renderRequest);
			validaPerfil(renderRequest);
			generaValoresPorDefecto(renderRequest);
			/*getInfoPaso1(renderRequest);*/

			String infoCot = CotizadorModularUtil.objtoJson(infCotizacion);
			
			System.err.println("infCotizacion: " + infCotizacion);
			
			renderRequest.setAttribute("retroactividad", diasRetroactividad());
			renderRequest.setAttribute("infCotizacionJson", infoCot);
			renderRequest.setAttribute("infCot", infCotizacion);
		}else{ /*Para pantalla 2, 3 y 4*/
			renderRequest.setAttribute("datosGen", datosGen);
			DatosGenerales dg = gson.fromJson(datosGen, DatosGenerales.class);
			
			switch (dg.getPaso()) {
			case 2:
				String responseP1 = ParamUtil.getString(renderRequest, "saveResponse");
				if( Validator.isNotNull(responseP1) ){
					System.err.println("responseP1: " + responseP1);
					UbicacionesResponse resUbicaciones = gson.fromJson(responseP1, UbicacionesResponse.class);					
					renderRequest.setAttribute("ubicacion", resUbicaciones);
				}else{
//					final PortletSession psession = renderRequest.getPortletSession();
//					
//					System.out.println("folio: "+ infCotizacion.getFolio());
//					System.out.println("cotizacion: "+ infCotizacion.getCotizacion());
//					System.out.println("version: "+ infCotizacion.getVersion());
//					
//					String auxNombre = "LIFERAY_SHARED_F=" + infCotizacion.getFolio() + "_C="
//							+ infCotizacion.getCotizacion() + "_V=" + infCotizacion.getVersion()
//							+ "_UBICACIONRESPONSE";
//
//					System.out.println("auxNombre" + auxNombre);
//					String ubicacion = (String) psession.getAttribute(auxNombre,
//							PortletSession.APPLICATION_SCOPE);
//
//					System.err.println("UBICACION");
//					System.err.println(ubicacion);
//
//					UbicacionesResponse ubi = gson.fromJson(ubicacion, UbicacionesResponse.class);
//					renderRequest.setAttribute("ubicacion", ubi);
				}
				
				
				break;

			default:
				break;
			}
			
			
		}
		
		
	}
	
	private void getIdPerfil( RenderRequest renderRequest ){
		try {
			HttpServletRequest originalRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
			idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.print("----------------- error en traer los catalogos");
			e.printStackTrace();
			SessionErrors.add(renderRequest, "errorConocido");
			renderRequest.setAttribute("errorMsg", "VOLVER A INICIAR SESIÓN");
			SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
	}
	
	private void getInfoPaso1(RenderRequest renderRequest){
		Gson gson = new Gson();
		String infoCotiP1Str = ParamUtil.getString(renderRequest, "infoCotiP1");
		
		if( Validator.isNotNull(infoCotiP1Str) ){
			CotizadorDataResponse infoCotiP1 = gson.fromJson(infoCotiP1Str, CotizadorDataResponse.class);
			
			try {
				ListaRegistro catalogo = _CMServicesP1.wsCatalogosDetallePadre(
						CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
						CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
						infoCotiP1.getDatosCotizacion().getGiro(), 
						CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
						user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
				
				catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
				renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.err.println("Error en subgiro");
			}
			
			
			System.err.println("infoCotiP1: "+ infoCotiP1.getDatosCotizacion());
			renderRequest.setAttribute("infoCotiP1", infoCotiP1.getDatosCotizacion());
			renderRequest.setAttribute("infoCliente", gson.toJson(infoCotiP1.getDatosCotizacion().getDatosCliente()));
		}else{
			System.err.println("Sin Información Paso 1");
			generaFechas(renderRequest);
			/****************GIROS*************************/
			ListaRegistro listaGiros = fGetCatGiro(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
					1, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
			
			renderRequest.setAttribute("listaGiros", listaGiros.getLista());
			/****************GIROS*************************/
		}
		
	}
	
	private void llenaInfoCotizacion(RenderRequest renderRequest, RenderResponse renderResponse) {
		Gson gson = new Gson();
		try {
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));

			user = (User) renderRequest.getAttribute(WebKeys.USER);
			idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");

			String inf = originalRequest.getParameter("infoCotizacion");
			String legal492 = originalRequest.getParameter("leg492");
			
			System.err.println("inf: " + inf);

			String nombreCotizador = "";
			if (Validator.isNotNull(inf)) {
				try {
					infCotizacion = CotizadorModularUtil.decodeURL(inf);					
				} catch (Exception e) {
					// TODO: handle exception
					infCotizacion = gson.fromJson(inf, InfoCotizacion.class);
					getInfoPaso1(renderRequest);
				}
				System.err.println("infCotizacion");
				System.err.println(infCotizacion);
				seleccionaModo(renderRequest, renderResponse);
			} else if (Validator.isNotNull(legal492)) {
				infCotizacion = generaCotLegal(renderRequest);
			} else {

				infCotizacion = new InfoCotizacion();
				generaFechas(renderRequest);
				/****************GIROS*************************/
				ListaRegistro listaGiros = fGetCatGiro(
						CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
						CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
						CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
						CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
						1, user.getScreenName(),
						LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
				
				renderRequest.setAttribute("listaGiros", listaGiros.getLista());
				/****************GIROS*************************/

//				infCotizacion.setVersion(1);
				infCotizacion.setTipoCotizacion(TipoCotizacion.RC);
					
				String btoa = originalRequest.getParameter("btoa");
				
				if(Validator.isNotNull(btoa)) {
				
					byte[] decodedBytes = Base64.getUrlDecoder().decode(btoa);		
					String decodeb64 = new String(decodedBytes);
					String idSolicitud = decodeb64.split(";")[0];
					
					infCotizacion.setSolicitud(idSolicitud);
					
					renderRequest.setAttribute("numeroSolicitud", true);
				}
			}
			
			infCotizacion.setPantalla(LiabilityQuotation73PortletKeys.LiabilityQuotation);
			
			
			renderRequest.setAttribute("tituloCotizador", nombreCotizador);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("------------------ llenaInfoCotizacion:");
			renderRequest.setAttribute("perfilMayorEjecutivo", false);
			e.printStackTrace();
		}
	}
	
	private InfoCotizacion generaCotLegal(RenderRequest renderRequest){
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
		
		InfoCotizacion in = new InfoCotizacion();
		
		String uri = originalRequest.getRequestURI();
		if (uri.toLowerCase().contains("familiar")) {
			in.setTipoCotizacion(TipoCotizacion.FAMILIAR);
			in.setFolio(Long.parseLong(originalRequest.getParameter("folioFamiliar")));
			in.setCotizacion(Long.parseLong(originalRequest.getParameter("cotizacionFamiliar")));
			in.setVersion(Integer.parseInt(originalRequest.getParameter("versionFamiliar")));
		} else if (uri.toLowerCase().contains("empresarial")) {
			in.setTipoCotizacion(TipoCotizacion.EMPRESARIAL);
			in.setFolio(Long.parseLong(originalRequest.getParameter("folioEmpresarial")));
			in.setCotizacion(Long.parseLong(originalRequest.getParameter("cotizacionEmpresarial")));
			in.setVersion(Integer.parseInt(originalRequest.getParameter("versionEmpresarial")));
		} 
		
		in.setModo(ModoCotizacion.FACTURA_492);
		
//		System.out.println("-----------");
//		System.out.println(in.toString());
		return in;
		
	}
	
	private void cargaCatalogos(RenderRequest renderRequest) {
		try {
			final PortletSession psession = renderRequest.getPortletSession();
			@SuppressWarnings("unchecked")
			List<Persona> listaAgentes = (List<Persona>) psession.getAttribute("listaAgentes",
					PortletSession.APPLICATION_SCOPE);
			verificaListaAgentes(renderRequest, listaAgentes);
			
			
			
			
			/*****************************************************/
			ListaRegistro listaMovimiento = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_MOVIMIENTO,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);

			ListaRegistro listaCatMoneda = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_MONEDA,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);

			ListaRegistro listaCatFormaPago = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_FORMA_PAGO,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);

			ListaRegistro listaCatDenominacion = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_DENOMINACION,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);

//			ListaRegistro listaGiros = fGetCatalogos(
//					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
//					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
//					CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
//					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
//					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);

//			ListaRegistro listaCanalNegocio = fGetCatalogos(
//					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
//					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
//					CotizadorModularServiceKey.LIST_CAT_CAN_NEG_TRANS,
//					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
//					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
//
//			ListaRegistro listaCanalNegocioJ = fGetCatalogos(
//					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
//					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
//					CotizadorModularServiceKey.LIST_CAT_CAN_NEG_TRANS_J,
//					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
//					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);

			ListaRegistro listaCoaseguro = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_TOPO_COASEGURO,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
			
			ListaRegistro listaCatGrdoRiesRC = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_GRDO_RIES_RC,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
			
			ListaRegistro listaCatEsquemaRC = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_ESQUEMA_RC,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
			
			ListaRegistro listaCatSector = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_SECTOR,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
			
			ListaRegistro listaCatFechaConvencional = fGetCatalogos(
					CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
					CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
					CotizadorModularServiceKey.LIST_CAT_FECHA_CONVENCIONAL,
					CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
					LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
			
			renderRequest.setAttribute("listaMovimiento", listaMovimiento.getLista());
			renderRequest.setAttribute("listaCatMoneda", listaCatMoneda.getLista());
			renderRequest.setAttribute("listaAgentes", listaAgentes);
			renderRequest.setAttribute("listaCatDenominacion", listaCatDenominacion.getLista());
			renderRequest.setAttribute("listaCatFormaPago", listaCatFormaPago.getLista());
			renderRequest.setAttribute("listaCoaseguro", listaCoaseguro.getLista());
			renderRequest.setAttribute("listaCatGrdoRiesRC", listaCatGrdoRiesRC.getLista());
			renderRequest.setAttribute("listaCatEsquemaRC", listaCatEsquemaRC.getLista());
			renderRequest.setAttribute("listaCatSector", listaCatSector.getLista());
			renderRequest.setAttribute("listaCatFechaConv",CotizadorModularUtil.objtoJson(listaCatFechaConvencional));
			
//			if( idPerfilUser > 20 ){
//				renderRequest.setAttribute("listaCanalNegocio", listaCanalNegocioJ.getLista());								
//			}else{
//				renderRequest.setAttribute("listaCanalNegocio", listaCanalNegocio.getLista());				
//			}
			/*****************************************************/
			
			renderRequest.setAttribute("listaAgentes", listaAgentes);
		}
		catch (Exception e) {
			// TODO: handle exception
			System.err.println("------------------ cargaCatalogos:");
			e.printStackTrace();
		}
	}
	
	private void verificaListaAgentes(RenderRequest renderRequest, List<Persona> listaAgentes) {
		if (Validator.isNull(listaAgentes)) {
			SessionErrors.add(renderRequest, "errorConocido");
			renderRequest.setAttribute("errorMsg", "Error al cargar su información cierre sesion");
			SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
	}
	
	private ListaRegistro fGetCatalogos(int p_rownum, String p_tiptransaccion, String p_codigo,
			int p_activo, String p_usuario, String p_pantalla, RenderRequest renderRequest) {
		try {
			ListaRegistro lr = _CMServicesGenerico.getCatalogo(p_rownum, p_tiptransaccion, p_codigo,
					p_activo, p_usuario, p_pantalla);

			lr.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return lr;
		} catch (Exception e) {
			System.err.print("----------------- error en traer los catalogos");
			e.printStackTrace();
			SessionErrors.add(renderRequest, "errorConocido");
			renderRequest.setAttribute("errorMsg", "Error en catalogos");
			SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			return null;
		}
	}
	
	private ListaRegistro fGetCatGiro(int p_rownum, String p_tiptransaccion, String p_codigo,
			int p_activo, int p_tipoPoliza, String p_usuario, String p_pantalla, RenderRequest renderRequest) {
		try {
			ListaRegistro lr = _CMPaso1RC.getCatalogoGiroRC(p_rownum, p_tiptransaccion, p_codigo, p_activo,
					p_tipoPoliza, p_usuario, p_pantalla);

			lr.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return lr;
		} catch (Exception e) {
			System.err.print("----------------- error en traer los catalogos");
			e.printStackTrace();
			SessionErrors.add(renderRequest, "errorConocido");
			renderRequest.setAttribute("errorMsg", "Error en catalogos");
			SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			return null;
		}
	}
	
	private void getTpoCambio(RenderRequest renderRequest){
		try {
			double tpoCambio =  _ServicePaso3.getTipoCambio().getTipoCambio();
			
			NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
			String auxFormato = formatoImporte.format(tpoCambio);
//			System.out.println("con b: " +  auxFormato);
			
			renderRequest.setAttribute("tpoCambio",  auxFormato);
		} catch (CotizadorModularException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generaFechas(RenderRequest renderRequest) {
		LocalDate fechaHoy = LocalDate.now();
		LocalDate fechaMasAnio = LocalDate.now().plusYears(1);

		renderRequest.setAttribute("fechaHoy", fechaHoy);
		renderRequest.setAttribute("fechaMasAnio", fechaMasAnio);
	}
	
	private void validaPerfil(RenderRequest renderRequest) {
		boolean flagPagoSusJ = true;
		String auxDisabledPerfil = "";
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
		
		user = (User) renderRequest.getAttribute(WebKeys.USER);
		idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		if( idPerfilUser < LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORJR){
			flagPagoSusJ = false;
			auxDisabledPerfil = "disabled";
			renderRequest.setAttribute("dNonePerfil", "d-none");
		}
		if( idPerfilUser ==  LiabilityQuotation73PortletKeys.PERFIL_EJECUTIVOJAPONES){			
			renderRequest.setAttribute("auxDisabledPerfil", "disabled");			
		}else{
			renderRequest.setAttribute("auxDisabledPerfil", auxDisabledPerfil);			
		}
		renderRequest.setAttribute("flagPagoSusJ", flagPagoSusJ);
	}
	
	private void generaValoresPorDefecto(RenderRequest renderRequest){
		Paso1_1 vxd1_1 = new Paso1_1();
		
		vxd1_1.setP_esquemaRC(LiabilityQuotation73PortletKeys.VAL_DEFAULT_ESQ_ASEGURA);
		
		renderRequest.setAttribute("P1_1", vxd1_1);
		
	}
	
	private int diasRetroactividad() {
		switch (idPerfilUser) {
		
			case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORJR:
				return LiabilityQuotation73PortletKeys.DIAS_RETROACTIVOS_SUSCRIPTORJR;
			case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORSR:
				return LiabilityQuotation73PortletKeys.DIAS_RETROACTIVOS_SUSCRIPTORSR;
			case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORMR:
				return LiabilityQuotation73PortletKeys.DIAS_RETROACTIVOS_SUSCRIPTORMR;
			case LiabilityQuotation73PortletKeys.PERFIL_JAPONES:
				return LiabilityQuotation73PortletKeys.DIAS_RETROACTIVOS_SUSCRIPTORJR;
			default: return 0;
		}
	}
	
	private void seleccionaModo(RenderRequest renderRequest, RenderResponse renderResponse) {
		CotizadorDataResponse respuesta = new CotizadorDataResponse();
		respuesta.setCode(5);
		respuesta.setMsg("Error al cargar su información");
		
		try {
			
			final PortletSession psession = renderRequest.getPortletSession();
			@SuppressWarnings("unchecked")
			List<Persona> listaAgentes = (List<Persona>) psession.getAttribute("listaAgentes",
					PortletSession.APPLICATION_SCOPE);
			verificaListaAgentes(renderRequest, listaAgentes);
			
			String codigoAgente = "";

			ListaRegistro listaCatCanalNegocio = null;
			ListaRegistro listaGiros = null;
			ListaRegistro catalogo = null;
			
			System.err.println("Pantalla: " + LiabilityQuotation73PortletKeys.LiabilityQuotation);
			System.err.println("infCotizacion.getModo(): " + infCotizacion.getModo());
			switch (infCotizacion.getModo()) {
				case EDICION:
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);

					codigoAgente = getCodeAgente (respuesta.getDatosCotizacion().getAgente() , listaAgentes);
					
					listaCatCanalNegocio = _CMServicesP1.getCanalNegocio(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_CAN_NEG,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							codigoAgente,
							user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);
					/****************GIROS*************************/
					listaGiros = fGetCatGiro(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							respuesta.getDatosCotizacion().getTipoComercio(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
					
					renderRequest.setAttribute("listaGiros", listaGiros.getLista());
					/****************GIROS*************************/
					catalogo = _CMServicesP1.wsCatalogosDetallePadre(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							respuesta.getDatosCotizacion().getGiro(), 
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
					renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
					
					renderRequest.setAttribute("listaCatCanalNegocio", listaCatCanalNegocio.getLista());
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					infCotizacion.setIdEstado(respuesta.getIdEstado());
					
					break;
				case COPIA:
					respuesta = _CMServicesP1.copyCotizadorData(infCotizacion.getFolio() + "",
							Integer.parseInt(infCotizacion.getCotizacion() + ""),
							infCotizacion.getVersion(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);

					infCotizacion
							.setFolio(Long.parseLong(respuesta.getDatosCotizacion().getFolio()));
					infCotizacion.setCotizacion(respuesta.getDatosCotizacion().getCotizacion());
					infCotizacion.setVersion(respuesta.getDatosCotizacion().getVersion());
					
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					codigoAgente = getCodeAgente (respuesta.getDatosCotizacion().getAgente() , listaAgentes);
					
					listaCatCanalNegocio = _CMServicesP1.getCanalNegocio(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_CAN_NEG,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							codigoAgente,
							user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);
					/****************GIROS*************************/
					listaGiros = fGetCatGiro(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							respuesta.getDatosCotizacion().getTipoComercio(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
					
					renderRequest.setAttribute("listaGiros", listaGiros.getLista());
					/****************GIROS*************************/
					catalogo = _CMServicesP1.wsCatalogosDetallePadre(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							respuesta.getDatosCotizacion().getGiro(), 
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
					renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
					
					renderRequest.setAttribute("listaCatCanalNegocio", listaCatCanalNegocio.getLista());
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case ALTA_ENDOSO:
					SimpleResponse infEndo = _CMServicesP1.GuardarCotizacionEndoso(
							infCotizacion.getCotizacion() + "", infCotizacion.getVersion() + "",
							LiabilityQuotation73PortletKeys.LiabilityQuotation, user.getScreenName());

					infCotizacion.setFolio(Long.parseLong(infEndo.getFolio()));
					infCotizacion.setCotizacion(infEndo.getCotizacion());
					infCotizacion.setVersion(infEndo.getVersion());

					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);

					renderRequest.setAttribute("perfilMayorEjecutivo", false);
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case EDITAR_ALTA_ENDOSO:
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);

					renderRequest.setAttribute("perfilMayorEjecutivo", false);
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case BAJA_ENDOSO:
					
					SimpleResponse simpleRespuesta = _CMServicesGenerico.guardarCotizacionEndosoBaja(infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), null, 1, 0, 0,
							user.getScreenName() , LiabilityQuotation73PortletKeys.LiabilityQuotation, 0, 0);
					
					infCotizacion.setFolio(Long.parseLong(simpleRespuesta.getFolio()));
					infCotizacion.setCotizacion(simpleRespuesta.getCotizacion());
					infCotizacion.setVersion(simpleRespuesta.getVersion());					

					respuesta = _CMServicesP1.getCotizadorData(Long.parseLong(simpleRespuesta.getFolio()),
							simpleRespuesta.getCotizacion(), simpleRespuesta.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					renderRequest.setAttribute("perfilMayorEjecutivo", false);
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case EDITAR_BAJA_ENDOSO:
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					infCotizacion.setModo(ModoCotizacion.BAJA_ENDOSO);
					renderRequest.setAttribute("perfilMayorEjecutivo", false);
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case AUX_PASO4:

					break;
				case NUEVA:
					
					HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
				
					String btoa = originalRequest.getParameter("btoa");
					
					if(Validator.isNotNull(btoa)) {
					
						byte[] decodedBytes = Base64.getUrlDecoder().decode(btoa);		
						String decodeb64 = new String(decodedBytes);
						String idSolicitud = decodeb64.split(";")[0];
						
						infCotizacion.setSolicitud(idSolicitud);
						
						renderRequest.setAttribute("numeroSolicitud", true);
					}
					
					break;
				case CONSULTA:
				case CONSULTAR_REVISION:
				case CONSULTA_VOBO_REASEGURO:
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					
					/******************************************/
					codigoAgente = getCodeAgente (respuesta.getDatosCotizacion().getAgente() , listaAgentes);
					
					listaCatCanalNegocio = _CMServicesP1.getCanalNegocio(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_CAN_NEG,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							codigoAgente,
							user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);
					/****************GIROS*************************/
					listaGiros = fGetCatGiro(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							respuesta.getDatosCotizacion().getTipoComercio(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
					
					renderRequest.setAttribute("listaGiros", listaGiros.getLista());
					/****************GIROS*************************/
					catalogo = _CMServicesP1.wsCatalogosDetallePadre(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							respuesta.getDatosCotizacion().getGiro(), 
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
					renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
					
					renderRequest.setAttribute("listaCatCanalNegocio", listaCatCanalNegocio.getLista());
					
					/******************************************/
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;	
				case FACTURA_492 :
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					codigoAgente = getCodeAgente (respuesta.getDatosCotizacion().getAgente() , listaAgentes);
					
					listaCatCanalNegocio = _CMServicesP1.getCanalNegocio(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_CAN_NEG,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							codigoAgente,
							user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);
					/****************GIROS*************************/
					listaGiros = fGetCatGiro(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							respuesta.getDatosCotizacion().getTipoComercio(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
					
					renderRequest.setAttribute("listaGiros", listaGiros.getLista());
					/****************GIROS*************************/
					catalogo = _CMServicesP1.wsCatalogosDetallePadre(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							respuesta.getDatosCotizacion().getGiro(), 
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
					renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
					
					renderRequest.setAttribute("listaCatCanalNegocio", listaCatCanalNegocio.getLista());
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case EDICION_JAPONES:
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					codigoAgente = getCodeAgente (respuesta.getDatosCotizacion().getAgente() , listaAgentes);
					
					listaCatCanalNegocio = _CMServicesP1.getCanalNegocio(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_CAN_NEG,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							codigoAgente,
							user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					renderRequest.setAttribute("listaCatCanalNegocio", listaCatCanalNegocio.getLista());
					
					/****************GIROS*************************/
					listaGiros = fGetCatGiro(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							respuesta.getDatosCotizacion().getTipoComercio(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
					
					renderRequest.setAttribute("listaGiros", listaGiros.getLista());
					/****************GIROS*************************/
					catalogo = _CMServicesP1.wsCatalogosDetallePadre(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							respuesta.getDatosCotizacion().getGiro(), 
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
					renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					
					break;
				case COASEGURO:
				case REASEGURO:
				case VOBO_REASEGURO:
				case REASEGURO_CONSULTA:
				case COASEGURO_CONSULTA:
					respuesta = _CMServicesP1.getCotizadorData(infCotizacion.getFolio(),
							infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);

					codigoAgente = getCodeAgente (respuesta.getDatosCotizacion().getAgente() , listaAgentes);
					
					listaCatCanalNegocio = _CMServicesP1.getCanalNegocio(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_CAN_NEG,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							codigoAgente,
							user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation);
					/****************GIROS*************************/
					listaGiros = fGetCatGiro(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_GIRO_EMP,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							respuesta.getDatosCotizacion().getTipoComercio(), user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderRequest);
					
					renderRequest.setAttribute("listaGiros", listaGiros.getLista());
					/****************GIROS*************************/
					catalogo = _CMServicesP1.wsCatalogosDetallePadre(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							respuesta.getDatosCotizacion().getGiro(), 
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS,
							user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					
					catalogo.getLista().sort(Comparator.comparing(Registro::getDescripcion));
					renderRequest.setAttribute("listaSubGiro", catalogo.getLista());
					
					renderRequest.setAttribute("listaCatCanalNegocio", listaCatCanalNegocio.getLista());
					
					validaFolioUsuario((int)infCotizacion.getCotizacion(),
							infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
							LiabilityQuotation73PortletKeys.LiabilityQuotation, renderResponse);
					break;
				default:
					break;

			}
			
//			if(infCotizacion.getModo() != ModoCotizacion.NUEVA) {
//				if (respuesta.getDatosCotizacion().getDatosCliente().getTipoPer() == 218) {
//					infCotizacion.setTipoPersona(TipoPersona.MORAL);
//				} else {
//					infCotizacion.setTipoPersona(TipoPersona.FISICA);
//				}
//			}
			
			
			if (infCotizacion.getModo() != ModoCotizacion.NUEVA) {

				if (respuesta.getCode() > 0) {
					SessionErrors.add(renderRequest, "errorConocido");
					renderRequest.setAttribute("errorMsg", respuesta.getMsg());
					SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
							+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				} else {
					String datosCliente = CotizadorModularUtil
							.objtoJson(respuesta.getDatosCotizacion().getDatosCliente());

					LocalDate fechaHoy = generaFecha(respuesta.getDatosCotizacion().getFecInicio());
					LocalDate fechaMasAnio = generaFecha(respuesta.getDatosCotizacion().getFecFin());
					if( !respuesta.getDatosCotizacion().getFechaConvencional().contains("-") ){
						LocalDate fechaConvencional = generaFecha(respuesta.getDatosCotizacion().getFechaConvencional());
						renderRequest.setAttribute("fechaConvencional", fechaConvencional);						
					}

					fechaHoy = validaCambioFecha(fechaHoy);


					renderRequest.setAttribute("fechaHoy", fechaHoy);
					renderRequest.setAttribute("fechaMasAnio", fechaMasAnio);
					renderRequest.setAttribute("infoCotiP1", respuesta.getDatosCotizacion());
					renderRequest.setAttribute("datosCliente", datosCliente);

				}
			}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			_log.error(e.getMessage());
		}

		if (infCotizacion.getModo() != ModoCotizacion.NUEVA) {

			if (respuesta.getCode() > 0) {
				SessionErrors.add(renderRequest, "errorConocido");
				renderRequest.setAttribute("errorMsg", respuesta.getMsg());
				SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
						+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			} else {
				String datosCliente = CotizadorModularUtil
						.objtoJson(respuesta.getDatosCotizacion().getDatosCliente());

				LocalDate fechaHoy = generaFecha(respuesta.getDatosCotizacion().getFecInicio());
				LocalDate fechaMasAnio = generaFecha(respuesta.getDatosCotizacion().getFecFin());

				fechaHoy = validaCambioFecha(fechaHoy);


				renderRequest.setAttribute("fechaHoy", fechaHoy);
				renderRequest.setAttribute("fechaMasAnio", fechaMasAnio);
				renderRequest.setAttribute("cotizadorData", respuesta.getDatosCotizacion());
				renderRequest.setAttribute("datosCliente", datosCliente);

			}
		}
	}
	
	private String getCodeAgente (int idAgente , List<Persona> listaAgentes){
		String codeAgente = "";
		for (Persona persona : listaAgentes) {
			if(persona.getIdPersona() == idAgente){
				String[] parts = persona.getNombre().split("-");
				codeAgente = parts[0].trim();
				break;
			}
		}
		return codeAgente;
	}
	
	private LocalDate generaFecha(String fecha) {
		String aux = "";
		for (char c : fecha.toCharArray()) {
			aux += Character.isDigit(c) ? c : "";
		}
		Timestamp t = new Timestamp(Long.parseLong(aux));
		return t.toLocalDateTime().toLocalDate();
	}
	
	private LocalDate validaCambioFecha(LocalDate fechaOriginal) {
		switch (infCotizacion.getModo()) {
			case ALTA_ENDOSO:
				return fechaMayor(fechaOriginal);
			case BAJA_ENDOSO:
				return fechaMayor(fechaOriginal);
			case EDITAR_ALTA_ENDOSO:
				return fechaMayor(fechaOriginal);
			case EDITAR_BAJA_ENDOSO:
				return fechaMayor(fechaOriginal);
			default:
				return fechaOriginal;
		}
	}
	
	private LocalDate fechaMayor(LocalDate fechaOriginal) {
		LocalDate hoy = LocalDate.now();
		if (hoy.isAfter(fechaOriginal)) {
			return hoy;
		}
		return fechaOriginal;
	}
	
	private int perfilSuscriptor() {
		System.err.println("getidPerfilUser: " + idPerfilUser);
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORJR:
					return 1;
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORSR:
					return 1;
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORMR:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private int perfilJapones() {
		System.err.println("getidPerfilUser: " + idPerfilUser);
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_JAPONES:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}
	
	private int perfilSuscriptorJr() {
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORJR:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private int perfilSuscriptorSrMr() {
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORSR:
					return 1;
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORMR:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private int perfilSuscriptorMr() {
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORMR:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private boolean perfilSuscriptorGeneral() {
		try {
			switch (idPerfilUser) {
			case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORJR:
				return true;
			case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORSR:
				return true;
				case LiabilityQuotation73PortletKeys.PERFIL_SUSCRIPTORMR:
					return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	private int perfilAgente() {
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_AGENTE:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private int perfilEjecutivo() {
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_EJECUTIVO:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private int perfilAgenteEjecutivo() {
		try {
			switch (idPerfilUser) {
				case LiabilityQuotation73PortletKeys.PERFIL_AGENTE:
					return 1;
				case LiabilityQuotation73PortletKeys.PERFIL_EJECUTIVO:
					return 1;
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	private void validaFolioUsuario(int cotizacion, int version, int perfilId, String usuario, String pantalla, RenderResponse renderResponse) throws IOException{
		SimpleResponse resp = new SimpleResponse();
		try {
			resp = _CMServicesGenerico.validaFolioUsuario(cotizacion, version, perfilId, usuario, pantalla);
		} catch (Exception e) {
			// TODO: handle exception	
			System.err.println("Error al validar permisos por perfil");
			resp.setCode(1);
		}finally {
			if( resp.getCode() != 0 ){
				PortalUtil.getHttpServletResponse(renderResponse).sendRedirect("/group/portal-agentes/" );
			}						
		}
	}
	
}