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
<!--    /config/managedApplications.jsp  -->
<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Set,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.core.config.MBeanConfig,
                 java.net.URLEncoder,
                 java.util.List"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<script language="JavaScript">
    function deleteApplication(appId, isCluster){
        var msg;
        if(isCluster){
            msg = "Are you sure you want to delete this Application Cluster and all child applications?";
        }else{
            msg = "Are you sure you want to delete this Application?";
        }
        if(confirm(msg) == true){
            location = '/config/deleteApplication.do?<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
    }
</script>
<table cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
        <td colspan="4">Managed Applications</td>
    </tr>
<%
    List applications = (List)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator iterator = applications.iterator();
    while(iterator.hasNext()){
        ApplicationConfig applicationConfig = (ApplicationConfig)iterator.next();
%>
  <tr>
    <%
        String appHref = "/app/appView.do?" +
                RequestParams.APPLICATION_ID + "=" + applicationConfig.getApplicationId();
    %>
    <td class="plaintext">
        <a href="<%=appHref%>"><%=applicationConfig.getName()%></a>
    </td>
    <td class="plaintext">
        <%if(!applicationConfig.isCluster()){%>
            <%=applicationConfig.getURL()%>
        <%}else{%>
            &nbsp;
        <%}%>
    </td>
    <%
      String href = null;
      if(!applicationConfig.isCluster()){
        href = "/config/showEditApplication.do";
      }else{
        href = "/config/showApplicationCluster.do";
      }%>
    <td align="right"><a href="<%=href%>?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" class="a1">Edit</a></td>
    <td align="right" width="60"><a href="JavaScript:deleteApplication('<%=applicationConfig.getApplicationId()%>', <%=applicationConfig.isCluster()%>);" class="a1">Delete</a></td>
  </tr>
  <%-- if this is a cluster, display the child applications as well --%>
      <%
      if(applicationConfig.isCluster()){
        for(Iterator childApps=applicationConfig.getApplications().iterator(); childApps.hasNext();){
            ApplicationConfig childAppConfig = (ApplicationConfig)childApps.next();
            appHref = "/app/appView.do" +
                "?" + RequestParams.APPLICATION_ID + "=" + childAppConfig.getApplicationId();
      %>
          <tr>
            <td class="plaintext">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="<%=appHref%>"><%=childAppConfig.getName()%></a>
            </td>
            <td class="plaintext">
                <%=childAppConfig.getURL()%>
            </td>
            <td align="right"><a href="/config/showEditApplication.do?<%=RequestParams.APPLICATION_ID+"="+childAppConfig.getApplicationId()%>" class="a1">Edit</a></td>
            <td align="right" width="60"><a href="JavaScript:deleteApplication('<%=childAppConfig.getApplicationId()%>', false);" class="a1">Delete</a></td>
          </tr>
      <%
        }
      }
      %>
  <%}//while ends %>
</table>
<br>
<%-- don't use the link tag here, as it adds applicationId request param --%>
<a href="/config/showAvailableApplications.do" class="a">Add New Application</a>
<br>
<a href="/config/showApplicationCluster.do" class="a">Add New Application Cluster</a>
