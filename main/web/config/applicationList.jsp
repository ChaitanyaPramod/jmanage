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
<body bgcolor="#E6EEF9" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div>
  <br>
  <table width="97%" border="0" align="center" cellpadding="5" cellspacing="0" class="Inside3d">
    <tr>
      <td height="20" bgcolor="#999999">
        <b><jmhtml:link href="/config/managedApplications.do" target="mbeanDetails" styleClass="a2">Managed Applications</jmhtml:link></b>
      </td>
    </tr>
<%
    List applications = (List)request.getAttribute(RequestAttributes.APPLICATIONS);
    Iterator iterator = applications.iterator();
    int row = 0;
    while(iterator.hasNext()) {
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        ApplicationConfig applicationConfig = (ApplicationConfig)iterator.next();
        row++;
        if(applicationConfig.isCluster()){
            // application cluster
%>
    <tr>
      <td class="<%=rowStyle%>">
        <a href="/app/clusterView.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" target="mbeanDetails" class="a1"><%=applicationConfig.getName()%></a>
      </td>
    </tr>
        <%
            // display cluster mbeans
            List mbeans = applicationConfig.getMBeans();
            if(mbeans != null){
                for(Iterator it=mbeans.iterator(); it.hasNext();){
                    rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
                    MBeanConfig mbeanConfig = (MBeanConfig)it.next();
                    row++;
        %>
    <tr>
      <td class="<%=rowStyle%>">&nbsp;&nbsp;&nbsp;&nbsp; <a href="/app/mbeanClusterView.do?<%=RequestParams.APPLICATION_ID%>=<%=applicationConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>" target="mbeanDetails" class="a1"><%=mbeanConfig.getName()%></a>
    </td>
    </tr>
              <%}
            }
            // display applications
            List childApplications = applicationConfig.getApplications();
            for(Iterator it=childApplications.iterator(); it.hasNext(); ){
                rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
                ApplicationConfig childApplication = (ApplicationConfig)it.next();
                row++;
                %>

    <tr>
      <td class="<%=rowStyle%>">
        <a href="/app/mbeanList.do?<%=RequestParams.APPLICATION_ID+"="+childApplication.getApplicationId()%>" target="mbeanDetails" class="a1"><%=childApplication.getName()%></a>
      </td>
    </tr>
        <%
                List appMbeans = childApplication.getMBeans();
                if(appMbeans != null){
                    for(Iterator it2=appMbeans.iterator(); it2.hasNext();){
                        rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
                        MBeanConfig mbeanConfig = (MBeanConfig)it2.next();
                        row++;
        %>
    <tr>
      <td class="<%=rowStyle%>">&nbsp;&nbsp;&nbsp;&nbsp; <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=childApplication.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>" target="mbeanDetails" class="a1"><%=mbeanConfig.getName()%></a>
    </td>
    </tr>
                <%
                    }
                }
            }
        }else{
            // standalone application
%>
    <tr>
      <td class="<%=rowStyle%>">
        <a href="/app/mbeanList.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" target="mbeanDetails" class="a1"><%=applicationConfig.getName()%></a>
      </td>
    </tr>
        <%
            List mbeans = applicationConfig.getMBeans();
            if(mbeans != null){
                for(Iterator it=mbeans.iterator(); it.hasNext();){
                    rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
                    MBeanConfig mbeanConfig = (MBeanConfig)it.next();
                    row++;
        %>
    <tr>
      <td class="<%=rowStyle%>">&nbsp;&nbsp;&nbsp;&nbsp; <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=applicationConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>" target="mbeanDetails" class="a1"><%=mbeanConfig.getName()%></a>
    </td>
    </tr>
              <%}
            }
        }// if ends
    }//while ends %>
  </table>
  <p>&nbsp;</p>
  <table width="97%" border="0" align="center" cellpadding="5" cellspacing="0" class="Inside3d">
    <tr>
      <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
        <jmhtml:link href="/auth/listUsers.do" target="mbeanDetails" styleClass="a">Users</jmhtml:link>
      </td>
    </tr>
    <tr>
      <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
        <jmhtml:link href="/auth/logout.do" target="_parent" styleClass="a">Logout</jmhtml:link>
      </td>
    </tr>
  </table>
</div>
</body>
</html>