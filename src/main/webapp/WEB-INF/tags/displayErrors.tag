<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="errors" required="true" type="java.util.Collection" %>

<c:forEach items="${errors}" var="error">
    <h4><span class="error_message">${error}</span></h4>
</c:forEach>