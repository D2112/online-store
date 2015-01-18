<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Prints user's name" %>
<%@ attribute name="user" type="com.epam.store.model.User" %>

<div class="welcome_label">
    <c:choose>
        <c:when test="${empty user}">
            <label>You're not logged in</label><br>
            <a href="<c:url value="/login"/>">Log in</a>
            or
            <a href="<c:url value="/registration"/>">register</a>
        </c:when>
        <c:otherwise>
            <label>Hello, ${user.name}!</label><br>
            <a href="<c:url value="/logout"/>">Log out</a>
        </c:otherwise>
    </c:choose>
</div>
