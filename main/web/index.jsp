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
<!--/index.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<html>
<head>
<title>JManage</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0">
  <tr>
    <td align="center" valign="middle">
    <table border="0" cellpadding="0" cellspacing="0">
      <tr><td width="318" class="Inside3d"><font face="Arial, Helvetica, sans-serif" size="5" color="#000080"><jmhtml:img src="/images/logo.jpg" width="408" height="48" /></font></td></tr>
      <tr>
        <td height="82">
        <jmhtml:javascript formName="loginForm" />
        <jmhtml:form action="/auth/login" method="post" focus="username"
                                    onsubmit="return validateLoginForm(this)" >
            <table width="100%" border="0" cellpadding="7" cellspacing="8" class="loginBg">
              <tr>
                <td colspan="2">
                    <div align="center"><jmhtml:errors /></div>
                </td>
              </tr>
              <tr>
                <td width="44%"><div align="right" class="plaintext">Username : </div>
                </td>
                <td width="56%"><jmhtml:text property="username" />
                </td>
              </tr>
              <tr>
                <td><div align="right" class="plaintext">Password : </div>
                </td>
                <td><jmhtml:password property="password" />
                </td>
              </tr>
              <tr>
                <td><div align="center">
                    </div>
                </td>
                <td><jmhtml:submit styleClass="Inside3d" value="Login" /></td>
              </tr>
            </table>
        </jmhtml:form>
        </td>
      </tr>
    </table>
    <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>