<!--    /config/dashboard.jsp  -->
<%@ page errorPage="/error.jsp" %>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<jmhtml:javascript formName="dashboardForm" />
<jmhtml:errors />
<jmhtml:form action="/config/saveDashboard" method="post"
                                    onsubmit="return validateDashboardForm(this)">

<jmhtml:hidden property="id"/>

<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableHeader">
<%
    if(request.getParameter("id")!=null){
%>
    <td colspan="2">Edit Dashboard</td>
<%
    }else{
%>
    <td colspan="2">Add Dashboard</td>
<%}%>
</tr>

<tr>
    <td class="headtext1">Name:</td>
    <td><jmhtml:text property="name" /></td>
</tr>
<tr>
    <td class="headtext1" colspan="2">Template:</td>
</tr>
<tr>
    <td class="plaintext" colspan="2">
        <jmhtml:textarea property="template" rows="50" cols="80"/>
    </td>
</tr>
<tr>
    <td align="center" colspan="2">
        <jmhtml:submit property="" value="Save" styleClass="Inside3d" />
        &nbsp;&nbsp;&nbsp;
        <jmhtml:button property="" value="Cancel" onclick="JavaScript:history.back();" styleClass="Inside3d" />
    </td>
</tr>
</table>
</jmhtml:form>
