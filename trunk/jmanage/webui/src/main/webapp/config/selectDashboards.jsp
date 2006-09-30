<%@ page import="java.util.List, org.jmanage.webui.util.RequestAttributes"%>
<%@ page import="org.jmanage.webui.dashboard.framework.DashboardConfig"%>
<%@ page import="java.util.Collection"%>
<%@ page import="org.jmanage.webui.forms.DashboardSelectionForm"%>
<!--    /config/selectDashboards.jsp  -->

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<jmhtml:errors/>
<jmhtml:javascript formName="dashboardSelectionForm" page="1"/>
<jmhtml:form action="/config/addDashboard" onsubmit="return validateDashboardSelectionForm(this)">
<jmhtml:hidden property="page" value="1"/>
<table border="0" cellspacing="0" cellpadding="5" width="900" class="table">
    <tr class="tableHeader">
        <td colspan="2">Available Qualifying Dashboards</td>
    </tr>
    <%
        Collection<DashboardConfig> dashboards =
                (Collection<DashboardConfig>)request.getAttribute(RequestAttributes.QUALIFYING_DASHBOARDS);
        DashboardSelectionForm dbForm =
                (DashboardSelectionForm)request.getAttribute("dashboardSelectionForm");
        String[] dbIDs = dbForm.getDashboards();
        for(DashboardConfig dashboard : dashboards){
            boolean exists = false;
            for(String dbID : dbIDs){
                if(dbID.equals(dashboard.getDashboardId())){
                    exists = true;
                    break;
                }
            }
    %>
        <tr>
            <%if(exists){%>
            <td class="plaintext"><input type="checkbox" name="dashboards" value="<%=dashboard.getDashboardId()%>" checked="true"/></td>
            <%}else{%>
            <td class="plaintext"><input type="checkbox" name="dashboards" value="<%=dashboard.getDashboardId()%>"/></td>
            <%}%>
            <td class="plaintext"><%=dashboard.getName()%></td>
        </tr>
    <%  }%>
</table>
<br/>
<table>
    <tr>
        <td align="center" colspan="2">
            <jmhtml:submit property="" value="Add" styleClass="Inside3d" />
            &nbsp;&nbsp;&nbsp;
            <jmhtml:button property="" value="Cancel"
                    onclick="JavaScript:history.back();" styleClass="Inside3d" />
        </td>
    </tr>
</table>
</jmhtml:form>