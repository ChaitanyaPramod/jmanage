package org.jmanage.webui.util;

import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.auth.User;

/**
 *
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class ServiceContextImpl implements ServiceContext {

    private WebContext webContext;

    public ServiceContextImpl(WebContext webContext){
        this.webContext = webContext;
    }

    public User getUser() {
        return webContext.getUser();
    }
}
