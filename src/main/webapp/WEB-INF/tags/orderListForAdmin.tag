<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="orderList" required="true" type="java.util.Collection" %>
<%@ attribute name="user" required="true" type="com.epam.store.model.User" %>

<div class="center_text">
    <table>
        <tr>
            <th colspan="4">
                <fmt:message key="order-list.label.orderListBelongsToUser:"/> ${user.name} (${user.email})
            </th>
        </tr>
    </table>
    <c:forEach items="${orderList}" var="order" varStatus="orderLoopStatus">
        <form id="changingStatusForm${orderLoopStatus.count}" method="POST"
              action="<c:url value="/admin/changePurchaseStatus"/>">
            <input type="hidden" value="${user.id}" name="userID"/>
                <%--for displaying success message--%>
            <input type="hidden" value="${orderLoopStatus.count}" name="orderNumber"/>
            <table align="right">
                <tr>
                    <th><page:date date="${order.date}"/></th>
                    <th><fmt:message key="order-list.label.totalPrice"/>: <page:price value="${order.totalPrice}"/></th>
                    <th><fmt:message key="order-list.label.status"/></th>
                </tr>
                <c:forEach items="${order.purchaseList}" var="purchase">
                    <tr>
                        <td>${purchase.product.name}</td>
                        <td><page:price value="${purchase.price.value}"/></td>
                        <td>
                            <input form="changingStatusForm${orderLoopStatus.count}" type="hidden"
                                   value="${purchase.id}"
                                   name="purchaseID"/>
                            <select form="changingStatusForm${orderLoopStatus.count}" name="purchaseStatus">
                                <option value="Delivery">Delivery</option>
                                <option value="Canceled">Canceled</option>
                                <option value="Paid">Paid</option>
                                <option value="Unpaid">Unpaid</option>
                                <option selected>${purchase.status.name}</option>
                            </select>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <th colspan="4">
                        <button type="submit" form="changingStatusForm${orderLoopStatus.count}" class="base_button">
                            <fmt:message key="order-list.button.confirmChanges"/>
                        </button>
                            <%--if in request present the number of the order it's mean that order has been changed--%>
                        <c:set var="currentOrderNumber">${orderLoopStatus.count}</c:set>
                        <c:if test="${not empty requestScope[currentOrderNumber]}">
                            <fmt:message var="successChangedMessage" key="order-list.button.successChanged"/>
                            ${successChangedMessage}
                        </c:if>
                    </th>
                </tr>
            </table>
        </form>
    </c:forEach>
</div>