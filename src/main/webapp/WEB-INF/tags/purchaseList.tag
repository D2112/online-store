<%@tag description="Overall side bar template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="orderList" required="true" type="java.util.Collection" %>

<div class="center_text">
    <table>
        <c:forEach items="${orderList}" var="order">
            <tr>
                <th><page:date date="${order.date}"/></th>
                <th>Total price: ${order.totalPrice}</th>
                <th>Status</th>
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