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
package org.jmanage.core.auth;

import org.jmanage.core.services.ServiceContext;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 *
 * Date:  Apr 8, 2005
 * @author	Rakesh Kalra
 */
public class ACL {

    private final String name;
    /* the no context authorized list (if specified) */
    private List authorizedList;
    private List contextList = new LinkedList();

    public ACL(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setAuthorizedList(List authorizedList){
        assert authorizedList != null;
        assert this.authorizedList == null:"authorized list is already specified";
        this.authorizedList = authorizedList;
    }

    public List getAuthorizedList(){
        return authorizedList;
    }

    public List getAuthorizedList(ACLContext context){

        if(context != null){
            for(Iterator it=contextList.iterator(); it.hasNext(); ){
                ACLContextWrapper wrapper = (ACLContextWrapper)it.next();
                if(wrapper.context.equals(context)){
                    return wrapper.authorizedList;
                }
            }
        }

        return getAuthorizedList();
    }

    public void add(ACLContext context, List authorizedList){
        assert context != null;
        assert authorizedList != null;
        contextList.add(new ACLContextWrapper(context, authorizedList));
    }

    /**
     * TODO: there is an issue with this implementation:
     * if a user has the same name as a role name, user will get access
     * even though user is not in that role.
     *
     * @param context
     * @param user
     * @return
     */
    public boolean isAuthorized(ACLContext context, User user) {
        List authorizedList = getAuthorizedList(context);
        for(Iterator it=authorizedList.iterator(); it.hasNext(); ){
            String authorized = (String)it.next();
            if(user.getName().equals(authorized) || user.hasRole(authorized)){
                return true;
            }
        }
        return false;
    }

    private class ACLContextWrapper {
        ACLContext context;
        List authorizedList;

        ACLContextWrapper(ACLContext context, List authorizedList){
            this.context = context;
            this.authorizedList = authorizedList;
        }
    }
}
