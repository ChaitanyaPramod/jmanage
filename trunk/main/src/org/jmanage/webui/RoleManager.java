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
