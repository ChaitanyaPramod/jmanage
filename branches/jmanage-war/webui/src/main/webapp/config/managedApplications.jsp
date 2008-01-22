<!--    /config/managedApplications.jsp  -->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.webui.util.RequestAttributes,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.webui.util.RequestParams,
                 java.net.URLEncoder,
                 java.util.List,
                 org.jmanage.core.alert.AlertInfo,
                 org.jmanage.webui.view.ApplicationViewHelper"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<script language="JavaScript">
    function deleteApplication(appId, isCluster){
        var msg;
        if(isCluster){
            msg = "Are you sure you want to delete this Application Cluster and all child applications?";
        }else{
            msg = "Are you sure you want to delete this Application?";
        }
        if(confirm(msg) == true){
            location = '<%=request.getContextPath()%>'+'/config/deleteApplication.do?<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
    }
</script>
<table cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
        <td colspan="4">Managed Applications</td>
    </tr>
<%
    List applications = (List)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator iterator = applications.iterator();
    while(iterator.hasNext()){
        ApplicationConfig applicationConfig = (ApplicationConfig)iterator.next();
%>
  <tr>
    <%
        String appHref = "/app/appView.do?" +
                RequestParams.APPLICATION_ID + "=" + applicationConfig.getApplicationId();
		pageContext.setAttribute("applRef",appHref);
    %>
    <td class="plaintext">
		  <%if(ApplicationViewHelper.isApplicationUp(applicationConfig)) {%>        
	        <jmhtml:img src="/images/bullet/green.gif" />
	      <%}else{ %>
	      	<jmhtml:img src="/images/bullet/red.gif" />
	      <%} %>&nbsp;
	      <jmhtml:link href="${pageScope.applRef}"><%=applicationConfig.getName()%></jmhtml:link>
    </td>
    <td class="plaintext">
        <%if(!applicationConfig.isCluster()){%>
            <%=applicationConfig.getURL()%>
        <%}else{%>
            &nbsp;
        <%}%>
    </td>
    <%
      String href = null;
      if(!applicationConfig.isCluster() && applicationConfig.getType().equals("connector")) {
        href = "/config/showEditConnector.do";
      }
      else if(!applicationConfig.isCluster()){
        href = "/config/showEditApplication.do";
      }else{
        href = "/config/showApplicationCluster.do";
      }%>
      <% pageContext.setAttribute("editLink",href+"?"+RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()); %>
    <td align="right"><jmhtml:link href="${pageScope.editLink}" styleClass="a1">Edit</jmhtml:link></td>
    <td align="right" width="60"><a href="JavaScript:deleteApplication('<%=applicationConfig.getApplicationId()%>', <%=applicationConfig.isCluster()%>);" class="a1">Delete</a></td>
  </tr>
  <%-- if this is a cluster, display the child applications as well --%>
      <%
      if(applicationConfig.isCluster()){
        for(Iterator childApps=applicationConfig.getApplications().iterator(); childApps.hasNext();){
            ApplicationConfig childAppConfig = (ApplicationConfig)childApps.next();
            appHref = "/app/appView.do" +
                "?" + RequestParams.APPLICATION_ID + "=" + childAppConfig.getApplicationId();
            pageContext.setAttribute("chldApplRef",appHref);
      %>
          <tr>
            <td class="plaintext">
                &nbsp;&nbsp;&nbsp;&nbsp;
	       				<%if(ApplicationViewHelper.isApplicationUp(childAppConfig)) {%>        
					        <jmhtml:img src="/images/bullet/green.gif" />
					      <%}else{ %>
					      	<jmhtml:img src="/images/bullet/red.gif" />
					      <%} %>&nbsp;
				  	    <jmhtml:link href="${pageScope.chldApplRef}"><%=childAppConfig.getName()%></jmhtml:link>
            </td>
            <td class="plaintext">
                <%=childAppConfig.getURL()%>
            </td>
            <%pageContext.setAttribute("showEditLink", "/config/showEditApplication.do?"+RequestParams.APPLICATION_ID+"="+childAppConfig.getApplicationId()); %>
            <td align="right"><jmhtml:link href="${pageScope.showEditLink}" styleClass="a1">Edit</jmhtml:link></td>
            <td align="right" width="60"><a href="JavaScript:deleteApplication('<%=childAppConfig.getApplicationId()%>', false);" class="a1">Delete</a></td>
          </tr>
      <%
        }
      }
      %>
  <%}//while ends %>
</table>
<br>
<%-- don't use the link tag here, as it adds applicationId request param --%>
<jmhtml:link href="/config/showAvailableApplications.do" styleClass="a">Add Application</jmhtml:link>
<br>
<jmhtml:link href="/config/showApplicationCluster.do" styleClass="a">Add Application Cluster</jmhtml:link>
<br><br>

<%
    List alerts = (List)request.getAttribute("alerts");
    if(alerts.size() == 0){
%>
<p class="plaintext">
    There are no alerts.
</p>
<%
    }else{
%>

<table cellspacing="0" cellpadding="5" width="800" class="table">
    <tr class="tableHeader">
        <td colspan="7">Triggered Alerts</td>
    </tr>
    <tr>
        <td class="headtext">Timestamp</td>
        <td class="headtext">Application</td>
        <td class="headtext">Alert Name</td>
        <td class="headtext">Message</td>
        <td class="headtext">Source</td>
        <td class="headtext">&nbsp;</td>
    </tr>
<%
        for(Iterator it=alerts.iterator(); it.hasNext(); ){
            AlertInfo alert = (AlertInfo)it.next();
%>
    <tr>
        <td class="plaintext"><%=alert.getFormattedTimeStamp()%></td>
        <td class="plaintext"><%=alert.getApplicationName()%></td>
        <td class="plaintext"><%=alert.getAlertName()%></td>
        <td class="plaintext"><%=alert.getMessage()%></td>
        <td class="plaintext">
		<%if(alert.getObjectName() != null){
			pageContext.setAttribute("alertLink","/app/mbeanView.do?"+RequestParams.APPLICATION_ID+"="+alert.getApplicationId()+"&"+RequestParams.OBJECT_NAME+"="+URLEncoder.encode(alert.getObjectName(), "UTF-8"));
		%>
        <jmhtml:link href="${pageScope.alertLink}"><%=alert.getObjectName()%></jmhtml:link>
        <%}else{ %>
        	&nbsp;
        <%} %>
        </td>
        <%pageContext.setAttribute("alertRemoveLink", "/app/removeConsoleAlert.do?alertId="+alert.getAlertId()); %>
        <td class="plaintext"><jmhtml:link href="${pageScope.alertRemoveLink}">Remove</jmhtml:link></td>
    </tr>
<%
        }
    }
%>
</table>


