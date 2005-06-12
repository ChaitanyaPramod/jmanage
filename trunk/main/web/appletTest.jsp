<%@ page import="org.jmanage.webui.util.Utils"%>
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


<applet
    code="org/jmanage/webui/applets/GraphApplet.class"
    archive="/applets/applets.jar,/applets/jfreechart-0.9.20.jar,/applets/jcommon-0.9.5.jar"
    width="600"
    height="500">
    <param name="graphTitle" value="Title of Graph"">
    <PARAM name="pollingInterval" value="5">
    <!-- todo: append jsessionid to the url, so that it uses the same user session -->
    <PARAM name="remoteURL" value="http://localhost:9090/app/fetchAttributeValues.do;jsessionid=<%=Utils.getCookieValue(request, "JSESSIONID")%>">
    <PARAM name="attributes" value="[testApp/jmanage:name=Configuration/AppUptime],[testApp2/jmanage:name=Configuration/AppUptime]">
</applet>
