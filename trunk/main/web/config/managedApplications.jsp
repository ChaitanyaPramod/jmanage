<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Set,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.core.config.MBeanConfig,
                 java.net.URLEncoder,
                 java.util.List"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />Managed Applications</b></span><br /><br />
<table border="0" cellspacing="1" cellpadding="2" width="200" bgcolor="#E6EEF9">
<%
    List applications = (List)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator iterator = applications.iterator();
    int row = 0;
    while(iterator.hasNext()){
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        ApplicationConfig applicationConfig = (ApplicationConfig)iterator.next();
        row++;
%>
  <tr class="<%=rowStyle%>">
    <td class="headtext1"><%=applicationConfig.getName()%></td>
    <td><a href="/config/showEditApplication.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" class="a1">Edit</a></td>
    <td><a href="/config/deleteApplication.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" class="a1">Delete</a></td>
  </tr>
  <%}//while ends %>
</table>
<br>
<jmhtml:link href="/config/showAddApplication.do" styleClass="a">Add New Application</jmhtml:link>
</body>
</html>