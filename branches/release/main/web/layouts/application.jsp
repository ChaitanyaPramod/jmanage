<%@ page import="org.jmanage.webui.util.RequestParams"%>
<%@ taglib uri="/WEB-INF/tags/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="8" topmargin="12" marginwidth="0" marginheight="0"
    <%if(request.getParameter(RequestParams.REFRESH_APPS) != null){%>
    onload="JavaScript:refreshApplicationsFrame();"
    <%}%> >
<table width="650" border="0" cellpadding="2" cellspacing="1">
  <tr>
    <td height="31" class="headtext"><b>Application Name:</b> <c:out value="${requestScope.applicationConfig.name}"/></td>
  </tr>
  <%-- TODO: we are forcing body.main to start with <tr>. We should fix this. --%>
  <tiles:insert attribute="body.main" />
</table>
</body>
</html>