package org.jmanage.core.services;

import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.auth.User;

/**
 *
 * date:  Jan 19, 2005
 * @author	Rakesh Kalra
 */
public class ServiceContextImpl implements ServiceContext {

    private User user;

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
