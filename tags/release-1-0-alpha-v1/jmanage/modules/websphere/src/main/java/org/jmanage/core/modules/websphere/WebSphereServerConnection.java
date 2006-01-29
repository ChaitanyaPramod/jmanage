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

package org.jmanage.core.modules.websphere;

import org.jmanage.core.modules.JMXServerConnection;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;

import java.util.Set;
import java.util.List;

import com.ibm.websphere.management.AdminClient;

import javax.management.MBeanInfo;
import javax.management.AttributeList;

/**
 * Date: Jan 23, 2005 5:54:49 PM
 * @author Shashank Bellary 
 */
public class WebSphereServerConnection extends JMXServerConnection{

    private final AdminClient adminClient;

    public WebSphereServerConnection(AdminClient adminClient){
        super(adminClient, AdminClient.class);
        assert adminClient != null;
        this.adminClient = adminClient;
    }
}
