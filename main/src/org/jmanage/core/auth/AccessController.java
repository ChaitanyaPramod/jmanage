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

/**
 * Date: Mar 14, 2005 12:16:11 AM
 * @author Shashank Bellary 
 */
public class AccessController {

    /**
     *
     * @param user
     * @param acl
     * @return
     */
    public static boolean canAccess(User user, String acl){
        if(user.canAccess(acl))
            return true;
        else
            throw new UnAuthorizedAccessException("Insufficient Privileges");
    }
}
