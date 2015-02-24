<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="categoryName" %>
<%@ attribute name="products" type="java.util.Collection" %>

<table align="right">
    <tr>
        <th colspan="4">${categoryName}:</th>
    </tr>
    <tr>
        <th><fmt:message key="cart.label.product"/></th>
        <th><fmt:message key="cart.label.price"/></th>
        <th><fmt:message key="product.button.details"/></th>
        <th><fmt:message key="cart.label.delete"/></th>
    </tr>
    <form id="deletingProducts" method="POST" action="<c:url value="/deleteProducts"/>">
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.name}</td>
                <td><page:price value="${product.price.value}"/></td>
                <td width="20">
                    <a href="<c:url value="/details/${product.id}"/>">
                        <fmt:message key="product.button.details"/>
                    </a>
                </td>
                <td>
                    <label>
                        <input type="checkbox" form="deletingProducts" name="productIdToDelete"
                               value="${product.id}"/>
                    </label>
                </td>
            </tr>
        </c:forEach>
    </form>
    <tr>
        <th colspan="3"></th>
        <th width="60">
            <button type="submit" form="deletingProducts" class="base_button">
                <fmt:message key="cart.button.delete"/>
            </button>
        </th>
    </tr>
</table>