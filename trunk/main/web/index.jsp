<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
<title>JManage</title>
</head>
<body>
<p>&nbsp;</p>
<table border="0" width="100%">
  <tr>
    <td width="100%" colspan="2"><font face="Monotype Corsiva" size="5" color="#000080">
    - Welcome to JManage - Your friendly tool to manage applications.</font>
    </td>
  </tr>
  <tr>
  	<td width="30%">
    	<jmhtml:form action="/auth/login" method="post">
        <br />
        <p align="center">
            <font color="#800000"><b><jmhtml:errors /></b></font>
        </p>
        <p>
            <font color="#000080">Username:</font>
            <jmhtml:text property="username" />
            <br>
            <font color="#000080">Password:</font>
            <jmhtml:password property="password" />
        </p>
        <p align="center">
            <br />
            <jmhtml:submit value="Login" style="color: #000080" />
        </p>
		</jmhtml:form>
	 </td>
	 <td width="70%">&nbsp;</td>
  </tr>
</table>
</body>
</html>