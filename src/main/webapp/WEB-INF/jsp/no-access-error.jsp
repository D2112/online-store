<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<page:genericPage title="The Suspicious Shop">
    <jsp:attribute name="leftSideBar">
        <page:userSideBar/>
    </jsp:attribute>
    <jsp:body>
        <div style="text-align: center;">
            <h1><fmt:message key="error.message.noAccess"/></h1>
        </div>
    </jsp:body>
</page:genericPage>