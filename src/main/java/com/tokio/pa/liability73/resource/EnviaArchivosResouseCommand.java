package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.DocumentoResponse;
import com.tokio.pa.cotizadorModularServices.Bean.IdCarpetaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso3;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
		"mvc.command.name=/cotizadores/paso3/cargaSlipWordURL"
	},
	service = MVCResourceCommand.class
)

public class EnviaArchivosResouseCommand extends BaseMVCResourceCommand{
	@Reference
	CotizadorGenerico _ServiceGenerico;
	@Reference
	private DLAppService _dlAppService;
	
	private static final Log _log = LogFactoryUtil.getLog(EnviaArchivosResouseCommand.class);
	
	InfoCotizacion infCot = null;
	
	String errorServicios = "";
	
	@Reference
	CotizadorPaso3 _ServicePaso3;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		/*
		generaInfoCot(resourceRequest);
		
		try {
			
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			String p_usuario = user.getScreenName();
			
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
			
			SimpleResponse responseValidaciones = _ServicePaso3.validacionesPaso3(
					(int)infCot.getCotizacion(), infCot.getVersion(), idPerfilUser, p_usuario,
					LiabilityQuotation73PortletKeys.LiabilityQuotation);
			
			if(responseValidaciones.getCode() == 0) {
		*/
				enviaArchivos(resourceRequest, resourceResponse);
		/*
			}
			else {
				Gson gson = new Gson();
				String jsonString = gson.toJson(responseValidaciones);
				
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(jsonString);
			}
		}
		catch(Exception e) {
			
		}
		*/
	}
	
	void enviaArchivos(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
		
		try {
			
			Gson gson = new Gson();
			
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			String p_usuario = user.getScreenName();
			
			generaInfoCot(resourceRequest);
			
			String auxiliarDoc = HtmlUtil.unescape(ParamUtil.getString(resourceRequest, "auxiliarDoc"));
			
			int idCatalogo = ParamUtil.getInteger(resourceRequest, "idcatalogo");
			
			JSONObject jsonObj;
			jsonObj = JSONFactoryUtil.createJSONObject(auxiliarDoc);
			UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(resourceRequest);
			
			IdCarpetaResponse carpeta = _ServiceGenerico.SeleccionaIdCarpeta((int)infCot.getFolio(), (int)infCot.getCotizacion(),
					infCot.getVersion());
			
			JsonArray jsonDocumentos = new JsonArray();
			
			String nombre = "file-0";
			File file = uploadRequest.getFile(nombre);
			String mimeType = uploadRequest.getContentType(nombre);

			JsonObject enviaDocumentos = new JsonObject();

			JSONObject jsonObj2;
			jsonObj2 = JSONFactoryUtil.createJSONObject(jsonObj.getString("plantillaSlip"));
			
			String nom = jsonObj2.getString("nom").replace(" ", "_");

			enviaDocumentos.addProperty("nombre", nom);
			enviaDocumentos.addProperty("extension", jsonObj2.getString("ext"));
			enviaDocumentos.addProperty("idCarpeta", carpeta.getIdCarpeta());
			enviaDocumentos.addProperty("idDocumento", "0");
			enviaDocumentos.addProperty("idCatalogoDetalle", idCatalogo);
			
			float n = file.length() / 1024 / 1024;
			float n64 = 4 * (n / 3) + (n % 3 != 0 ? 4 : 0);
			
			Map<String, Object> info = null;
			
			if (n64 > 1.49) {
				info = guardaDocumentos(resourceRequest, file, nom, mimeType,
						jsonObj2.getString("ext"));
				enviaDocumentos.addProperty("documento", "");
				enviaDocumentos.addProperty("url", (String) info.get("url"));
				enviaDocumentos.addProperty("leer", 1);
			} else {
				enviaDocumentos.addProperty("documento",
						Base64.encode(FileUtils.readFileToByteArray(file)));
				enviaDocumentos.addProperty("url", "");
				enviaDocumentos.addProperty("leer", 0);
			}

			
			jsonDocumentos.add(enviaDocumentos);
			
			System.out.println("json :" + enviaDocumentos.toString());
			
			try {
				DocumentoResponse respuesta = _ServiceGenerico.wsDocumentos(
						CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
						CotizadorModularServiceKey.TMX_CTE_TRANSACCION_POST,
						jsonDocumentos, 1, "COTIZACIONES", (int) infCot.getCotizacion(), "",
						p_usuario, LiabilityQuotation73PortletKeys.LiabilityQuotation);
				
				if (n64 > 1.49) {
					if (respuesta.getMsg().toLowerCase().trim().equals("ok")) {
						elimianArchivo((long) info.get("idDoc"));
					}
				}
				
				String responseString = gson.toJson(respuesta);
				
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(responseString);
			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error en el archivo");
		}
	}
	
	private void generaInfoCot(ResourceRequest resourceRequest){
		
		Gson gson = new Gson();
		
		String infoCot = ParamUtil.getString(resourceRequest, "infoCot");
		System.out.println("infoCot String :" + infoCot);
		infCot = gson.fromJson(infoCot, InfoCotizacion.class);
		System.out.println("infCot objeto: " + infCot);
		
	}
	
	Map<String, Object> guardaDocumentos(ResourceRequest resourceRequest, File file, String nombre, String mimeType, String ext){
		try {
			
			Map<String, Object> respuesta = new HashMap<String, Object>();

			long idGroup = PortalUtil.getScopeGroupId(resourceRequest);
			ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), resourceRequest);
			
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			
			
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			
			String cotizacion = ParamUtil.getString(resourceRequest, "cotizacion");
			String version = ParamUtil.getString(resourceRequest, "version");
			String folio = ParamUtil.getString(resourceRequest, "folio");
			String url = ParamUtil.getString(resourceRequest, "url2");
//			

			
			String aux2 = user.getScreenName() + "-" + nombre + "-F_" + folio + "-C_" + cotizacion + "-V_" + version;
			
			DLFolder fCotizadores = DLFolderLocalServiceUtil.getFolder(idGroup, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					"Documentos_Cotizadores");
			
			
			FileEntry fileEntry = _dlAppService.addFileEntry(idGroup, fCotizadores.getFolderId(), nombre + "." + ext,
					mimeType, nombre + "." + ext, aux2, "hi", file, serviceContext);
			
			
			String urlDoc = url + "/documents/" + idGroup + "/" + fileEntry.getFolderId() + "/" + fileEntry.getFileName()
			+ "/" + fileEntry.getUuid();
			
			System.out.println(urlDoc);
			
			respuesta.put("url", urlDoc); 
			respuesta.put("idDoc", fileEntry.getFileEntryId()); 
			return respuesta;
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			agregaErrorString(" - Error al almacenar los documentos");
			e.printStackTrace();
			return null;
		}
		
	}
	
	void elimianArchivo(long idDoc){
		try {
			_dlAppService.deleteFileEntry(idDoc);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void agregaErrorString(String msgErr){
		if(Validator.isNull(errorServicios)){
			errorServicios = msgErr;
		}else{
			errorServicios += ",<br>" +  errorServicios;
		}
	}
	
}
