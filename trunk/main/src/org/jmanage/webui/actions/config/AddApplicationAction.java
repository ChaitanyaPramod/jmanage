package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.WeblogicApplicationForm;
import org.jmanage.core.config.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra
 */
public class AddApplicationAction extends BaseAction {

    /**
     * Add a new application
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        WeblogicApplicationForm appForm = (WeblogicApplicationForm)actionForm;
        //todo: Need something better
        String appId = String.valueOf(System.currentTimeMillis());
        ApplicationConfig config =
                ApplicationConfigFactory.create(appId, appForm.getName(),
                        ApplicationType.WEBLOGIC,
                        appForm.getHost(),
                        new Integer(appForm.getPort()),
                        null,
                        appForm.getUsername(),
                        appForm.getPassword(),
                        null);

        ApplicationConfigManager.addApplication(config);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
