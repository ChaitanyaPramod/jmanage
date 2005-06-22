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
import org.jmanage.core.management.ObjectAttribute;
import org.jmanage.core.data.OperationResultData;
import org.jmanage.core.data.AttributeListData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra
 * @author  Shashank Bellary
 */
public interface MBeanService {

    public List queryMBeans(ServiceContext context,
                          String filter)
            throws ServiceException;

    /**
     * Gets the MBean information.
     *
     * @param context   instance of ServiceContext
     * @return  instance of ObjectInfo
     * @throws ServiceException
     */
    public ObjectInfo getMBeanInfo(ServiceContext context)
            throws ServiceException;

    /**
     * @return list of all attribute values
     */
    public AttributeListData[] getAttributes(ServiceContext context)
            throws ServiceException;

    public AttributeListData[] getAttributes(ServiceContext context,
                                             String[] attributes,
                                             boolean handleCluster)
            throws ServiceException;

    public ObjectAttribute getObjectAttribute(ServiceContext context,
                                              String attribute)
            throws ServiceException;

    /**
     * Invokes MBean operation
     * @return
     * @throws ServiceException
     */
    public OperationResultData[] invoke(ServiceContext context,
                                        String operationName,
                                        String[] params)
            throws ServiceException;

    /**
     * Invokes MBean operation
     * @return
     * @throws ServiceException
     */
    public OperationResultData[] invoke(ServiceContext context,
                                        String operationName,
                                        String[] params,
                                        String[] signature)
        throws ServiceException;


    public AttributeListData[] setAttributes(ServiceContext context,
                                             String[][] attributes)
            throws ServiceException;

    /**
     * Updates MBean attributes at a stand alone application level or at a
     * cluster level.
     *
     * @param context
     * @param request
     * @throws ServiceException
     */
    public AttributeListData[] setAttributes(ServiceContext context,
                                             HttpServletRequest request)
            throws ServiceException;
}