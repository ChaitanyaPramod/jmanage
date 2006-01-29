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
package org.jmanage.webui.servlets;

import org.jmanage.core.remote.RemoteInvocation;
import org.jmanage.core.remote.InvocationResult;
import org.jmanage.core.remote.server.ServiceCallHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * date:  Feb 24, 2005
 * @author	Rakesh Kalra
 */
public class ServiceInvokerServlet extends HttpServlet {

    private static String RESPONSE_CONTENT_TYPE =
       "application/x-java-serialized-object; class=org.jmanage.core.remote.InvocationResult";

    /**
     * Handles the HTTP <code>GET</code> method.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Servlet to remotely invoke service methods";
    }


    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(RESPONSE_CONTENT_TYPE);
        try {
            /* get the RemoteInvocation object */
            ServletInputStream sis = request.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(sis);
            RemoteInvocation invocation = (RemoteInvocation) ois.readObject();
            ois.close();
            /* execute the method */
            InvocationResult result = ServiceCallHandler.execute(invocation);
            /* write the result */
            ServletOutputStream sos = response.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(sos);
            oos.writeObject(result);
            oos.close();
        } catch (Throwable t) {
            /* write the exception */
            ServletOutputStream sos = response.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(sos);
            oos.writeObject(new InvocationResult(t));
            oos.close();
        }
    }
}
