<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
</head>
<body>
<b>Edit Application</b>
<br><br>

<jmhtml:form action="/config/editApplication" method="post">
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
</table>
</jmhtml:form>
</body>
</html>