<!--    /app/mbeanList.jsp  -->
<%@ page import="java.util.List,
                 java.util.Iterator,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestAttributes,
                 java.net.URLEncoder,
                 org.jmanage.core.management.ObjectName,
                 java.util.Map"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/struts/struts-bean.tld" prefix="bean"%>

<tr>
<td bgcolor="#E6EEF9">
<jmhtml:form action="/app/mbeanList" method="post">
    <jmhtml:text property="objectName" />&nbsp;&nbsp;<jmhtml:submit styleClass="Inside3d" value="Filter by object name" />
</jmhtml:form>
</td>
</tr>
<%-- TODO: /config/saveMBeanConfig has to be implemented
<jmhtml:form action="/config/saveMBeanConfig" method="post">
--%>
<%
    Map domainToObjectNameListMap = (Map)request.getAttribute("domainToObjectNameListMap");
    int row = 0;
    for(Iterator it = domainToObjectNameListMap.keySet().iterator(); it.hasNext(); ){
        String domain = (String)it.next();
        %>
        <tr>
        <td class="headtext">
            <br>
            <%=domain%>
        </td>
        </tr>
        <%
        List objectNameList = (List)domainToObjectNameListMap.get(domain);
        for(Iterator objectNameIt = objectNameList.iterator(); objectNameIt.hasNext();){
            String objectName = (String)objectNameIt.next();
            pageContext.setAttribute("objectName",
                    domain + ":" + objectName, PageContext.PAGE_SCOPE);
            String rowStyle = row % 2 != 0 ? "oddrow" : "evenrow";
            row++;
%>
            <tr>
            <td class="<%=rowStyle%>">
                <input type="checkbox" name="mbeans" value="<%=domain + ":" + objectName%>"/>
                <jmhtml:link action="/app/mbeanView"
                             paramId="objName"
                             paramName="objectName">
                    <%=objectName%></jmhtml:link>
            </td>
            </tr>
<%      } // inner for
    } // outer for
%>
<tr>
    <td>
        <jmhtml:submit styleClass="Inside3d" value="Add to Application" />
    </td>
</tr>
<%--</jmhtml:form> --%>