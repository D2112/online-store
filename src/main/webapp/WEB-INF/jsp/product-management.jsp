<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:adminSideBar/>
    </jsp:attribute>
    <jsp:body>
        <div style="padding-top: 20px">
            <form id="selectCategory" method="GET"
                  action="<c:url value="/admin/products"/>">
                <page:selectCategoriesMenu formName="selectCategory" paramName="category"/>
                <button type="submit"><fmt:message key="product-management.button.change"/></button>
            </form>
        </div>
        <c:if test="${products != null}">
            <c:choose>
                <c:when test="${fn:length(products) > 0}">
                    <page:productsTable products="${products}"/>
                </c:when>
                <c:otherwise>
                    <fmt:message key="product-management.message.empty" var="emptyMessage"/>
                    <page:message text="${emptyMessage}"/>
                </c:otherwise>
            </c:choose>
        </c:if>
    </jsp:body>
</page:genericPage>
