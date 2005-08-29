<!--/app/graphView.jsp-->
<%@ page import="org.jmanage.webui.util.Utils,
                 org.jmanage.core.config.GraphConfig,
                 org.jmanage.core.util.JManageProperties"%>
<%
    GraphConfig graphConfig = (GraphConfig)request.getAttribute("graphConfig");
%>

<p>
<applet
    code="org/jmanage/webui/applets/GraphApplet.class"
    archive="/applets/applets.jar,/applets/jfreechart-0.9.20.jar,/applets/jcommon-0.9.5.jar"
    width="600"
    height="500">
    <param name="graphTitle" value="<%=graphConfig.getName()%>"/>
    <param name="pollingInterval" value="<%=graphConfig.getPollingInterval()%>"/>
    <param name="remoteURL" value="<%=JManageProperties.getJManageURL()%>/app/fetchAttributeValues.do;jsessionid=<%=Utils.getCookieValue(request, "JSESSIONID")%>"/>
    <param name="displayNames" value="<%=graphConfig.getAttributeDisplayNames()%>"/>
    <param name="attributes" value='<%=graphConfig.getAttributesAsString()%>'/>
</applet>
</p>
