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
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.data.OperationResultData;
import org.jmanage.core.data.AttributeListData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra, Shashank Bellary
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
     * @return list of all attribute values
     */
    public AttributeListData[] getAttributes(ServiceContext context,
                                             String appName,
                                             String mbeanName)
            throws ServiceException;

    public AttributeListData[] getAttributes(ServiceContext context,
                                             String appName,
                                             String mbeanName,
                                             String[] attributes,
                                             boolean handleCluster)
            throws ServiceException;

    /**
     * Invokes MBean operation
     * @return
     * @throws ServiceException
     */
    public OperationResultData[] invoke(ServiceContext context,
                                        String appName,
                                        String mbeanName,
                                        String operationName,
                                        String[] params)
            throws ServiceException;

    /**
     * Invokes MBean operation
     * @return
     * @throws ServiceException
     */
    public OperationResultData[] invoke(ServiceContext context,
                                        String appName,
                                        ObjectName objectName,
                                        String operationName,
                                        String[] params,
                                        String[] signature)
        throws ServiceException;


    public AttributeListData[] setAttributes(ServiceContext context,
                                             String objName,
                                             String appName,
                                             String[][] attributes)
            throws ServiceException;

    /**
     * Updates MBean attributes at a stand alone application level or at a
     * cluster level.
     *
     * @param context
     * @param request
     * @param objName
     * @param appName
     * @throws ServiceException
     */
    public AttributeListData[] setAttributes(ServiceContext context,
                                             HttpServletRequest request,
                                             String objName,
                                             String appName)
            throws ServiceException;
}
