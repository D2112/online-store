<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="formName" required="true" %>
<%@ attribute name="paramName" required="true" %>
<%@ attribute name="label" required="false" %>

<label class="input_field">${label}
    <select form="${formName}" name="${paramName}">
        <c:forEach items="${categories}" var="category">
            <option value="${category.name}">${category.name}</option>
        </c:forEach>
        <c:if test="${not empty paramName}">
            <c:choose>
                <c:when test="${not empty param[paramName]}">
                    <option selected>${param[paramName]}</option>
                </c:when>
                <c:when test="${not empty requestScope[paramName]}">
                    <option selected>${requestScope[paramName]}</option>
                </c:when>
            </c:choose>
        </c:if>
    </select>
</label>
