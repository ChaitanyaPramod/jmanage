<%@ page import="java.util.List,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Iterator"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<span class="headtext"><b><br />User Activities</b></span><br /><br />
<table border="0" cellspacing="1" cellpadding="2" width="100%" bgcolor="#E6EEF9">
<%
    List activities = (List)request.getAttribute(RequestAttributes.USER_ACTIVITIES);
    Iterator iterator = activities.iterator();
    int row = 0;
    while(iterator.hasNext()){
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        row++;
%>
  <tr class="<%=rowStyle%>">
    <td class="plaintext"><%=iterator.next()%></td>
  </tr>
  <%}//while ends %>
</table>
</body>
</html>