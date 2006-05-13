<%@ page import="org.jmanage.webui.util.WebContext"%>
<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<%@ page import="org.jmanage.core.config.DashboardConfig"%>
<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
    String currentDashboardId = request.getParameter("dashBID");
    DashboardConfig currentDashboardConfig = null;
    for(DashboardConfig dashboardConfig : appConfig.getDashboards()){
        if(currentDashboardId.equals(dashboardConfig.getDashboardId())){
            currentDashboardConfig = dashboardConfig;
            break;
        }
    }
%>
<jsp:include page="<%=currentDashboardConfig.getTemplate()%>" />