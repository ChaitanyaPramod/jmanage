<%@ taglib uri="/WEB-INF/tags/struts/struts-tiles.tld" prefix="tiles" %>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>
<body>
<tiles:insert attribute="body.main" />
</body>
</html>