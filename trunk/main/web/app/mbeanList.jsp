<%@ page import="java.util.List,
                 java.util.Iterator,
                 javax.management.ObjectName,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestAttributes,
                 java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/tags/struts/struts-html.tld" prefix="html"%>


<html:form action="/app/mbeanList" method="post">
    Filter by object name: <html:text property="objectName" /> &nbsp;&nbsp;
    <html:submit/>
</html:form>

<hr width="100%"/>
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