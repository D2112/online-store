<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="cart" type="com.epam.store.model.Cart" %>

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
        <th width="60">
            <button type="submit" form="deletingCheckboxes" class="base_button">Delete Selected</button>
        </th>
    </tr>
</table>

<form method="POST" action="<c:url value="/user/confirmOrder"/>">
    <div class="center_text" style="padding-top: 20px">
        <button type="submit" class="base_button creating_product_button">
            Confirm The Order
        </button>
    </div>
</form>


