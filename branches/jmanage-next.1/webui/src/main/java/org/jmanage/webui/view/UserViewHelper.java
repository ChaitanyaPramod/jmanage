/**
 * jManage - Open Source Application Management
 * Copyright (C) 2006 jManage.org.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 */
package org.jmanage.webui.view;

import javax.servlet.http.HttpServletRequest;

import org.jmanage.core.services.AccessController;
import org.jmanage.webui.util.WebContext;

/**
 * View helper functions for JSP.
 *  
 * @author rkalra
 */
public class UserViewHelper {
    
    public static boolean hasAdminAccess(HttpServletRequest request){
        WebContext webContext = null;
        try{
            webContext = WebContext.get(request);
            return AccessController.hasAdminAccess(webContext.getServiceContext());
        }finally{
            if(webContext != null)
                webContext.releaseResources();
        }
    }
}
