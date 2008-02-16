<!--/app/appView.jsp-->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.webui.util.RequestParams,
                 java.net.URLEncoder,
				 org.jmanage.webui.util.RequestParams,
				 org.jmanage.webui.util.WebContext,
				 org.jmanage.core.services.AccessController,
                 java.util.*,
                 org.jmanage.webui.util.Utils,
                 org.jmanage.core.util.ACLConstants,
                 org.jmanage.core.management.ObjectName,
                 org.jmanage.core.config.*,
                 org.jmanage.webui.dashboard.framework.DashboardRepository,
                 org.jmanage.webui.dashboard.framework.DashboardConfig,
                 org.jmanage.webui.view.ApplicationViewHelper"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<script language="JavaScript">
    function deleteAlert(appId){
        var msg,alertIds;
		alertIds = getAlertIdsToBeRemoved();
        msg = "Are you sure you want to delete this Alert?";
        if(confirm(msg) == true){
            location = '/config/deleteAlert.do?<%=RequestParams.ALERT_ID%>=' + alertIds + '&<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
        return;
    }
	function getAlertIdsToBeRemoved(){
		var nosAlertRows = document.getElementById("nosAlertRows").value;
		var alertIds="";
		for(i=0;i<nosAlertRows;i++){
			if(document.getElementById("chkAlertId"+i).checked == true){
				if(alertIds==""){
					alertIds = document.getElementById("hidAlertId"+i).value;
				}else{
					alertIds = alertIds + "," +document.getElementById("hidAlertId"+i).value;
				}
			}
		}
		return alertIds;
	}
    function deleteGraph(graphId, appId){
        var msg;
        msg = "Are you sure you want to delete this Graph?";
        if(confirm(msg) == true){
            location = '/config/deleteGraph.do?<%=RequestParams.GRAPH_ID%>=' + graphId + '&<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
        return;
    }
    function deleteDashboard(dashboardID, appId){
        var msg;
        msg = "Are you sure you want to delete this Dashboard?";
        if(confirm(msg) == true){
            location = '/config/deleteDashboard.do?<%=RequestParams.DASHBOARD_ID%>='+dashboardID+'&<%=RequestParams.APPLICATION_ID%>='+appId +'&refreshApps=true';
        }
        return;
    }
</script>
<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
		final String availabilityGraphURL = "/app/availabilityGraph.do?" +
		    RequestParams.APPLICATION_ID + "=" + appConfig.getApplicationId();
%>
<%-- Application Information --%>
<table border="0" width="900" cellpadding="0" cellspacing="5">
<tr><td valign="top" width="450">
	<table cellspacing="0" cellpadding="5" width="100%" class="table">
	   <tr class="tableHeader">
    	   <td><%=appConfig.getName()%></td>
	   </tr>
	   <tr><td>
		<p>
			<jmhtml:form action="/app/mbeanList" method="post">
			    <jmhtml:text property="objectName" />&nbsp;&nbsp;<jmhtml:submit styleClass="Inside3d" value="Find Managed Objects" />
			</jmhtml:form>
		</p>
		<p>			
		<jmhtml:link href="/config/showAddDashboard.do" acl="<%=ACLConstants.ACL_ADD_DASHBOARD%>" styleClass="a">
		    Add Dashboard</jmhtml:link>
		</p>
		<%
		    String link = "/config/showMBeans.do?"
		            + RequestParams.END_URL + "=" + Utils.urlEncode("/config/showAddGraph.do")
		            + "&" + RequestParams.MULTIPLE + "=true&"
		            + RequestParams.DATA_TYPE + "=java.lang.Number&"
		            + RequestParams.DATA_TYPE + "=javax.management.openmbean.CompositeData&"
		            + RequestParams.NAVIGATION + "=" + Utils.urlEncode("Add Graph");
		%>
		<p>
		    <jmhtml:link href='<%=link%>' acl="<%=ACLConstants.ACL_ADD_GRAPH%>" styleClass="a">
		        Add Graph</jmhtml:link>
		</p>
		<p>
			<jmhtml:link href="/config/showSelectAlertSourceType.do" acl="<%=ACLConstants.ACL_ADD_ALERT%>" styleClass="a">
			    Add Alert</jmhtml:link>
		</p>
		<p>
		    <jmhtml:link href="/config/startAddMultiMBeanConfig.do" acl="<%=ACLConstants.ACL_ADD_MBEAN_CONFIG%>"
		        styleClass="a">Add Managed Objects</jmhtml:link>
		</p>
		<br/>
	  </td></tr>
	</table>
</td>
<td align="right" valign="top">
	<table cellspacing="0" cellpadding="5" width="100%" class="table">
	   <tr class="tableHeader">
    	   <td colspan="2">Availability</td>
	   </tr>
	   <tr><td colspan="2" align="center">
   		<img src="<%=availabilityGraphURL%>" />
	   </td></tr>	   
	   <tr>
	   	<td align="left" class="plaintext">
			Status: <%=ApplicationViewHelper.isApplicationUp(appConfig)?"Up":"Down"%>        
	   	</td>
	   	<td align="right" class="plaintext">
   			Recording Since: <%=ApplicationViewHelper.getRecordingSince(appConfig)%>
	   	</td>
	   </tr>
	 </table>
</td></tr>
</table>
<br/>
<table border="0" width="900" cellpadding="0" cellspacing="5">
<tr>
<%--Dashboards--%>
<%
if(appConfig.getDashboards() != null && !appConfig.getDashboards().isEmpty()){
%>
<td valign="top" width="450">
<table cellspacing="0" cellpadding="5" width="100%" class="table">
    <tr class="tableHeader">
        <td colspan="2">Dashboards</td>
    </tr>
    <%
        for(String dashboardId : appConfig.getDashboards()){
            DashboardConfig dashboardConfig = DashboardRepository.getInstance().get(dashboardId);
    %>
    <tr>
        <td class="plaintext">
            <a href="/config/viewDashboard.do?applicationId=<%=appConfig.getApplicationId()%>&dashBID=<%=dashboardConfig.getDashboardId()%>">
                <%=dashboardConfig.getName()%></a></td>
        <td align="right" width="60">
        <%
            String deleteDashboardLink = "JavaScript:deleteDashboard('"
                    + dashboardConfig.getDashboardId() + "','" + appConfig.getApplicationId() + "');";
        %>
           <jmhtml:link href="<%=deleteDashboardLink%>" acl="<%=ACLConstants.ACL_EDIT_DASHBOARD%>"  styleClass="a1">
            Delete</jmhtml:link>
       </td>
    </tr>
    <%}%>
</table>
</td>
<%}%>
<%-- Configured Graphs --%>
<%if(appConfig.getGraphs().size() > 0){%>
<td valign="top">
<table border="0" cellspacing="0" cellpadding="5" width="100%" class="table">
    <tr class="tableHeader">
       <td colspan="3">Graphs</td>
    </tr>
<%
    for(Iterator it=appConfig.getGraphs().iterator(); it.hasNext();){
        GraphConfig graphConfig = (GraphConfig)it.next();
%>
    <tr>
        <td class="plaintext">
            <a href="/app/graphView.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>&graphId=<%=graphConfig.getId()%>">
                    <%=graphConfig.getName()%></a>
        </td>
        <td align="right">
        <%
                String editGraphLink ="/config/showEditGraph.do?"
                        + RequestParams.GRAPH_ID + "=" + graphConfig.getId();
            %>
            <jmhtml:link href="<%=editGraphLink%>" acl="<%=ACLConstants.ACL_EDIT_GRAPH%>" styleClass="a1">Edit</jmhtml:link>
        </td>
        <td align="right" width="30">
        <%
            String deleteGraphLink = "JavaScript:deleteGraph('"
                    + graphConfig.getId() + "','" + appConfig.getApplicationId() + "');";
        %>
            <jmhtml:link href="<%=deleteGraphLink%>" acl="<%=ACLConstants.ACL_EDIT_GRAPH%>" styleClass="a1">
                Delete</jmhtml:link>
       </td>
    </tr>
<%
    }
%>
</table>
</td>
<%}%>
</tr>
</table>
<br/>
<%-- Configured MBeans --%>
<%if(appConfig.getMBeans().size() > 0){%>
<table border="0" width="900" cellpadding="0" cellspacing="5">
<tr><td valign="top" width="100%">
<table border="0" cellspacing="0" cellpadding="5" width="900" class="table">
    <tr class="tableHeader">
       <td colspan="2">Managed Objects</td>
    </tr>
<%
    List mbeans = appConfig.getMBeans();
    List sortedMBeans = new ArrayList(mbeans);
    Collections.sort(sortedMBeans, new Comparator(){
        public int compare(Object obj1, Object obj2){
            String name1 = ((MBeanConfig)obj1).getName();
            String name2 = ((MBeanConfig)obj2).getName();
            return name1.compareTo(name2);
        }
    });
    for(Iterator it=sortedMBeans.iterator(); it.hasNext();){
        MBeanConfig mbeanConfig = (MBeanConfig)it.next();
%>
    <tr>
        <td class="plaintext" width="25%">
            <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>">
                    <%=mbeanConfig.getName()%></a>
        </td>
        <td class="plaintext">
            <%=mbeanConfig.getObjectName()%>
        </td>
    </tr>
<%
    }
%>
</table>
</td></tr>
</table>
<br/>
<%}%>
<%
if(appConfig.getAlerts().size() > 0){
%>
<table border="0" width="900" cellpadding="0" cellspacing="5">
<tr><td valign="top" width="100%">
<table cellspacing="0" cellpadding="5" width="900" class="table">
    <tr class="tableHeader">
        <td colspan="6">Configured Alerts</td>
    </tr>
    <tr>
        <td class="headtext1">
		 <%
		 String delAlertLink = "JavaScript:deleteAlert('"+ appConfig.getApplicationId() + "')";
		 %>
		 <input type="button" value="Remove" onClick="<%=delAlertLink%>" />
		</td>
        <td class="headtext1">Alert Name</td>
        <td class="headtext1">Source</td>
        <td class="headtext1">Source Type</td>
        <td class="headtext1">Alert Delivery</td>
        <td class="headtext1">&nbsp;</td>
    </tr>
    <%
		/*Check if the user has access to delete the alerts*/
		boolean hasAccess=false;
        if(ACLConstants.ACL_EDIT_ALERT != null){
            if(!AccessController.canAccess(Utils.getServiceContext(webContext),ACLConstants.ACL_EDIT_ALERT)){
                hasAccess = false;
            }else{
				hasAccess=true;
			}
        }

		int nosAlertRows =0;
		List alerts = appConfig.getAlerts();
        Iterator itr = alerts.iterator();
        while(itr.hasNext()){
            AlertConfig alertConfig = (AlertConfig)itr.next();
            String[] alertDelivery = alertConfig.getAlertDelivery();
            String alertDel = "";
            for(int i=0; i<alertDelivery.length;i++){
                if(i>0){
                    alertDel = alertDel + "," + alertDelivery[i];
                }else{
                    alertDel = alertDel + alertDelivery[i];
                }
            }
    %>
    <tr>
	    <td align="center" width="30">
		<% if(hasAccess){ %>
			<input type="checkbox" id="chkAlertId<%=nosAlertRows %>"/>
			<input type="hidden" value="<%=alertConfig.getAlertId() %>" id="hidAlertId<%=nosAlertRows %>"/>
		<% } %>
        </td>
        <td class="plaintext">
             <%=alertConfig.getAlertName()%>
        </td>
        <td class="plaintext">
            <%if(alertConfig.getAlertSourceConfig().getObjectName() != null){%>
            <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=alertConfig.getAlertSourceConfig().getApplicationConfig().getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(alertConfig.getAlertSourceConfig().getObjectName(), "UTF-8")%>">
             <%=ObjectName.getShortName(alertConfig.getAlertSourceConfig().getObjectName())%>
            <%}else{ %>
             	&nbsp;
            <%} %>
        </td>
        <td class="plaintext"><%=alertConfig.getAlertSourceConfig().getSourceTypeDesc()%></td>
        <td class="plaintext"><%=alertDel%></td>
        <td align="right" width="60">
            <%
                String editAlertLink ="/config/showEditAlert.do?"
                        + RequestParams.ALERT_ID + "=" + alertConfig.getAlertId();
            %>
            <jmhtml:link href="<%=editAlertLink%>" acl="<%=ACLConstants.ACL_EDIT_ALERT%>" styleClass="a1">Edit</jmhtml:link>
        </td>
    </tr>
    <%
		nosAlertRows++;
	}
	%>
	<input type="hidden" value="<%=nosAlertRows%>" id="nosAlertRows"/>
</table>
</td></tr>
</table>
<%}%>

