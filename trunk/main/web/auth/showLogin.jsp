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
<!--/auth/showLogin.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<jmhtml:javascript formName="loginForm" />
<jmhtml:form action="/auth/login" method="post" focus="username"
                            onsubmit="return validateLoginForm(this)" >

<jmhtml:errors />
<br/>
<table cellspacing="0" cellpadding="5" width="400" class="table">
  <tr class="tableHeader">
    <td colspan="2">Login</td>
  </tr>
  <tr>
    <td class="headtext1">
        Username
    </td>
    <td>
        <jmhtml:text property="username" />
    </td>
  </tr>
  <tr>
    <td class="headtext1">
        Password
    </td>
    <td>
        <jmhtml:password property="password" />
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><jmhtml:submit styleClass="Inside3d" value="Login" /></td>
  </tr>
</table>
</jmhtml:form>
