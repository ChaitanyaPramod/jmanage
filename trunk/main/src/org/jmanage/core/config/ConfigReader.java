package org.jmanage.core.config;

import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank
 */
public class ConfigReader implements ConfigConstants{

    /*  Default config file to use  */
    private static final String DEFAULT_CONFIG_FILE_NAME =
            "config/config.xml";
    /*  Single instance */
    private static ConfigReader configReader =
            new ConfigReader(new File(DEFAULT_CONFIG_FILE_NAME));
    /*  Last modified time of the configuration file    */
    private static long lastModified = -1;
    /*  Cache the configuration file    */
    private static Document config = null;

    /**
     * Initilizations done here.
     *
     * @param configFile
     */
    private ConfigReader(File configFile){
        try{
            lastModified = configFile.lastModified();
            config = new SAXBuilder().build(configFile);
        }catch(JDOMException jdEx){
            System.out.println("Error reading config file " +
                    DEFAULT_CONFIG_FILE_NAME);
            jdEx.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public static ConfigReader getInstance(){
        File configFile = new File(DEFAULT_CONFIG_FILE_NAME);
        if(lastModified < configFile.lastModified()){
            /*  Refresh the cache   */
            configReader = new ConfigReader(configFile);
        }
        return configReader;
    }

    /**
     * To retrieve the details of all configured applications.
     * @param applicationConfig
     */
    public void loadApplications(Map applicationConfig){
        List applications =
                config.getRootElement().getChild(APPLICATIONS).getChildren();
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
                applicationConfig.put(config.getApplicationId(), config);
            }
        }
    }
}