<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:adminSideBar/>
    </jsp:attribute>
    <jsp:body>
        <c:choose>
            <c:when test="${users != null}">
                <page:usersTable users="${users}"/>
            </c:when>
            <c:when test="${blackList != null}">
                <page:blackListTable blackList="${blackList}"/>
            </c:when>
            <c:when test="${orderList != null}">
                <page:orderList orderList="${orderList}"/>
            </c:when>
            <c:when test="${products != null}">
                <form id="selectCategory" method="GET" action="/admin/changeCategory/${categoryName}">
                    <page:selectCategoriesMenu formName="selectCategory" paramName="categoryName"/>
                    <button type="submit">Change Category</button>
                </form>
                <page:productsTable products="${products}"/>
            </c:when>
        </c:choose>
    </jsp:body>
</page:genericPage>
