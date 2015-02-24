<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="categories" type="java.util.Collection" %>

<div class="categories_list">
    <table>
        <tr>
            <th><fmt:message key="product-management.label.selectCategory"/></th>
        </tr>
        <c:forEach items="${categories}" var="category">
            <tr>
                <td width="250">
                    <a href="/admin/products/${category.name}">${category.name}</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>