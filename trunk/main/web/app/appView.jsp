<!--/app/appView.jsp-->
<%@ page import="org.jmanage.webui.util.WebContext,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.core.config.MBeanConfig,
                 org.jmanage.webui.util.RequestParams,
                 java.util.List,
                 java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<%
    WebContext webContext = WebContext.get(request);
    ApplicationConfig appConfig = webContext.getApplicationConfig();
%>
<%if(appConfig.getMBeans().size() > 0){%>
<table border="0" cellspacing="0" cellpadding="5" width="600" class="table">
    <tr class="tableHeader">
       <td colspan="2">Managed Objects</td>
    </tr>
<%
    List mbeans = appConfig.getMBeans();
    for(Iterator it=mbeans.iterator(); it.hasNext();){
        MBeanConfig mbeanConfig = (MBeanConfig)it.next();
%>
    <tr>
        <td class="plaintext" width="25%">
            <a href="/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=appConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>">
                    <%=mbeanConfig.getName()%></a>
        </td>
        <td class="plaintext">
            <%=mbeanConfig.getObjectName()%>
        </td>
    </tr>
<%
    }
%>
</table>
<%}else{%>
<p class="plaintext">
    There are no configured objects.
</p>
<%}%>
<p>
<jmhtml:form action="/app/mbeanList" method="post">
    <jmhtml:text property="objectName" />&nbsp;&nbsp;<jmhtml:submit styleClass="Inside3d" value="Find More Objects" />
</jmhtml:form>
</p>
