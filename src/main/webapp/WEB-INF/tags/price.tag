<%@ tag description="Writes the HTML code for inserting a product view." pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="value" %>

<fmt:setLocale value="${locale.displayName}"/>
    <span class="value">
        <fmt:formatNumber type="currency" value="${value}" currencySymbol="₸"/>
    </span>