<!--    /app/mbeanView.jsp  -->
<%@ page import="org.jmanage.core.management.*,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.webui.util.WebContext,
                 org.jmanage.core.auth.User,
                 org.jmanage.webui.util.MBeanUtils,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.*"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<%
    final WebContext webContext = WebContext.get(request);
    final User user = webContext.getUser();

    ObjectInfo objectInfo = (ObjectInfo)request.getAttribute("objInfo");
    ObjectAttributeInfo[] attributes = objectInfo.getAttributes();
    ObjectOperationInfo[] operations = objectInfo.getOperations();
    ObjectNotificationInfo[] notifications = objectInfo.getNotifications();

    ApplicationConfig applicationConfig = webContext.getApplicationConfig();
    Map appConfigToAttrListMap =
            (Map)request.getAttribute("appConfigToAttrListMap");
%>
<script type="text/javascript" language="Javascript1.1">
<!--
    function addToApplicationCluster(){
        document.mbeanConfigForm.applicationCluster.value='true';
        document.mbeanConfigForm.submit();
        return;
    }
-->
</script>

<tr>
    <td>
    <table border="0">
        <tr><td class="headtext" width="30%" nowrap><b>Object Name</b></td><td class="plaintext" width="70%"><c:out value="${param.objName}" /></td></tr>
        <tr><td class="headtext" width="30%" nowrap><b>Class Name</b></td><td class="plaintext" width="70%"><c:out value="${requestScope.objInfo.className}" /></td></tr>
        <tr><td class="headtext" width="30%" nowrap><b>Description</b></td><td class="plaintext" width="70%"><c:out value="${requestScope.objInfo.description}" /></td></tr>
        <tr><td colspan="2" nowrap class="plaintext">
            <c:choose>
                <c:when test="${requestScope.mbeanIncludedIn != null}">
                    <jmhtml:form action="/config/removeMBeanConfig">
                        <jmhtml:hidden property="objectName"/>
                        <jmhtml:hidden property="refreshApps" value="true"/>
                        <a href="JavaScript:document.mbeanConfigForm.submit();" class="a1">
                            Remove from Application <c:if test="${requestScope.mbeanIncludedIn == 'cluster'}">Cluster</c:if></a>
                    </jmhtml:form>
                </c:when>
                <c:otherwise>
                    <%if(!applicationConfig.isCluster()){%>
                    <jmhtml:form action="/config/addMBeanConfig">
                        <jmhtml:text property="name"/>
                        <jmhtml:hidden property="objectName"/>
                        <jmhtml:hidden property="refreshApps" value="true"/>
                        <jmhtml:hidden property="applicationCluster"/>
                        <a href="JavaScript:document.mbeanConfigForm.submit();" class="a1">Add to Application</a>
                        <%if(applicationConfig.getClusterConfig() != null){%>
                        &nbsp;Or&nbsp;
                        <a href="JavaScript:addToApplicationCluster();" class="a1">Add to Application Cluster</a>
                        <%}%>
                    </jmhtml:form>
                    <%}%>
                </c:otherwise>
            </c:choose>
        </td></tr>
    </table>
    </td>
</tr>
<tr><td>&nbsp;</td></tr>
<%
    if(attributes.length > 0){
%>
<tr><td class="headtext" align="left"><b>Attributes</b>&nbsp;&nbsp;
<jmhtml:link action="/app/mbeanView" styleClass="a1">Refresh</jmhtml:link>
</td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<jmhtml:form action="/app/updateAttributes" method="post">
<table border="0" cellspacing="5">
<tr>
    <td class="headtext"><b>Name</b></td>
<%
    if(applicationConfig.isCluster()){
        for(Iterator it=applicationConfig.getApplications().iterator(); it.hasNext();){
            ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
%>
    <td class="headtext"><b><%=childAppConfig.getName()%></b></td>
<%
        }
    }else{
%>
    <td class="headtext"><b><%=applicationConfig.getName()%></b></td>
<%
    }
%>
    <td class="headtext"><b>Type</b></td>
</tr>
<%
    for(int index=0; index < attributes.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        ObjectAttributeInfo attributeInfo = attributes[index];
%>
<tr>
<td class="<%=rowStyle%>">
    <a href="JavaScript:showDescription('<%=MBeanUtils.jsEscape(attributeInfo.getDescription())%>');"><%=attributeInfo.getName()%></a>
</td>
<%
        List childApplications = null;
        if(applicationConfig.isCluster()){
            childApplications = applicationConfig.getApplications();
        }else{
            childApplications = new ArrayList(1);
            childApplications.add(applicationConfig);
        }

        for(Iterator it=childApplications.iterator(); it.hasNext();){
            ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
            List attributeList = (List)appConfigToAttrListMap.get(childAppConfig);
    %>
<td class="<%=rowStyle%>">
        <%if(attributeList != null){%>
            <%
                String attrValue =
                        MBeanUtils.getAttributeValue(attributeList, attributeInfo.getName());
            %>
            <%if(user.isAdmin() && attributeInfo.isWritable() && !attrValue.equals("Object")){%>
                <input type="text" name="attr+<%=childAppConfig.getApplicationId()%>+<%=attributeInfo.getName()%>+<%=attributeInfo.getType()%>" size="50"
                value="<%=attrValue%>"/>
            <%}else{%>
                <%=attrValue%>
            <%}%>
        <%}else{%>
            &lt;unavailable&gt;
        <%}%>
</td>
<%
}
%>

<td class="<%=rowStyle%>">
    <%=attributeInfo.getType()%>
</td>
</tr>
<%  }%>
</table>
<%if(user.isAdmin()){%>
To save the changes to the attribute values click on
<jmhtml:submit value="Save" styleClass="Inside3d" />
<%}%>
</jmhtml:form>
</td>
</tr>
<%
    }
%>
<%if(operations.length > 0 && user.isAdmin()){%>
<tr><td class="headtext" align="left"><b>Operations</b></td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<table width="100%">
<%
    int tabIndex=1;
    for(int index=0; index < operations.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        ObjectOperationInfo operationInfo = operations[index];
        ObjectParameterInfo[] params = operationInfo.getSignature();
%>
<jmhtml:form action="/app/executeOperation">
<tr>
    <td class="<%=rowStyle%>"><%=operationInfo.getReturnType()%>
    <a href="JavaScript:showDescription('<%=MBeanUtils.jsEscape(operationInfo.getDescription())%>');"><%=operationInfo.getName()%></a>
    <input type="hidden" name="paramCount" value="<%=params.length%>"/>
    </td>
    <td class="<%=rowStyle%>">
    <%if(params.length > 0){
        int paramIndex = 0;
    %>
        <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
        <input tabindex="<%=tabIndex++%>" type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value" value=""/>
        <%=params[paramIndex].getType()%>
    <%}else{%>
        &nbsp;
    <%}%>
    </td>
    <td class="<%=rowStyle%>">
        <input type="hidden" name="operationName" value="<%=operationInfo.getName()%>"/>
        <input tabindex="<%=(tabIndex++) + params.length%>" type="submit" value="Execute" class="Inside3d"/>&nbsp;
        [Impact: <%=MBeanUtils.getImpact(operationInfo.getImpact())%>]
    </td>
</tr>
    <%
        for(int paramIndex = 1; paramIndex < params.length; paramIndex ++){
    %>
<tr>
    <td class="<%=rowStyle%>">&nbsp;</td>
    <td class="<%=rowStyle%>">
        <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
        <input tabindex="<%=tabIndex++%>" type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value" value=""/>
        <%=params[paramIndex].getType()%>
    </td>
    <td class="<%=rowStyle%>">&nbsp;</td>
</tr>
    <%  } %>

</jmhtml:form>
<%  }%>
</table>
</td>
</tr>
<%}%>

<%if(notifications.length > 0 && user.isAdmin()){%>
<tr><td class="headtext" align="left"><b>Notifications</b></td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<table>
<%
    for(int index=0; index < notifications.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        ObjectNotificationInfo notificationInfo = notifications[index];
%>
<tr>
    <td class="<%=rowStyle%>"><%=notificationInfo.getName()%></td>
    <td class="<%=rowStyle%>"><%=notificationInfo.getDescription()%></td>
</tr>
<%  }%>
</table>
</td>
</tr>
<%}%>