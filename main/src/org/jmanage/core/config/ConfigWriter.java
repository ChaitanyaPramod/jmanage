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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jmanage.core.crypto.Crypto;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class ConfigWriter {

    private static final ConfigWriter configWriter = new ConfigWriter();

    public static ConfigWriter getInstance(){
        return configWriter;
    }

    private ConfigWriter(){}

    public void write(List applications) {

        try {
            Document doc = new Document();
            Element rootElement = new Element(ConfigConstants.APPLICATION_CONFIG);
            Element applicationsElement = new Element(ConfigConstants.APPLICATIONS);
            rootElement.addContent(applicationsElement);
            for(Iterator it=applications.iterator(); it.hasNext();){
                ApplicationConfig application = (ApplicationConfig)it.next();
                /* get the application or application-cluster element */
                Element applicationElement = null;
                if(application.isCluster()){
                    applicationElement =
                            createApplicationClusterElement(application);
                }else{
                    applicationElement = createApplicationElement(application);
                }

                /* add this application element to the root node */
                applicationsElement.addContent(applicationElement);
            }
            doc.setRootElement(rootElement);
            /* write to the disc */
            XMLOutputter writer = new XMLOutputter();
            writer.output(doc, new FileOutputStream(ConfigConstants.DEFAULT_CONFIG_FILE_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the complete XML for ApplicationCluster
     *
     * @param application
     * @return
     */
    private Element createApplicationClusterElement(ApplicationConfig application){
        assert application.isCluster();
        Element applicationElement = createApplicationElement(application);
        Element childApplicationsElement = new Element(ConfigConstants.APPLICATIONS);
        applicationElement.addContent(childApplicationsElement);
        for(Iterator it=application.getApplications().iterator(); it.hasNext();){
            ApplicationConfig appConfig = (ApplicationConfig)it.next();
            Element childAppElement = createApplicationElement(appConfig);
            childApplicationsElement.addContent(childAppElement);
        }
        return applicationElement;
    }

    private Element createApplicationElement(ApplicationConfig application){
        Element applicationElement = null;
        if(application.isCluster()){
            applicationElement = new Element(ConfigConstants.APPLICATION_CLUSTER);
        }else{
            applicationElement = new Element(ConfigConstants.APPLICATION);
        }
        // <application id="1234" name="Weblogic 6.1" server="localhost" port="7001" username="system" password="12345678" type="Weblogic">
        applicationElement.setAttribute(ConfigConstants.APPLICATION_ID,
                application.getApplicationId());
        applicationElement.setAttribute(ConfigConstants.APPLICATION_NAME,
                application.getName());
        if(application.getHost() != null){
            applicationElement.setAttribute(ConfigConstants.HOST,
                    application.getHost());
            applicationElement.setAttribute(ConfigConstants.PORT,
                    String.valueOf(application.getPort()));
        }
        if(application.getURL() != null){
            applicationElement.setAttribute(ConfigConstants.URL,
                    application.getURL());
        }
        if(application.getUsername() != null){
            applicationElement.setAttribute(ConfigConstants.USERNAME,
                    application.getUsername());
        }
        if(application.getPassword() != null){
            String encryptedPassword =
                    Crypto.encryptToString(application.getPassword());
            applicationElement.setAttribute(ConfigConstants.PASSWORD,
                    encryptedPassword);
        }
        if(!application.isCluster()){
            applicationElement.setAttribute(ConfigConstants.APPLICATION_TYPE,
                                application.getType());
        }
        final Map params = application.getParamValues();
        if(params != null){
            for(Iterator param=params.keySet().iterator(); param.hasNext(); ){
                String paramName = (String)param.next();
                Element paramElement =
                        new Element(ConfigConstants.PARAMETER);
                Element paramNameElement =
                        new Element(ConfigConstants.PARAMETER_NAME);
                Element paramValueElement =
                        new Element(ConfigConstants.PARAMETER_VALUE);
                paramNameElement.setText(paramName);
                paramValueElement.setText((String)params.get(paramName));
                paramElement.addContent(paramNameElement);
                paramElement.addContent(paramValueElement);
                applicationElement.addContent(paramElement);
            }
        }

        /* add mbeans */
        Element mbeansElement = createMBeansElement(application);
        applicationElement.addContent(mbeansElement);

        /* add alerts*/
        Element alertsElement = createAlertsElement(application);
        applicationElement.addContent(alertsElement);
        if(!application.isCluster()){
            /* add graphs */
            Element graphsElement = createGraphsElement(application);
            applicationElement.addContent(graphsElement);
        }
        return applicationElement;
    }

    private Element createMBeansElement(ApplicationConfig application){
        Element mbeansElement = new Element(ConfigConstants.MBEANS);
        if(application.getMBeans() == null){
            return mbeansElement;
        }
        for(Iterator mbeans = application.getMBeans().iterator();
            mbeans.hasNext();){
            MBeanConfig mbeanConfig = (MBeanConfig)mbeans.next();
            Element mbeanElement = new Element(ConfigConstants.MBEAN);
            mbeanElement.setAttribute(ConfigConstants.MBEAN_NAME,
                    mbeanConfig.getName()) ;
            Element objectNameElement =
                    new Element(ConfigConstants.MBEAN_OBJECT_NAME);
            objectNameElement.setText(mbeanConfig.getObjectName());
            mbeanElement.addContent(objectNameElement);
            mbeansElement.addContent(mbeanElement);
        }
        return mbeansElement;
    }

    private Element createAlertsElement(ApplicationConfig application){
        Element alertsElement = new Element(ConfigConstants.ALERTS);
        if(application.getAlerts()==null){
            return alertsElement;
        }
        for(Iterator alerts = application.getAlerts().iterator();
            alerts.hasNext();){
            AlertConfig alertConfig = (AlertConfig)alerts.next();
            Element alertElement = new Element(ConfigConstants.ALERT);
            alertElement.setAttribute(ConfigConstants.ALERT_ID,
                    alertConfig.getAlertId());
            alertElement.setAttribute(ConfigConstants.ALERT_NAME,
                    alertConfig.getAlertName());
            alertElement.setAttribute(ConfigConstants.ALERT_SUBJECT,
                    alertConfig.getSubject());
            // add source
            AlertSourceConfig sourceConfig = alertConfig.getAlertSourceConfig();
            Element source = new Element(ConfigConstants.ALERT_SOURCE);
            source.setAttribute(ConfigConstants.ALERT_SOURCE_TYPE,
                    sourceConfig.getSourceType());
            source.setAttribute(ConfigConstants.ALERT_SOURCE_MBEAN,
                    sourceConfig.getObjectName().getCanonicalName());
            source.setAttribute(ConfigConstants.ALERT_SOURCE_NOTIFICATION_TYPE,
                    sourceConfig.getNotificationType());
            alertElement.addContent(source);

            // add delivery
            String[] alertDelivery = alertConfig.getAlertDelivery();
            for(int i=0;i<alertDelivery.length;i++){
                Element alertDel = new Element(ConfigConstants.ALERT_DELIVERY);
                alertDel.setAttribute(ConfigConstants.ALERT_DELIVERY_TYPE,
                        alertDelivery[i]);
                if(alertDelivery[i].equals(AlertDeliveryConstants.EMAIL_ALERT_DELIVERY_TYPE)){
                    alertDel.setAttribute(ConfigConstants.ALERT_EMAIL_ADDRESS,
                            alertConfig.getEmailAddress());
                }
                alertElement.addContent(alertDel);
            }


            alertsElement.addContent(alertElement);
        }
        return alertsElement;
    }

    private Element createGraphsElement(ApplicationConfig application){
        Element graphsElement = new Element(ConfigConstants.GRAPHS);
        if(application.getGraphs() == null){
            return graphsElement;
        }
        for(Iterator graphs = application.getGraphs().iterator();
            graphs.hasNext();){
            GraphConfig graphConfig = (GraphConfig)graphs.next();
            Element graphElement = new Element(ConfigConstants.GRAPH);
            graphElement.setAttribute(ConfigConstants.GRAPH_ID,
                    graphConfig.getId());
            graphElement.setAttribute(ConfigConstants.GRAPH_NAME,
                    graphConfig.getName());
            graphElement.setAttribute(ConfigConstants.GRAPH_POLLING_INTERVAL,
                    Long.toString(graphConfig.getPollingInterval()));
            for(Iterator attributes = graphConfig.getAttributes().iterator();
                    attributes.hasNext();){
                GraphAttributeConfig attrConfig =
                        (GraphAttributeConfig)attributes.next();
                Element attributeElement =
                        new Element(ConfigConstants.GRAPH_ATTRIBUTE);
                attributeElement.setAttribute(ConfigConstants.GRAPH_ATTRIBUTE_MBEAN,
                        attrConfig.getMBean());
                attributeElement.setAttribute(ConfigConstants.GRAPH_ATTRIBUTE_NAME,
                        attrConfig.getAttribute());
                attributeElement.setAttribute(ConfigConstants.GRAPH_ATTRIBUTE_DISPLAY_NAME,
                        attrConfig.getDisplayName());
                graphElement.addContent(attributeElement);
            }
            graphsElement.addContent(graphElement);
        }
        return graphsElement;
    }

}
