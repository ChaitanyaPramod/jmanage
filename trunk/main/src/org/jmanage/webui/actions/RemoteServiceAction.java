package org.jmanage.webui.actions;

import org.apache.xmlrpc.XmlRpcServer;
import org.apache.xmlrpc.Echo;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.jmanage.webui.util.WebContext;
import org.jmanage.core.remote.server.ServiceCallHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Vector;

/**
 * RemoteServiceAction is used for handling XML RPC requests.
 *
 * date:  Jan 17, 2005
 * @author	Rakesh Kalra
 */
public class RemoteServiceAction extends BaseAction{

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        byte[] result = xmlrpc.execute(request.getInputStream ());
        response.setContentType("text/xml");
        response.setContentLength(result.length);
        OutputStream output = response.getOutputStream();
        output.write(result);
        output.flush();

        return null;
    }

    private final static XmlRpcServer xmlrpc;
    static{
        xmlrpc = new XmlRpcServer();
        xmlrpc.addHandler("$default", new ServiceCallHandler());
    }
}
