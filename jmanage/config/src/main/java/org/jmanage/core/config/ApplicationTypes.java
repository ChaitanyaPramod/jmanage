/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.core.config;

import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 *
 * <p>
 * Date:  Sep 25, 2005
 * @author	Rakesh Kalra
 */
public class ApplicationTypes {

    private static final Logger logger =
            Loggers.getLogger(ApplicationTypes.class);

    private static final String CONFIG_FILE = CoreUtils.getConfigDir() +
            File.separatorChar + "system" +
            File.separatorChar + "application-type-config.xml";

    /* app type/id to ApplicationType mapping */
    private static Map<String, ApplicationType> applications = new TreeMap<String, ApplicationType>();
    /* module id to ModuleConfig mapping */
    private static Map<String, ModuleConfig> modules = new HashMap<String, ModuleConfig>();

    public static void init(){
        try {
            File configFile = new File(CONFIG_FILE);
            Document config = new SAXBuilder().build(configFile);
            init(config);
            logger.info("ApplicationRegistry initialized from " + CONFIG_FILE);
        } catch (JDOMException e) {
            logger.log(Level.SEVERE, "Error reading config file " +
                    CONFIG_FILE);
            throw new RuntimeException(e);
        }
    }

    public static ApplicationType getApplicationType(String id){
        return (ApplicationType)applications.get(id);
    }

    public static Map getAll(){
        return applications;
    }

    private static ModuleConfig getModule(String id){
        final ModuleConfig config = (ModuleConfig)modules.get(id);
        assert config != null: "Invalid module id: " + id;
        return config;
    }

    private static void init(Document config){
        Element rootElement = config.getRootElement();
        /* initialize modules */
        List modules = rootElement.getChild("modules").getChildren();
        initModules(modules);
        /* initialize applications */
        List applications = rootElement.getChild("application-types").getChildren();
        initApplications(applications);
    }

    private static void initModules(List modulesList){
        for(Iterator it= modulesList.iterator(); it.hasNext();){
            Element moduleElement = (Element)it.next();
            String id = moduleElement.getAttributeValue("id");
            String connectionFactory =
                    moduleElement.getAttributeValue("connectionFactory");
            Element configElement = moduleElement.getChild("config");
            String configClass = configElement.getAttributeValue("class");
            Boolean displayHost =
                    Boolean.valueOf(configElement.getAttributeValue("displayHost"));
            Boolean displayPort =
                    Boolean.valueOf(configElement.getAttributeValue("displayPort"));
            Boolean displayURL =
                    Boolean.valueOf(configElement.getAttributeValue("displayURL"));
            Boolean displayUsername =
                    Boolean.valueOf(configElement.getAttributeValue("displayUsername"));
            Boolean displayPassword =
                    Boolean.valueOf(configElement.getAttributeValue("displayPassword"));
            MetaApplicationConfig metaConfig = new MetaApplicationConfig(
                    displayHost.booleanValue(), displayPort.booleanValue(),
                    displayURL.booleanValue(), displayUsername.booleanValue(),
                    displayPassword.booleanValue(),
                    configClass);
            ModuleConfig moduleConfig =
                    new ModuleConfig(id, metaConfig, connectionFactory);
            modules.put(id, moduleConfig);
        }
    }

    private static void initApplications(List applicationList){
        for(Iterator it=applicationList.iterator(); it.hasNext(); ){
            Element appElement = (Element)it.next();
            String id = appElement.getAttributeValue("id");
            String name = appElement.getAttributeValue("name");
            String moduleId = appElement.getAttributeValue("module");
            Boolean compatibleJMX =
                    Boolean.valueOf(appElement.getAttributeValue("compatibleJMX"));
            ModuleConfig module = getModule(moduleId);
            if(!module.isAvailable()){
                logger.log(Level.WARNING, "Skipping application: " + id +
                        " as the module: " + moduleId + " is not available");
                continue;
            }
            ApplicationType application = new ApplicationType(id, name, module,
                    compatibleJMX.booleanValue(),
                    appElement.getAttributeValue("defaultHost"),
                    appElement.getAttributeValue("defaultPort"),
                    appElement.getAttributeValue("defaultURL"));
            applications.put(id, application);
        }
    }
}
