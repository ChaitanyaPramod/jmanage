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
package org.jmanage.webui;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.core.auth.User;

import java.util.List;

/**
 * Date : Jul 4, 2004 8:17:24 PM
 * @author Shashank
 */
public class RoleManager {

    /**
     * Authorize the request.
     *
     * @param context
     * @return
     */
    static ActionForward ensureRole(WebContext context, ActionMapping mapping,
                                    String[] roles){
        ActionForward roleForward = null;
        if(context.getUser() == null && !mapping.getPath().equals("/remoteService")){
            roleForward = mapping.findForward(Forwards.LOGIN);
        }else{
            User loggedInUser = context.getUser();
            boolean isAuthorized = isAuthorized(loggedInUser.getRoles(), roles);
            roleForward = isAuthorized ?
                    null :
                    mapping.findForward(Forwards.UN_AUTHORIZED);
        }
        return roleForward;
    }


    /**
     *
     * @param currentUserRoles
     * @param currentRequestRoles
     * @return
     */
    private static boolean isAuthorized(List currentUserRoles,
                                        String[] currentRequestRoles){
        if(currentRequestRoles.length == 0)
            return true;

        for(int i=0; i < currentRequestRoles.length; i++){
            if(currentUserRoles.contains(currentRequestRoles[i])){
                return true;
            }
        }
        return false;
    }
}
