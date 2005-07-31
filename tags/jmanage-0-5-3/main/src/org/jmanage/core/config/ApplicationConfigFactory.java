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
package org.jmanage.core.config;

import org.jmanage.core.module.ModuleConfig;
import org.jmanage.core.module.ModuleRegistry;

import java.util.Map;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigFactory {

    public static ApplicationConfig create(String applicationId,
                                           String name,
                                           String type,
                                           String host,
                                           Integer port,
                                           String url,
                                           String username,
                                           String password,
                                           Map paramValues){

        try {
            final ModuleConfig moduleConfig = ModuleRegistry.getModule(type);
            final MetaApplicationConfig metaAppConfig =
                    moduleConfig.getMetaApplicationConfig();
            final Class metaConfigClass =
                    Class.forName(metaAppConfig.getApplicationConfigClassName(),
                            true, moduleConfig.getClassLoader());
            final ApplicationConfig appConfig =
                    (ApplicationConfig)metaConfigClass.newInstance();
            appConfig.setApplicationId(applicationId);
            appConfig.setType(type);
            appConfig.setName(name);
            appConfig.setHost(host);
            appConfig.setPort(port);
            appConfig.setURL(url);
            appConfig.setUsername(username);
            appConfig.setPassword(password);
            appConfig.setParamValues(paramValues);
            return appConfig;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
