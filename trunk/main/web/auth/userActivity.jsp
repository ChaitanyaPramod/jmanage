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
<%@ page import="java.util.List,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Iterator"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />User Activities</b></span><br /><br />
<table border="0" cellspacing="1" cellpadding="2" width="650" bgcolor="#E6EEF9">
<%
    List activities = (List)request.getAttribute(RequestAttributes.USER_ACTIVITIES);
    Iterator iterator = activities.iterator();
    int row = 0;
    while(iterator.hasNext()){
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        row++;
%>
  <tr class="<%=rowStyle%>">
    <td class="plaintext"><%=iterator.next()%></td>
  </tr>
  <%}//while ends %>
</table>
</body>
</html>