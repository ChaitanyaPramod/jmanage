<!--    /config/applicationList.jsp  -->
<%@ page import="java.util.Map,
                 org.jmanage.webui.util.RequestAttributes,
                 java.util.Set,
                 org.jmanage.core.config.ApplicationConfig,
                 java.util.Iterator,
                 org.jmanage.webui.util.RequestParams,
                 org.jmanage.core.config.MBeanConfig,
                 java.net.URLEncoder,
                 java.util.List"%>

<%@ taglib uri="/WEB-INF/tags/jmanage/html.tld" prefix="jmhtml"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/css/styles.css" rel="stylesheet" type="text/css" />

    <style>
       BODY {background-color: white}
       TD {font-size: 11px;
           font-family: Verdana, Arial, Helvetica, sans-serif;
           text-decoration: none;
           white-space:nowrap;}
       A  {text-decoration: none;
           color: black}

        .specialClass {font-family:garamond; font-size:12pt;color:green;font-weight:bold;text-decoration:underline}
    </style>

<!-- Code for browser detection -->
<script src="/js/ua.js"></script>

<!-- Infrastructure code for the tree -->
<script src="/js/ftiens4.js"></script>

<script>

    // Decide if the names are links or just the icons
    USETEXTLINKS = 1;  //replace 0 with 1 for hyperlinks

    // Decide if the tree is to start all open or just showing the root folders
    STARTALLOPEN = 0; //replace 0 with 1 to show the whole tree

    ICONPATH = '/images/treeview/'; //change if the gif's folder is a subfolder, for example: 'images/'

    HIGHLIGHT = 1; // highlights the selected nodes
    PRESERVESTATE = 1; // preserves the state of the tree, using cookies

    foldersTree = gFld("<b>Managed Applications</b>", "/config/managedApplications.do");

    <%
        List apps = (List)request.getAttribute(RequestAttributes.APPLICATIONS);
        Iterator appsit= apps.iterator();
        while(appsit.hasNext()) {
            ApplicationConfig applicationConfig = (ApplicationConfig)appsit.next();
    %>
            aux1 = insFld(foldersTree, gFld("<%=applicationConfig.getName()%>", "<%=applicationConfig.isCluster()?"/app/clusterView.do":"/app/mbeanList.do"%>?<%=RequestParams.APPLICATION_ID+"="+applicationConfig.getApplicationId()%>"));

        <%
            List mbeans = applicationConfig.getMBeans();
            if(mbeans != null){
                for(Iterator it=mbeans.iterator(); it.hasNext();){
                    MBeanConfig mbeanConfig = (MBeanConfig)it.next();
        %>
            insDoc(aux1, gLnk("R", "<%=mbeanConfig.getName()%>", "<%=applicationConfig.isCluster()?"/app/mbeanClusterView.do":"/app/mbeanView.do"%>?<%=RequestParams.APPLICATION_ID%>=<%=applicationConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>"));
         <%
                }// for ends
            }// if ends

            if(applicationConfig.isCluster()){
                for(Iterator it=applicationConfig.getApplications().iterator(); it.hasNext(); ){
                    ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
                 %>
                    aux2 = insFld(aux1, gFld("<%=childAppConfig.getName()%>", "/app/mbeanList.do?<%=RequestParams.APPLICATION_ID+"="+childAppConfig.getApplicationId()%>"));
                <%
                    mbeans = childAppConfig.getMBeans();
                    if(mbeans != null){
                        for(Iterator it2=mbeans.iterator(); it2.hasNext();){
                            MBeanConfig mbeanConfig = (MBeanConfig)it2.next();
                    %>
                        insDoc(aux2, gLnk("R", "<%=mbeanConfig.getName()%>", "/app/mbeanView.do?<%=RequestParams.APPLICATION_ID%>=<%=childAppConfig.getApplicationId()%>&<%=RequestParams.OBJECT_NAME%>=<%=URLEncoder.encode(mbeanConfig.getObjectName(), "UTF-8")%>"));
                    <%
                        }// for ends
                    }// if ends
                }// for ends
            }// if ends
        }//while ends
    %>

</script>


</head>
<body bgcolor="#E6EEF9" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div>
  <br />
    <%-- tree view --%>
    <table width="97%" border="0" align="center" cellpadding="5" cellspacing="0"  class="Inside3d">
        <tr>
            <td bgcolor="#E6EEF9">
            <!-- By making any changes to this code you are violating your user agreement.
                 Corporate users or any others that want to remove the link should check
                 the online FAQ for instructions on how to obtain a version without the link -->
            <!-- Removing this link will make the script stop from working -->
            <div style="position:absolute; top:0; left:0; ">
            <table border="0">
                <tr>
                <td>
                    <font size="-2">
                    <a style="font-size:7pt;text-decoration:none;color:silver"
                                href="http://www.treemenu.net/" target="_blank">
                                JavaScript Tree Menu
                    </a>
                    </font>
                </td>
                </tr>
            </table>
            </div>
            <script>initializeDocument()</script>
            <noscript>
                You must enable JavaScript in your browser.
            </noscript>
            </td>
        </tr>
        <%-- --%>
        <tr><td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">&nbsp;</td></tr>
        <tr><td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">&nbsp;</td></tr>
        <tr>
          <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
            <a href="/config/showAvailableApplications.do" target="basefrm" class="a">Add New Application</a>
          </td>
        </tr>
        <tr>
          <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
            <a href="/config/showApplicationCluster.do" target="basefrm" class="a">Add New Application Cluster</a>
          </td>
        </tr>
        <tr>
          <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
            <jmhtml:link href="/auth/listUsers.do" target="basefrm" styleClass="a">Users</jmhtml:link>
          </td>
        </tr>
        <tr>
          <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
            <jmhtml:link action="/auth/showChangePassword" target="basefrm" styleClass="a">Change Password</jmhtml:link>
          </td>
        </tr>
        <tr>
          <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
            <jmhtml:link action="/config/showConfigure" target="basefrm" styleClass="a">Configure</jmhtml:link>
          </td>
        </tr>
        <tr>
          <td height="20" bordercolor="#E6EEF9" bgcolor="#E6EEF9">
            <jmhtml:link href="/auth/logout.do" target="_parent" styleClass="a">Logout</jmhtml:link>
          </td>
        </tr>
    </table>
</div>

</body>
</html>
