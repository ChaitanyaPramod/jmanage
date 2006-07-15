<%@ page import="org.jmanage.webui.util.WebContext"%>
<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<!--/dashboard/default.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/jm.tld" prefix="jm"%>
<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
%>
<table class="plaintext" style="border:1;border-style:solid;border-width:1px;border-color:#C0C0C0">
    <tr><td colspan="2" align="center"><b><u>Application Summary</u></b></td></tr>
    <tr><td><jm:dashboardComponent id="com1"/></td><td><jm:dashboardComponent id="com2"/></td></tr>
    <tr><td><jm:dashboardComponent id="com3"/></td><td>&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2" align="center"><b><u><a href="/config/viewDashboard.do?applicationId=<%=appConfig.getApplicationId()%>&dashBID=jvmThreads">Thread Details</a></u></b></td></tr>
    <tr><td><jm:dashboardComponent id="com4"/></td><td><jm:dashboardComponent id="com5"/></td></tr>
    <tr><td><jm:dashboardComponent id="com6"/></td><td><jm:dashboardComponent id="com7"/></td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2" align="center"><b><u>Memory Details</u></b></td></tr>
    <tr><td><jm:dashboardComponent id="com8"/></td><td><jm:dashboardComponent id="com9"/></td></tr>
    <tr><td><jm:dashboardComponent id="com10"/></td><td>&nbsp;</td></tr>
    <tr><td colspan="2"><jm:dashboardComponent id="com11"/></td></tr>
    <tr><td colspan="2"><jm:dashboardComponent id="com12"/></td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2" align="center"><b><u>Classes</u></b></td></tr>
    <tr><td><jm:dashboardComponent id="com13"/></td><td><jm:dashboardComponent id="com14"/></td></tr>
    <tr><td><jm:dashboardComponent id="com15"/></td><td>&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2" align="center"><b><u>OS Details</u></b></td></tr>
    <tr><td><jm:dashboardComponent id="com16"/></td><td><jm:dashboardComponent id="com17"/></td></tr>
    <tr><td><jm:dashboardComponent id="com18"/></td><td>&nbsp;</td></tr>
</table>