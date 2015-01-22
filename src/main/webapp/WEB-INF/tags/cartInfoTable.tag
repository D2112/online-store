<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="cart" type="com.epam.store.model.Cart" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/style.css"/>"/>
<table>
    <tr>
        <th>Product</th>
        <th>Category</th>
        <th>Price</th>
        <th>Delete</th>
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
        <th colspan="2">Total:</th>
        <th><page:price value="${cart.totalPrice}"/></th>
        <th><button type="submit" form="deletingCheckboxes" class="baseButton">Delete Selected</button></th>
    </tr>
</table>