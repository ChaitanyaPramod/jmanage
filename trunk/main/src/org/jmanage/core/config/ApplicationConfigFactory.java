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
