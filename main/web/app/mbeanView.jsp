<%--
  Copyright 2004-2005 jManage.org

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!--    /app/mbeanView.jsp  -->
<%@ page import="org.jmanage.core.management.*,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.webui.util.WebContext,
                 org.jmanage.core.auth.User,
                 org.jmanage.webui.util.MBeanUtils,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.*,
                 org.jmanage.core.util.ACLConstants"%>

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
<jmhtml:errors />
<table class="table" border="0" cellspacing="0" cellpadding="5" width="900">
    <tr>
        <td class="headtext" width="100">Object Name</td>
        <td class="plaintext"><c:out value="${param.objName}" /></td>
    </tr>
    <tr>
        <td class="headtext" width="100">Class Name</td>
        <td class="plaintext"><c:out value="${requestScope.objInfo.className}" /></td>
    </tr>
    <tr>
        <td class="headtext" width="100">Description</td>
        <td class="plaintext"><c:out value="${requestScope.objInfo.description}" /></td>
    </tr>
    <tr>
        <td class="headtext" width="150" valign="top">Configured Name</td>
        <td class="plaintext">
            <c:choose>
                <c:when test="${requestScope.mbeanIncludedIn != null}">
                    <jmhtml:form action="/config/removeMBeanConfig">
                        <jmhtml:hidden property="objectName"/>
                        <jmhtml:hidden property="refreshApps" value="true"/>
                        <c:out value="${requestScope.mbeanConfig.name}"/>&nbsp;
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
        </td>
    </tr>
</table>

<%
    if(attributes.length > 0){
        boolean showUpdateButton = false;
        int columns = 3;
%>
<jmhtml:form action="/app/updateAttributes" method="post">
<table class="table" border="0" cellspacing="0" cellpadding="5">
<tr class="tableHeader">
    <td><b>Name</b></td>
<%
    if(applicationConfig.isCluster()){
        columns = 3 + (applicationConfig.getApplications().size() - 1);
        for(Iterator it=applicationConfig.getApplications().iterator(); it.hasNext();){
            ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
%>
    <td><b><%=childAppConfig.getName()%></b></td>
<%
        }
    }else{
%>
    <td><b>Value</b></td>
<%
    }
%>
    <td><b>Type</b></td>
</tr>
<%
    for(int index=0; index < attributes.length; index++){
        ObjectAttributeInfo attributeInfo = attributes[index];
%>
<tr>
<td class="plaintext">
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
<td class="plaintext">
        <%if(attributeList != null){%>
            <%
                String attrValue =
                        MBeanUtils.getAttributeValue(attributeList, attributeInfo.getName());
            %>
            <%if((user.canAccess(ACLConstants.ACL_UPDATE_MBEAN_ATTRIBUTES) ||
                    user.canAccess(ACLConstants.ACL_UPDATE_MBEAN_ATTRIBUTES+"."+attributeInfo.getName())) &&
                    attributeInfo.isWritable() && !attrValue.equals("Object")){
                showUpdateButton = true;
            %>
                <%if(attributeInfo.getType().equals("boolean") || attributeInfo.getType().equals("java.lang.Boolean")){%>
                    <input type="radio" name="attr+<%=childAppConfig.getApplicationId()%>+<%=attributeInfo.getName()%>+<%=attributeInfo.getType()%>" value="true" <%=attrValue.equals("true")?" CHECKED":""%> />&nbsp;True
                    &nbsp;&nbsp;&nbsp;<input type="radio" name="attr+<%=childAppConfig.getApplicationId()%>+<%=attributeInfo.getName()%>+<%=attributeInfo.getType()%>" value="false" <%=attrValue.equals("false")?" CHECKED":""%>/>&nbsp;False
                <%}else{%>
                    <input type="text" name="attr+<%=childAppConfig.getApplicationId()%>+<%=attributeInfo.getName()%>+<%=attributeInfo.getType()%>" size="50"
                    value="<%=attrValue%>"/>
                <%}%>
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
<td class="plaintext">
    <%=attributeInfo.getType()%>
</td>
</tr>
<%  }// for ends%>
<tr>
    <td class="plaintext" colspan="<%=columns%>">
        To save the changes to the attribute values click on
        <%if(showUpdateButton){%>
            <jmhtml:submit value="Save" styleClass="Inside3d" />
        <%}else{%>
            <jmhtml:submit value="Save" styleClass="Inside3d" disabled="true" />
        <%}%>
    </td>
</tr>
</table>
</jmhtml:form>
<%
    }
%>
<%if(operations.length > 0){%>
<br/>
<table class="table" border="0" cellspacing="0" cellpadding="5" width="900">
<tr class="tableHeader">
    <td colspan="3">Operations</td>
</tr>
<%
    int tabIndex=1;
    for(int index=0; index < operations.length; index++){
        ObjectOperationInfo operationInfo = operations[index];
        ObjectParameterInfo[] params = operationInfo.getSignature();
%>
<jmhtml:form action="/app/executeOperation">
<tr>
    <td class="plaintext"><%=operationInfo.getReturnType()%>
    <a href="JavaScript:showDescription('<%=MBeanUtils.jsEscape(operationInfo.getDescription())%>');"><%=operationInfo.getName()%></a>
    <input type="hidden" name="paramCount" value="<%=params.length%>"/>
    </td>
    <td class="plaintext">
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
    <td class="plaintext">
        <input type="hidden" name="operationName" value="<%=operationInfo.getName()%>"/>
        <%  if(user.canAccess(ACLConstants.ACL_EXECUTE_MBEAN_OPERATIONS) ||
                user.canAccess(ACLConstants.ACL_EXECUTE_MBEAN_OPERATIONS+"."+operationInfo.getName())){  %>
        <input tabindex="<%=(tabIndex++) + params.length%>" type="submit" value="Execute" class="Inside3d"/>&nbsp;
        <%  }else{  %>
        <input tabindex="<%=(tabIndex++) + params.length%>" type="submit" value="Execute" class="Inside3d" disabled/>&nbsp;
        <%  }   %>
        [Impact: <%=MBeanUtils.getImpact(operationInfo.getImpact())%>]
    </td>
</tr>
    <%
        for(int paramIndex = 1; paramIndex < params.length; paramIndex ++){
    %>
<tr>
    <td class="plaintext">&nbsp;</td>
    <td class="plaintext">
        <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
        <input tabindex="<%=tabIndex++%>" type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value" value=""/>
        <%=params[paramIndex].getType()%>
    </td>
    <td class="plaintext">&nbsp;</td>
</tr>
    <%  } %>

</jmhtml:form>
<%  }%>
</table>
<%}%>

<%if(notifications.length > 0 &&
        user.canAccess(ACLConstants.ACL_VIEW_MBEAN_NOTIFICATIONS)){%>
<br/>
<table class="table" border="0" cellspacing="0" cellpadding="5" width="900">
    <tr class="tableHeader">
        <td colspan="2">Notifications</td>
    </tr>
<%
    for(int index=0; index < notifications.length; index++){
        ObjectNotificationInfo notificationInfo = notifications[index];
%>
<tr>
    <td class="plaintext"><%=notificationInfo.getName()%></td>
    <td class="plaintext"><%=notificationInfo.getDescription()%></td>
</tr>
<%  }%>
</table>
<%}%>
