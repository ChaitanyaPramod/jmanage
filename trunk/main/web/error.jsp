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
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
<title>Error</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />

</head>
<body leftmargin="8" topmargin="12" marginwidth="0" marginheight="0">
<table width="650" border="0" cellpadding="2" cellspacing="1">
  <tr>
    <td height="31" class="headtext">Error</td>
  </tr>
  <tr>
    <td height="31"><jmhtml:errors /></td>
  </tr>
</table>
</body>
</html>