<!--/dashboard/default.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/jm.tld" prefix="jm"%>
<table class="plaintext">
    <tr>
        <td><jm:dashboardComponent id="com1"/></td>
        <td><jm:dashboardComponent id="com2"/></td>
    </tr>
    <tr>
        <td colspan="2">
            <table class="plaintext">
                <th>Misc</th>
                <tr>
                    <td><jm:dashboardComponent id="com3"/></td>
                </tr>
            </table>
        </td>
    </tr>
</table>