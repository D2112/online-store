<%@ tag description="Writes the HTML code for output categories menu." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="categories" required="true" type="java.util.Collection" %>

<div class="title_box"><fmt:message key="main.label.categories"/></div>
<ul class="list_menu">
    <c:set var="count" value="0" scope="page"/>
    <c:forEach items="${categories}" var="category" varStatus="loopStatus">
        <c:url var="categoryUrl" value="/catalog/${category.name}"/>
        <c:choose>
            <c:when test="${loopStatus.count % 2 == 0}">
                <li class="odd">
                    <a href="<c:url value="${categoryUrl}"/>">${category.name}</a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="even">
                    <a href="<c:url value="${categoryUrl}"/>">${category.name}</a>
                </li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</ul>