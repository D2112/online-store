<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for inserting a tab menu." %>

<div id="menu_tab">
    <ul class="menu">
        <li><a href="<c:url value="/catalog"/>" class="nav">Home</a></li>
        <li class="divider"></li>
        <li><a href="<c:url value="/login"/>" class="nav">Log In</a></li>
        <c:if test="${sessionScope.user.role.name == 'Admin'}">
            <li class="divider"></li>
            <li><a href="<c:url value="/admin"/>" class="nav">Admin Panel</a></li>
        </c:if>
    </ul>
</div>