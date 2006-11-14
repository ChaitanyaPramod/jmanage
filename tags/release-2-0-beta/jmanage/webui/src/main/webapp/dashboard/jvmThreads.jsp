<!--/dashboard/default.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/jm.tld" prefix="jm"%>

<div class="plaintext">
<p>
<jm:dashboardComponent id="com1" width="600" height="400"/>
</p>
<p>
<table border="0" cellpadding="5">
<tr>
<td valign="top">
	<jm:dashboardComponent id="com2"/>
</td>
<td valign="top">
	<jm:dashboardComponent id="com3"/>
</td>
</tr>
</table>
</p>
</div>
