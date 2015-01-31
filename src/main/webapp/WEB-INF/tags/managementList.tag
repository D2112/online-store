<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for output managements menu." %>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/style.css"/>"/>
<div class="title_box">Management</div>
<ul class="list_menu">
    <li class="odd">
        <a href="<c:url value="/admin/creating-product"/>">Create Product</a>
    </li>
    <li class="even">
        <a href="<c:url value="/admin/categories"/>">Categories</a>
    </li>
    <li class="odd">
        <a href="<c:url value="/admin/users"/>">Users</a>
    </li>
    <li class="even">
        <a href="<c:url value="/admin/black-list"/>">Black list</a>
    </li>
</ul>