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

<script language="JavaScript">
    function deleteUser(username){
        if(confirm("Are you sure you want to delete this user?") == true){
            location = '/auth/deleteUser.do?<%=RequestParams.USER_NAME%>='+username;
        }
    }
</script>

<table cellspacing="0" cellpadding="5" width="400" class="table">
    <tr class="tableHeader">
        <td colspan="3">Users</td>
    </tr>
<%
    Map users = (Map)request.getAttribute(RequestAttributes.USERS);
    Iterator iterator = users.values().iterator();
    while(iterator.hasNext()){
        User user = (User)iterator.next();
%>
  <tr>
    <td class="plaintext"><%=user.getName()%></td>
    <td class="plaintext" align="right">
    <%if(!AuthConstants.USER_ADMIN.equals(user.getUsername())){%>
    <a href="/auth/showEditUser.do?<%=RequestParams.USER_NAME+"="+user.getUsername()%>" class="a1">Edit</a>
    <%}else{%>
    &nbsp;
    <%}%>
    </td>
    <td class="plaintext" align="right" width="60">
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
