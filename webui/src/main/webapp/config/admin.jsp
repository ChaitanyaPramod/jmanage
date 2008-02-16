<!--    /config/admin.jsp  -->
<%@ page errorPage="/error.jsp" %>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableHeader">
    <td>Administration</td>
</tr>
<c:if test="${!sessionScope.authenticatedUser.externalUser}" >
<tr>
    <td class="plaintext">
        <jmhtml:link href="/auth/listUsers.do">User Management</jmhtml:link>
    </td>
</tr>
</c:if>
<tr>
    <td class="plaintext">
        <jmhtml:link href="/auth/showUserActivity.do">User Activity Log</jmhtml:link>
    </td>
</tr>
</table>
