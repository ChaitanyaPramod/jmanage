package org.jmanage.core.config;

import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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
                /* get the application element */
                Element applicationElement = createApplicationElement(application);
                /* add mbeans */
                Element mbeansElement = createMBeansElement(application);
                applicationElement.addContent(mbeansElement);
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

    private Element createApplicationElement(ApplicationConfig application){
        Element applicationElement =
                    new Element(ConfigConstants.APPLICATION);
        // <application id="1234" name="Weblogic 6.1" server="localhost" port="7001" username="system" password="12345678" type="Weblogic">
        applicationElement.setAttribute(ConfigConstants.APPLICATION_ID,
                application.getApplicationId());
        applicationElement.setAttribute(ConfigConstants.APPLICATION_NAME,
                application.getName());
        applicationElement.setAttribute(ConfigConstants.HOST,
                application.getHost());
        applicationElement.setAttribute(ConfigConstants.PORT,
                String.valueOf(application.getPort()));
        applicationElement.setAttribute(ConfigConstants.USERNAME,
                String.valueOf(application.getUsername()));
        applicationElement.setAttribute(ConfigConstants.PASSWORD,
                String.valueOf(application.getPassword()));
        applicationElement.setAttribute(ConfigConstants.APPLICATION_TYPE,
                            String.valueOf(application.getType()));
        final Map params = application.getParamValues();
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
