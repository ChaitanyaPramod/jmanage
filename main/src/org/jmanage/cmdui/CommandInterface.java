package org.jmanage.cmdui;

import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.auth.User;
import org.jmanage.core.data.ApplicationConfigData;

/**
 *
 * date:  Jan 19, 2005
 * @author	Rakesh Kalra
 */
public class CommandInterface {

    static{
        /* initialize ServiceFactory */
        ServiceFactory.init(ServiceFactory.MODE_REMOTE);
    }

    public static void main(String[] args){
        ConfigurationService configService =
                ServiceFactory.getConfigurationService();

        ApplicationConfigData configData = new ApplicationConfigData();
        configData.setHost("localhost");
        configData.setPort(new Integer(7001));
        configData.setName("testApp");
        configData.setType("weblogic");
        configData.setUsername("system");
        configData.setPassword("12345678");

        configData = configService.addApplication(getServiceContext(),
                configData);
        System.out.println("ApplicationId:" + configData.getApplicationId());
        System.out.println("Host:" + configData.getHost());
        System.out.println("Port:" + configData.getPort());
        System.out.println("Name:" + configData.getName());
        System.out.println("Type:" + configData.getType());
        System.out.println("Username:" + configData.getUsername());
        System.out.println("Password:" + configData.getPassword());
    }

    private static ServiceContext getServiceContext(){
        ServiceContextImpl context = new ServiceContextImpl();
        User user = new User("admin", null, null, null, 0);
        context.setUser(user);
        return context;
    }
}
