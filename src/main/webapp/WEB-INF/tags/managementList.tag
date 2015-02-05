<%@ tag description="Writes the HTML code for output managements menu." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="title_box"><fmt:message key="admin.label.management"/></div>
<ul class="list_menu">
    <li class="odd">
        <a href="<c:url value="/admin/creating-product"/>"><fmt:message key="admin.label.createProduct"/></a>
    </li>
    <li class="even">
        <a href="<c:url value="/admin/products"/>"><fmt:message key="admin.label.products"/></a>
    </li>
    <li class="odd">
        <a href="<c:url value="/admin/categories"/>"><fmt:message key="admin.label.categories"/></a>
    </li>
    <li class="even">
        <a href="<c:url value="/admin/users"/>"><fmt:message key="admin.label.users"/></a>
    </li>
    <li class="odd">
        <a href="<c:url value="/admin/black-list"/>"><fmt:message key="admin.label.blackList"/></a>
    </li>
</ul>