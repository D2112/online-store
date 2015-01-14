<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Prints user's name" %>
<%@ attribute name="user" type="com.epam.store.model.User" %>

<c:choose>
    <c:when test="${empty user}">
        <c:set var="welcomeMessage" value="You're not logged in"/>
    </c:when>
    <c:otherwise>
        <c:set var="welcomeMessage" value="Hello, ${user.name}!"/>
    </c:otherwise>
</c:choose>

<div class="welcome_label"><label>${welcomeMessage}</label></div>
