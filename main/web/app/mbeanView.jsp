<%@ page import="java.util.Iterator,
                 org.jmanage.core.management.*,
                 org.jmanage.webui.util.RequestParams,
                 java.util.Arrays,
                 java.util.Comparator,
                 org.jmanage.webui.util.WebContext,
                 org.jmanage.core.auth.User,
                 java.util.List"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>

<%
    final WebContext webContext = WebContext.get(request);
    final User user = webContext.getUser();

    ObjectInfo objectInfo = (ObjectInfo)request.getAttribute("objInfo");
    ObjectAttributeInfo[] attributes = objectInfo.getAttributes();
    Arrays.sort(attributes, new Comparator(){
        public int compare(Object o1, Object o2) {
            ObjectAttributeInfo attrInfo1 = (ObjectAttributeInfo)o1;
            ObjectAttributeInfo attrInfo2 = (ObjectAttributeInfo)o2;
            return attrInfo1.getName().compareToIgnoreCase(attrInfo2.getName());
        }
    });
    ObjectOperationInfo[] operations = objectInfo.getOperations();
    ObjectNotificationInfo[] notifications = objectInfo.getNotifications();

    List attributeList =
            (List)request.getAttribute("attributeList");
%>

<%!
    private String jsEscape(String str){
        StringBuffer buff = new StringBuffer(str.length());
        for(int i=0; i<str.length(); i++){
            final char ch = str.charAt(i);
            if(ch == '"'){
                buff.append("&quot;");
            }else if(ch == '\''){
                buff.append("\\");
                buff.append(ch);
            }else{
                buff.append(ch);
            }
        }
        return buff.toString();
    }

    private String getValue(List attributeList,
                            String attrName){
        String value = null;
        for(Iterator it=attributeList.iterator(); it.hasNext(); ){
            ObjectAttribute attribute = (ObjectAttribute)it.next();
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
            case ObjectOperationInfo.INFO:
                return "Information";
            case ObjectOperationInfo.ACTION:
                return "Action";
            case ObjectOperationInfo.ACTION_INFO:
                return "Action and Information";
            case ObjectOperationInfo.UNKNOWN:
                return "Unknown";
            default:
                return "Invalid Impact Value";

        }
    }
%>

<script language="JavaScript1.1" type="text/javascript">

    function refreshApplicationsFrame(){
        parent.frames.applications.location = '/config/applicationList.do';
    }

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
        <tr><td colspan="2" nowrap>
            <c:choose>
                <c:when test="${requestScope.mbeanIncluded == 'true'}">
                    <jmhtml:form action="/config/removeMBeanConfig">
                        <jmhtml:hidden property="objectName"/>
                        <jmhtml:hidden property="refreshApps" value="true"/>
                        <a href="JavaScript:document.mbeanConfigForm.submit();" class="a1">Remove from Favorites</a>
                    </jmhtml:form>
                </c:when>
                <c:otherwise>
                    <jmhtml:form action="/config/addMBeanConfig">
                        <jmhtml:text property="name"/>
                        <jmhtml:hidden property="objectName"/>
                        <jmhtml:hidden property="refreshApps" value="true"/>
                        <a href="JavaScript:document.mbeanConfigForm.submit();" class="a1">Add to Favorites</a>
                    </jmhtml:form>
                </c:otherwise>
            </c:choose>
        </td></tr>
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
        ObjectAttributeInfo attributeInfo = attributes[index];
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
    <%if(user.isAdmin() && attributeInfo.isWritable() && !attrValue.equals("Object")){%>
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
<%if(user.isAdmin()){%>
To save the changes to the attribute values click on
<jmhtml:submit value="Save" styleClass="Inside3d" />
<%}%>
</jmhtml:form>
</td>
</tr>

<%if(operations.length > 0 && user.isAdmin()){%>
<tr><td class="headtext" align="left"><b>Operations</b></td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<table>
<%
    for(int index=0; index < operations.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        ObjectOperationInfo operationInfo = operations[index];
%>
<jmhtml:form action="/app/executeOperation">
<tr>
    <td class="<%=rowStyle%>"><%=operationInfo.getReturnType()%>
    <a href="JavaScript:showDescription('<%=jsEscape(operationInfo.getDescription())%>');"><%=operationInfo.getName()%></a>
    (
    <%
        ObjectParameterInfo[] params = operationInfo.getSignature();
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

<%if(notifications.length > 0 && user.isAdmin()){%>
<tr><td class="headtext" align="left"><b>Notifications</b></td></tr>
<tr>
<td bgcolor="#E6EEF9" class="plaintext">
<table>
<%
    for(int index=0; index < notifications.length; index++){
        String rowStyle = index % 2 != 0 ? "oddrow" : "evenrow";
        ObjectNotificationInfo notificationInfo = notifications[index];
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