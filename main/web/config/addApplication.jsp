<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
</head>
<body>
<b>Add Application</b>
<br><br>

<jmhtml:form action="/config/addApplication" method="post">

<table border="1" bordercolor="black" cellspacing="0" cellpadding="2" width="200" bgcolor="lightgreen">
<tr>
    <td>Name:</td>
    <td><jmhtml:text property="name" /></td>
</tr>
<tr>
    <td>Host:</td>
    <td><jmhtml:text property="host" /></td>
</tr>
<tr>
    <td>Port:</td>
    <td><jmhtml:text property="port" /></td>
</tr>
<tr>
    <td>Username:</td>
    <td><jmhtml:text property="username" /></td>
</tr>
<tr>
    <td>Password:</td>
    <td><jmhtml:text property="password" /></td>
</tr>
<tr>
    <td>Server Name:</td>
    <td><jmhtml:text property="serverName" /></td>
</tr>
</table>
<br>
&nbsp;&nbsp;
<jmhtml:submit value="Save" />
&nbsp;&nbsp;&nbsp;
<jmhtml:button property="" value="Back" onclick="JavaScript:history.back();" />
</jmhtml:form>
</body>
</html>