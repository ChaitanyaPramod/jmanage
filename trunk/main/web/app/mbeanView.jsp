<%@ page import="java.util.Iterator,
                 javax.management.*,
                 org.jmanage.webui.util.RequestParams"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

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

<b>Object Name:</b> <%=request.getParameter(RequestParams.OBJECT_NAME)%> <br>
<b>Class Name:</b> <%=mbeanInfo.getClassName()%> <br>
<b>Description:</b><br> <%=mbeanInfo.getDescription()%>
<br><br>
<b>Attributes:</b>
<br><br>
<jmhtml:form action="/app/updateAttributes" method="post">

<table border="1" cellspacing="5">
<tr>
    <td><b>Name</b></td>
    <td><b>Value</b></td>
    <td><b>Type</b></td>
</tr>
<%
    for(int index=0; index < attributes.length; index++){
        MBeanAttributeInfo attributeInfo = attributes[index];
%>
<tr>
    <td>
        <a href="JavaScript:showDescription('<%=jsEscape(attributeInfo.getDescription())%>');">
            <%=attributeInfo.getName()%></a>
    </td>
    <td>
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
    <td>
        <%=attributeInfo.getType()%>
    </td>
</tr>
<%
    }
%>
</table>
To save the changes to the attribute values click on
<jmhtml:submit value="Save"/>

</jmhtml:form>

<%if(operations.length > 0){%>
<br><br>
<b>Operations:</b>
<br><br>
<table>
<%
    for(int index=0; index < operations.length; index++){
        MBeanOperationInfo operationInfo = operations[index];
%>
<jmhtml:form action="/app/executeOperation">
<tr>
    <td>
        <%=operationInfo.getReturnType()%>
        <a href="JavaScript:showDescription('<%=jsEscape(operationInfo.getDescription())%>');">
            <%=operationInfo.getName()%></a>
        (
        <%
            MBeanParameterInfo[] params = operationInfo.getSignature();
            %>
            <input type="hidden" name="paramCount" value="<%=params.length%>"/>
            <%
            for(int paramIndex = 0; paramIndex < params.length; paramIndex ++){
        %>
            <%if(paramIndex>0){%>,&nbsp;<%}%>
            <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
            <input type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value" value=""/>
            <%=params[paramIndex].getType()%>
        <%
            }
        %>
        )
    </td>
    <td>
        <input type="hidden" name="operationName" value="<%=operationInfo.getName()%>"/>
        <jmhtml:submit value="Execute"/>&nbsp;
        [Impact: <%=getImpact(operationInfo.getImpact())%>]
    </td>
</tr>
</jmhtml:form>
<%
    }
%>
</table>
<%}%>
<%if(notifications.length > 0){%>
<br><br>
<b>Notifications:</b>
<table>
<%
    for(int index=0; index < notifications.length; index++){
        MBeanNotificationInfo notificationInfo = notifications[index];
%>
<tr>
    <td>
        <%=notificationInfo.getName()%>
    </td>
    <td>
        <%=notificationInfo.getDescription()%>
    </td>
</tr>
<%
    }
%>
</table>
<%}%>
