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
<!--    /config/cofigure.jsp  -->
<%-- TODO: not used --%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />Configure</b></span><br /><br />
<%--<jmhtml:javascript formName="configureForm" />--%>
<jmhtml:errors />
<jmhtml:form action="/config/configure" method="post">
  <table border="0" bordercolor="black" cellspacing="1" cellpadding="2" width="350">
    <tr>
      <td class="headtext1">Maximum Login Attempts</td>
      <td> <jmhtml:text property="maxLoginAttempts" />
      </td>
    </tr>
  </table>
  </br>
  <jmhtml:submit value="Save" styleClass="Inside3d"/>
  &nbsp;&nbsp;&nbsp;
  <jmhtml:button property="" value="Back" onclick="JavaScript:history.back();" styleClass="Inside3d" />
  </jmhtml:form>
</body>
</html>