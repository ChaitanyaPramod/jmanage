package org.jmanage.core.config;

import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;
import org.jmanage.core.crypto.Crypto;

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
     * Invalidate cached information about configured applciations if the
     * configuration file got updated after last read.
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
     */
    public List read(){

        List applications =
                config.getRootElement().getChild(APPLICATIONS).getChildren();
        return getApplicationConfigList(applications);
    }


    private List getApplicationConfigList(List applications){

        final List applicationConfigList = new LinkedList();
        for(Iterator appIterator = applications.iterator(); appIterator.hasNext();){
            Element application = (Element)appIterator.next();
            ApplicationConfig appConfig = null;
            if(APPLICATION.equalsIgnoreCase(application.getName())){
                appConfig = getApplicationConfig(application);
            }else if(APPLICATION_CLUSTER.equals(application.getName())){
                appConfig = getApplicationClusterConfig(application);
            }else{
                assert false:"Invalid element:" + application.getName();
            }
            applicationConfigList.add(appConfig);
        }
        return applicationConfigList;
    }

    private ApplicationConfig getApplicationConfig(Element application){

        List params = application.getChildren(PARAMETER);
        Iterator paramIterator = params.iterator();
        Map paramValues = new HashMap(1);
        while(paramIterator.hasNext()){
            Element param = (Element)paramIterator.next();
            paramValues.put(param.getChildTextTrim(PARAMETER_NAME),
                    param.getChildTextTrim(PARAMETER_VALUE));
        }

        final String encryptedPassword =
                application.getAttributeValue(PASSWORD);
        String password = null;
        if(encryptedPassword != null){
            password = Crypto.decrypt(encryptedPassword);
        }
        Integer port = null;
        if(application.getAttributeValue(PORT) != null){
            port = new Integer(application.getAttributeValue(PORT));
        }
        ApplicationConfig config =
                ApplicationConfigFactory.create(
                        application.getAttributeValue(APPLICATION_ID),
                        application.getAttributeValue(APPLICATION_NAME),
                        application.getAttributeValue(APPLICATION_TYPE),
                        application.getAttributeValue(HOST),
                        port,
                        application.getAttributeValue(URL),
                        application.getAttributeValue(USERNAME),
                        password,
                        paramValues);

        config.setMBeans(getMBeanConfigList(application));
        return config;
    }

    private ApplicationConfig getApplicationClusterConfig(Element application){

        ApplicationClusterConfig config =
                new ApplicationClusterConfig(
                        application.getAttributeValue(APPLICATION_ID),
                        application.getAttributeValue(APPLICATION_NAME));

        if(application.getChild(APPLICATIONS) != null){
            List applications =
                    application.getChild(APPLICATIONS).getChildren();
            List appConfigList = getApplicationConfigList(applications);
            config.addAllApplications(appConfigList);
        }

        config.setMBeans(getMBeanConfigList(application));
        return config;
    }

    private List getMBeanConfigList(Element application){
        /* read mbeans */
        List mbeanConfigList = new LinkedList();
        if(application.getChild(MBEANS) != null){
            List mbeans = application.getChild(MBEANS).getChildren(MBEAN);
            for(Iterator it=mbeans.iterator(); it.hasNext(); ){
                Element mbean = (Element)it.next();
                MBeanConfig mbeanConfig =
                        new MBeanConfig(mbean.getAttributeValue(MBEAN_NAME),
                                mbean.getChildTextTrim(MBEAN_OBJECT_NAME));
                mbeanConfigList.add(mbeanConfig);
            }
        }
        return mbeanConfigList;
    }
}