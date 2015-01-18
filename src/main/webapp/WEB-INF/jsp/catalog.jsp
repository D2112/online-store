<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:userSideBar/>
    </jsp:attribute>
    <jsp:attribute name="rightSideBar">
        <page:sideFilterMenu/>
    </jsp:attribute>
    <jsp:body>
        <page:displayProducts products="${param.products}"/>
    </jsp:body>
</page:genericPage>



