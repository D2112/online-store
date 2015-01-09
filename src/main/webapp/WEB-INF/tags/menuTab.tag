<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for inserting a tab menu."%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>

<div id="menu_tab">
    <ul class="menu">
        <li><a href="<c:url value="signup"/>" class="nav"> Home </a></li>
        <li class="divider"></li>
        <li><a href="<c:url value="signup"/>" class="nav">Sign Up</a></li>
    </ul>
</div>