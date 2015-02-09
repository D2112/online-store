<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="text" required="true" %>
<%@ attribute name="error" required="false" type="java.lang.Boolean" %>
<%@ attribute name="success" required="false" type="java.lang.Boolean" %>

<c:choose>
    <c:when test="${error}">
        <div class="error_message">
            <h2>${text}</h2>
        </div>
    </c:when>
    <c:when test="${success}">
        <div class="success_message">
            <h2>${text}</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="message">
            <h2>${text}</h2>
        </div>
    </c:otherwise>
</c:choose>
