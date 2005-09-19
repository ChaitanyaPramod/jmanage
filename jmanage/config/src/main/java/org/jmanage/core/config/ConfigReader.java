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

import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.CoreUtils;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank
 */
public class ConfigReader implements ConfigConstants{

    private static final Logger logger = Loggers.getLogger(ConfigReader.class);

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
            logger.log(Level.SEVERE, "Error reading config file " +
                    DEFAULT_CONFIG_FILE_NAME);
            throw new RuntimeException(jdEx);
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
        return getApplicationConfigList(applications, null);
    }


    private List getApplicationConfigList(List applications,
                                          ApplicationConfig clusterConfig){

        final List applicationConfigList = new LinkedList();
        for(Iterator appIterator = applications.iterator(); appIterator.hasNext();){
            Element application = (Element)appIterator.next();
            ApplicationConfig appConfig = null;
            if(APPLICATION.equalsIgnoreCase(application.getName())){
                appConfig = getApplicationConfig(application);
                appConfig.setClusterConfig(clusterConfig);
            }else if(APPLICATION_CLUSTER.equals(application.getName())){
                assert clusterConfig == null: "found cluster within a cluster";
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
        config.setAlerts(getAlertsList(application, config));
        config.setGraphs(getGraphConfigList(application, config));
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
            List appConfigList = getApplicationConfigList(applications, config);
            config.addAllApplications(appConfigList);
        }

        config.setMBeans(getMBeanConfigList(application));
        // todo: graphs are not yet supported at the cluster level
        // todo: to enable this, we will have to store applicationId at graph attribute level
        //config.setGraphs(getGraphConfigList(application, config));
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

    private List getGraphConfigList(Element application, ApplicationConfig appConfig){
        /* read graphs */
        List graphConfigList = new LinkedList();
        if(application.getChild(GRAPHS) != null){
            List graphs = application.getChild(GRAPHS).getChildren(GRAPH);
            for(Iterator it=graphs.iterator(); it.hasNext(); ){
                Element graph = (Element)it.next();
                List attributes = graph.getChildren(GRAPH_ATTRIBUTE);
                List attributeConfigList = new LinkedList();
                for(Iterator attrIt=attributes.iterator(); attrIt.hasNext(); ){
                    Element attribute = (Element)attrIt.next();
                    GraphAttributeConfig graphAttrConfig =
                            new GraphAttributeConfig(
                                    attribute.getAttributeValue(GRAPH_ATTRIBUTE_MBEAN),
                                    attribute.getAttributeValue(GRAPH_ATTRIBUTE_NAME),
                                    attribute.getAttributeValue(GRAPH_ATTRIBUTE_DISPLAY_NAME));
                    attributeConfigList.add(graphAttrConfig);
                }
                String graphId = graph.getAttributeValue(GRAPH_ID);
                String graphName = graph.getAttributeValue(GRAPH_NAME);
                long pollingInterval = Long.parseLong(
                        graph.getAttributeValue(GRAPH_POLLING_INTERVAL));

                GraphConfig graphConfig =
                        new GraphConfig(graphId, graphName, pollingInterval,
                                appConfig, attributeConfigList);
                graphConfigList.add(graphConfig);
            }
        }
        return graphConfigList;
    }

    private List getAlertsList(Element application, ApplicationConfig appConfig){
        List alertsList = new LinkedList();
        if(application.getChild(ALERTS)!=null){
            List alerts = application.getChild(ALERTS).getChildren(ALERT);
            for(Iterator it=alerts.iterator(); it.hasNext();){
                AlertConfig alertConfig = new AlertConfig();
                Element alert = (Element)it.next();
                alertConfig.setAlertId(alert.getAttributeValue(ALERT_ID));
                alertConfig.setAlertName(alert.getAttributeValue(ALERT_NAME));
                alertConfig.setSubject(alert.getAttributeValue(ALERT_SUBJECT));
                List alertDeliveryList = alert.getChildren(ALERT_DELIVERY);
                String[] alertDelivery = new String[alertDeliveryList.size()];
                for(int i=0;i<alertDeliveryList.size();i++){
                    Element alertDel = (Element)alertDeliveryList.get(i);
                    alertDelivery[i] = alertDel.getAttributeValue(
                            ALERT_DELIVERY_TYPE);
                    if(alertDelivery[i].equals(AlertDeliveryConstants.EMAIL_ALERT_DELIVERY_TYPE)){
                        alertConfig.setEmailAddress(alertDel.getAttributeValue(
                                ConfigConstants.ALERT_EMAIL_ADDRESS));
                    }
                }
                alertConfig.setAlertDelivery(alertDelivery);

                /* set alert source */
                Element source = alert.getChild(ALERT_SOURCE);
                String sourceType = source.getAttributeValue(ALERT_SOURCE_TYPE);
                String mbean = source.getAttributeValue(ALERT_SOURCE_MBEAN);
                AlertSourceConfig sourceConfig = null;
                if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_NOTIFICATION)){
                    String notificationType =
                        source.getAttributeValue(ALERT_SOURCE_NOTIFICATION_TYPE);
                    sourceConfig = new AlertSourceConfig(mbean,notificationType);
                }else if(sourceType.equals(
                        AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR)){
                    String attribute = source.getAttributeValue(ALERT_ATTRIBUTE_NAME);
                    String lowThreshold = source.getAttributeValue(
                            ALERT_ATTRIBUTE_LOW_THRESHOLD);
                    String highThreshold = source.getAttributeValue(
                            ALERT_ATTRIBUTE_HIGH_THRESHOLD);
                    String attributeDataType = source.getAttributeValue(
                            ALERT_ATTRIBUTE_DATA_TYPE);
                    sourceConfig = new AlertSourceConfig(mbean, attribute,
                            CoreUtils.valueOf(lowThreshold,attributeDataType),
                            CoreUtils.valueOf(highThreshold,attributeDataType),
                            attributeDataType);
                }else if(sourceType.equals(
                        AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR)){
                    String attribute = source.getAttributeValue(
                            ALERT_ATTRIBUTE_NAME);
                    String stringAttributeValue = source.getAttributeValue(
                            ALERT_STRING_ATTRIBUTE_VALUE);
                    sourceConfig = new AlertSourceConfig(mbean, attribute,
                            stringAttributeValue);
                }
                sourceConfig.setApplicationConfig(appConfig);
                alertConfig.setAlertSourceConfig(sourceConfig);
                alertsList.add(alertConfig);
            }
        }
        return alertsList;
    }
}