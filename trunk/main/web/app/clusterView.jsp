<!--- /app/ClusterView.jsp -->
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.core.config.ApplicationClusterConfig,
                 java.util.List,
                 java.util.Iterator,
                 org.jmanage.core.config.ApplicationConfig"%>
<%
    WebContext context = WebContext.get(request);
    ApplicationClusterConfig clusterConfig =
            (ApplicationClusterConfig)context.getApplicationConfig();
    List childApplications = clusterConfig.getApplications();
    for(Iterator it=childApplications.iterator(); it.hasNext();){
        ApplicationConfig appConfig = (ApplicationConfig)it.next();
%>
<tr>
    <td class="plaintext">
        <a href="/app/mbeanList.do?applicationId=<%=appConfig.getApplicationId()%>">
            <%=appConfig.getName()%></a>
    </td>
</tr>
<%
    }
%>
