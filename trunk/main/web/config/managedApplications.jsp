<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Set,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.core.config.MBeanConfig,
                 java.net.URLEncoder,
                 java.util.List"%>
<html>
<head>
</head>
<body>
<b>Managed Applications</b>
<br><br>
<table border="1" bordercolor="black" cellspacing="0" cellpadding="2" width="200" bgcolor="lightgreen">
<%
    List applications =
            (List)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator iterator = applications.iterator();
    while(iterator.hasNext()){
        ApplicationConfig applicationConfig =
                (ApplicationConfig)iterator.next();
%>
  <tr>
    <td><%=applicationConfig.getName()%></td>
    <td><a href="/config/showEditApplication.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>">Edit</a></td>
    <td><a href="/config/deleteApplication.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>">Delete</a></td>
  </tr>
<%  }//while ends %>
</table>
<br>
<a href="/config/showAddApplication.do">Add New Application</a>
</body>
</html>