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
package org.jmanage.core.services;

import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.data.MBeanData;

import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra
 */
public class MBeanServiceImpl implements MBeanService {

    private static final String DEFAULT_FILTER = "*:*";

    public ArrayList getMBeans(ServiceContext context,
                               String applicationName,
                               String filter)
            throws ServiceException {

        ServerConnection serverConnection =
                ServiceUtils.getServerConnection(applicationName);

        if(filter == null){
            filter = DEFAULT_FILTER;
        }
        Set mbeans =
                serverConnection.queryNames(new ObjectName(filter));
        ArrayList mbeanDataList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator();it.hasNext(); ){
            ObjectName objName = (ObjectName)it.next();
            mbeanDataList.add(new MBeanData(objName.getCanonicalName()));
        }
        return mbeanDataList;
    }
}
