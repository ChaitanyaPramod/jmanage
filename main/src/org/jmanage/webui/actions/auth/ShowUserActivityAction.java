/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.services.AccessController;
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
    private final String USER_ACTIVITY_LOG_FILE_PATH =
            UserActivityLogger.getActivityLogFilePath();

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
        AccessController.checkAccess(context.getServiceContext(), ACL_VIEW_USER_ACTIVITY);
        BufferedReader reader =
                new BufferedReader(new FileReader(USER_ACTIVITY_LOG_FILE_PATH));
        List activities = new ArrayList(1);
        String activity = null;
        while((activity = reader.readLine()) != null){
            activities.add(activity);
        }
        request.setAttribute(RequestAttributes.USER_ACTIVITIES, activities);
        return mapping.findForward(Forwards.SUCCESS);
    }
}