package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.core.util.CoreUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Date: Nov 15, 2004 11:28:24 PM
 * @author Shashank Bellary 
 */
public class ShowUserActivityAction extends BaseAction{
    /*  Activity log file   */
    private static final String USER_ACTIVITY_LOG_FILE_NAME =
            CoreUtils.getLogDir() + "/userActivityLog.txt";
    /**
     * Show activities performed by all the users of jManage.
     *
     * @param context
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        BufferedReader reader =
                new BufferedReader(new FileReader(USER_ACTIVITY_LOG_FILE_NAME));
        List activities = new ArrayList(1);
        String activity = null;
        while((activity = reader.readLine()) != null){
            activities.add(activity);
        }
        request.setAttribute(RequestAttributes.USER_ACTIVITIES, activities);
        return mapping.findForward(Forwards.SUCCESS);
    }
}