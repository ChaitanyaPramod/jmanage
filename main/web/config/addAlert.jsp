<%@ page import="org.jmanage.core.config.AlertDeliveryConstants,
                 java.util.List,
                 org.jmanage.core.config.ApplicationConfigManager,
                 org.jmanage.webui.util.WebContext,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestParams"%>
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
<!--    /config/addAlert.jsp  -->
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<jmhtml:javascript formName="alertForm" />
<jmhtml:errors />
<jmhtml:form action="/config/addAlert" method="post"
                                    onsubmit="return validateAlertForm(this)">
<jmhtml:hidden property="alertId" />

<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
%>

<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableHeader">
<%
    if(request.getParameter(RequestParams.ALERT_ID)!=null){
%>
    <td colspan="2">Edit Alert</td>
<%
    }else{
%>
    <td colspan="2">Add Alert</td>
<%}%>
</tr>

<tr>
    <td class="headtext1">Name:</td>
    <td><jmhtml:text property="alertName" /></td>
</tr>
<tr>
    <td class="headtext1">Alert Delivery:</td>
    <td><jmhtml:select property="alertDelivery" multiple="true">
            <jmhtml:option value="<%=AlertDeliveryConstants.EMAIL_ALERT_DELIVERY_TYPE%>"/>
            <jmhtml:option value="<%=AlertDeliveryConstants.CONSOLE_ALERT_DELIVERY_TYPE%>"/>
        </jmhtml:select>
    </td>
</tr>
<tr>
    <td class="headtext1">Subject:</td>
    <td><jmhtml:text property="subject" /></td>
</tr>
<tr>
    <td class="headtext1">Email Address:</td>
    <td><jmhtml:text property="emailAddress" /></td>
</tr>

<tr>
    <td align="center" colspan="2">
        <jmhtml:submit property="" value="Save" styleClass="Inside3d" />
        &nbsp;&nbsp;&nbsp;
        <jmhtml:button property="" value="Cancel" onclick="JavaScript:history.back();" styleClass="Inside3d" />
    </td>
</tr>
</table>
</jmhtml:form>
