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
<!--    /app/mbeanList.jsp  -->
<%@ page import="java.util.List,
                 java.util.Iterator,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestAttributes,
                 java.net.URLEncoder,
                 org.jmanage.core.management.ObjectName,
                 java.util.Map,
                 java.util.Set"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/struts/struts-bean.tld" prefix="bean"%>

<tr>
<td bgcolor="#E6EEF9">
<jmhtml:form action="/app/mbeanList" method="post">
    <jmhtml:text property="objectName" />&nbsp;&nbsp;<jmhtml:submit styleClass="Inside3d" value="Filter by object name" />
</jmhtml:form>
</td>
</tr>
<%
    Map domainToObjectNameListMap = (Map)request.getAttribute("domainToObjectNameListMap");
    int row = 0;
    for(Iterator it = domainToObjectNameListMap.keySet().iterator(); it.hasNext(); ){
        String domain = (String)it.next();
        %>
        <tr>
        <td class="headtext">
            <br>
            <%=domain%>
        </td>
        </tr>
        <%
        Set objectNameList = (Set)domainToObjectNameListMap.get(domain);
        for(Iterator objectNameIt = objectNameList.iterator(); objectNameIt.hasNext();){
            String objectName = (String)objectNameIt.next();
            pageContext.setAttribute("objectName",
                    domain + ":" + objectName, PageContext.PAGE_SCOPE);
            String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
            row++;
%>
            <tr>
            <td class="<%=rowStyle%>">
                <jmhtml:link action="/app/mbeanView"
                             paramId="objName"
                             paramName="objectName">
                    <%=objectName%></jmhtml:link>
            </td>
            </tr>
<%      } // inner for
    } // outer for
%>
