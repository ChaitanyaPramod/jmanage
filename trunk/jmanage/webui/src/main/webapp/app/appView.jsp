<!--/app/appView.jsp-->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.core.config.MBeanConfig,
                 org.jmanage.webui.util.RequestParams,
                 java.net.URLEncoder,
                 java.util.*,
                 org.jmanage.core.config.GraphConfig,
                 org.jmanage.core.config.AlertConfig,
                 org.jmanage.webui.util.Utils"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<script language="JavaScript">
    function deleteAlert(alertId, appId){
        var msg;
        msg = "Are you sure you want to delete this Alert?";
        if(confirm(msg) == true){
            location = '/config/deleteAlert.do?<%=RequestParams.ALERT_ID%>=' + alertId + '&<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
        }
    }
</script>
<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
%>
<%-- Configured MBeans --%>
<%if(appConfig.getMBeans().size() > 0){%>
<table border="0" cellspacing="0" cellpadding="5" width="600" class="table">
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
<%}else{%>
<p class="plaintext">
    There are no configured objects.
</p>
<%}%>
<p>
<jmhtml:form action="/app/mbeanList" method="post">
    <jmhtml:text property="objectName" />&nbsp;&nbsp;<jmhtml:submit styleClass="Inside3d" value="Find More Objects" />
</jmhtml:form>
</p>
<%-- Configured Graphs --%>
<%if(appConfig.getGraphs().size() > 0){%>
<table border="0" cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
       <td colspan="1">Graphs</td>
    </tr>
<%
    for(Iterator it=appConfig.getGraphs().iterator(); it.hasNext();){
        GraphConfig graphConfig = (GraphConfig)it.next();
%>
    <tr>
        <td class="plaintext" width="25%">
            <a href="/app/graphView.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>&graphId=<%=graphConfig.getId()%>">
                    <%=graphConfig.getName()%></a>
        </td>
    </tr>
<%
    }
%>
</table>
<%}else{%>
<p class="plaintext">
    There are no configured graphs.
</p>
<%}%>
<p>
   <a href="/config/showMBeans.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>&<%=RequestParams.END_URL%>=<%=Utils.encodeURL("/config/showAddGraph.do")%>&<%=RequestParams.MULTIPLE%>=true&<%=RequestParams.DATA_TYPE%>=java.lang.Number" class="a">Add Graph</a>
</p>
<%
if(appConfig.getAlerts().size() > 0){
%>
<table cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
        <td colspan="5">Alerts</td>
    </tr>
    <tr>
        <td class="headtext1">Alert Name</td>
        <td class="headtext1">Application Name</td>
        <td class="headtext1">Alert Delivery</td>
        <td class="headtext1">Subject</td>
    </tr>
    <%
        List alerts = appConfig.getAlerts();
        Iterator itr = alerts.iterator();
        while(itr.hasNext()){
            AlertConfig alertConfig = (AlertConfig)itr.next();
            String appName = alertConfig.getAlertSourceConfig().
                    getApplicationConfig().getName();
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
        <td class="plaintext"><a href="/config/showEditAlert.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>&<%=RequestParams.ALERT_ID%>=<%=alertConfig.getAlertId()%>">
             <%=alertConfig.getAlertName()%>
        </td>
        <td class="plaintext">
             <%=appName%>
        </td>
        <td class="plaintext"><%=alertDel%></td>
        <td class="plaintext"><%=alertConfig.getSubject()%></td>
        <td align="right" width="60"><a href="JavaScript:deleteAlert('<%=alertConfig.getAlertId()%>',<%=appConfig.getApplicationId()%>);" class="a1">Delete</a></td>
    </tr>
    <%}%>
</table>
<%
}else{
%>
<p class="plaintext">
    There are no configured alerts.
</p>
<%}%>
<p>
<a href="/config/showSelectAlertSourceType.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>" class="a">Add New Alert</a>


