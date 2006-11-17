<!--/dashboard/default.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/jm.tld" prefix="jm"%>

<div class="plaintext">
<p>
<jm:dashboardComponent id="com1" width="600" height="400"/>
</p>
<p>
<table class="plaintext" cellpadding="5" style="border:1;border-style:solid;border-width:1px;border-color:#C0C0C0">
	<tr>
		<td valign="top">
			<jm:dashboardComponent id="com2"/>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<jm:dashboardComponent id="com3"/>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<jm:dashboardComponent id="com4"/>
		</td>
	</tr>
</table>
<a href="JavaScript:history.back()">back</a>
</p>
</div>
