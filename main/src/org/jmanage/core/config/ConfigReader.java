package org.jmanage.core.config;

import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank
 */
public class ConfigReader {

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
     *
     * @return
     */
    public Element getApplications(){
        return config.getRootElement().getChild(
                ConfigConstants.APPLICATIONS);
    }
}
