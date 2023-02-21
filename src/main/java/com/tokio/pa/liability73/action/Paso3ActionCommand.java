package com.tokio.pa.liability73.action;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CaratulaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.ClausulaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Bean.UbicacionesResponse;
import com.tokio.pa.cotizadorModularServices.Bean.ValidarReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Exception.CotizadorModularException;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3RC;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.util.Base64;
import java.util.Comparator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = { 
				"javax.portlet.init-param.copy-request-parameters=true",
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
				"mvc.command.name=/liability/actionPaso3"
				},
		service = MVCActionCommand.class
		)

public class Paso3ActionCommand extends BaseMVCActionCommand{
	@Reference
	CotizadorPaso3 _ServicePaso3;
	@Reference
	CotizadorPaso3RC _ServicePaso3RC;
	@Reference
	CotizadorGenerico _ServiceGenerico;

	double tpoCambio = 0;
	int tipoMoneda = 0;
	double minPrima = 0;
	CaratulaResponse caratulaResponse = new CaratulaResponse();
	InfoCotizacion infoCoti = new InfoCotizacion();
	User user;
	
	private static final Log _log = LogFactoryUtil.getLog(Paso3ActionCommand.class);
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		Gson gson = new Gson();
		user = (User) actionRequest.getAttribute(WebKeys.USER);
		
		int tipoCotizacion = ParamUtil.getInteger(actionRequest, "tipoCoti");
		int canalNegocio = ParamUtil.getInteger(actionRequest, "canalNegocio");
		int coaseguro = ParamUtil.getInteger(actionRequest, "coaseguro");
		String modoCotizacion = ParamUtil.getString(actionRequest, "modoCoti");
		String saveResponse = ParamUtil.getString(actionRequest, "saveResponse");
		tipoMoneda = ParamUtil.getInteger(actionRequest, "tipoMoneda");
		
		String infoRecalcula = ParamUtil.getString(actionRequest, "infoRecalcula");
		
		int suscripcion = ParamUtil.getInteger(actionRequest, "suscripcion");
		
		UbicacionesResponse resUbicaciones = getInfoUbicaciones(saveResponse);
		llenaCatalogos(actionRequest);

		infoCoti.setCotizacion(resUbicaciones.getCotizacion());
		infoCoti.setVersion(resUbicaciones.getVersion());
		infoCoti.setFolio( Long.parseLong( resUbicaciones.getFolio() ) );
		infoCoti.setPantalla(LiabilityQuotation73PortletKeys.LiabilityQuotation);
		infoCoti.setCanalNegocio(canalNegocio);
		infoCoti.setTipoCoaseguro(coaseguro);
		
		if( Validator.isNull(infoRecalcula) ){
			caratulaResponse = _ServicePaso3RC.getCaratulaRC(resUbicaciones.getCotizacion(), ""+resUbicaciones.getVersion(), user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);

			if( !modoCotizacion.equals(ModoCotizacion.CONSULTA.toString()) &&
					!modoCotizacion.equals(ModoCotizacion.CONSULTA_VOBO_REASEGURO.toString()) &&
					!modoCotizacion.equals(ModoCotizacion.COASEGURO_CONSULTA.toString()) && 
					!modoCotizacion.equals(ModoCotizacion.REASEGURO_CONSULTA.toString()) &&
					!modoCotizacion.equals(ModoCotizacion.COASEGURO.toString()) &&
					!modoCotizacion.equals(ModoCotizacion.REASEGURO.toString())
					){
				validaPrimaminima(actionRequest);
			}
		}else{
			caratulaResponse = fGetRecalculaCaratula(infoRecalcula);
		}
		fSetModoCotizacion( modoCotizacion, actionRequest);
		
		
		String infoCotJson = gson.toJson(infoCoti);
		
//		ComisionesResponse responseComAgente = _ServicePaso3.getComisionesAgente(resUbicaciones.getCotizacion(), resUbicaciones.getVersion(), user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		ClausulaResponse respClauAdi = _ServicePaso3.getClausulasAdicionales(resUbicaciones.getCotizacion(), resUbicaciones.getVersion(), user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));

		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		ValidarReaseguroResponse response=_ServicePaso3.validarReaseguro(resUbicaciones.getCotizacion(), resUbicaciones.getVersion(), idPerfilUser, user.getScreenName(),LiabilityQuotation73PortletKeys.LiabilityQuotation);
		if(response.getCode()!=0) {
			SessionErrors.add(actionRequest, "reaseguroEliminado");
			actionRequest.setAttribute("reaseguroEliminadoMsg",response.getMsg());
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
		
		actionRequest.setAttribute("tipoCotizacion", tipoCotizacion);
		actionRequest.setAttribute("saveResponse", saveResponse);
		
		actionRequest.setAttribute("mailUser", Base64.getEncoder().encodeToString(user.getEmailAddress().toString().getBytes()));
		caratulaResponse.setEmail(Base64.getEncoder().encodeToString(caratulaResponse.getEmail().toString().getBytes()));
		
		actionRequest.setAttribute("caratulaResponse", caratulaResponse);
		actionRequest.setAttribute("respClauAdi", respClauAdi);
		actionRequest.setAttribute("infoCotJson", infoCotJson);
		actionRequest.setAttribute("infoCoti", infoCoti);
		actionRequest.setAttribute("tipoMoneda", tipoMoneda );
		actionRequest.setAttribute("modoCotizacion", modoCotizacion);
		actionRequest.setAttribute("suscripcion", suscripcion);
		
		_log.info("La URL codificada es: " + CotizadorModularUtil.encodeURL(infoCoti));
		
		actionRequest.setAttribute("infoCotizacionURL", CotizadorModularUtil.encodeURL(infoCoti));
		

		
		actionResponse.setRenderParameter("jspPage", "/jsp/paso3.jsp");
		
	}
	
	private UbicacionesResponse getInfoUbicaciones(String saveResponse){
		Gson gson = new Gson();
		if( Validator.isNotNull(saveResponse) ){
			UbicacionesResponse response = gson.fromJson(saveResponse, UbicacionesResponse.class);
			return response;
		}else{
			return new UbicacionesResponse();
		}
	}

	private void llenaCatalogos(ActionRequest actionRequest){
		User user = (User) actionRequest.getAttribute(WebKeys.USER);
		
		ListaRegistro listaPrimaMinima = fGetCatalogos(CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_MIN_PRI,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
				LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
		ListaRegistro listaMotivoRechazo = fGetCatalogos(
				CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_MOTI_RECHAZO,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
				LiabilityQuotation73PortletKeys.LiabilityQuotation);
		
		Registro r = listaPrimaMinima.getLista().stream().filter(r2 -> "PRIMINRC".equals(r2.getCodigo())).findAny().orElse(new Registro());
		
		minPrima = Double.parseDouble(r.getValor());
		llenaTipoCambio();
		
		actionRequest.setAttribute("motivoRechazo", listaMotivoRechazo.getLista());
		actionRequest.setAttribute("minPrima", minPrima);
		actionRequest.setAttribute("tpoCambio", tpoCambio);
	}
	
	private ListaRegistro fGetCatalogos(int p_rownum, String p_tiptransaccion, String p_codigo,
			int p_activo, String p_usuario, String p_pantalla) {
		try {
			ListaRegistro list = _ServiceGenerico.getCatalogo(p_rownum, p_tiptransaccion, p_codigo,
					p_activo, p_usuario, p_pantalla);
			list.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return list;
			/* return null; */
		} catch (Exception e) {
			return null;
		}
	}
	
	private CaratulaResponse fGetRecalculaCaratula (String responseInfo){
		Gson gson = new Gson();
		try {
			CaratulaResponse resp = gson.fromJson(responseInfo, CaratulaResponse.class);
			return  resp;			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error al convertir info Caratula");
			return new CaratulaResponse();
		}
	}
	
	private void llenaTipoCambio(){
		try {
			tpoCambio = _ServicePaso3.getTipoCambio().getTipoCambio();
		} catch (CotizadorModularException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void validaPrimaminima(ActionRequest actionRequest) {
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));

		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		double auxPrimaMin = 0;

//		String tipoMoneda = ""+tipoMoneda;
		if(tipoMoneda == 1){
//			auxPrimaMin = minPrima;
			auxPrimaMin = minPrima * tpoCambio;
		}else{
//			auxPrimaMin = minPrima / tpoCambio;
			auxPrimaMin = minPrima;
		}
		
		if (caratulaResponse.getPrimaNeta() < auxPrimaMin) {
			System.out.println("APLICAR PRIMA MINIMA");
//			String cur_version = String.valueOf(infoCoti.getVersion());

			try {
//				caratulaResponse = _ServicePaso3.GetCaratulaPrimaObjetivo(
//						(int) infoCoti.getCotizacion(), cur_version, auxPrimaMin,
//						user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
				JsonArray p_lista = new JsonArray();
		        JsonObject ramo = new JsonObject();
		        ramo.addProperty("p_codigo_ramo", "41A6");
		        ramo.addProperty("p_prima_objetiva", auxPrimaMin);
		        p_lista.add(ramo);
				
				caratulaResponse = _ServicePaso3.calculaPrimaObjetivo((int) infoCoti.getCotizacion(), infoCoti.getVersion(), idPerfilUser, caratulaResponse.getRecargo(), caratulaResponse.getGastos(), p_lista, user.getScreenName(), LiabilityQuotation73PortletKeys.LiabilityQuotation);
				SessionErrors.add(actionRequest, "errorConocido");
				actionRequest.setAttribute("errorMsg",
						"Se ha aplicado la prima mínima del producto");
				SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
						+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	private void fSetModoCotizacion(String modoCotizacion, ActionRequest actionRequest) {
//		if( modoCotizacion.equals(ModoCotizacion.CONSULTA.toString()) ){
//			System.err.println("entre set modo consulta");
//			infoCoti.setModo(ModoCotizacion.CONSULTA);
//		}
		
		switch (modoCotizacion) {
		case "NUEVA":
			infoCoti.setModo(ModoCotizacion.NUEVA);
			break;
		case "EDICION":
			infoCoti.setModo(ModoCotizacion.EDICION);
			break;
		case "COPIA":
			infoCoti.setModo(ModoCotizacion.COPIA);
			break;
		case "AUX_PASO4":
			infoCoti.setModo(ModoCotizacion.AUX_PASO4);
			break;
		case "ALTA_ENDOSO":
			infoCoti.setModo(ModoCotizacion.ALTA_ENDOSO);
			break;
		case "BAJA_ENDOSO":
			infoCoti.setModo(ModoCotizacion.BAJA_ENDOSO);
			break;
		case "EDITAR_ALTA_ENDOSO":
			infoCoti.setModo(ModoCotizacion.EDITAR_ALTA_ENDOSO);
			break;
		case "EDITAR_BAJA_ENDOSO":
			infoCoti.setModo(ModoCotizacion.EDITAR_BAJA_ENDOSO);
			break;
		case "CONSULTA":
			infoCoti.setModo(ModoCotizacion.CONSULTA);
			break;
		case "FACTURA_492":
			infoCoti.setModo(ModoCotizacion.FACTURA_492);
			actionRequest.setAttribute("Leg492", "factura");
			break;
		case "REVIRE":
			infoCoti.setModo(ModoCotizacion.REVIRE);
			break;
		case "ERROR":
			infoCoti.setModo(ModoCotizacion.ERROR);
			break;
		case "RENOVACION_AUTOMATICA":
			infoCoti.setModo(ModoCotizacion.RENOVACION_AUTOMATICA);
			break;
		case "EDITAR_RENOVACION_AUTOMATICA":
			infoCoti.setModo(ModoCotizacion.EDITAR_RENOVACION_AUTOMATICA);
			break;
		case "CONSULTAR_RENOVACION_AUTOMATICA":
			infoCoti.setModo(ModoCotizacion.CONSULTAR_RENOVACION_AUTOMATICA);
			break;
		case "EDICION_JAPONES":
			infoCoti.setModo(ModoCotizacion.EDICION_JAPONES);
			break;
		case "CONSULTAR_REVISION":
			infoCoti.setModo(ModoCotizacion.CONSULTAR_REVISION);
			break;
		case "REASEGURO":
			infoCoti.setModo(ModoCotizacion.EDICION);
			break;
		case "REASEGURO_CONSULTA":
			if(caratulaResponse.getEstado()==362) {
				infoCoti.setModo(ModoCotizacion.CONSULTA_VOBO_REASEGURO);
			}else {
				infoCoti.setModo(ModoCotizacion.CONSULTA);	
			}
			break;
		case "COASEGURO":
			infoCoti.setModo(ModoCotizacion.COASEGURO);
			break;
		case "COASEGURO_CONSULTA":
			if(caratulaResponse.getEstado()==362) {
				infoCoti.setModo(ModoCotizacion.CONSULTA_VOBO_REASEGURO);
			}else {
				infoCoti.setModo(ModoCotizacion.CONSULTA);
			}
			break;
		case "VOBO_REASEGURO":
			infoCoti.setModo(ModoCotizacion.VOBO_REASEGURO);
			break;
		case "CONSULTA_VOBO_REASEGURO":
			infoCoti.setModo(ModoCotizacion.CONSULTA_VOBO_REASEGURO);
			break;
		default:
			infoCoti.setModo(ModoCotizacion.ERROR);
			break;
		}
	}
	
}
