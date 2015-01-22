<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for inserting a filter menu." %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>

<div class="filter_menu">
    <div class="title_box">Filter Menu</div>
    <ul class="list_menu">
        <li class="filter_node">Something here:</li>
        <li class="filter_element">
            <label><input type="checkbox" name="checkbox" value="value">Text</label>
        </li>
    </ul>
    <input type="text" name="keyword" class="keyword_input" placeholder="type a keyword here"/>

    <div align="right">
        <button type="button" class="baseButton">Apply Filter</button>
    </div>
</div>