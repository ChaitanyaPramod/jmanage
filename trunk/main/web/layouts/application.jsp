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
<%@ page import="org.jmanage.webui.util.RequestParams"%>
<%@ taglib uri="/WEB-INF/tags/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />

<script language="JavaScript1.1" type="text/javascript">

    function refreshApplicationsFrame(){
        parent.frames.applications.location = '/config/applicationList.do';
    }

    function showDescription(description){
        if(description.length == 0){
            description = "No description available.";
        }
        alert(description);
    }
</script>

</head>
<body leftmargin="8" topmargin="12" marginwidth="0" marginheight="0"
    <%if(request.getParameter(RequestParams.REFRESH_APPS) != null){%>
    onload="JavaScript:refreshApplicationsFrame();"
    <%}%> >
<table width="650" border="0" cellpadding="2" cellspacing="1">
  <tr>
    <td height="31" class="headtext">
        <c:choose>
            <c:when test="${requestScope.applicationConfig.cluster}">
                Application Cluster Name:
            </c:when>
            <c:otherwise>
                Application Name:
            </c:otherwise>
        </c:choose>
        <c:out value="${requestScope.applicationConfig.name}"/>
    </td>
  </tr>
  <%-- TODO: we are forcing body.main to start with <tr>. We should fix this. --%>
  <tiles:insert attribute="body.main" />
</table>
</body>
</html>