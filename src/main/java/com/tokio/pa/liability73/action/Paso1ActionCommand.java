package com.tokio.pa.liability73.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.tokio.pa.liability73.constants.LiabilityQuotation73PortletKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = { 
				"javax.portlet.init-param.copy-request-parameters=true",
				"javax.portlet.name=" + LiabilityQuotation73PortletKeys.LiabilityQuotation,
				"mvc.command.name=/liability/actionPaso1"
				},
		service = MVCActionCommand.class
		)

public class Paso1ActionCommand extends BaseMVCActionCommand{
	
	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		/*
		Enumeration<String> en = actionRequest.getParameterNames();
		while (en.hasMoreElements()) {
			Object objOri = en.nextElement();
			String param = (String) objOri;
			String value = actionRequest.getParameter(param);
			System.err.println("[ ---> " +param + " : " + value );
		}
		*/
		
		actionResponse.getRenderParameters().setValue("jspPage", "/jsp/paso1.jsp");
		actionResponse.getRenderParameters().setValue("jspPage", "/jsp/paso1.jsp");
	}

}
