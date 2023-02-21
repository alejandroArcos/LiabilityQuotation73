package com.tokio.pa.liability73.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import com.tokio.cotizadorUtilities.excel.Interface.CargaMasivaRC;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import java.io.File;
import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true, 
		property = { 
			"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation, 
			"mvc.command.name=/cotizadores/paso2/cargaMasiva"
		},
		service = MVCResourceCommand.class
)

public class CargaMasivaResourceCommand extends BaseMVCResourceCommand {

	@Reference
	CargaMasivaRC _CUServices;
	
	private static final Log _log = LogFactoryUtil.getLog(CargaMasivaResourceCommand.class);
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		_log.info("Entre a la lectura del archivo");
		
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(resourceRequest);
		
		File carga = uploadRequest.getFile("docAgenSusc");
		
		JsonArray jsonArray = _CUServices.readFile(carga);
		String jsonString = jsonArray.toString();
		
		System.err.println(jsonString);
		
		_log.error(jsonString);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(jsonString);
	}

}
