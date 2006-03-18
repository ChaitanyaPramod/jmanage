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

import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.config.GraphConfig;

import java.util.List;

/**
 *
 * date:  Jan 9, 2005
 * @author	Rakesh Kalra
 */
public interface ConfigurationService {

    public ApplicationConfigData addApplication(ServiceContext context,
                                                ApplicationConfigData data);

    public List getAllApplications(ServiceContext context);

    public List getConfiguredMBeans(ServiceContext context)
            throws ServiceException;
    public GraphConfig addGraph(ServiceContext context, GraphConfig graphConfig);
}
