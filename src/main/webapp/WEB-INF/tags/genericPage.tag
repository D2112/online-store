<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="title" fragment="false" required="false" %>
<%@attribute name="leftSideBar" fragment="true" required="false" %>
<%@attribute name="rightSideBar" fragment="true" required="false" %>

<fmt:setLocale value="${locale.displayName}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/css/style.css"/>"/>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title>${title}</title>
</head>
<body>
<div id="main_container">
    <page:header/>
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
