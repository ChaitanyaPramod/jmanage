<script type="text/javascript" src="<%=request.getContextPath()%>/js/dojo/dojo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dashboard.js"></script>

<jsp:include page="<%=(String)request.getAttribute("dashboardPage")%>" />