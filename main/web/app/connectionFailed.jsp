<%--
  Copyright 2004-2005 jManage.org

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!--    /app/connectionFailed.jsp  -->
<%@ page import="org.jmanage.webui.util.RequestParams,
                 org.jmanage.webui.util.RequestAttributes,
                 org.jmanage.core.config.ApplicationConfig"%>
<%
    ApplicationConfig applicationConfig =
            (ApplicationConfig)
            request.getAttribute(RequestAttributes.APPLICATION_CONFIG);
%>
<table cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
        <td>Connection to the application failed</td>
    </tr>
    <tr>
        <td class="plaintext">
            Please check the following:
            <ol>
                <li class="plaintext">Application is running.</li>
                <li class="plaintext">Application is correctly configured in jManage.
                Click
                <a href="/config/showEditApplication.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" class="a1">here</a>
                to edit configuration.
                </li>
            </ol>
        </td>
    </tr>
</table>
