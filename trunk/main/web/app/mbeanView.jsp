<%@ page import="java.util.Iterator,
                 javax.management.*,
                 org.jmanage.webui.util.RequestParams"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<%
    MBeanInfo mbeanInfo = (MBeanInfo)request.getAttribute("mbeanInfo");
    MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
    MBeanOperationInfo[] operations = mbeanInfo.getOperations();
    MBeanNotificationInfo[] notifications = mbeanInfo.getNotifications();

    AttributeList attributeList =
            (AttributeList)request.getAttribute("attributeList");
%>

<%!
    private String jsEscape(String str){
        // TODO: implement to escapge ', "", and others
        // str = str.replaceAll("\\'", "\'");
        //str = str.replaceAll("\\\"", "\\\"");
        return str;
    }

    private String getValue(AttributeList attributeList,
                            String attrName){
        String value = null;
        for(Iterator it=attributeList.iterator(); it.hasNext(); ){
            Attribute attribute = (Attribute)it.next();
            if(attribute.getName().equals(attrName)){
                //TODO: handle different return types
                Object objValue = attribute.getValue();
                if(isPrimitive(objValue.getClass())){
                    value = objValue.toString();
                }else{
                    value = "Object";
                }
                break;
            }
        }
        return value;
    }

    private boolean isPrimitive(Class clazz){
        if(clazz.isPrimitive()
            || clazz.isAssignableFrom(Boolean.class)
            || clazz.isAssignableFrom(Character.class)
            || clazz.isAssignableFrom(Byte.class)
            || clazz.isAssignableFrom(Short.class)
            || clazz.isAssignableFrom(Integer.class)
            || clazz.isAssignableFrom(Long.class)
            || clazz.isAssignableFrom(Float.class)
            || clazz.isAssignableFrom(Double.class)
            || clazz.isAssignableFrom(Void.class)
            || clazz.isAssignableFrom(String.class)){
            return true;
        }
        return false;
    }

    private String getImpact(int impact){
        switch(impact){
            case MBeanOperationInfo.INFO:
                return "Information";
            case MBeanOperationInfo.ACTION:
                return "Action";
            case MBeanOperationInfo.ACTION_INFO:
                return "Action and Information";
            case MBeanOperationInfo.UNKNOWN:
                return "Unknown";
            default:
                return "Invalid Impact Value";

        }
    }
%>

<script language="JavaScript1.1" type="text/javascript">
    function showDescription(description){
        if(description.length == 0){
            description = "No description available.";
        }
        alert(description);
    }
</script>

<tr>
    <td>
    <table border="0">
        <tr><td class="headtext" width="30%" nowrap><b>Object Name</b></td><td class="plaintext" width="70%"><c:out value="${param.objName}" /></td></tr>
        <tr><td class="headtext" width="30%" nowrap><b>Class Name</b></td><td class="plaintext" width="70%"><c:out value="${requestScope.mbeanInfo.className}" /></td></tr>
        <tr><td class="headtext" width="30%" nowrap><b>Description</b></td><td class="plaintext" width="70%"><c:out value="${requestScope.mbeanInfo.description}" /></td></tr>
        <tr><td>&nbsp;</td></tr>
        <tr><td class="headtext" colspan="2" align="left"><b>Attributes</b></td></tr>
    </table>
    </td>
</tr>

<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<jmhtml:form action="/app/updateAttributes" method="post">
<table border="0" cellspacing="5">
<tr>
    <td class="headtext"><b>Name</b></td>
    <td class="headtext"><b>Value</b></td>
    <td class="headtext"><b>Type</b></td>
</tr>
<%
    for(int index=0; index < attributes.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        MBeanAttributeInfo attributeInfo = attributes[index];
%>
<tr>
<td class="<%=rowStyle%>">
    <a href="JavaScript:showDescription('<%=jsEscape(attributeInfo.getDescription())%>');"><%=attributeInfo.getName()%></a>
</td>
<td class="<%=rowStyle%>">
    <%
        String attrValue =
                getValue(attributeList, attributeInfo.getName());
    %>
    <%if(attributeInfo.isWritable() && !attrValue.equals("Object")){%>
        <input type="text" name="attr+<%=attributeInfo.getName()%>+<%=attributeInfo.getType()%>" size="50"
        value="<%=attrValue%>"/>
    <%}else{%>
        <%=attrValue%>
    <%}%>
</td>
<td class="<%=rowStyle%>">
    <%=attributeInfo.getType()%>
</td>
</tr>
<%  }%>
</table>
To save the changes to the attribute values click on
<jmhtml:submit value="Save" styleClass="Inside3d" />
</jmhtml:form>
</td>
</tr>

<%if(operations.length > 0){%>
<tr><td class="headtext" align="left"><b>Operations</b></td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<table>
<%
    for(int index=0; index < operations.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        MBeanOperationInfo operationInfo = operations[index];
%>
<jmhtml:form action="/app/executeOperation">
<tr>
    <td class="<%=rowStyle%>"><%=operationInfo.getReturnType()%>
    <a href="JavaScript:showDescription('<%=jsEscape(operationInfo.getDescription())%>');"><%=operationInfo.getName()%></a>
    (
    <%
        MBeanParameterInfo[] params = operationInfo.getSignature();
    %>
    <input type="hidden" name="paramCount" value="<%=params.length%>"/>
    <%
        for(int paramIndex = 0; paramIndex < params.length; paramIndex ++){
            if(paramIndex>0){%>,&nbsp;<%}%>
            <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
            <input type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value" value=""/>
            <%=params[paramIndex].getType()%>
    <%  }%>
    )
    </td>
    <td class="<%=rowStyle%>">
        <input type="hidden" name="operationName" value="<%=operationInfo.getName()%>"/>
        <jmhtml:submit value="Execute" styleClass="Inside3d"/>&nbsp;
        [Impact: <%=getImpact(operationInfo.getImpact())%>]
    </td>
</tr>
</jmhtml:form>
<%  }%>
</table>
</td>
</tr>
<%}%>

<%if(notifications.length > 0){%>
<tr><td class="headtext" align="left"><b>Notifications</b></td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<table>
<%
    for(int index=0; index < notifications.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        MBeanNotificationInfo notificationInfo = notifications[index];
%>
<tr>
    <td class="<%=rowStyle%>"><%=notificationInfo.getName()%></td>
    <td class="<%=rowStyle%>"><%=notificationInfo.getDescription()%></td>
</tr>
<%  }%>
</table>
</td>
</tr>
<%}%>