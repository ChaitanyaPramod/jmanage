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
        <%=appConfig.getName()%>
    </td>
</tr>
<%
    }
%>
<tr>
    <td class="errortext">
        <br><br>
        This page should contain functionality to:
        <ul>
        <li>Add mbeans from an application to the cluster
        <li>Server status (if the server is running or not) ?
        </ul>
    </td>
</tr>

