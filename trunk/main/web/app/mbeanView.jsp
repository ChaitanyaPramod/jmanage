<%@ page import="javax.management.MBeanInfo,
                 javax.management.MBeanAttributeInfo,
                 java.util.Iterator"%>
<%
    MBeanInfo mbeanInfo = (MBeanInfo)request.getAttribute("mbeanInfo");
    MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
%>
Class Name: <%=mbeanInfo.getClassName()%>
<br><br>
Attributes:
<br>
<%
    for(int index=0; index < attributes.length; index++){
        MBeanAttributeInfo attributeInfo = attributes[index];
%>
Name: <%=attributeInfo.getName()%>
<br>
<%
    }
%>
