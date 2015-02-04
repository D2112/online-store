<%@ tag description="Prints user's name" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="user" type="com.epam.store.model.User" %>

<div class="welcome_label">
    <c:choose>
        <c:when test="${empty user}">
            <label><fmt:message key="main.label.notAuthorized"/></label><br>
            <a href="<c:url value="/login"/>"><fmt:message key="main.label.login"/></a>
            <fmt:message key="main.label.or"/>
            <a href="<c:url value="/registration"/>"><fmt:message key="main.label.register"/></a>
        </c:when>
        <c:otherwise>
            <label><fmt:message key="main.label.hello"/>, ${user.name}!</label><br>
            <a href="<c:url value="/logout"/>"><fmt:message key="main.label.logout"/></a>
        </c:otherwise>
    </c:choose>
</div>
