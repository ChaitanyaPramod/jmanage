package org.jmanage.core.config;

import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.util.*;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank
 */
public class ConfigReader implements ConfigConstants{

    /*  Single instance */
    private static ConfigReader configReader =
            new ConfigReader(new File(ConfigConstants.DEFAULT_CONFIG_FILE_NAME));
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
                    ConfigConstants.DEFAULT_CONFIG_FILE_NAME);
            jdEx.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public static ConfigReader getInstance(){
        File configFile = new File(ConfigConstants.DEFAULT_CONFIG_FILE_NAME);
        if(lastModified < configFile.lastModified()){
            /*  Refresh the cache   */
            configReader = new ConfigReader(configFile);
        }
        return configReader;
    }

    /**
     * To retrieve the details of all configured applications.
     */
    public List read(){

        List applicationConfigList = new LinkedList();
        List applications =
                config.getRootElement().getChild(APPLICATIONS).getChildren();
        Iterator appIterator = applications.iterator();
        while(appIterator.hasNext()){
            Element application = (Element)appIterator.next();
            /*  App clusters not supported  */
            if("application".equalsIgnoreCase(application.getName())){
                List params = application.getChildren(PARAMETER);
                Iterator paramIterator = params.iterator();
                Map paramValues = new HashMap(1);
                while(paramIterator.hasNext()){
                    Element param = (Element)paramIterator.next();
                    paramValues.put(param.getChildTextTrim(PARAMETER_NAME),
                            param.getChildTextTrim(PARAMETER_VALUE));
                }

                /* read mbeans */
                List mbeanConfigList = new LinkedList();
                List mbeans = application.getChild(MBEANS).getChildren(MBEAN);
                for(Iterator it=mbeans.iterator(); it.hasNext(); ){
                    Element mbean = (Element)it.next();
                    MBeanConfig mbeanConfig =
                            new MBeanConfig(mbean.getAttributeValue(MBEAN_NAME),
                                    mbean.getChildTextTrim(MBEAN_OBJECT_NAME));
                    mbeanConfigList.add(mbeanConfig);
                }

                ApplicationConfig config =
                        ApplicationConfigFactory.create(
                                application.getAttributeValue(APPLICATION_ID),
                                application.getAttributeValue(APPLICATION_NAME),
                                application.getAttributeValue(APPLICATION_TYPE),
                                application.getAttributeValue(HOST),
                                Integer.parseInt(application.getAttributeValue(
                                        PORT)),
                                application.getAttributeValue(USERNAME),
                                application.getAttributeValue(PASSWORD),
                                paramValues);

                config.setMBeans(mbeanConfigList);
                applicationConfigList.add(config);
            }
        }
        return applicationConfigList;
    }
}