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
package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.management.*;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class UpdateMBeanAttributesAction extends BaseAction {

    private static final Logger logger =
            Loggers.getLogger(UpdateMBeanAttributesAction.class);

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        final ObjectName objectName = context.getObjectName();
        final ApplicationConfig appConfig = context.getApplicationConfig();
        List applications = null;
        if(appConfig.isCluster()){
            applications = appConfig.getApplications();
        }else{
            applications = new ArrayList(1);
            applications.add(appConfig);
        }
        for(Iterator it=applications.iterator(); it.hasNext(); ){

            final ApplicationConfig childAppConfig =
                        (ApplicationConfig)it.next();
            try {
                final ServerConnection serverConnection =
                        ServerConnector.getServerConnection(childAppConfig);
                List attributeList = buildAttributeList(request,
                        childAppConfig.getApplicationId());
                serverConnection.setAttributes(objectName, attributeList);
                String logString = getLogString(attributeList);
                UserActivityLogger.getInstance().logActivity(
                        context.getUser().getUsername(),
                        "Updated the attributes of application:" +
                        childAppConfig.getName() + ", object name:" +
                        objectName.getCanonicalName() +
                        logString);
            } catch (ConnectionFailedException e) {
                logger.log(Level.FINE, "Error connecting to :" +
                        childAppConfig.getName(), e);
            }
        }

        return mapping.findForward(Forwards.SUCCESS);
    }

    /**
     * request parameter is of the format:
     * attr+<applicationId>+<attrName>+<attrType>
     *
     */
    private List buildAttributeList(HttpServletRequest request,
                                    String applicationId){

        Enumeration enum = request.getParameterNames();
        List attributeList = new LinkedList();
        while(enum.hasMoreElements()){
            String param = (String)enum.nextElement();
            if(param.startsWith("attr+")){
                StringTokenizer tokenizer = new StringTokenizer(param, "+");
                if(tokenizer.countTokens() < 4){
                    throw new RuntimeException("Invalid param name: " + param);
                }
                tokenizer.nextToken(); // equals to "attr"
                if(applicationId.equals(tokenizer.nextToken())){ // applicationId
                    String attrName = tokenizer.nextToken();
                    String attrType = tokenizer.nextToken();
                    String attrValue = request.getParameter(param);
                    ObjectAttribute attribute = new ObjectAttribute(attrName,
                            Utils.getTypedValue(attrValue, attrType));
                    attributeList.add(attribute);
                }
            }
        }
        return attributeList;
    }

    /**
     *
     * @param attributes
     * @return
     */
    private String getLogString(List attributes){
        StringBuffer logString = new StringBuffer("");
        for(Iterator iterator = attributes.iterator(); iterator.hasNext(); ){
            ObjectAttribute attribute = (ObjectAttribute)iterator.next();
            logString.append(" [");
            logString.append(attribute.getName());
            logString.append("=");
            logString.append(attribute.getValue());
            logString.append("]");
        }
        return logString.toString();
    }
}
