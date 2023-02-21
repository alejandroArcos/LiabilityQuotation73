package com.tokio.pa.liability73.action;

import com.google.gson.Gson;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.cotizador.jsonformservice.JsonFormService;
import com.tokio.pa.cotizadorModularServices.Bean.CpData;
import com.tokio.pa.cotizadorModularServices.Bean.CpResponse;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.Ubicacion;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Enum.TipoCotizacion;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso2;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.beans.DatosGenerales;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = { 
				"javax.portlet.init-param.copy-request-parameters=true",
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
				"mvc.command.name=/liability/actionPaso2"
				},
		service = MVCActionCommand.class
		)

public class Paso2ActionCommand extends BaseMVCActionCommand{
	@Reference
	JsonFormService _JsonFormService;
	@Reference
	CotizadorPaso2 _CMServicesP2;
	
	@Reference
	private DLAppService _dlAppService;
	
	InfoCotizacion infCotizacion;
	User user;
	int idPerfilUser;
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		/*
		Enumeration<String> en = actionRequest.getParameterNames();
		while (en.hasMoreElements()) {
			Object objOri = en.nextElement();
			String param = (String) objOri;
			String value = actionRequest.getParameter(param);
			System.out.println("[ ---> " +param + " : " + value );
		}
		*/
		user = (User) actionRequest.getAttribute(WebKeys.USER);
		infCotizacion = new InfoCotizacion();
		Gson gson = new Gson();
		int tipoCotizacion = ParamUtil.getInteger(actionRequest, "tipoCoti");
		int tipoMoneda = ParamUtil.getInteger(actionRequest, "tipoMoneda");
		int subGiroRiesgo = ParamUtil.getInteger(actionRequest, "subGiroRiesgo");

		
		String saveResponse = ParamUtil.getString(actionRequest, "saveResponse");
		String datosGen = ParamUtil.getString(actionRequest, "auxDG");
		
		String infCotJson = CotizadorModularUtil.objtoJson(infCotizacion);

		System.err.println("saveResponse: " + saveResponse);
		
		if( Validator.isNotNull(saveResponse) ){
			UbicacionesResponse responseP1 = gson.fromJson(saveResponse, UbicacionesResponse.class);
			/*procesar Filds y layouts*/
			generaInfoCotizacion(actionRequest, responseP1);
			infCotizacion.setNoUbicaciones( responseP1.getUbicaciones().size() );
			generaAcordeones(actionRequest, responseP1);
			generaListColonias(actionRequest, responseP1);			
			validaPaso( actionRequest, datosGen);
			actionRequest.setAttribute("responseP1", responseP1 );
		}else{
			llenaInfCotizacion(actionRequest);
			recuperaInfoUbicaciones(actionRequest);
		}
		validaUbicacionActual( actionRequest );
		
		recuperaInfoCargaMasiva(actionRequest);
		
		/*************************/

		System.err.println("infCotizacion ACTION");
		System.err.println(infCotizacion);
		
		actionRequest.setAttribute("infCotJson", infCotJson);
		actionRequest.setAttribute("infCotizacion", infCotizacion);
		actionRequest.setAttribute("tipoCotizacion", tipoCotizacion);
		
		actionRequest.setAttribute("tipoMoneda", tipoMoneda );
		actionRequest.setAttribute("subGiroRiesgo", subGiroRiesgo );
		
		urlDocCargaMasiva(actionRequest);
		
		actionResponse.getRenderParameters().setValue("jspPage", "/jsp/paso2.jsp");
		
	}
	
	private void generaInfoCotizacion(ActionRequest actionRequest, UbicacionesResponse responseP1){
		String infoCotizacionStr = ParamUtil.getString(actionRequest, "infoCotizacion");
		Gson gson = new Gson();
		/*InfoCotizacion infoCoti = new InfoCotizacion();*/
		int canalNegocio = ParamUtil.getInteger(actionRequest, "canalNegocio");
		int tipoCoaseguro = ParamUtil.getInteger(actionRequest, "tipoCoaseguro");
		
		if(Validator.isNotNull(infoCotizacionStr)){
			InfoCotizacion infCotizacionAux = gson.fromJson(infoCotizacionStr, InfoCotizacion.class);
			infCotizacion.setModo(infCotizacionAux.getModo());			
		}
		
		infCotizacion.setCotizacion(responseP1.getCotizacion());
		infCotizacion.setFolio( Long.parseLong(responseP1.getFolio()) );
		infCotizacion.setPantalla( LiabilityQuotation73PortletKeys.LiabilityQuotation );
		infCotizacion.setVersion( responseP1.getVersion() );
		infCotizacion.setCanalNegocio(canalNegocio);
		infCotizacion.setTipoCotizacion(TipoCotizacion.RC);
		infCotizacion.setTipoCoaseguro(tipoCoaseguro);
		
		if(infCotizacion.getModo() == ModoCotizacion.VOBO_REASEGURO || 
				infCotizacion.getModo() == ModoCotizacion.REASEGURO) {
			infCotizacion.setModo(ModoCotizacion.EDICION);
		}
		
		String infoCotiStr = gson.toJson(infCotizacion);
		
		actionRequest.setAttribute("infoCoti", infoCotiStr );
	}
	
	private void generaAcordeones(ActionRequest actionRequest, UbicacionesResponse ubi) {
		if( Validator.isNotNull(ubi) ){
			try {
				Gson gson = new Gson();
				Map<Integer, String> jsonFiels = new HashMap<Integer, String>();
				
				String dataProvide = gson.toJson(ubi.getDataProviders());
				int i = 1;
				for (Ubicacion ubicacion : ubi.getUbicaciones()) {
					if( Validator.isNotNull(ubicacion.getField()) && Validator.isNotNull(ubicacion.getLayouts()) ){
						String jsonformfields = "{\"fields\":" + ubicacion.getField() + "}";
						String jsonlayout = ubicacion.getLayouts();
					String jsonDataProviders = "{\"dataProviders\":" + dataProvide + "}";
						/*String jsonDataProviders = "{\"dataProviders\": [] }";*/
						String u = ubicacion.getIdubicacion() + "";
						String htmlUbicacion ="";
						try {
							htmlUbicacion = _JsonFormService.parse(jsonformfields, jsonlayout, jsonDataProviders, u);							
						} catch (Exception e) {
							// TODO: handle exception
							System.err.println("Error al convertir html ubicacion " + i);
						}
						jsonFiels.put(i, htmlUbicacion);						
					}
					i++;
				}
				
//				System.out.println("jsonFiels");
//				System.out.println(jsonFiels);
				actionRequest.setAttribute("jsonFiels", jsonFiels);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("------------------ generaAcordeones");
				e.printStackTrace();
				SessionErrors.add(actionRequest, "errorConocido");
				actionRequest.setAttribute("errorMsg", "Error al cargar las ubicaciones");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
						+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			}			
		}
	}
	
	private void generaListColonias(ActionRequest actionRequest, UbicacionesResponse ubi) {
		// TODO Auto-generated method stub
		try {
			ArrayList<List<CpData>> colonias = new ArrayList<List<CpData>>();
			List<CpData> dataVacia = null;
			for (Ubicacion u : ubi.getUbicaciones()) {
				String cp = u.getCpData().getCp();
				if (Validator.isNotNull(cp)) {
					CpResponse cpdata = _CMServicesP2.getCP(cp, user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
					if (cpdata.getCode() == 0 || cpdata.getCode() == 4) {
						colonias.add(cpdata.getListaCpData());
					} else {
						colonias.add(dataVacia);
					}
				} else {
					colonias.add(dataVacia);
				}
			}
			System.out.println("colonias: " + colonias);
			actionRequest.setAttribute("colonias", colonias);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("------------------ Colonias");
			e.printStackTrace();
			SessionErrors.add(actionRequest, "errorConocido");
			actionRequest.setAttribute("errorMsg", "Error al cargar las ubicaciones");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
	}
	
	private void validaPaso( ActionRequest actionRequest,  String datosGen){
		if( Validator.isNotNull(datosGen) ){
			Gson gson = new Gson();
			DatosGenerales dg = gson.fromJson(datosGen, DatosGenerales.class);
			dg.setPaso(2);
			
//			actionRequest.setAttribute("datosGen", dg );
			actionRequest.setAttribute("datosGenTxt", gson.toJson(dg) );
		}else{
			System.err.println("Error al cargar DatosGenerales");
		}
	}
	
	private void validaUbicacionActual( ActionRequest actionRequest ){
		int curUbicacion = ParamUtil.getInteger(actionRequest, "curUbicacion");
		actionRequest.setAttribute("curUbicacion", curUbicacion );
	}
	
	private void llenaInfCotizacion(ActionRequest actionRequest) {
		Gson gson = new Gson();
		try {
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));

			user = (User) actionRequest.getAttribute(WebKeys.USER);
			idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");

			System.out.println("-------------------aqui-------------------------------");
			String inf = "";
			if (Validator.isNull(actionRequest.getAttribute("infoCotizacionString"))) {
				System.out.println("recupere de original");
				inf = originalRequest.getParameter("infoCotizacion");
				infCotizacion = CotizadorModularUtil.decodeURL(inf);
				System.out.println(inf);
			} else {
				System.out.println("recupere de render");
				inf = (String) actionRequest.getAttribute("infoCotizacionString");
				infCotizacion = gson.fromJson(inf, InfoCotizacion.class);
				System.out.println(infCotizacion.toString());
				System.out.println(inf);
			}



			System.err.println(infCotizacion.toString());
			infCotizacion.setPantalla(LiabilityQuotation73PortletKeys.LiabilityQuotation);
			
			String infoCotiStr = gson.toJson(infCotizacion);
			
			actionRequest.setAttribute("infoCoti", infoCotiStr );
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("------------------ llenaInfoCotizacion:");
			e.printStackTrace();
			SessionErrors.add(actionRequest, "errorConocido");
			actionRequest.setAttribute("errorMsg", "Error al cargar la cotización");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
	}
	
	
	private void recuperaInfoUbicaciones(ActionRequest actionRequest) {
		try {
			final PortletSession psession = actionRequest.getPortletSession();
			Map<Integer, Integer> relacionUbicaciones = new HashMap<Integer, Integer>();

			Gson gson = new Gson();
			String auxNombre = "LIFERAY_SHARED_F=" + infCotizacion.getFolio() + "_C="
					+ infCotizacion.getCotizacion() + "_V=" + infCotizacion.getVersion()
					+ "_UBICACIONRESPONSE";

			System.out.println("auxNombre" + auxNombre);
			String ubicacion = (String) psession.getAttribute(auxNombre,
					PortletSession.APPLICATION_SCOPE);

			System.err.println("UBICACION");
			System.err.println(ubicacion);

			UbicacionesResponse ubi = gson.fromJson(ubicacion, UbicacionesResponse.class);
			actionRequest.setAttribute("ubicacion", ubi);

			if (Validator.isNotNull(ubi)) {
				
				System.out.println("Numero ubicaciones");
				System.out.println(ubi.getUbicaciones().size());
				infCotizacion.setNoUbicaciones(ubi.getUbicaciones().size());
				//infCotizacion.setNoUbicaciones(1);

				generaAcordeones(actionRequest, ubi);
				generaListColonias(actionRequest, ubi);

				actionRequest.setAttribute("ubicacion", ubi);
				actionRequest.setAttribute("relacionUbicaciones", relacionUbicaciones);
				

			} else {
				infCotizacion.setNoUbicaciones(0);
				SessionErrors.add(actionRequest, "errorConocido");
				actionRequest.setAttribute("errorMsg", "Error al traer su informacion");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
						+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
				
			}
			
			actionRequest.setAttribute("folioCotizacion", infCotizacion.getFolio());
			actionRequest.setAttribute("versionCotizacion", infCotizacion.getVersion());

		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("------------------ info ubicaciones:");
			e.printStackTrace();
			SessionErrors.add(actionRequest, "errorConocido");
			actionRequest.setAttribute("errorMsg", "Error al cargar las ubicaciones");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			
			actionRequest.setAttribute("folioCotizacion", infCotizacion.getFolio());
			actionRequest.setAttribute("versionCotizacion", infCotizacion.getVersion());
		}
	}
	
	private void urlDocCargaMasiva(ActionRequest actionRequest){
		
		try {
			long idGroup = PortalUtil.getScopeGroupId(actionRequest);
			DLFolder fCotizadores = DLFolderLocalServiceUtil.getFolder(idGroup, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					"Documentos_Aux_Cotizadores");
			FileEntry fileEntry = _dlAppService.getFileEntry(idGroup, fCotizadores.getFolderId(), "RC_Ubicaciones_Layout.xlsm");
			String urlDoc = actionRequest.getScheme() + "://" + actionRequest.getServerName() + ":" + actionRequest.getServerPort() + "/documents/" + idGroup + "/" + fileEntry.getFolderId() + "/" + fileEntry.getFileName()
			+ "/" + fileEntry.getUuid();
			System.out.println("-----> url : " + urlDoc);
			
			String url = actionRequest.getScheme() + "://" + actionRequest.getServerName() + ":" + actionRequest.getServerPort() + actionRequest.getContextPath();
			System.out.println(url);
			actionRequest.setAttribute("urlDoc", urlDoc);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void recuperaInfoCargaMasiva(ActionRequest actionRequest) {
		
		String auxNombre = "LIFERAY_SHARED_F=" + infCotizacion.getFolio() + "_C="
				+ infCotizacion.getCotizacion() + "_V=" + infCotizacion.getVersion() + "_CARGAMASIVA";

		final PortletSession psession = actionRequest.getPortletSession();
		if (Validator
				.isNotNull(psession.getAttribute(auxNombre, PortletSession.APPLICATION_SCOPE))) {
			
			boolean infoCargaMasiva = (boolean) psession.getAttribute(auxNombre,
					PortletSession.APPLICATION_SCOPE);

			System.out.println("Carga Masiva: " + infoCargaMasiva);
			
			actionRequest.setAttribute("infoCargaMasiva", infoCargaMasiva);
		}
		else {
			actionRequest.setAttribute("infoCargaMasiva", false);
		}
		
	}

}
