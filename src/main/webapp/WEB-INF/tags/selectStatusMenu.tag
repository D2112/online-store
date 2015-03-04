<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="defaultValue" required="false" %>
<%@ attribute name="formName" required="false" %>

<select form="${formName}" name="purchaseStatus">
    <option value="Delivery"><fmt:message key="status.Delivery"/></option>
    <option value="Canceled"><fmt:message key="status.Canceled"/></option>
    <option value="Paid"><fmt:message key="status.Paid"/></option>
    <option value="Unpaid"><fmt:message key="status.Unpaid"/></option>
    <option selected><fmt:message key="status.${defaultValue}"/></option>
</select>