package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectAttribute;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class UpdateMBeanAttributesAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        final ObjectName objectName = context.getObjectName();
        final ServerConnection serverConnection = context.getServerConnection();
        List attributeList = buildAttributeList(request);
        serverConnection.setAttributes(objectName, attributeList);
        String logString = getLogString(attributeList);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Updated the attributes of "+ objectName.getCanonicalName() +
                logString);
        return mapping.findForward(Forwards.SUCCESS);
    }

    private List buildAttributeList(HttpServletRequest request){

        Enumeration enum = request.getParameterNames();
        List attributeList = new LinkedList();
        while(enum.hasMoreElements()){
            String param = (String)enum.nextElement();
            if(param.startsWith("attr+")){
                StringTokenizer tokenizer = new StringTokenizer(param, "+");
                if(tokenizer.countTokens() < 3){
                    throw new RuntimeException("Invalid param name: " + param);
                }
                tokenizer.nextToken(); // equals to "param"
                String attrName = tokenizer.nextToken();
                String attrType = tokenizer.nextToken();
                String attrValue = request.getParameter(param);
                ObjectAttribute attribute = new ObjectAttribute(attrName,
                        CoreUtils.getTypedValue(attrValue, attrType));
                attributeList.add(attribute);
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
