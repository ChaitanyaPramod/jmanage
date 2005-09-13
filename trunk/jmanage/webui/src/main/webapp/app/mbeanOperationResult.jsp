<!-- /app/mbeanOperationResult.jsp -->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.core.data.OperationResultData,
                 org.jmanage.util.StringUtils"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<table class="table" border="0" cellspacing="0" cellpadding="5" width="900">
    <tr>
        <td class="headtext" width="150" nowrap><b>Object Name</b></td>
        <td class="plaintext"><c:out value="${param.objName}" /></td>
    </tr>
    <tr>
        <td class="headtext" width="150"><b>Operation Name</b></td>
        <td class="plaintext"><c:out value="${param.operationName}" /></td>
    </tr>
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
