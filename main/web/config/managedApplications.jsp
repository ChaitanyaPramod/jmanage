<!--    /config/managedApplications.jsp  -->
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
    <script language="JavaScript">
        function refreshApplicationsFrame(){
            parent.frames.applications.location = '/config/applicationList.do';
        }

        function deleteApplication(appId, isCluster){
            var msg;
            if(isCluster){
                msg = "Are you sure you want to delete this Application Cluster and all child applications?";
            }else{
                msg = "Are you sure you want to delete this Application?";
            }
            if(confirm(msg) == true){
                location = '/config/deleteApplication.do?<%=RequestParams.APPLICATION_ID%>=' + appId + '&refreshApps=true';
            }
        }
    </script>
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0"
    <%if(request.getParameter(RequestParams.REFRESH_APPS) != null){%>
    onload="JavaScript:refreshApplicationsFrame();"
    <%}%> >
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
    <%
      String href = null;
      if(!applicationConfig.isCluster()){
        href = "/config/showEditApplication.do";
      }else{
        href = "/config/showApplicationCluster.do";
      }%>
    <td><a href="<%=href%>?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" class="a1">Edit</a></td>
    <td><a href="JavaScript:deleteApplication('<%=applicationConfig.getApplicationId()%>', <%=applicationConfig.isCluster()%>);" class="a1">Delete</a></td>
  </tr>
  <%-- if this is a cluster, display the child applications as well --%>
      <%
      if(applicationConfig.isCluster()){
        for(Iterator childApps=applicationConfig.getApplications().iterator(); childApps.hasNext();){
            ApplicationConfig childAppConfig = (ApplicationConfig)childApps.next();
      %>
          <tr class="<%=rowStyle%>">
            <td class="headtext1">&nbsp;&nbsp;&nbsp;<%=childAppConfig.getName()%></td>
            <td><a href="/config/showEditApplication.do?<%=RequestParams.APPLICATION_ID+"="+childAppConfig.getApplicationId()%>" class="a1">Edit</a></td>
            <td><a href="JavaScript:deleteApplication('<%=childAppConfig.getApplicationId()%>', false);" class="a1">Delete</a></td>
          </tr>
      <%
        }
      }
      %>
  <%}//while ends %>
</table>
<br>
<%-- don't use the link tag here, as it adds applicationId request param --%>
<a href="/config/showAvailableApplications.do" class="a">Add New Application</a>
<br>
<a href="/config/showApplicationCluster.do" class="a">Add New Application Cluster</a>
</body>
</html>