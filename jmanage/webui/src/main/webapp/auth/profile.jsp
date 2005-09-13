<!--    /auth/profile.jsp  -->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.core.auth.User,
                 org.jmanage.core.auth.AuthConstants"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%
    WebContext webContext = WebContext.get(request);
    User user = webContext.getUser();
%>

<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableHeader">
    <td colspan="2">User Profile</td>
</tr>
<tr>
    <td width="100" class="headtext1">Username</td>
    <td class="plaintext"><%=user.getName()%></td>
</tr>
<tr>
    <td class="headtext1">Roles</td>
    <td class="plaintext"><%=user.getRolesAsString()%></td>
</tr>
</table>
<%--TODO: if condition can be removed once the bug in ChangePasswordAction is fixed --%>
<%if(!user.getName().equals(AuthConstants.USER_ADMIN)){%>
<p>
<jmhtml:link href="/auth/showChangePassword.do" styleClass="a">Change Password</jmhtml:link>
</p>
<%}%>