<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="label" %>
<%@ attribute name="inputName" %>
<%@ attribute name="formName" required="true" %>
<%@ attribute name="value" required="false" %>

<label class="input_field">${label}
    <input type="text" name="${inputName}" value="${value}" form="${formName}" class="product_name_input"/>
</label>
