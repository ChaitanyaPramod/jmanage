<%@ page import="java.util.List,
                 java.util.Iterator,
                 javax.management.ObjectName"%>

<%
    List mbeanInfoList = (List)request.getAttribute("objectNameList");
    for(Iterator it = mbeanInfoList.iterator(); it.hasNext(); ){
        ObjectName objectName = (ObjectName)it.next();
        %>
            <%=objectName.getCanonicalName()%><br>
        <%
    }

%>