<!--/dashboard/default.jsp-->
<%@ taglib uri="/WEB-INF/tags/jmanage/jm.tld" prefix="jm"%>

<p>
<jm:dashboardComponent id="com1"/>
</p>

<script>
	function refresh(component){

		dojo.io.bind({
    		url: '/app/drawDashboardComponent.do?applicationId=<%=request.getParameter("applicationId")%>&dashBID=jvmThreads&componentID=' + component,
	    	load: function(type, data, evt){
	    	   	if(type == "load"){
					var divElement = document.getElementById(component);
					//alert(data);
            		divElement.innerHTML = data;		           
		       	}else if(type == "error"){
		           alert("error getting data from server");
		       	}else{
		           // other types of events might get passed, handle them here
		       	}
	    	  },
	    	mimetype: "text/plain"
		});
	}
</script>

<p>
<div id="com2">
	<jm:dashboardComponent id="com2"/>
</div>
<a href="JavaScript:refresh('com2')">Refresh</a>
</p>
<p>
<div id="com3">
	<jm:dashboardComponent id="com3"/>
</div>
<a href="JavaScript:refresh('com3')">Refresh</a>
</p>