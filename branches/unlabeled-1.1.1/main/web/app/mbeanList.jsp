<%@ page import="java.util.List,
                 java.util.Iterator,
                 javax.management.MBeanInfo"%>

<%
    List mbeanInfoList = (List)request.getAttribute("mbeanInfoList");
    for(Iterator it = mbeanInfoList.iterator(); it.hasNext(); ){
        MBeanInfo mbeanInfo = (MBeanInfo)it.next();
        %>
            <%=mbeanInfo.getClassName()%><br>
        <%
    }

%>