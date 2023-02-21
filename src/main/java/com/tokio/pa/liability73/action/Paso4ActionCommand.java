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
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.tokio.pa.cotizadorModularServices.Bean.DomicilioResponse;
import com.tokio.pa.cotizadorModularServices.Bean.EmisionDataResponse;
import com.tokio.pa.cotizadorModularServices.Bean.InfoAuxPaso1;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.Persona;
import com.tokio.pa.cotizadorModularServices.Bean.PersonasBloqueadasResponse;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Bean.RetroactividadRequest;
import com.tokio.pa.cotizadorModularServices.Bean.UmbralVoBo;
import com.tokio.pa.cotizadorModularServices.Bean.ValidaResponse;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Enum.TipoPersona;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso4;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.liability73.beans.LogVoBo;
import com.tokio.pa.liability73.beans.ModoAuxiliar;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = { 
				"javax.portlet.init-param.copy-request-parameters=true",
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
				"mvc.command.name=/liability/actionPaso4"
				},
		service = MVCActionCommand.class
		)

public class Paso4ActionCommand extends BaseMVCActionCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(Paso4ActionCommand.class);
	
	@Reference
	CotizadorPaso4 _ServicePaso4;
	@Reference
	CotizadorGenerico _ServiceGenerico;
	
	InfoCotizacion infCotizacion = new InfoCotizacion();
	InfoAuxPaso1 infoPaso1 = new InfoAuxPaso1();
	EmisionDataResponse responseEmision = new EmisionDataResponse();
	DomicilioResponse responseCp = new DomicilioResponse();
	User user;
	LogVoBo vobo = new LogVoBo();
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		
		SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		HttpServletRequest originalRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		user = (User) actionRequest.getAttribute(WebKeys.USER);
		
		String saveResponse = ParamUtil.getString(actionRequest, "saveResponse");
		int tipoCotizacion = ParamUtil.getInteger(actionRequest, "tipoCoti");
		int tipoMoneda = ParamUtil.getInteger(actionRequest, "tipoMoneda");
		String modoCotizacion = ParamUtil.getString(actionRequest, "modoCoti");
		
		llenaInfoCotizacion(actionRequest);
		recuperaInfoPaso1(actionRequest);
		validaModoCot(actionRequest);
		llenaInfoEmisionData(actionRequest);
		fValidarRetroactividad(idPerfilUser, actionRequest);
		validaColonia(actionRequest);		
		fSolicitaUmbralVoBo((int)infCotizacion.getCotizacion(), infCotizacion.getVersion(), actionRequest);		
		String infoCotJson = CotizadorModularUtil.objtoJson(infCotizacion);			
		llenaCatalogo(actionRequest);
		llenaLogVoBo(actionRequest);
		
		
		actionRequest.setAttribute("response", responseEmision);
		actionRequest.setAttribute("responseCp", responseCp);
		actionRequest.setAttribute("infoCotJson", infoCotJson);		
		actionRequest.setAttribute("infCotizacion", infCotizacion);
		actionRequest.setAttribute("saveResponse", saveResponse);
		actionRequest.setAttribute("tipoCotizacion", tipoCotizacion);
		actionRequest.setAttribute("tipoMoneda", tipoMoneda );
		actionRequest.setAttribute("modoCotizacion", modoCotizacion);
		actionRequest.setAttribute("infoCoti", CotizadorModularUtil.encodeURL(infCotizacion));
		//actionRequest.setAttribute("userMail", user.getEmailAddress());
		actionRequest.setAttribute("userMail", Base64.getEncoder().encodeToString(user.getEmailAddress().toString().getBytes()));
		
		actionResponse.setRenderParameter("jspPage", "/jsp/paso4.jsp");
		
	}
	
	private DomicilioResponse getDomicilioPersonas(String cp){
		try{
			return _ServicePaso4.getDomicilioPersonas(cp);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	private void fSolicitaUmbralVoBo(int cotizacion, int version, ActionRequest actionRequest){
		String infReqCom = "";
		String infReqComS = "";
		try{
			UmbralVoBo respVoBo = _ServicePaso4.SolicitaUmbralVoBo(cotizacion, version);
			if (respVoBo.getCode()==0){
				//respVoBo.setCodigoVoBo("VPVOBO1");
				System.err.println("respVoBo.getCodigoVoBo(): " + respVoBo.getCodigoVoBo());
				switch (respVoBo.getCodigoVoBo()) {
				case "VPVOBO1":
					//BAJO
					fQuienEsQuien(actionRequest);
					break;
				case "VPVOBO2":
					//MEDIO
					infReqCom="infReq";
					infReqComS="infReqS";
					vobo.setP_tipoVOBO(vobo.getP_tipoVOBO() + "Prima media,");
					fQuienEsQuien(actionRequest);
					break;
				default:
					//ALTO
					infReqCom="infReq";
					infReqComS="infReqS";
					vobo.setP_tipoVOBO(vobo.getP_tipoVOBO() + "Prima alta,");
					fQuienEsQuien(actionRequest);
					break;
				}
				fPersonasBloqueadasCNSF(actionRequest);
				System.err.println("respVoBo");
				System.err.println(respVoBo);			
			}
			else{
				SessionErrors.add(actionRequest, "errorServicios");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		actionRequest.setAttribute("infReqCom", infReqCom);
		actionRequest.setAttribute("infReqComS", infReqComS);
	}
	
	private EmisionDataResponse fGetEmisionData(int cotizacion, int version, String usuario, String pantalla){
		try{
			return _ServicePaso4.getEmisionData(cotizacion, version, usuario, pantalla);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	private ListaRegistro fGetCatalogos(int p_rownum, String p_tiptransaccion, String p_codigo, int p_activo,
			String p_usuario, String p_pantalla) {
		try {
			ListaRegistro list = _ServiceGenerico.getCatalogo(p_rownum, p_tiptransaccion, p_codigo, p_activo, p_usuario, p_pantalla);
			list.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return list;
			/* return null; */
		} catch (Exception e) {
			return null;
		}
	}
	
	private void llenaInfoCotizacion(ActionRequest actionRequest){
		
		try {
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));

			String inf = originalRequest.getParameter("infoCotizacion");
			
			String nombreCotizador = "";
			if(Validator.isNotNull(inf)){
				infCotizacion = CotizadorModularUtil.decodeURL(inf);
			}else{
				infCotizacion = new InfoCotizacion();
			}
			
			actionRequest.setAttribute("tituloCotizador", nombreCotizador);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("------------------ llenaInfoCotizacion:");
			SessionErrors.add(actionRequest, "errorServicios");
			e.printStackTrace();
		}

	}
	
	private void llenaInfoEmisionData(ActionRequest actionRequest){
		String disableDenom = "";
		if((infCotizacion.getCotizacion()!=0)&&(infCotizacion.getVersion()!=0)){

			responseEmision = fGetEmisionData((int)infCotizacion.getCotizacion(), infCotizacion.getVersion(), user.getScreenName(), infCotizacion.getPantalla()); //Prueba Persona Fisica
			if(responseEmision.getCode()==0){
				
				responseCp = getDomicilioPersonas(responseEmision.getCpData().getCp());				
				responseEmision.getDatosFisica().setFechaNacimineto(fDateToString(responseEmision.getDatosFisica().getFechaNacimineto()));
				responseEmision.getDatosMoral().setFechaConstitucion(fDateToString(responseEmision.getDatosMoral().getFechaConstitucion()));
				if(responseEmision.getDatosCliente().getIdDenominacion() != 0){
					disableDenom = "disabled";
				}
				
			}
			else{
				SessionErrors.add(actionRequest, "errorServicios");				
			}
		}
		else{
			responseEmision = new EmisionDataResponse();
			SessionErrors.add(actionRequest, "errorInfo");
		}
		actionRequest.setAttribute("disableDenom", disableDenom);
	}
	
	private void validaColonia(ActionRequest actionRequest){
		String disableColonia = "";
		if((infCotizacion.getCotizacion()!=0)&&(infCotizacion.getVersion()!=0)&&(responseEmision.getCode()==0)){
			if(responseEmision.getCpData().getColonia() != ""){
				Registro e = new Registro();
				e.setId(0);
				e.setCodigo("0");
				e.setDescripcion(responseEmision.getCpData().getColonia());
				responseCp.getListaColonia().add(e);
				disableColonia="disabled";
			}
		}
		actionRequest.setAttribute("disableColonia", disableColonia);
	}
	
	private String fDateToString(String valorFecha) {
		String res = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if (valorFecha != null) {
			String aux = "";
			for (char c : valorFecha.toCharArray()) {
				aux += Character.isDigit(c) ? c : "";
			}
			try {
				cal.setTime(new Date(Long.parseLong(aux)));
				res = dateFormat.format(cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	private void fPersonasBloqueadasCNSF(ActionRequest actionRequest) {
		
		Persona persona = responseEmision.getDatosCliente();
		String personasBloqueadasJsonStr;
		Gson gson = new Gson();
		
		try {
			PersonasBloqueadasResponse response = _ServicePaso4.getListaPersonasBloqueadasCNSF(persona.getNombre(),
					persona.getAppPaterno(), persona.getAppMaterno(), persona.getRfc(),
					(persona.getTipoPer() == TipoPersona.MORAL.getTipoPersona()) ? 2 : 1 , persona.getAppPaterno().replace(".", ""));
			
			System.out.println(response);
			
			personasBloqueadasJsonStr = gson.toJson(response);
			
			actionRequest.setAttribute("responsePersonasBloqueadasCNSF", personasBloqueadasJsonStr);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fQuienEsQuien(ActionRequest actionRequest){
		boolean isMoral = (responseEmision.getDatosCliente().getTipoPer() == TipoPersona.MORAL.getTipoPersona()) ? true : false;
		
		JsonObject respuesta = new JsonObject();
		JsonArray responseXML = new JsonArray();
		int codigo = 0;
		
		try {
			Client client = Client.create();
			WebResource webResource = client.resource("https://qeq.com.mx/datos/qws/access")
					.queryParam("var1", LiabilityQuotation73PortletKeys.QQ_USUARIO)
					.queryParam("var2", LiabilityQuotation73PortletKeys.QQ_CONTRASENA);
			
			String response = webResource.accept(MediaType.APPLICATION_XML).header("user-agent", "").get(String.class);
			
			_log.info("Resourse: " + webResource);
			_log.info("Request : " + response);
			
			String cookiesServ = ""; 
			
			for ( NewCookie cookie : webResource.head().getCookies()) {
				cookiesServ += Validator.isNull( cookiesServ) ?
						cookie.getName() + " : " + cookie.getValue() :
							", " + cookie.getName() + " : " + cookie.getValue();
			}
			
			_log.info("Cookies : " + cookiesServ);
			
			NewCookie cookieId = webResource.head().getCookies().stream().filter(NewCookie -> "qnid".equals(NewCookie.getName())).findAny().orElse(null);
			
			if (Validator.isNull(cookieId)) {
				codigo = 1;
				respuesta.addProperty("code", codigo);
				respuesta.addProperty("msg", "Error de login Quien es Quien");
				_log.info("quien es quien, sin cookie id: " + respuesta.toString());
			} else {
				
				List<NewCookie> cookies = webResource.head().getCookies();
				
				if (isMoral){
					JsonObject xmlRazon = new JsonObject();
					JsonObject xmlRfc = new JsonObject();
					JsonObject xmlCompleto = new JsonObject();
					
					
					WebResource	razMor = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_MORAL)
							.queryParam("razonsoc",
									HtmlUtil.escape( responseEmision.getDatosCliente().getNombre().toLowerCase()));					
					
					WebResource	rfcMor = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_MORAL)
							.queryParam("rfc", responseEmision.getDatosCliente().getRfc());

					WebResource	nom_den_rfc = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_MORAL)
							.queryParam("razonsoc",
									HtmlUtil.escape( 
											responseEmision.getDatosCliente().getNombre().toLowerCase() + " " +
											responseEmision.getDatosCliente().getAppPaterno().toLowerCase()))
							.queryParam("rfc", responseEmision.getDatosCliente().getRfc());
					
					
					String respRazMor = razMor
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);
					
					String respRfcMor = rfcMor
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);

					String respNom_den_rfc = nom_den_rfc
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);
					
					xmlRazon.addProperty("tipo", "Razón Social");
					xmlRazon.addProperty("XML", respRazMor);
					
					xmlRfc.addProperty("tipo", "RFC");
					xmlRfc.addProperty("XML", respRfcMor);

					xmlCompleto.addProperty("tipo", "Razón Social + RFC");
					xmlCompleto.addProperty("XML", respNom_den_rfc);
					
					responseXML.add(xmlRazon);
					responseXML.add(xmlRfc);
					responseXML.add(xmlCompleto);
					
					_log.info("Resourse Moral-RazonSocial: " + razMor);
					_log.info("Request Moral-RazonSocial: " + respRazMor);
					_log.info("Resourse Moral-RFC: " + rfcMor);
					_log.info("Request Moral-RFC: " + respRfcMor);
					_log.info("Resourse RazónSocial+RFC: " + nom_den_rfc);
					_log.info("Request RazónSocial+RFC: " + respNom_den_rfc);
					
					String unionQQ = "Persona Moral - Razon Social: " + respRazMor +
							", RFC:" + respRfcMor + ", RazónSocial+RFC:" + respNom_den_rfc;
					
					vobo.setP_resultadoQeQ(HtmlUtil.escape( unionQQ));
					
				}else{
					
					JsonObject xmlNombreCompleto = new JsonObject();
					JsonObject xmlNombreRfc = new JsonObject();
					JsonObject xmlApPRfc = new JsonObject();
					JsonObject xmlApMRfc = new JsonObject();
					
					
					
					WebResource nomCom = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_FISICA)
							.queryParam("nombre", HtmlUtil.escape(responseEmision.getDatosCliente().getNombre().toLowerCase()))
							.queryParam("paterno", HtmlUtil.escape(responseEmision.getDatosCliente().getAppPaterno().toLowerCase()))
							.queryParam("materno",HtmlUtil.escape( responseEmision.getDatosCliente().getAppMaterno().toLowerCase()));					

					WebResource nomRFC = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_FISICA)
							.queryParam("nombre", HtmlUtil.escape(responseEmision.getDatosCliente().getNombre().toLowerCase()))
							.queryParam("rfc", responseEmision.getDatosCliente().getRfc());					

					WebResource apPRFC = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_FISICA)
							.queryParam("paterno", HtmlUtil.escape(responseEmision.getDatosCliente().getAppPaterno().toLowerCase()))
							.queryParam("rfc", responseEmision.getDatosCliente().getRfc());					

					WebResource apMRFC = client.resource(LiabilityQuotation73PortletKeys.QQ_URL_FISICA)
							.queryParam("materno", HtmlUtil.escape(responseEmision.getDatosCliente().getAppMaterno().toLowerCase()))
							.queryParam("rfc", responseEmision.getDatosCliente().getRfc());
					
					String rnomCom = nomCom
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);
					
					String rnomRFC = nomRFC
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);
					
					String rapPRFC = apPRFC
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);
					
					String rapMRFC = apMRFC
							.accept(MediaType.APPLICATION_XML)
							.header("user-agent", "")
							.cookie(cookies.get(0))
							.cookie(cookies.get(1))
							.cookie(cookies.get(2))
							.get(String.class);
					
					xmlNombreCompleto.addProperty("tipo", "Nombre Completo");
					xmlNombreCompleto.addProperty("XML", rnomCom);
					
					xmlNombreRfc.addProperty("tipo", "Nombre-RFC");
					xmlNombreRfc.addProperty("XML", rnomRFC);
					
					xmlApPRfc.addProperty("tipo", "ApP-RFC");
					xmlApPRfc.addProperty("XML", rapPRFC);
					
					xmlApMRfc.addProperty("tipo", "ApM-RFC");
					xmlApMRfc.addProperty("XML", rapMRFC);
					
					responseXML.add(xmlNombreCompleto);
					responseXML.add(xmlNombreRfc);
					responseXML.add(xmlApPRfc);
					responseXML.add(xmlApMRfc);
					
					_log.info("Resourse Nombre Completo: " + nomCom);
					_log.info("Request Nombre Completo: " + rnomCom);
					_log.info("Resourse Nombre-RFC: " + nomRFC);
					_log.info("Request Nombre-RFC: " + rnomRFC);
					_log.info("Resourse ApP-RFC: " + apPRFC);
					_log.info("Request ApP-RFC: " + rapPRFC);
					_log.info("Resourse ApM-RFC: " + apMRFC);
					_log.info("Request ApM-RFC: " + rapMRFC);
					
					String unionQQ = "Persona Fisica - Nombre complero: " + rnomCom +
							", Nombre+RFC:" + rnomRFC + ", ApP+RFC:" + rapPRFC + "ApM+RFC" + rapMRFC; 
					
					vobo.setP_resultadoQeQ(HtmlUtil.escape(unionQQ));
									
				}
				
				codigo = 0;
				respuesta.addProperty("code", codigo);
				respuesta.addProperty("msg", "ok");
				respuesta.add("listaXML", responseXML);
				System.out.println("respuestaQQ: " + respuesta.toString());

			}
			
		} catch (Exception e) {
			// TODO: handle exception
			codigo = 5;
			respuesta.addProperty("code", codigo);
			respuesta.addProperty("msg", "Error de conexión");
			e.printStackTrace();
			_log.error(e.toString());
		}
		
		actionRequest.setAttribute("responseQuienQuien", respuesta);
	}
	
	private void fValidarRetroactividad(int idPerfilUser, ActionRequest actionRequest){
		ValidaResponse respuesta = new ValidaResponse();
		RetroactividadRequest rr = new RetroactividadRequest();
		rr.setIdPerfil(idPerfilUser);
		rr.setP_cotizacion((int)infCotizacion.getCotizacion());
		rr.setP_version(infCotizacion.getVersion());
		try {
			respuesta = _ServicePaso4.wsValidarRetroactividad(rr, infCotizacion.getPantalla(), user.getScreenName());
			
		} catch (Exception e) {
			// TODO: handle exception
			_log.error("Error en ws Valida retroactividad");
		}
		
		actionRequest.setAttribute("infocotizacionExpiro", respuesta.getMsg());
		actionRequest.setAttribute("cotizacionExpiro", respuesta.getCode());
		
	}
	
	private void recuperaInfoPaso1(ActionRequest actionRequest) {
		try {
			final PortletSession psession = actionRequest.getPortletSession();
			Gson gson = new Gson();
			String auxNombre = "LIFERAY_SHARED_F=" + infCotizacion.getFolio() + "_C="
					+ infCotizacion.getCotizacion() + "_V=" + infCotizacion.getVersion()
					+ "_DATOSP1";
			System.err.println("SESION REQUEST: " + auxNombre);
			
			String datosP1 = (String) psession.getAttribute(auxNombre, PortletSession.APPLICATION_SCOPE);
			
			if (Validator.isNotNull(datosP1)) {				
				System.err.println("SESION RESPONSE: " + datosP1);
				
				infoPaso1 = gson.fromJson(datosP1, InfoAuxPaso1.class);
				System.err.println("infoPaso1: " + infoPaso1);
				actionRequest.setAttribute("datosP1", datosP1);
			}
			else{
				System.err.println("CAMBIAR VALOR COTIZACION");
				infCotizacion.setModo(ModoCotizacion.ERROR);
			}
		} catch (Exception e) {
			// TODO: handle exception
			infoPaso1 = new InfoAuxPaso1();
			e.printStackTrace();
			SessionErrors.add(actionRequest, "errorConocido");
			actionRequest.setAttribute("errorMsg", "Error al cargar las ubicaciones");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}
	}
	
	
	void validaModoCot(ActionRequest actionRequest){
	int respuesta = 0;
		switch (infCotizacion.getModo()) {
			case ALTA_ENDOSO:
				respuesta = ModoAuxiliar.esEndoso.getModoCotizacion();
				break;
			case BAJA_ENDOSO:
				respuesta = ModoAuxiliar.esEndoso.getModoCotizacion();
				break;
			case EDITAR_ALTA_ENDOSO:
				respuesta = ModoAuxiliar.esEndoso.getModoCotizacion();
				break;
			case EDITAR_BAJA_ENDOSO:
				respuesta = ModoAuxiliar.esEndoso.getModoCotizacion();
				break;

			case RENOVACION_AUTOMATICA:
				respuesta = ModoAuxiliar.esRenovacion.getModoCotizacion();
			default:
				respuesta = ModoAuxiliar.esCotizacion.getModoCotizacion();
				break;
		}
		actionRequest.setAttribute("ModoCot", respuesta);
	}
	
	
	private void llenaCatalogo(ActionRequest actionRequest) {
		// TODO Auto-generated method stub
		ListaRegistro denomList = fGetCatalogos(CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_DENOMINACION,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(), infCotizacion.getPantalla());
		ListaRegistro listCargo = fGetCatalogos(CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_CARGO_PUESTO,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(), infCotizacion.getPantalla());
		ListaRegistro listaGiroVulne = fGetCatalogos(CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_GIRO_VULNERABLE,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(), infCotizacion.getPantalla());
		ListaRegistro listaNacionalidad = fGetCatalogos(CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_NACIONALIDAD,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(), infCotizacion.getPantalla());
		ListaRegistro listaRegimen = fGetCatalogos(CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_REGIMEN,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(), infCotizacion.getPantalla());
		ListaRegistro listaCatCoaseguro = fGetCatalogos(
				CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_COASEGURO,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
				infCotizacion.getPantalla());
		
		actionRequest.setAttribute("denomList", denomList.getLista());
		actionRequest.setAttribute("regimenList", listaRegimen.getLista());
		actionRequest.setAttribute("listCargo", listCargo.getLista());
		actionRequest.setAttribute("listaGiroVulne", listaGiroVulne.getLista());
		actionRequest.setAttribute("listaNacionalidad", listaNacionalidad.getLista());
		
		Gson gson = new Gson();
		
		actionRequest.setAttribute("listaCatCoaseguro", gson.toJson(listaCatCoaseguro.getLista()));
	}
	
	private void llenaLogVoBo(ActionRequest actionRequest) {
		// TODO Auto-generated method stub
		vobo.setP_cotizacion((int) infCotizacion.getCotizacion());
		vobo.setP_version(infCotizacion.getVersion());
		vobo.setP_usuario(user.getScreenName());
		vobo.setP_idpersona(responseEmision.getDatosCliente().getIdPersona());
		vobo.setP_tipoVOBO(vobo.getP_tipoVOBO() + " ");
		vobo.setP_estatus("SUCCES");
		String lgVoBo =CotizadorModularUtil.objtoJson(vobo);
		
		
		
		System.out.println(vobo.toString());
		System.out.println(lgVoBo);
		actionRequest.setAttribute("lgVoBo", lgVoBo);
	}

}
