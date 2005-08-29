<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.core.config.AlertDeliveryConstants,
                 org.jmanage.core.config.AlertSourceConfig"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<jmhtml:form action="/config/selectAlertSourceType" method="post">

<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableHeader">
    <td>Select Alert Source</td>
</tr>
<tr>
    <td class="plaintext"><jmhtml:radio property="alertSourceType"
                      value="<%=AlertSourceConfig.SOURCE_TYPE_NOTIFICATION%>"/>
                      MBean Notification</td>
</tr>
<tr>
    <td class="plaintext"><jmhtml:radio property="alertSourceType"
                      value="<%=AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR%>"/>
                      MBean Attribute Value Thresholds</td>
</tr>
<tr>
    <td class="plaintext"><jmhtml:radio property="alertSourceType"
                      value="<%=AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR%>"/>
                      MBean Attribute String Value Monitoring</td>
</tr>
<tr>
    <td align="center" colspan="2">
        <jmhtml:submit property="" value="Next" styleClass="Inside3d" />
        &nbsp;&nbsp;&nbsp;
        <jmhtml:button property="" value="Cancel" onclick="JavaScript:history.back();" styleClass="Inside3d" />
    </td>
</tr>
</table>
</jmhtml:form>
