<!--    /auth/ChangePassword.jsp  -->
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />Change Password</b></span><br /><br />
<%--<jmhtml:javascript formName="ChangePasswordForm" />--%>
<jmhtml:errors />
<jmhtml:form action="/auth/changePassword" method="post">

<table border="0" bordercolor="black" cellspacing="1" cellpadding="2" width="300">
<tr class="oddrow">
    <td class="headtext1">Old Password:</td>
    <td><jmhtml:password property="oldPassword" value=""/></td>
</tr>
<tr class="evenrow">
    <td class="headtext1">New Password:</td>
    <td><jmhtml:password property="newPassword" value=""/></td>
</tr>
<tr class="oddrow">
    <td class="headtext1">Confirm Password:</td>
    <td><jmhtml:password property="confirmPassword" value=""/></td>
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