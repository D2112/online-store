<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="orderList" required="true" type="java.util.Collection" %>

<div class="center_text">
    <table>
        <c:forEach items="${orderList}" var="order">
            <tr>
                <th><page:date date="${order.date}"/></th>
                <th><fmt:message key="order-list.label.totalPrice"/>: <page:price value="${order.totalPrice}"/></th>
                <th><fmt:message key="order-list.label.status"/></th>
            </tr>
            <c:forEach items="${order.purchaseList}" var="purchase">
                <tr>
                    <td>${purchase.product.name}</td>
                    <td><page:price value="${purchase.price.value}"/></td>
                    <td>${purchase.status.name}</td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</div>