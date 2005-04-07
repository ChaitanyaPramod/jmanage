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

import java.util.StringTokenizer;

/**
 * Date: Mar 13, 2005 10:43:07 PM
 * @author Shashank Bellary 
 */
public class Role {

    private String name;

    public Role(String name) {
        this.name = name;
    }

    /**
     * Check whether this role/group is in the controlled list.
     *
     * @param acl
     * @return
     */
    public boolean canAccess(String acl){
        boolean canAccess = false;
        String authorizedList = ACLStore.getInstance().getProperty(acl);
        /*if acl is not configured, by default there is access */
        if(authorizedList == null)
            return true;
        StringTokenizer tokenizer = new StringTokenizer(authorizedList, ",");
        while(tokenizer.hasMoreTokens()){
            if(getName().equals(tokenizer.nextToken())){
                canAccess = true;
                break;
            }
        }
        return canAccess;
    }

    public String getName() {
        return name;
    }
}
