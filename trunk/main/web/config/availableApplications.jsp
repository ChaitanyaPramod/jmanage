<!--    /config/availableApplications.jsp  -->
<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript">
        function setType(type){
            document.forms[0].type.value=type;
            document.forms[0].submit();
        }
    </script>
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />Add Applications</b></span><br /><br />
<table border="0" cellspacing="1" cellpadding="2" width="200" bgcolor="#E6EEF9">
<jmhtml:form action="/config/showAddApplication.do" method="post">
    <jmhtml:hidden property="type" value="" />
</jmhtml:form>
<%
    Map applications = (Map)request.getAttribute(RequestAttributes.AVAILABLE_APPLICATIONS);
    Iterator iterator = applications.keySet().iterator();
    int row = 0;
    while(iterator.hasNext()){
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        String applicationType = (String)iterator.next();
        row++;
%>
  <tr class="<%=rowStyle%>">
    <td class="headtext1">
        <a href="#" style="a" onclick="setType('<%=applicationType%>')"><b><%=applicationType%> application</b></a>
    </td>
  </tr>
  <%}//while ends %>
</table>
</body>
</html>