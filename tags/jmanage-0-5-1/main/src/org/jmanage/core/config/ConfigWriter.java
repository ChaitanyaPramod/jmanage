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

import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jmanage.core.crypto.Crypto;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

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
}
