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
<%@ page import="java.util.List,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Iterator"%>
<table cellspacing="0" cellpadding="5" width="900" class="table">
<tr class="tableHeader">
    <td>User Activities</td>
</tr>
<%
    List activities = (List)request.getAttribute(RequestAttributes.USER_ACTIVITIES);
    Iterator iterator = activities.iterator();
    int row = 0;
    while(iterator.hasNext()){
%>
  <tr>
    <td class="plaintext"><%=iterator.next()%></td>
  </tr>
  <%}//while ends %>
</table>
