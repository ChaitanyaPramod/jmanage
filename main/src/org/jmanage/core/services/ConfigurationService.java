package org.jmanage.core.services;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.util.UserActivityLogger;

/**
 *
 * date:  Jan 9, 2005
 * @author	Rakesh Kalra
 */
public interface ConfigurationService {

    public ApplicationConfigData addApplication(ServiceContext context,
                                                ApplicationConfigData data);
}
