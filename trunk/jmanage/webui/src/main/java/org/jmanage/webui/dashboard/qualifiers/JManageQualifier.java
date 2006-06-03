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
package org.jmanage.webui.dashboard.qualifiers;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationType;
import org.jmanage.webui.dashboard.framework.DashboardQualifier;

/**
 * Date: May 13, 2006 5:01:15 PM
 *
 * @author Shashank Bellary
 */
public class JManageQualifier implements DashboardQualifier {
    
    private static final String TYPE_JMANAGE = "jManage";
    
    public boolean isQualified(ApplicationConfig applicationConfig) {
        ApplicationType applicationType = applicationConfig.getApplicationType();
        return TYPE_JMANAGE.equals(applicationType.getName());
    }
}
