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

package org.jmanage.core.config.dashboard.framework;

import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.config.DashboardConfig;
import org.jmanage.core.config.DashboardQualifier;
import org.jmanage.core.config.DashboardComponent;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;

/**
 * Date: Apr 23, 2006 5:23:03 PM
 *
 * @author Shashank Bellary
 */
public class DashboardLoader {
    private static final Logger logger =
            Loggers.getLogger(DashboardLoader.class);
    private List<InputStream> dashboardConfigFiles = new ArrayList<InputStream>();

    /*  Dashboard XML file elements */
    private static final String DASHBOARD = "dashboard";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COMMENT = "comment";
    private static final String TEMPLATE = "template";

    private static final String QUALIFICATIONS = "qualifications";
    private static final String QUALIFIER = "qualifier";
    private static final String CLASS = "class";

    private static final String COMPONENTS = "components";
    private static final String COMPONENT = "component";



    public DashboardLoader() {
        loadDashboards();
    }

    /**
     * Load all dashboard configuration (XML) files and keep it ready for parsing
     * and initializing each dashboard.
     */
    private void loadDashboards(){
        logger.info("Loading dashboards");
        File dashboardDir = new File(CoreUtils.getDashboardsDir());
        try{
            if(dashboardDir.listFiles().length == 0){
                logger.info("No dashboards found");
                return;
            }
            for(File dashboardXML : dashboardDir.listFiles()){
                InputStream xmlInputStream = new FileInputStream(dashboardXML);
                dashboardConfigFiles.add(xmlInputStream);
            }
        }catch(FileNotFoundException e){
            logger.severe("Error loading dashboard(s)");
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param dashboards
     */
    public void initialize(Map<String, DashboardConfig> dashboards){
        logger.info("Initializing dashboards");
        if(dashboardConfigFiles.isEmpty())
            return;
        try{
            for (InputStream dashboardConfigFile : dashboardConfigFiles) {
                Document dashboardDocument = new SAXBuilder().build(dashboardConfigFile);
                DashboardConfig dashboard = buildDashboardConfig(dashboardDocument);
                dashboards.put(dashboard.getDashboardId(), dashboard);
            }
        }catch(JDOMException e){
            logger.severe("Error Initilizing dashboards");
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param dashboard
     * @return
     */
    private DashboardConfig buildDashboardConfig(Document dashboard){

        Element dashboardRootElement = dashboard.getRootElement();
        List<DashboardQualifier> qualifiers =
                new ArrayList<DashboardQualifier>();
        Map<String, DashboardComponent> components =
                new HashMap<String, DashboardComponent>();
        String dashboardID = dashboardRootElement.getAttribute(ID).getValue();
        String dashboardName = dashboardRootElement.getAttribute(NAME).getValue();
        String dashboardTemplate = dashboardRootElement.getAttribute(TEMPLATE).getValue();;

        //  Load dashboard qualifiers.
        List<Element> qualifierElements =
                dashboardRootElement.getChild(QUALIFICATIONS).getChildren(QUALIFIER);
        for(Element qualifierElement: qualifierElements){
            String qualifierClassName =
                    qualifierElement.getAttribute(CLASS).getValue();
            try{
                Class qualifierClass = Class.forName(qualifierClassName);
                DashboardQualifier dashboardQualifier =
                        (DashboardQualifier) qualifierClass.newInstance();
                qualifiers.add(dashboardQualifier);
            }catch(Exception e){
                logger.severe("Error loading qualifier class: "+qualifierClassName);
                throw new RuntimeException(e);
            }
        }

        //  Load dashboard components
        List<Element> componentElements =
                dashboardRootElement.getChild(COMPONENTS).getChildren(COMPONENT);
        for(Element componentElement : componentElements){
            String componentClassName =
                    componentElement.getAttribute(CLASS).getValue();
            try{
                Class componentClass = Class.forName(componentClassName);
                DashboardComponent dashboardComponent =
                        (DashboardComponent)componentClass.newInstance();
                dashboardComponent.init(componentElement);
                components.put(dashboardComponent.getId(), dashboardComponent);
            }catch(Exception e){
                logger.severe("Error loading component class: "+componentClassName);
                throw new RuntimeException(e);
            }
        }
        return new DashboardConfig(dashboardID, dashboardName, dashboardTemplate,
                components, qualifiers);
    }
}