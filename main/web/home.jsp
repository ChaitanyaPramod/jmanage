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
<html>
<head>
<title>jManage Home</title>
</head>
<frameset rows="48,*" cols="*" frameborder="NO" border="0" framespacing="0">
	<frame src="/top.jsp" name="applications">
	<frameset rows="*" cols="100, 680" framespacing="no" border="0">
		<frame src="/config/applicationList.do" name="applications">
		<frame src="/config/managedApplications.do" name="basefrm">
	</frameset>
</frameset>
<noframes></noframes>
<noframes>
</noframes>
</html>