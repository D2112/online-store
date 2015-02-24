<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:message key="title.product-management" var="titleMsg"/>
<page:genericPage title="${titleMsg}">
    <jsp:attribute name="leftSideBar">
        <page:adminSideBar/>
    </jsp:attribute>
    <jsp:body>
        <page:categoriesTable categories="${applicationScope.categories}"/>
        <c:if test="${products != null}">
            <c:choose>
                <c:when test="${fn:length(products) > 0}">
                    <page:productsTable products="${products}" categoryName="${categoryName}"/>
                </c:when>
                <c:otherwise>
                    <fmt:message key="product-management.message.empty" var="emptyMessage"/>
                    <page:message text="${emptyMessage}"/>
                </c:otherwise>
            </c:choose>
        </c:if>
    </jsp:body>
</page:genericPage>
