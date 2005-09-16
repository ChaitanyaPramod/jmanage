<!--    /config/addGraph.jsp  -->
<%@ page errorPage="/error.jsp" %>
<%@ page import="org.jmanage.core.util.Expression,
                 org.jmanage.webui.util.RequestParams"%>
<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>
<jmhtml:javascript formName="graphForm" />
<jmhtml:errors />
<jmhtml:form action="/config/addGraph" method="post"
                                    onsubmit="return validateGraphForm(this)">
<jmhtml:hidden property="graphId"/>
<table cellspacing="0" cellpadding="5" width="400" class="table">
<tr class="tableheader">
<%
    if(request.getParameter(RequestParams.GRAPH_ID)!=null){
%>
    <td colspan="2">Edit Graph</td>
<%
    }else{
%>
    <td colspan="2">Add Graph</td>
<%
    }
%>
</tr>
<tr>
    <td class=headtext1>Graph Name</td>
    <td><jmhtml:text property="graphName"/></td>
</tr>
<tr>
    <td class=headtext1>Polling Interval</td>
    <td><jmhtml:text property="pollInterval"/></td>
</tr>
</table>
<br/>
<table cellspacing="0" cellpadding="5" width="600" class="table">
<tr class="tableheader">
    <td>Attribute Name</td>
    <td>Object Name</td>
    <td>Display Name</td>
</tr>
<%
    String[] attributeNames = (String[])request.getAttribute("attributeNames");
    String[] objectNames = (String[])request.getAttribute("objectNames");
    String[] displayNames = (String[])request.getAttribute("displayNames");
    for(int i=0; i<attributeNames.length; i++){
        Expression expression = new Expression("",objectNames[i], attributeNames[i]);
%>
<jmhtml:hidden property="attributes" value="<%=expression.toString()%>"/>
<tr>
    <td class="plaintext"><%=attributeNames[i]%></td>
    <td class="plaintext"><%=objectNames[i]%></td>
    <td><input type="text" name="displayNames" value="<%=displayNames[i]%>"/></td>
<%
    }
%>
</table>
<br/>
<table>
<tr>
    <td align="center" colspan="2">
        <jmhtml:submit property="" value="Save" styleClass="Inside3d" />
        &nbsp;&nbsp;&nbsp;
        <jmhtml:button property="" value="Cancel"
                onclick="JavaScript:history.back();" styleClass="Inside3d" />
    </td>
</tr>
</table>
</jmhtml:form>