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
<!--    /auth/configuredUsers.jsp  -->
<%@ page import="org.jmanage.webui.util.RequestAttributes,
                 java.util.Map,
                 java.util.Collection,
                 java.util.Iterator,
                 org.jmanage.core.auth.User,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.core.auth.AuthConstants"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript">
        function deleteUser(username){
            if(confirm("Are you sure you want to delete this user?") == true){
                location = '/auth/deleteUser.do?<%=RequestParams.USER_NAME%>='+username;
            }
        }
    </script>
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />Users</b></span><br /><br />
<table border="0" cellspacing="1" cellpadding="2" width="200" bgcolor="#E6EEF9">
<%
    Map users = (Map)request.getAttribute(RequestAttributes.USERS);
    Iterator iterator = users.values().iterator();
    int row = 0;
    while(iterator.hasNext()){
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        User user = (User)iterator.next();
        row++;
%>
  <tr class="<%=rowStyle%>">
    <td class="headtext1"><%=user.getName()%></td>
    <td>
    <%if(!AuthConstants.USER_ADMIN.equals(user.getUsername())){%>
    <a href="/auth/showEditUser.do?<%=RequestParams.USER_NAME+"="+user.getUsername()%>" class="a1">Edit</a>
    <%}else{%>
    &nbsp;
    <%}%>
    </td>
    <td>
    <%if(!AuthConstants.USER_ADMIN.equals(user.getUsername())){%>
    <a href="JavaScript:deleteUser('<%=user.getUsername()%>');" class="a1">Delete</a>
    <%}else{%>
    &nbsp;
    <%}%>
    </td>
  </tr>
  <%}//while ends %>
</table>
<br>
<jmhtml:link href="/auth/showAddUser.do" styleClass="a">Add New User</jmhtml:link>
<br/>
<jmhtml:link href="/auth/showUserActivity.do" styleClass="a">View User Activities</jmhtml:link>
</body>
</html>