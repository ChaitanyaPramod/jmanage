package org.jmanage.core.services;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.util.UserActivityLogger;

/**
 *
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class ConfigurationServiceImpl implements ConfigurationService {

    public ApplicationConfigData addApplication(ServiceContext context,
                                                   ApplicationConfigData data){

        /* TODO: first check the permission */

        /* do the operation */
        String appId = ApplicationConfig.getNextApplicationId();
        Integer port = data.getPort();
        ApplicationConfig config =
                ApplicationConfigFactory.create(appId, data.getName(),
                        data.getType(),
                        data.getHost(),
                        port,
                        data.getURL(),
                        data.getUsername(),
                        data.getPassword(),
                        null);

        ApplicationConfigManager.addApplication(config);
        data.setApplicationId(appId);

        /* log the operation */
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Added application "+ "\""+config.getName()+"\"");

        return data;
    }
}
