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
<!--    /auth/editUser.jsp  -->
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<jmhtml:javascript formName="userForm" />
<jmhtml:errors />
<jmhtml:form action="/auth/editUser" method="post"
                                    onsubmit="return validateUserForm(this)">
<jmhtml:hidden property="username" />

<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableHeader">
    <td colspan="2">Edit User</td>
</tr>
<tr>
    <td class="headtext1">Username:</td>
    <td class="plaintext"><c:out value="${requestScope.userForm.username}" /></td>
</tr>
<tr>
    <td class="headtext1">Password:</td>
    <td><jmhtml:password property="password" /></td>
</tr>
<tr>
    <td class="headtext1">Role:</td>
    <td><jmhtml:select property="role">
            <jmhtml:option value="" > --------- Select --------- </jmhtml:option>
            <jmhtml:options collection="roles" property="name" labelProperty="name" />
        </jmhtml:select>
    </td>
</tr>
<tr>
    <td class="headtext1">Lock Account:</td>
    <td><jmhtml:checkbox property="status" value="I" styleId="checked"/></td>
</tr>
<tr>
    <td align="center" colspan="2">
        <jmhtml:submit value="Save" styleClass="Inside3d" />
        &nbsp;&nbsp;&nbsp;
        <jmhtml:button property="" value="Cancel" onclick="JavaScript:history.back();" styleClass="Inside3d" />
    </td>
</tr>
</table>
</jmhtml:form>
