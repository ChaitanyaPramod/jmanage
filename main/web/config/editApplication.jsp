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
<!--    /config/editApplication.jsp  -->
<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<jmhtml:errors />
<jmhtml:javascript formName="applicationForm" />

<jmhtml:form action="/config/editApplication" method="post"
                                onsubmit="return validateApplicationForm(this)">
  <jmhtml:hidden property="applicationId" />
  <jmhtml:hidden property="refreshApps" value="true" />

<table class="table" border="0" cellspacing="0" cellpadding="5" width="500">
    <tr class="tableHeader">
    <td colspan="2">Edit Application</td>
    </tr>
    <tr>
      <td class="headtext1">Type:</td>
      <td class="plaintext"><c:out value="${requestScope.applicationForm.type}" />
        <jmhtml:hidden property="type" />
      </td>
    </tr>
    <tr>
      <td class="headtext1">Name:</td>
      <td><jmhtml:text property="name" size="50"/></td>
    </tr>
    <c:if test="${requestScope.metaAppConfig.displayHost}">
    <tr>
      <td class="headtext1">Host:</td>
      <td><jmhtml:text property="host" size="50" /></td>
    </tr>
    </c:if>
    <c:if test="${requestScope.metaAppConfig.displayPort}">
    <tr>
      <td class="headtext1">Port:</td>
      <td><jmhtml:text property="port" size="50" /></td>
    </tr>
    </c:if>
    <c:if test="${requestScope.metaAppConfig.displayURL}">
    <tr>
      <td class="headtext1">URL:</td>
      <td><jmhtml:text property="URL" size="50" /></td>
    </tr>
    </c:if>
    <c:if test="${requestScope.metaAppConfig.displayUsername}">
    <tr>
      <td class="headtext1">Username:</td>
      <td><jmhtml:text property="username" size="50" /></td>
    </tr>
    </c:if>
    <c:if test="${requestScope.metaAppConfig.displayPassword}">
    <tr>
      <td class="headtext1">Password:</td>
      <td><jmhtml:password property="password" size="50" /></td>
    </tr>
    </c:if>
    <tr>
        <td>&nbsp;</td>
        <td>
          <jmhtml:submit value="Save" styleClass="Inside3d" />&nbsp;&nbsp;&nbsp;
          <jmhtml:button property="" value="Cancel" onclick="JavaScript:history.back();" styleClass="Inside3d" />
        </td>
    </tr>
  </table>
</jmhtml:form>
