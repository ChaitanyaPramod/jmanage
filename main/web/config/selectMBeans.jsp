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
<!--    /config/selectMBeans.jsp  -->
<%@ page import="java.util.Map,
                 java.util.Iterator,
                 java.util.Set"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<jmhtml:errors/>
<jmhtml:form action="/config/showAttributes" onsubmit="validateGraphForm(this)">
<%
    Map domainToObjectNameListMap = (Map)request.getAttribute("domainToObjectNameListMap");
    for(Iterator it = domainToObjectNameListMap.keySet().iterator(); it.hasNext(); ){
        String domain = (String)it.next();
%>
<table border="0" cellspacing="0" cellpadding="5" width="900" class="table">

    <tr class="tableHeader">
        <td colspan="2"><%=domain%></td>
    </tr>
        <%
        Set objectNameList = (Set)domainToObjectNameListMap.get(domain);
        for(Iterator objectNameIt = objectNameList.iterator(); objectNameIt.hasNext();){
            String objectName = (String)objectNameIt.next();
            String value = domain + ":" + objectName;
        %>
    <tr>
        <td class="plaintext"><jmhtml:checkbox property="mbeans" value="<%=value%>"/> <%=objectName%></td>
   </tr>
<%      } // inner for
%>
</table>
<br/>
<%
    } // outer for
%>
<table>
    <tr>
        <td align="center" colspan="2">
            <jmhtml:submit property="" value="Next" styleClass="Inside3d" />
            &nbsp;&nbsp;&nbsp;
            <jmhtml:button property="" value="Cancel"
                    onclick="JavaScript:history.back();" styleClass="Inside3d" />
        </td>
    </tr>
</table>
</jmhtml:form>
