<%@ tag description="Writes the HTML code for inserting a product view." pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="value" %>

<span class="value">
    <fmt:formatNumber type="currency" value="${value}" currencySymbol="₸"/>
</span>