package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ConfigureForm;
import org.jmanage.core.config.JManageProperties;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Dec 27, 2004
 * @author	Vandana Taneja
 */
public class ShowConfigureAction extends BaseAction{

    public ActionForward execute(WebContext context,
                                    ActionMapping mapping,
                                    ActionForm actionForm,
                                    HttpServletRequest request,
                                    HttpServletResponse response){

           ConfigureForm confgForm = (ConfigureForm)actionForm;
           final JManageProperties jManageProperties = JManageProperties.getInstance();
           final int MAX_LOGIN_ATTEMPTS_ALLOWED =
                Integer.parseInt(jManageProperties.
                    getProperty(JManageProperties.maxLoginAttempts));
           confgForm.setMaxLoginAttempts(MAX_LOGIN_ATTEMPTS_ALLOWED);
           return mapping.findForward(Forwards.SUCCESS);
       }

}
