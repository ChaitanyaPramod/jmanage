package org.jmanage.core.config;

import org.jdom.Element;

import java.util.*;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigManager implements ConfigConstants{

    private static final Map applicationConfigs =
            Collections.synchronizedMap(new HashMap());

    private static final ConfigReader configReader = ConfigReader.getInstance();
    //TODO remove this
    public static final String TEST_APP_ID = "1234";

    static{
        /*  Currently supporting only Weblogic (Weblogic 6.1)   */
        List applications = configReader.getApplications().getChildren();
        Iterator appIterator = applications.iterator();
        while(appIterator.hasNext()){
            Element application = (Element)appIterator.next();
            /*  App clusters not supported  */
            if("application".equalsIgnoreCase(application.getName())){
                List params = application.getChildren(PARAMETERS);
                Iterator paramIterator = params.iterator();
                Map paramValues = new HashMap(1);
                while(paramIterator.hasNext()){
                    Element param = (Element)paramIterator.next();
                    paramValues.put(param.getChildTextTrim(PARAMETER_NAME),
                            param.getChildTextTrim(PARAMETER_VALUE));
                }
                ApplicationConfig config =
                        ApplicationConfigFactory.create(
                                application.getAttributeValue(APPLICATION_ID),
                                application.getAttributeValue(APPLICATION_NAME),
                                application.getAttributeValue(APPLICATION_TYPE),
                                application.getAttributeValue(SERVER_URL),
                                Integer.parseInt(application.getAttributeValue(
                                        SERVER_PORT)),
                                application.getAttributeValue(USERNAME),
                                application.getAttributeValue(PASSWORD),
                                paramValues);
                applicationConfigs.put(config.getApplicationId(), config);
            }
        }
    }

    public static ApplicationConfig getApplicationConfig(String applicationId){
        return (ApplicationConfig)applicationConfigs.get(applicationId);
    }

    /**
     * Retrieve all configured applications.
     *
     * @return
     */
    public static Map getApplications(){
        return applicationConfigs;
    }
}
