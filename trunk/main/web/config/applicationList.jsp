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
<b><a href="/config/managedApplications.do" target="mbeanDetails">Managed Applications</a></b>
<table border="0" width="100%">
<%
    List applications =
            (List)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator iterator = applications.iterator();
    while(iterator.hasNext()){
        ApplicationConfig applicationConfig =
                (ApplicationConfig)iterator.next();
%>
  <tr>
    <td width="100%">
        <a href="/app/mbeanList.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" target="mbeanDetails"><%=applicationConfig.getName()%></a>
    </td>
  </tr>
<%
        for(Iterator it=applicationConfig.getMBeans().iterator(); it.hasNext();){
            MBeanConfig mbeanConfig = (MBeanConfig)it.next();
%>
    <tr>
      <td>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=applicationConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>" target="mbeanDetails">
            <%=mbeanConfig.getName()%></a>
      </td>
    </tr>
<%
        }
%>

<%  }//while ends %>
</table>
</body>
</html>