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
package org.jmanage.webui;

import org.apache.struts.config.ModuleConfig;
import org.apache.struts.action.*;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.alert.AlertEngine;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.AuthService;
import org.jmanage.core.services.ServiceException;
import org.jmanage.monitoring.downtime.ApplicationDowntimeService;
import org.jmanage.util.db.DBUtils;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Arrays;
import java.util.Enumeration;
import java.beans.XMLEncoder;

/**
 * Date : Jul 3, 2004 12:38:42 PM
 * @author Shashank
 */
public class JManageRequestProcessor extends TilesRequestProcessor{

    public Logger logger = Loggers.getLogger(this.getClass());;

    /**
     * Initialize the request processor.
     *
     * @param servlet
     * @param moduleConfig
     * @throws ServletException
     */
    public void init(ActionServlet servlet, ModuleConfig moduleConfig)
            throws ServletException {
        super.init(servlet, moduleConfig);
        String isWebDeploy = servlet.getServletConfig().getInitParameter("web-deploy");
        if("true".equals(isWebDeploy)){
        	String rootDirAbsPath = System.getProperty("JMANAGE_ROOT");
        	//TODO: Where to get this password from ?
            char[] password = {'1','2','3','4','5','6'};
        	try{
	        	String metadataDir = servlet.getServletConfig().getInitParameter("metadata-dir");
	        	String metadataDirAbsPath = servlet.getServletContext().getRealPath(metadataDir);

	        	File configDir = new File(rootDirAbsPath + File.separator + "config");
    	    	File configSrcDir = new File(metadataDirAbsPath + File.separator + "config");
        		CoreUtils.copyConfig(configDir, configSrcDir);

	        	String dataFormatConfigSysProp = servlet.getServletConfig().getInitParameter("jmanage-data-formatConfig");
	        	System.setProperty(dataFormatConfigSysProp, rootDirAbsPath+File.separatorChar+"config"+File.separatorChar+"html-data-format.properties");
	        	
	        	String jaasConfigSysProp = servlet.getServletConfig().getInitParameter("jaas-config");
	        	System.setProperty(jaasConfigSysProp, rootDirAbsPath+File.separatorChar+"config"+File.separatorChar+"jmanage-auth.conf");

	        	CoreUtils.init(rootDirAbsPath, metadataDirAbsPath);
	        	
	            ServiceFactory.init(ServiceFactory.MODE_LOCAL);
	
	            /* initialize Crypto */
	            Crypto.init(password);
	
	            /* clear the password */
	            Arrays.fill(password, ' ');
	
        		DBUtils.init();
            	AlertEngine.getInstance().start();
            	ApplicationDowntimeService.getInstance().start();
                JManageProperties.getInstance();

        	}catch(Exception e){
        		e.printStackTrace();
        		throw new ServletException(e);
        	}
        }

    }

    /**
     * Override the base processActionPerform method to perform the following,
     *
     * 1) Ensure that the preconditions for the action are satisfied,
     * e.g. User is logged in,
     *      User has privileges to access the current resource, etc.
     *
     * @param request
     * @param response
     * @param action
     * @param form
     * @param mapping
     * @return
     * @throws IOException
     * @throws ServletException
     */
    protected ActionForward processActionPerform(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Action action,
                                                 ActionForm form,
                                                 ActionMapping mapping)
            throws IOException, ServletException{

        final String requestPath = mapping.getPath();
        logger.fine("Start Request Path:" + requestPath);

        WebContext context = null;
        ActionForward resultForward = null;
        try{
            context = WebContext.get(request);
            /* ensure user is logged-in (except for login page)*/
            resultForward = ensureLoggedIn(context, request, response, mapping);
            if(resultForward == null){
                /*  execute the action  */
                resultForward = action.execute(mapping, form, request, response);
            }
        }catch (Exception e){
            logger.log(Level.FINE, "Exception on Request: " + requestPath, e);
            /* process exception */
            resultForward =
                    processException(request, response, e, form, mapping);
        }finally{
            /* release resources */
            if(context != null)
                context.releaseResources();
            /* logging */
            String resultForwardPath = (resultForward == null) ?
                    "none" : resultForward.getPath();
            if(resultForwardPath == null){
                /* the path attribute of resultForward was null */
                resultForwardPath = "none";
            }
            logger.fine("End Request:" + requestPath +
                    " Forward:" + resultForwardPath);
        }

        /* handle debug mode*/
        resultForward = handleDebugMode(request, response, resultForward);
        return resultForward;
    }

    private ActionForward ensureLoggedIn(WebContext context,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         ActionMapping mapping) {

        if(context.getUser() == null){
            String path = mapping.getPath();
            if(!path.equals("/auth/showLogin") && !path.equals("/auth/login")){
                /* check if username and pwd are specifed as URL params */
                String username =
                        request.getParameter(RequestParams.JMANAGE_USERNAME);
                String password =
                        request.getParameter(RequestParams.JMANAGE_PASSWORD);
                if(username != null && password != null){
                    // try logging-in the user
                    if(login(context, username, password)){
                        return null;
                    }
                }
                /* see if the application is configured to not require login (not-secure)*/
                if(JManageProperties.isAutoLoginAdminUser()){
                    logger.warning("AUTO LOGIN IS ENABLED -- this is not safe for secure environment.");
                    context.setUser(UserManager.getInstance().getUser(AuthConstants.USER_ADMIN));
                    return null;
                }
                
                return mapping.findForward(Forwards.LOGIN);
            }
        }
        return null;
    }

    // logs-in the user if the username and password are valid
    private boolean login(WebContext context, String username, String password){
        AuthService authService = ServiceFactory.getAuthService();
        try {
            authService.login(context.getServiceContext(),
                    username,
                    password);
            return true;
        } catch (ServiceException e) {
            return false;
        }
    }

    private ActionForward handleDebugMode(HttpServletRequest request,
                                          HttpServletResponse response,
                                          ActionForward resultForward)
        throws IOException {

        if("true".equals(request.getParameter("debug.xml"))){
            response.setContentType("text/xml");
            XMLEncoder encoder = new XMLEncoder(response.getOutputStream());
            for(Enumeration en=request.getAttributeNames();
                en.hasMoreElements();){
                String attribute = (String)en.nextElement();
                Object attrValue = request.getAttribute(attribute);
                encoder.writeObject(attribute);
                encoder.writeObject(attrValue);
            }

            encoder.writeObject(request);
            encoder.close();
            resultForward = null;
        }
        return resultForward;
    }

    /**
     * Disabling the base class method.
     *
     * @param request
     * @param response
     * @param mapping
     * @return
     * @throws IOException
     * @throws ServletException
     */
    protected boolean processRoles(HttpServletRequest request,
                                   HttpServletResponse response,
                                   ActionMapping mapping)
            throws IOException, ServletException {
        return true;
    }
}