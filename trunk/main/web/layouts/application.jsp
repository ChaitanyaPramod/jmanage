<%@ taglib uri="/WEB-INF/tags/struts/struts-tiles.tld" prefix="tiles" %>

<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>
<body>
<table>
<tr>
    <td><b>Application: <c:out value="${requestScope.applicationConfig.name}"/></b></td>
</tr>
</table>
<tiles:insert attribute="body.main" />
</body>
</html>