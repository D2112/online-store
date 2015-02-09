<%@ tag description="Writes HTML code to display cart info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="cart" type="com.epam.store.model.Cart" %>

<table>
    <tr>
        <th><fmt:message key="cart.label.product"/></th>
        <th><fmt:message key="cart.label.category"/></th>
        <th><fmt:message key="cart.label.price"/></th>
        <th><fmt:message key="cart.label.delete"/></th>
    </tr>
    <form id="deletingCheckboxes" method="POST" action="<c:url value="/deleteFromCart"/>">
        <c:forEach var="product" items="${cart.products}">
            <tr>
                <td>${product.name}</td>
                <td>${product.category.name}</td>
                <td><page:price value="${product.price.value}"/></td>
                <td>
                    <label>
                        <input type="checkbox" form="deletingCheckboxes" name="productIdToDelete"
                               value="${product.id}"/>
                    </label>
                </td>
            </tr>
        </c:forEach>
    </form>
    <tr>
        <th colspan="2"><fmt:message key="cart.label.total"/>:</th>
        <th><page:price value="${cart.totalPrice}"/></th>
        <th width="60">
            <button type="submit" form="deletingCheckboxes" class="base_button">
                <fmt:message key="cart.button.delete"/>
            </button>
        </th>
    </tr>
</table>

<form method="POST" action="<c:url value="/user/confirmOrder"/>">
    <div class="center_text" style="padding-top: 20px">
        <button type="submit" class="base_button creating_product_button">
            <fmt:message key="cart.button.confirm"/>
        </button>
    </div>
</form>


