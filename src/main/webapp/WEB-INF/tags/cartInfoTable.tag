<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="cart" type="com.epam.store.model.Cart" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<table>
    <thead>
    <tr>
        <th>Product</th>
        <th>Category</th>
        <th>Price</th>
        <th>Delete</th>
    </tr>
    </thead>
    <tbody>
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
    </tbody>
    <tfoot>
    <tr>
        <th colspan="2">Total:</th>
        <th>${cart.totalPrice}</th>
        <td><span><button type="submit" form="deletingCheckboxes">Delete Selected</button></span></td>
    </tr>
    </tfoot>
</table>