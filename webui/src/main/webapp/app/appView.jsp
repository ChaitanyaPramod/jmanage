<!--/app/appView.jsp-->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.webui.util.RequestParams,
                 java.net.URLEncoder,
                 java.util.*,
                 org.jmanage.webui.util.Utils,
                 org.jmanage.core.util.ACLConstants,
                 org.jmanage.core.management.ObjectName,
                 org.jmanage.core.config.*,
                 org.jmanage.webui.dashboard.framework.DashboardRepository,
                 org.jmanage.webui.dashboard.framework.DashboardConfig,
                 org.jmanage.webui.view.ApplicationViewHelper,
                 org.jmanage.monitoring.data.model.ObservedMBeanAttribute,
                 org.jmanage.monitoring.data.dao.ObservedMBeanAttributeDAO"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<script language="JavaScript">
    function deleteAlert(alertId, appId){
        var msg;
        msg = "Are you sure you want to delete this Alert?";
        if(confirm(msg) == true){
            location = '<%=request.getContextPath()%>'+'/config/deleteAlert.do?<%=RequestParams.ALERT_ID%>=' + alertId + '&<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
        return;
    }
    function deleteGraph(graphId, appId){
        var msg;
        msg = "Are you sure you want to delete this Graph?";
        if(confirm(msg) == true){
            location = '<%=request.getContextPath()%>'+'/config/deleteGraph.do?<%=RequestParams.GRAPH_ID%>=' + graphId + '&<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
        return;
    }
    function deleteDashboard(dashboardID, appId){
        var msg;
        msg = "Are you sure you want to delete this Dashboard?";
        if(confirm(msg) == true){
            location = '<%=request.getContextPath()%>'+'/config/deleteDashboard.do?<%=RequestParams.DASHBOARD_ID%>='+dashboardID+'&<%=RequestParams.APPLICATION_ID%>='+appId +'&refreshApps=true';
        }
        return;
    }
    function deletePersistence(configID, appId){
        var msg;
        msg = "Are you sure you want to delete this Persistence Config?";
        if(confirm(msg) == true){
            location = '<%=request.getContextPath()%>'+'/config/deletePersistence.do?<%=RequestParams.PERSISTENCE_ID%>='+configID+'&<%=RequestParams.APPLICATION_ID%>='+appId+ '&refreshApps=true';
        }
        return;
    }
</script>
<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
	final String availabilityGraphURL = "/app/availabilityGraph.do?" +
		    RequestParams.APPLICATION_ID + "=" + appConfig.getApplicationId();
	pageContext.setAttribute("appAvailabilityGrphURL",availabilityGraphURL);
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
		<jmhtml:link href="/config/showAddDashboard.do" acl="<%=ACLConstants.ACL_ADD_DASHBOARD%>" styleClass="a" appendQSParams="true">
		    Add Dashboard</jmhtml:link>
		</p>
		<%
		    String link = "/config/showMBeans.do?"
		            + RequestParams.END_URL + "=" + Utils.urlEncode("/config/showAddGraph.do")
		            + "&" + RequestParams.MULTIPLE + "=true&"
		            + RequestParams.DATA_TYPE + "=java.lang.Number&"
		            + RequestParams.DATA_TYPE + "=javax.management.openmbean.CompositeData&"
		            + RequestParams.NAVIGATION + "=" + Utils.urlEncode("Add Graph");
			pageContext.setAttribute("graphLink",link);
			
		    String persistDataLink = "/config/showMBeans.do?"
		            + RequestParams.END_URL + "=" + Utils.urlEncode("/config/showAddPersistence.do")
		            + "&" + RequestParams.MULTIPLE + "=true&"
		            + RequestParams.DATA_TYPE + "=java.lang.Number&"
		            + RequestParams.DATA_TYPE + "=javax.management.openmbean.CompositeData&"
		            + RequestParams.NAVIGATION + "=" + Utils.urlEncode("Persist Data");
			pageContext.setAttribute("persistLink",persistDataLink);		            
		%>
		<p>
		    <jmhtml:link href="${pageScope.graphLink}" acl="<%=ACLConstants.ACL_ADD_GRAPH%>" styleClass="a" appendQSParams="true">
		        Add Graph</jmhtml:link>
		</p>
		<p>
			<jmhtml:link href="/config/showSelectAlertSourceType.do" acl="<%=ACLConstants.ACL_ADD_ALERT%>" styleClass="a" appendQSParams="true">
			    Add Alert</jmhtml:link>
		</p>
		<p>
		    <jmhtml:link href="/config/startAddMultiMBeanConfig.do" acl="<%=ACLConstants.ACL_ADD_MBEAN_CONFIG%>"
		        styleClass="a" appendQSParams="true">Add Managed Objects</jmhtml:link>
		</p>
		<p>
		    <jmhtml:link href="${pageScope.persistLink}" acl="<%=ACLConstants.ACL_ADD_PERSISTENCE%>" styleClass="a" appendQSParams="true">
		        Persist Data</jmhtml:link>
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
   		<jmhtml:img src="${pageScope.appAvailabilityGrphURL}" />
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
            pageContext.setAttribute("dashboardLink", "/config/viewDashboard.do?applicationId="+appConfig.getApplicationId()+"&dashBID="+dashboardConfig.getDashboardId());
    %>
    <tr>
        <td class="plaintext">
            <jmhtml:link href="${pageScope.dashboardLink}"><%=dashboardConfig.getName()%></jmhtml:link></td>
        <td align="right" width="60">
        <%
            String deleteDashboardLink = "JavaScript:deleteDashboard('"
                    + dashboardConfig.getDashboardId() + "','" + appConfig.getApplicationId() + "');";
			pageContext.setAttribute("deletedashboardLink", deleteDashboardLink);
        %>
           <jmhtml:link href="${pageScope.deletedashboardLink}" acl="<%=ACLConstants.ACL_EDIT_DASHBOARD%>" styleClass="a1">
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
        pageContext.setAttribute("grphViewLnk", "/app/graphView.do?"+RequestParams.APPLICATION_ID+"="+appConfig.getApplicationId()+"&graphId="+graphConfig.getId());
		pageContext.setAttribute("grphEditLnk", "/config/showEditGraph.do?"+ RequestParams.GRAPH_ID + "=" + graphConfig.getId());
%>
    <tr>
        <td class="plaintext">
            <jmhtml:link href="${pageScope.grphViewLnk}"><%=graphConfig.getName()%></jmhtml:link>
        </td>
        <td align="right">
            <jmhtml:link href="${pageScope.grphEditLnk}" acl="<%=ACLConstants.ACL_EDIT_GRAPH%>" styleClass="a1" appendQSParams="true">Edit</jmhtml:link>
        </td>
        <td align="right" width="30">
        <%
            String deleteGraphLink = "JavaScript:deleteGraph('"+ graphConfig.getId() + "','" + appConfig.getApplicationId() + "');";
            pageContext.setAttribute("deleteGraphLink", deleteGraphLink);
        %>
            <jmhtml:link href="${pageScope.deleteGraphLink}" acl="<%=ACLConstants.ACL_EDIT_GRAPH%>" styleClass="a1">Delete</jmhtml:link>
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
        pageContext.setAttribute("mbeanLink", "/app/mbeanView.do?"+RequestParams.APPLICATION_ID+"="+
        							appConfig.getApplicationId()+"&"+RequestParams.OBJECT_NAME+"="+
        							URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8"));
%>
    <tr>
        <td class="plaintext" width="25%">
            <jmhtml:link href="${pageScope.mbeanLink}"><%=mbeanConfig.getName()%></jmhtml:link>
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
        <td class="headtext1">Alert Name</td>
        <td class="headtext1">Source</td>
        <td class="headtext1">Source Type</td>
        <td class="headtext1">Alert Delivery</td>
        <td class="headtext1">&nbsp;</td>
        <td class="headtext1">&nbsp;</td>
    </tr>
    <%
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
        <td class="plaintext">
             <%=alertConfig.getAlertName()%>
        </td>
        <td class="plaintext">
            <%if(alertConfig.getAlertSourceConfig().getObjectName() != null){
            	pageContext.setAttribute("alertLink","/app/mbeanView.do?"+RequestParams.APPLICATION_ID+"="+
            								alertConfig.getAlertSourceConfig().getApplicationConfig().getApplicationId()+
            								"&"+RequestParams.OBJECT_NAME+"="+
            								URLEncoder.encode(alertConfig.getAlertSourceConfig().getObjectName(), "UTF-8"));
            %>
            <jmhtml:link href="${pageScope.alertLink}"><%=ObjectName.getShortName(alertConfig.getAlertSourceConfig().getObjectName())%></jmhtml:link>
            <%}else{ %>
             	&nbsp;
            <%} %>
        </td>
        <td class="plaintext"><%=alertConfig.getAlertSourceConfig().getSourceTypeDesc()%></td>
        <td class="plaintext"><%=alertDel%></td>
        <td align="right" width="60">
            <%
                pageContext.setAttribute("editAlertLink","/config/showEditAlert.do?"+ RequestParams.ALERT_ID + "=" + alertConfig.getAlertId());
            %>
            <jmhtml:link href="${pageScope.editAlertLink}" acl="<%=ACLConstants.ACL_EDIT_ALERT%>" styleClass="a1" appendQSParams="true">Edit</jmhtml:link>
        </td>
        <td align="right" width="60">
        <%
            String deleteAlertLink = "JavaScript:deleteAlert('"
                    + alertConfig.getAlertId() + "','" + appConfig.getApplicationId() + "');";
        %>
           <jmhtml:link  href="<%=deleteAlertLink%>" acl="<%=ACLConstants.ACL_EDIT_ALERT%>" styleClass="a1">Delete</jmhtml:link >
       </td>
    </tr>
    <%}%>
</table>
</td></tr>
</table>
<%}%>

<%
ObservedMBeanAttributeDAO dao = new ObservedMBeanAttributeDAO();
List<ObservedMBeanAttribute> attribList = dao.find(appConfig); 

if(attribList.size() > 0){
%>
<table border="0" width="900" cellpadding="0" cellspacing="5">
<tr><td valign="top" width="100%">
<table cellspacing="0" cellpadding="5" width="900" class="table">
    <tr class="tableHeader">
        <td colspan="6">Persisted Data</td>
    </tr>
    <tr>
        <td class="headtext1">MBean Name</td>
        <td class="headtext1">Attribute Name</td>
        <td class="headtext1">Since</td>
        <td class="headtext1">&nbsp;</td>
    </tr>
    <%
        Iterator itr = attribList.iterator();
        while(itr.hasNext()){
            ObservedMBeanAttribute mbeanAttrib = (ObservedMBeanAttribute)itr.next();
    %>
    <tr>

        <td class="plaintext">
        <%
            	pageContext.setAttribute("mbeanLink","/app/mbeanView.do?"+RequestParams.APPLICATION_ID+"="+
            								appConfig.getApplicationId()+
            								"&"+RequestParams.OBJECT_NAME+"="+
            								URLEncoder.encode(mbeanAttrib.getMBeanName(), "UTF-8"));
            %>
            <jmhtml:link href="${pageScope.mbeanLink}"><%=ObjectName.getShortName(mbeanAttrib.getMBeanName())%></jmhtml:link>

        </td>
        <td class="plaintext">
             <%=mbeanAttrib.getAttributeName()%>
        </td>
        <%
        
        String startedDateStr = "";
        if(mbeanAttrib.getWhenStarted() != null)
        {
        	startedDateStr = Utils.getFormattedDate(new Date(mbeanAttrib.getWhenStarted().getTime()));
        }
        %>
        <td class="plaintext"><%=startedDateStr%></td>
        <td align="right" width="60">
        <%
            String deletePersistenceLink = "JavaScript:deletePersistence(" + mbeanAttrib.getId() + ",'" + appConfig.getApplicationId() + "');";
        %>
           <jmhtml:link  href="<%=deletePersistenceLink%>" acl="<%=ACLConstants.ACL_EDIT_PERSISTENCE%>" styleClass="a1">Remove</jmhtml:link >
       </td>
    </tr>
    <%}%>
</table>
</td></tr>
</table>
<%}%>

