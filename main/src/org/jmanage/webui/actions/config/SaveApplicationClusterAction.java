package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.ApplicationClusterForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.ApplicationClusterConfig;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 *
 * date:  Oct 17, 2004
 * @author	Rakesh Kalra
 */
public class SaveApplicationClusterAction extends BaseAction {

    /* todo: add and update operations should be moved to ApplicationConfigManager
        and should be synchronized */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        ApplicationClusterForm clusterForm = (ApplicationClusterForm)actionForm;
        String[] childAppIds =
                Utils.csvToStringArray(clusterForm.getSelectedChildApplications());
        /* build list of new child applications */
        List newChildApplications = getNewChildApplications(childAppIds);

        String applicationId = clusterForm.getApplicationId();
        if(applicationId != null){
            /* update existing application */
            ApplicationClusterConfig clusterConfig =
                    (ApplicationClusterConfig)
                    ApplicationConfigManager.getApplicationConfig(applicationId);
            assert clusterConfig != null;
            clusterConfig.setName(clusterForm.getName());
            final List oldChildApplications = clusterConfig.getApplications();
            /* add applications outside the cluster
            (applications that are no longer in the cluster) */
            for(Iterator it=oldChildApplications.iterator();it.hasNext();){
                ApplicationConfig appConfig = (ApplicationConfig)it.next();
                if(!newChildApplications.contains(appConfig)){
                    ApplicationConfigManager.addApplication(appConfig);
                    appConfig.setClusterConfig(null);
                }
            }
            /* remove applications outside the cluster
            (applications that are now part of cluster) */
            for(Iterator it=newChildApplications.iterator();it.hasNext();){
                ApplicationConfig appConfig = (ApplicationConfig)it.next();
                if(!oldChildApplications.contains(appConfig)){
                    ApplicationConfigManager.deleteApplication(appConfig);
                    appConfig.setClusterConfig(clusterConfig);
                }
            }
            clusterConfig.setApplications(newChildApplications);
            ApplicationConfigManager.updateApplication(clusterConfig);

        }else{
            /* add new application cluster */
            ApplicationClusterConfig clusterConfig =
                    new ApplicationClusterConfig(
                            ApplicationConfig.getNextApplicationId(),
                            clusterForm.getName());
            for(Iterator it=newChildApplications.iterator();it.hasNext();){
                ApplicationConfig appConfig = (ApplicationConfig)it.next();
                appConfig.setClusterConfig(clusterConfig);
                ApplicationConfigManager.deleteApplication(appConfig);
            }
            clusterConfig.setApplications(newChildApplications);
            /* remove from stand-alone list */

            ApplicationConfigManager.addApplication(clusterConfig);
        }

        return mapping.findForward(Forwards.SUCCESS);
    }

    private List getNewChildApplications(String[] childAppIds){
        /* build list of new child applications */
        List newChildApplications = new LinkedList();
        if(childAppIds != null){
            for(int i=0; i < childAppIds.length; i++){
                ApplicationConfig newChildApplication =
                        ApplicationConfigManager.getApplicationConfig(childAppIds[i]);
                newChildApplications.add(
                        newChildApplication);
            }
        }
        return newChildApplications;
    }
}
