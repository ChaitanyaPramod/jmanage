<%@ page import="java.util.List,
                 java.util.Iterator,
                 javax.management.ObjectName,
                 org.jmanage.core.config.ApplicationConfig,
                 org.jmanage.webui.util.RequestAttributes,
                 java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<%@ taglib uri="/WEB-INF/tags/struts/struts-bean.tld" prefix="bean"%>


<jmhtml:form action="/app/mbeanList" method="post">
    Filter by object name: <jmhtml:text property="objectName" /> &nbsp;&nbsp;
    <jmhtml:submit/>
</jmhtml:form>

<hr width="100%"/>
<%
    List mbeanInfoList = (List)request.getAttribute("objectNameList");
    for(Iterator it = mbeanInfoList.iterator(); it.hasNext(); ){
        ObjectName objectName = (ObjectName)it.next();
        pageContext.setAttribute("objectName", objectName, PageContext.PAGE_SCOPE);
        %>
            <jmhtml:link action="/app/mbeanView" paramId="objName" paramName="objectName" paramProperty="canonicalName">
                <%=objectName.getCanonicalName()%></jmhtml:link><br>
        <%
    }

%>