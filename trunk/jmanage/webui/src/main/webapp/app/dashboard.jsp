<%@ page import="org.jmanage.core.util.CoreUtils"%>
<%
String jsp = CoreUtils.RELATIVE_DASHBOARDS_PATH + request.getParameter("id") + ".jsp";
%>
<jsp:include page='<%=jsp%>' />

