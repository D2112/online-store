<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@attribute name="leftSideBar" fragment="true" required="false" %>
<%@attribute name="rightSideBar" fragment="true" required="false" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title></title>
</head>
<body>
<div id="main_container">
    <div id="header"></div>
    <div id="main_content">
        <page:menuTab/>
        <jsp:invoke fragment="leftSideBar"/>
        <jsp:doBody/>
        <jsp:invoke fragment="rightSideBar"/>
    </div>
    <div class="footer"></div>
</div>
</body>
</html>
