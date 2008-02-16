<%@ page import="org.jmanage.webui.util.WebContext"%>
<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<!--/dashboard/jvmSummary.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/jm.tld" prefix="jm"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
%>
<table class="plaintext" cellspacing="5" width="800" style="border:1;border-style:solid;border-width:1px;border-color:#C0C0C0">
	<tr><td align="right"><jmhtml:img src="/images/dashboards/java5.gif"/></td><td><b>Application Summary</b></td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td><jm:dashboardComponent id="com1"/></td><td><jm:dashboardComponent id="com2"/></td></tr>
    <tr><td><jm:dashboardComponent id="com3"/></td><td>&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr class="tableHeader">
    	<td colspan="2">
    		<% pageContext.setAttribute("threadDetailLink", "/config/viewDashboard.do?applicationId="+appConfig.getApplicationId()+"&dashBID=jvmThreads");%>
    		<jmhtml:link href="${pageScope.threadDetailLink}">Thread Details</jmhtml:link>
    	</td>
    </tr>
    <tr><td><jm:dashboardComponent id="com4"/></td><td><jm:dashboardComponent id="com5"/></td></tr>
    <tr><td><jm:dashboardComponent id="com6"/></td><td><jm:dashboardComponent id="com7"/></td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr class="tableHeader"><td colspan="2">Memory Details</td></tr>
    <tr><td><jm:dashboardComponent id="com8"/></td><td><jm:dashboardComponent id="com9"/></td></tr>
    <tr><td><jm:dashboardComponent id="com10"/></td><td>&nbsp;</td></tr>
    <tr><td colspan="2"><jm:dashboardComponent id="com11"/></td></tr>
    <tr><td colspan="2"><jm:dashboardComponent id="com12"/></td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr class="tableHeader">
    	<td colspan="2">
    		<% pageContext.setAttribute("classesLink", "/config/viewDashboard.do?applicationId="+appConfig.getApplicationId()+"&dashBID=classes");%>
    		<jmhtml:link href="${pageScope.classesLink}">Classes</jmhtml:link>
    	</td>
    </tr>
    <tr><td><jm:dashboardComponent id="com13"/></td><td><jm:dashboardComponent id="com14"/></td></tr>
    <tr><td><jm:dashboardComponent id="com15"/></td><td>&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr class="tableHeader"><td colspan="2">OS Details</td></tr>
    <tr><td><jm:dashboardComponent id="com16"/></td><td><jm:dashboardComponent id="com17"/></td></tr>
    <tr><td><jm:dashboardComponent id="com18"/></td><td>&nbsp;</td></tr>
</table>
<script type="text/javascript">
  function toggleSystemProperties(){
    var sysPropsStyle = document.getElementById('systemProperties').style;
    if(sysPropsStyle.visibility == 'visible'){
      sysPropsStyle.visibility='hidden';
      document.getElementById('showText').innerHTML = '&gt;&gt';
    }else{
	  sysPropsStyle.visibility='visible';
      document.getElementById('showText').innerHTML = '&lt;&lt';
	}
  }
</script>
<p class="headtext">System Properties 
<a href="JavaScript:toggleSystemProperties();" class="a"><span id="showText">&gt;&gt;</span></a>
</p>
<div id="systemProperties" style="visibility:hidden">
	<jm:dashboardComponent id="systemProperties"/>
</div>