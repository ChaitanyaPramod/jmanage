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
import java.util.Properties;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 *
 * date:  Dec 27, 2004
 * @author	Vandana Taneja
 */
public class ConfigureAction extends BaseAction {

    public ActionForward execute (WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response){

        JManageProperties jmanageProperties = JManageProperties.getInstance();
        ConfigureForm configureForm = (ConfigureForm)actionForm;
        jmanageProperties.storeMaxLoginAttempts(configureForm.getMaxLoginAttempts());
        return mapping.findForward(Forwards.SUCCESS);
    }

}
