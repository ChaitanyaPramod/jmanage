<%@ page import="org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestParams"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />Edit Application</b></span><br /><br />

<jmhtml:form action="/config/editApplication" method="post">
<jmhtml:hidden property="applicationId" />
<jmhtml:hidden property="refreshApps" value="true" />
<table border="0" bordercolor="black" cellspacing="1" cellpadding="2" width="250">
<tr class="oddrow">
    <td class="headtext1">Name:</td>
    <td><jmhtml:text property="name" /></td>
</tr>
<tr class="evenrow">
    <td class="headtext1">Host:</td>
    <td><jmhtml:text property="host" /></td>
</tr>
<tr class="oddrow">
    <td class="headtext1">Port:</td>
    <td><jmhtml:text property="port" /></td>
</tr>
<tr class="evenrow">
    <td class="headtext1">Username:</td>
    <td><jmhtml:text property="username" /></td>
</tr>
<tr class="oddrow">
    <td class="headtext1">Password:</td>
    <td><jmhtml:password property="password" /></td>
</tr>
</table>
<br>
&nbsp;&nbsp;
<jmhtml:submit value="Save" styleClass="Inside3d" />
&nbsp;&nbsp;&nbsp;
<jmhtml:button property="" value="Back" onclick="JavaScript:history.back();" styleClass="Inside3d" />
</jmhtml:form>
</body>
</html>