<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:userSideBar/>
    </jsp:attribute>
    <jsp:body>
        <page:userPersonalData user="${sessionScope.user}"/>
        <h2><fmt:message key="profile.label.orderList"/>:<h2>
        <page:orderList orderList="${orderList}"/>
    </jsp:body>
</page:genericPage>
