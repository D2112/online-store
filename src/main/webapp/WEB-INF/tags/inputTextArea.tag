<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="label" %>
<%@ attribute name="inputName" %>
<%@ attribute name="formName" required="false" %>
<%@ attribute name="value" required="false" %>

<div class="input_field">
    <label>${label}<br/>
        <textarea form="${formName}" name="${inputName}" rows="3" cols="87">${value}</textarea>
    </label>
</div>