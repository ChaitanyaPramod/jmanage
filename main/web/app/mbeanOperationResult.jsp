<%--
  Copyright 2004-2005 jManage.org

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!-- /app/mbeanOperationResult.jsp -->

<%@ page import="org.jmanage.core.data.OperationResultData"%>
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
                OperationResultData[] resultData =
                        (OperationResultData[])request.getAttribute("operationResultData");
                for(int i=0; i<resultData.length; i++){
                    OperationResultData operationResult = resultData[i];
                %>
                    <tr>
                        <td class="plaintext"><%=operationResult.getApplicationName()%></td>
                        <td class="plaintext">
                            <%=(operationResult.getResult() == OperationResultData.RESULT_OK)?"OK":"Error"%>
                        </td>
                        <td class="plaintext"><%=operationResult.getOutput()%></td>
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
