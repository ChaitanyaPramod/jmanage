package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ApplicationClusterForm;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 *
 * date:  Oct 17, 2004
 * @author	Rakesh Kalra
 */
public class ShowApplicationClusterAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {


        ApplicationClusterForm clusterForm =
                (ApplicationClusterForm)actionForm;
        String applicationId = clusterForm.getApplicationId();
        List selectedApplications = null;
        if(applicationId != null){
            ApplicationConfig clusterConfig =
                    ApplicationConfigManager.getApplicationConfig(applicationId);
            assert clusterConfig.isCluster();
            clusterForm.setName(clusterConfig.getName());
            selectedApplications = clusterConfig.getApplications();
        }else{
            selectedApplications = new LinkedList();
        }

        List applications = new LinkedList();
        for(Iterator it=ApplicationConfigManager.getApplications().iterator();
            it.hasNext();){
            ApplicationConfig config = (ApplicationConfig)it.next();
            if(!config.isCluster() && !selectedApplications.contains(config)){
                applications.add(config);
            }
        }
        request.setAttribute("applications", applications);
        request.setAttribute("selectedApplications", selectedApplications);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
