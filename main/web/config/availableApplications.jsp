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
<!--    /config/availableApplications.jsp  -->
<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<script language="JavaScript">
    function setType(type){
        document.forms[0].type.value=type;
        document.forms[0].submit();
    }
</script>

<jmhtml:form action="/config/showAddApplication.do" method="post">
    <jmhtml:hidden property="type" value="" />
</jmhtml:form>
<table class="table" border="0" cellspacing="0" cellpadding="5" width="300">
<tr class="tableHeader">
    <td>Select Application Type</td>
</tr>

<%
    Map applications = (Map)request.getAttribute(RequestAttributes.AVAILABLE_APPLICATIONS);
    Iterator iterator = applications.keySet().iterator();
    while(iterator.hasNext()){
        String applicationType = (String)iterator.next();
%>
  <tr>
    <td class="plaintext">
        <a href="javascript:setType('<%=applicationType%>');"><b><%=applicationType%> application</b></a>
    </td>
  </tr>
  <%}//while ends %>
</table>
