<%@ page import="java.util.Map,
                 java.util.Iterator,
                 java.util.List,
                 org.jmanage.core.management.ObjectAttributeInfo,
                 org.jmanage.core.util.Expression"%> <%-- Copyright 2004-2005 jManage.org

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
<!-- /config/showAttributes.jsp  -->
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<jmhtml:errors/>
<jmhtml:form action="/config/showAddGraph" onsubmit="validateGraphForm(this)">
<%
    Map mbeanAttributesMap = (Map)request.getAttribute("mbeanAttributesMap");
    for(Iterator itr=mbeanAttributesMap.keySet().iterator(); itr.hasNext() ;){
        String objectName = (String)itr.next();
%>
<table class="table" border="0" cellspacing="0" cellpadding="3" width="600">
    <tr class="tableheader">
        <td colspan="4"><%=objectName%></td>
    </tr>
<%
        ObjectAttributeInfo[] attributes = (ObjectAttributeInfo[])mbeanAttributesMap.get(objectName);
        for(int i=0; i<attributes.length; i++){
            ObjectAttributeInfo objAttrInfo = attributes[i];
            Expression expression = new Expression("",objectName,objAttrInfo.getName());
%>
    <tr>
        <td>
            <jmhtml:checkbox property="attributes" value="<%=expression.getHtmlEscaped()%>"/>
        </td>
        <td class="plaintext"><%=objAttrInfo.getName()%></td>
        <td class="palintext"><%=objAttrInfo.getType()%></td>
        <td class="palintext"><%=objAttrInfo.getDescription()%></td>
    </tr>
<%
        }//inner for loop
%>
</table>
<br/>
<%
    }//outer for loop
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