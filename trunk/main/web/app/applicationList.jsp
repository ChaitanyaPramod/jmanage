<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Set,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.webui.util.RequestParams"%>
<html>
<head>
</head>
<body>
<b>Managed Applications</b>
<table border="0" width="100%">
<%
    Map applications = (Map)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator ietrator = applications.keySet().iterator();
    while(ietrator.hasNext()){
        ApplicationConfig applicationConfig =
                (ApplicationConfig)applications.get(ietrator.next());
%>
  <tr>
    <td width="100%">
    <a href="/app/mbeanList.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" target="mbeanDetails"><%=applicationConfig.getName()%></a>
    </td>
  </tr>
<%  }%>
</table>
</body>
</html>