<%@ page import="java.util.List,
                 java.util.Iterator,
                 javax.management.ObjectName,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestAttributes,
                 java.net.URLEncoder"%>

<%
    ApplicationConfig appConfig =
            (ApplicationConfig)request.getAttribute(RequestAttributes.APPLICATION_CONFIG);
    List mbeanInfoList = (List)request.getAttribute("objectNameList");
    for(Iterator it = mbeanInfoList.iterator(); it.hasNext(); ){
        ObjectName objectName = (ObjectName)it.next();
        %>
            <a href="/app/mbeanView.do?appId=<%=appConfig.getApplicationId()%>&objName=<%=URLEncoder.encode(objectName.getCanonicalName(), "UTF-8")%>">
                <%=objectName.getCanonicalName()%></a><br>
        <%
    }

%>