<%@ page import="java.util.List,
                 java.util.Iterator,
                 javax.management.ObjectName,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestAttributes,
                 java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/struts/struts-bean.tld" prefix="bean"%>

<tr>
<td bgcolor="#E6EEF9">
<jmhtml:form action="/app/mbeanList" method="post">
    <jmhtml:text property="objectName" />&nbsp;&nbsp;<jmhtml:submit styleClass="Inside3d" value="Filter by object name" />
</jmhtml:form>
</td>
</tr>
<%
    List mbeanInfoList = (List)request.getAttribute("objectNameList");
    int row = 0;
    for(Iterator it = mbeanInfoList.iterator(); it.hasNext(); ){
        String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
        ObjectName objectName = (ObjectName)it.next();
        pageContext.setAttribute("objectName", objectName, PageContext.PAGE_SCOPE);
        row++;
%>
<tr>
<td class="<%=rowStyle%>">
    <jmhtml:link action="/app/mbeanView" paramId="objName" paramName="objectName" paramProperty="canonicalName"><%=objectName.getCanonicalName()%></jmhtml:link>
</td>
</tr>
<%  }%>