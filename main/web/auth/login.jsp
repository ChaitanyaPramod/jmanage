<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head></head>
<body>
<jmhtml:form action="/auth/login" method="post">
Username: <jmhtml:text property="username"/>
<br>
Password: <jmhtml:password property="password"/>
<br>
<jmhtml:submit value="Login"/>
</jmhtml:form>
</body>
</html>