<!-- /app/mbeanOperationResult.jsp -->

<%@ page import="java.util.Map,
                 java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/tags/jstl/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<tr>
    <td>
    <table border="0">
        <tr>
            <td class="headtext" width="30%" nowrap><b>Object Name</b></td>
            <td class="plaintext" width="70%"><c:out value="${param.objName}" /></td>
        </tr>
        <tr>
            <td class="headtext" width="30%" nowrap><b>Operation Name</b></td>
            <td class="plaintext" width="70%"><c:out value="${param.operationName}" /></td>
        </tr>
    </table>
    </td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<%--
          TODO: we will have to create a OperationResult object which contains
          the application name, result (error, ok), error message.
        --%>
<tr>
    <td class="headtext">Results</td>
</tr>
<tr>
    <td>
        <table border="0">
            <tr>
                <td class="headtext">Application</td>
                <td class="headtext">Status</td>
                <td class="headtext">Output</td>
            </tr>
            <%
                Map appNameToResultMap =
                        (Map)request.getAttribute("appNameToResultMap");
                for(Iterator it=appNameToResultMap.keySet().iterator(); it.hasNext();){
                    String appName = (String)it.next();
                    Object output = appNameToResultMap.get(appName);
                %>
                    <tr>
                        <td class="plaintext"><%=appName%></td>
                        <td class="plaintext">OK</td>
                        <td class="plaintext"><%=output%></td>
                    </tr>
                <%
                }
            %>
        </table>
    </td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td class="plaintext"><jmhtml:link action="/app/mbeanView">Back</jmhtml:link></td>
</tr>
