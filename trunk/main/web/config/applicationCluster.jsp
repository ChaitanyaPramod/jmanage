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
<%@ page import="org.jmanage.core.config.ApplicationConfig"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" language="Javascript1.1">
        function add(){
            var availableList =
                document.applicationClusterForm.standAloneApplicationIds;
            var selectedList =
                document.applicationClusterForm.childApplicationIds;
            for(var i=0; i<availableList.length; i++){
                if(availableList.options[i].selected == true){
                    selectedList.options[selectedList.length] =
                        new Option(availableList.options[i].text,
                                   availableList.options[i].value,
                                   false, false);
                    availableList.options[i] = null;
                    i--;
                }
            }
            computeSelectedChildApplications();
        }

        function remove(){
            var availableList =
                document.applicationClusterForm.standAloneApplicationIds;
            var selectedList =
                document.applicationClusterForm.childApplicationIds;
            for(var i=0; i<selectedList.length; i++){
                if(selectedList.options[i].selected == true){
                    availableList.options[availableList.length] =
                        new Option(selectedList.options[i].text,
                                   selectedList.options[i].value,
                                   false, false);
                    selectedList.options[i] = null;
                    i--;
                }
            }
            computeSelectedChildApplications();
        }

        function computeSelectedChildApplications(){
            var selectedChildApplications = "";
            var selectedList =
                document.applicationClusterForm.childApplicationIds;
            for(var i=0; i<selectedList.length; i++){
                if(selectedChildApplications != "")
                    selectedChildApplications += ",";
                selectedChildApplications += selectedList.options[i].value;
            }
            document.applicationClusterForm.selectedChildApplications.value =
                    selectedChildApplications;
         }
    </script>
</head>
<body leftmargin="10" topmargin="10" marginwidth="0" marginheight="0"
    onLoad="computeSelectedChildApplications();">
<span class="headtext"><b><br />Application Cluster</b></span><br /><br />
<jmhtml:form action="/config/saveApplicationCluster" method="post">
 <jmhtml:hidden property="applicationId" />
 <jmhtml:hidden property="refreshApps" value="true" />
 <jmhtml:hidden property="selectedChildApplications" value="" />
  <table border="0" bordercolor="black" cellspacing="1" cellpadding="2" width="250">
<tr class="oddrow">
    <td class="headtext1">Name:</td>
    <td><jmhtml:text property="name" /></td>
</tr>
</table>
<br>
<table>
<tr>
    <td colspan="3" class="headtext1">Applications in this cluster:</td>
</tr>
<tr>
    <td class="plaintext">Available</td>
    <td>&nbsp;</td>
    <td class="plaintext">Selected</td>
<tr>
    <td>
        <jmhtml:select property="standAloneApplicationIds" multiple="true">
            <jmhtml:options collection="applications" labelProperty="name" property="applicationId"/>
        </jmhtml:select>
    </td>
    <td>
        <input type="button" class="Inside3d" onClick="add();" value="Add >>"/>
        <br>
        <input type="button" class="Inside3d" onClick="remove()" value="<< Remove"/>
    </td>
    <td>
        <jmhtml:select property="childApplicationIds" multiple="true">
            <jmhtml:options collection="selectedApplications" labelProperty="name" property="applicationId"/>
        </jmhtml:select>
    </td>
</tr>
</table>
<br>
  &nbsp;&nbsp;
  <jmhtml:submit value="Save" styleClass="Inside3d"/>
  &nbsp;&nbsp;&nbsp;
  <jmhtml:button property="" value="Back" onclick="JavaScript:history.back();" styleClass="Inside3d" />
</jmhtml:form>
</body>
</html>