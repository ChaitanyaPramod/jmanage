<!--    /config/availableApplications.jsp  -->
<%@ page errorPage="/error.jsp" %>
<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<script language="JavaScript">
    function setType(type){
        document.forms[0].type.value=type;
        document.forms[0].submit();
    }
</script>

<jmhtml:form action="/config/showAddApplication.do" method="post">
    <jmhtml:hidden property="type" value="" />
</jmhtml:form>
<table class="table" border="0" cellspacing="0" cellpadding="5" width="300">
<tr class="tableHeader">
    <td>Select Application Type</td>
</tr>

<%
    Map applications = (Map)request.getAttribute(RequestAttributes.AVAILABLE_APPLICATIONS);
    Iterator iterator = applications.keySet().iterator();
    while(iterator.hasNext()){
        String applicationType = (String)iterator.next();
%>
  <tr>
    <td class="plaintext">
        <a href="javascript:setType('<%=applicationType%>');"><b><%=applicationType%> application</b></a>
    </td>
  </tr>
  <%}//while ends %>
</table>
