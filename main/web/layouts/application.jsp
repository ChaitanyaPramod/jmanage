<%@ taglib uri="/WEB-INF/tags/struts/struts-tiles.tld" prefix="tiles" %>

<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>
<body>
<table width="800">
<tr>
    <td>
    <table border="0" cellspacing="4" bgcolor="lightgreen" width="100%"><tr><td>
    <b>Application Name:</b>
    <c:out value="${requestScope.applicationConfig.name}"/>
    </td></tr></table>
    </td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td>
        <tiles:insert attribute="body.main" />
    </td>
</tr>
</table>
</body>
</html>