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
