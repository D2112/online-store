<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="formName" required="true" %>

<label class="input_field">Select category:
    <select form="${formName}" name="categoryName">
        <c:forEach items="${categories}" var="category">
            <option value="${category.name}">${category.name}</option>
        </c:forEach>
        <c:if test="${not empty categoryName}">
            <option selected>${categoryName}</option>
        </c:if>
    </select>
</label>
