<%@ page import="org.jmanage.webui.util.RequestAttributes,
                 java.util.Map,
                 java.util.Collection,
                 java.util.Iterator,
                 org.jmanage.core.auth.User,
                 org.jmanage.webui.util.RequestParams"%>
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
    <td><a href="/auth/showEditUser.do?<%=RequestParams.USER_NAME+"="+user.getUsername()%>" class="a1">Edit</a></td>
    <td><a href="JavaScript:deleteUser('<%=user.getUsername()%>');" class="a1">Delete</a></td>
  </tr>
  <%}//while ends %>
</table>
<br>
<jmhtml:link href="/auth/showAddUser.do" styleClass="a">Add New User</jmhtml:link>
</body>
</html>