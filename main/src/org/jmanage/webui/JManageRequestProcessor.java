package org.jmanage.webui;

import org.apache.struts.config.ModuleConfig;
import org.apache.struts.action.*;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.jmanage.webui.util.WebContext;
import org.jmanage.core.util.Loggers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.Enumeration;
import java.beans.XMLEncoder;

/**
 * Date : Jul 3, 2004 12:38:42 PM
 * @author Shashank
 */
public class JManageRequestProcessor extends TilesRequestProcessor{

    public static final Logger logger =
            Loggers.getLogger(JManageRequestProcessor.class);

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
        logger.info("Start Request Path:" + requestPath);

        ActionForward resultForward = null;
        WebContext context = WebContext.get(request);

        try{
            resultForward = checkPreConditionsForRequest(context,
                    (JManageActionMapping)mapping);

            if(resultForward != null){
                /*  Some precondition check failed, take appropriate action via
                    the forward.    */
                return resultForward;
            }
            /*  execute the action  */
            resultForward = action.execute(mapping, form, request, response);
        }catch (Exception e){
            logger.log(Level.INFO, "Exception on Request: " + requestPath, e);
            /* process exception */
            resultForward =
                    processException(request, response, e, form, mapping);
        }finally{
            String resultForwardPath = (resultForward == null) ?
                    "none" : resultForward.getPath();
            if(resultForwardPath == null){
                /* the path attribute of resultForward was null */
                resultForwardPath = "none";
            }
            logger.info("End Request:" + requestPath +
                    " Forward:" + resultForwardPath);
        }

        /* handle debug mode*/
        resultForward = handleDebugMode(request, response, resultForward);
        return resultForward;
    }

    private ActionForward handleDebugMode(HttpServletRequest request,
                                          HttpServletResponse response,
                                          ActionForward resultForward)
        throws IOException {

        if("true".equals(request.getParameter("debug.xml"))){
            response.setContentType("text/xml");
            XMLEncoder encoder = new XMLEncoder(response.getOutputStream());
            for(Enumeration enum=request.getAttributeNames();
                enum.hasMoreElements();){
                String attribute = (String)enum.nextElement();
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
     * Checks that the perconditions associated with this action satisfy. If
     * they don't do whatever is necessary to ensure they are satisfied.
     *
     * @param context
     * @param mapping
     * @return
     * @throws Exception
     */
    private ActionForward checkPreConditionsForRequest(WebContext context,
                                                       JManageActionMapping mapping)
            throws Exception {
        ActionForward roleForward = null;
        if(mapping.getRoleNames().length > 0){
            roleForward = RoleManager.ensureRole(context, mapping,
                    mapping.getRoleNames());
        }
        return roleForward;
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