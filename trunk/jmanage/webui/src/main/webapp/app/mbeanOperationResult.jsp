<!-- /app/mbeanOperationResult.jsp -->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.core.data.OperationResultData,
                 org.jmanage.util.StringUtils,
                 org.jmanage.core.management.ObjectOperationInfo,
                 org.jmanage.core.management.ObjectParameterInfo,
                 org.jmanage.webui.util.MBeanUtils,
                 org.jmanage.core.services.AccessController,
                 org.jmanage.core.util.ACLConstants,
                 org.jmanage.webui.util.WebContext"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<%
    final WebContext webContext = WebContext.get(request);
    ObjectOperationInfo operationInfo = (ObjectOperationInfo)request.getAttribute("operationInfo");
%>
<table class="table" border="0" cellspacing="0" cellpadding="5" width="900">
    <tr>
        <td class="headtext" width="150" nowrap><b>Object Name</b></td>
        <td class="plaintext"><c:out value="${param.objName}" /></td>
        <td class="plaintext">&nbsp;</td>
    </tr>
    <tr>
        <td class="headtext" width="150"><b>Operation Name</b></td>
        <td class="plaintext"><c:out value="${param.operationName}" /></td>
        <td class="plaintext">&nbsp;</td>
    </tr>
    <%
        int tabIndex=1;
        ObjectParameterInfo[] params = operationInfo.getSignature();
    %>
    <jmhtml:form action="/app/executeOperation">
    <tr>
        <td class="headtext" width="150"><b>Attributes</b>
            <input type="hidden" name="paramCount" value="<%=params.length%>"/>
        </td>
        <td class="plaintext">
            <%
                if(params.length > 0) {
                    int paramIndex = 0;
            %>
            <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
            <input tabindex="<%=tabIndex++%>" type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value"
                value="<%=request.getParameter(operationInfo.getName() + paramIndex + "_value")%>"/>
            <%
                    String argName = params[paramIndex].getName();
                    if(argName == null || argName.length() == 0) {
                        argName = params[paramIndex].getType();
                    }
                    String description = params[paramIndex].getDescription();
                    if(description != null && description.length() > 0) {
            %>
           <a href="JavaScript:showDescription('<%=MBeanUtils.jsEscape(description)%>');"><%=argName%></a>
            <%
                    } else {
            %>
            <%=argName%>
            <%
                    }
            %>
            <%
                    if(!params[paramIndex].getType().equals(argName)) {
            %>
            (<%=params[paramIndex].getType()%>)
            <%
                    }
            %>
            <%
                } else {
            %>
            &nbsp;
            <%
                }
            %>
        </td>
        <td class="plaintext">
            <input type="hidden" name="operationName" value="<%=operationInfo.getName()%>"/>
            <%
                if(AccessController.canAccess(webContext.getServiceContext(),
                        ACLConstants.ACL_EXECUTE_MBEAN_OPERATIONS,
                        operationInfo.getName())) {
            %>
            <input tabindex="<%=(tabIndex++) + params.length%>" type="submit" value="Execute" class="Inside3d"/>&nbsp;
            <%
                } else {
            %>
            <input tabindex="<%=(tabIndex++) + params.length%>" type="submit" value="Execute" class="Inside3d" disabled/>&nbsp;
            <%
                }
            %>
            [Impact: <%=MBeanUtils.getImpact(operationInfo.getImpact())%>]
        </td>
    </tr>
        <%
            for(int paramIndex = 1; paramIndex < params.length; paramIndex ++){
        %>
    <tr>
        <td class="plaintext">&nbsp;</td>
        <td class="plaintext">
            <input type="hidden" name="<%=operationInfo.getName()%><%=paramIndex%>_type" value="<%=params[paramIndex].getType()%>"/>
            <input tabindex="<%=tabIndex++%>" type="text" name="<%=operationInfo.getName()%><%=paramIndex%>_value"
                value="<%=request.getParameter(operationInfo.getName() + paramIndex + "_value")%>"/>
            <%
                String argName = params[paramIndex].getName();
                if(argName == null || argName.length() == 0) {
                    argName = params[paramIndex].getType();
                }
                String description = params[paramIndex].getDescription();
                if(description != null && description.length() > 0) {
            %>
            <a href="JavaScript:showDescription('<%=MBeanUtils.jsEscape(description)%>');"><%=argName%></a>
            <%
                } else {
            %>
            <%=argName%>
            <%
                }
            %>
            <%
                if(!params[paramIndex].getType().equals(argName)) {
            %>
            (<%=params[paramIndex].getType()%>)
            <%
                }
            %>
        </td>
        <td class="plaintext">&nbsp;</td>
    </tr>
        <%
            }
        %>
</jmhtml:form>
</table>
<br/>
<table class="table" border="0" cellspacing="0" cellpadding="5" width="900">
<tr class="tableHeader">
    <td width="150" nowrap>Application</td>
    <td width="50" nowrap>Status</td>
    <td>Output</td>
</tr>
<%
    OperationResultData[] resultData =
            (OperationResultData[])request.getAttribute("operationResultData");
    for(int i=0; i<resultData.length; i++){
        OperationResultData operationResult = resultData[i];
    %>
        <tr>
            <td valign="top" class="plaintext"><%=operationResult.getApplicationName()%></td>
            <td valign="top" class="plaintext">
                <%=(operationResult.getResult() == OperationResultData.RESULT_OK)?"OK":"Error"%>
            </td>
            <td valign="top" class="plaintext"><%=StringUtils.toString(operationResult.getOutput(), "<br/>", true)%></td>
        </tr>
    <%
    }
%>
</table>
<p class="plaintext"><jmhtml:link action="/app/mbeanView">Back</jmhtml:link></p>
