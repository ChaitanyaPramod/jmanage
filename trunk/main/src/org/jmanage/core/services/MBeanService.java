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

import org.jmanage.core.management.ObjectInfo;

import java.util.List;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra
 */
public interface MBeanService {

    public List getMBeans(ServiceContext context,
                          String applicationName,
                          String filter)
            throws ServiceException;

    /**
     * Gets the MBean information.
     *
     * @param context   instance of ServiceContext
     * @param appName   configured application name
     * @param mbeanName mbean name: either configured name or object name
     * @return  instance of ObjectInfo
     * @throws ServiceException
     */
    public ObjectInfo getMBean(ServiceContext context,
                               String appName,
                               String mbeanName)
            throws ServiceException;

    /**
     * @return list of attribute values for given attributes
     */
    public List getAttributes(ServiceContext context,
                              String appName,
                              String mbeanName,
                              String[] attributes)
            throws ServiceException;
}
