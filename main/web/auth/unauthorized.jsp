<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
<title>Authorization Failure</title>
</head>
<body>
<table border="0" width="100%">
  <tr>
    <td width="100%" bgcolor="#C0C0C0">
      <p align="center">
      <span style="background-color: #C0C0C0; letter-spacing: 5pt">
      <b><font color="#000080" face="Impact" size="5">Not Authorized</font></b>
      </span>
      </p>
    </td>
  </tr>
  <tr>
    <td width="100%">
      <p align="center">
      <font face="Arial" size="3" color="#800000">
        You don't have the permission to view this page
      </font>
      </p>
      <jmhtml:button property="" value="Back" onclick="JavaScript:history.back();" />
    </td>
  </tr>
</table>
</body>
</html>