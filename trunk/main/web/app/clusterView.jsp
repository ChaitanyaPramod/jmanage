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
<!--- /app/ClusterView.jsp -->
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.core.config.ApplicationClusterConfig,
                 java.util.List,
                 java.util.Iterator,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.core.config.MBeanConfig,
                 org.jmanage.webui.util.RequestParams,
                 java.net.URLEncoder"%>
<%
    WebContext context = WebContext.get(request);
    ApplicationClusterConfig clusterConfig =
            (ApplicationClusterConfig)context.getApplicationConfig();
%>
<table border="0" cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
        <td colspan="2">Applications</td>
    </tr>
<%
    List childApplications = clusterConfig.getApplications();
    for(Iterator it=childApplications.iterator(); it.hasNext();){
        ApplicationConfig appConfig = (ApplicationConfig)it.next();
%>
    <tr>
        <td class="plaintext" width="25%">
            <a href="/app/appView.do?applicationId=<%=appConfig.getApplicationId()%>">
                    <%=appConfig.getName()%></a>
        </td>
        <td class="plaintext">
            <%=appConfig.getURL()%>
        </td>
    </tr>
<%
    }
%>
</table>
<br/><br/>
<%if(clusterConfig.getMBeans().size() > 0){%>
<table border="0" cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
        <td colspan="2">Managed Objects</td>
    </tr>
<%
    List mbeans = clusterConfig.getMBeans();
    for(Iterator it=mbeans.iterator(); it.hasNext();){
        MBeanConfig mbeanConfig = (MBeanConfig)it.next();
%>
    <tr>
        <td class="plaintext" width="25%">
            <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=clusterConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>">
                    <%=mbeanConfig.getName()%></a>
        </td>
        <td class="plaintext">
            <%=mbeanConfig.getObjectName()%>
        </td>
    </tr>
<%
    }
%>
</table>
<%}%>