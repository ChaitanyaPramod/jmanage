package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.data.OperationResultData;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Executes mbean operation for Application as well as Application Cluster.
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class ExecuteMBeanOperationAction extends BaseAction {

    private static final Logger logger =
            Loggers.getLogger(ExecuteMBeanOperationAction.class);

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception{

        final ObjectName objectName = context.getObjectName();
        /* get the information about the operation to be executed */
        final String operationName = request.getParameter("operationName");
        final int paramCount =
                Integer.parseInt(request.getParameter("paramCount"));
        Object[] params = new Object[paramCount];
        String[] signature = new String[paramCount];
        for(int paramIndex=0; paramIndex < paramCount; paramIndex ++){
            String type =
                    request.getParameter(operationName + paramIndex + "_type");
            String value =
                    request.getParameter(operationName + paramIndex + "_value");
            params[paramIndex] = Utils.getTypedValue(value, type);
            signature[paramIndex] = type;
        }

        final ApplicationConfig appConfig = context.getApplicationConfig();
        final Map appNameToResultMap = new TreeMap();

        if(appConfig.isCluster()){
            /* we need to perform this operation for all servers
                in this cluster */
            for(Iterator it=appConfig.getApplications().iterator(); it.hasNext();){
                ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
                executeMBeanOperation(context, childAppConfig,
                        operationName, params, signature, appNameToResultMap);
            }
        }else{
            executeMBeanOperation(context, appConfig,
                    operationName, params, signature, appNameToResultMap);
        }

        request.setAttribute("appNameToResultMap", appNameToResultMap);
        return mapping.findForward(Forwards.SUCCESS);
    }

    private static void executeMBeanOperation(WebContext context,
                                              ApplicationConfig appConfig,
                                              String operationName,
                                              Object[] params,
                                              String[] signature,
                                              Map appNameToResultMap){

        OperationResultData resultData = new OperationResultData();
        try {
            Object result = executeMBeanOperation(context, appConfig,
                            operationName, params, signature);
            resultData.setOutput(result);
        } catch (Exception e) {
            logger.log(Level.FINE, "Error executing operation " +
                    operationName + " on " + context.getObjectName(), e);
            resultData.setResult(OperationResultData.RESULT_ERROR);
            resultData.setErrorString(e.getMessage());
        }
        appNameToResultMap.put(appConfig.getName(), resultData);
    }

    private static Object executeMBeanOperation(WebContext context,
                                                ApplicationConfig appConfig,
                                                String operationName,
                                                Object[] params,
                                                String[] signature){

        final ServerConnection serverConnection =
                ServerConnector.getServerConnection(appConfig);
        final ObjectName objectName = context.getObjectName();
        final Object result = serverConnection.invoke(objectName, operationName,
                params, signature);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Performed "+operationName+" on "+objectName.getCanonicalName()
                + " in application " + appConfig.getName());
        return result;
    }
}
