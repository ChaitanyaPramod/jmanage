<%@ page import="org.jmanage.webui.util.RequestParams,
                 org.jmanage.webui.util.RequestAttributes,
                 org.jmanage.core.config.ApplicationConfig"%>
<%
    ApplicationConfig applicationConfig =
            (ApplicationConfig)
            request.getAttribute(RequestAttributes.APPLICATION_CONFIG);
%>
<tr>
<td class="headtext1">
Connection to the application failed
</td>
</tr>
<tr>
<td class="plaintext">
<br>
Please check the following:
<ol>
<li>Application is running.
<li>Application is correctly configured in jManage.
Click
<a href="/config/showEditApplication.do?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>" class="a1">here</a>
to edit configuration.
</ol>
</td>
</tr>