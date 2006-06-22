/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.webui.dashboard.framework;

import org.jmanage.core.config.ApplicationConfig;
import org.jdom.Element;

import java.util.List;
import java.util.Map;

/**
 * Date: Jun 21, 2006 7:07:19 PM
 *
 * @ author: Shashank Bellary
 */
public abstract class BaseDashboardQualifier implements DashboardQualifier{
    private Properties properties;

    public void init(Element qualifyingCriteria) {
        /* initialize properties */
        properties = new Properties(qualifyingCriteria);
        init(properties);
    }
    
    protected abstract void init(Map<String, String> properties);
}
