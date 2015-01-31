<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:adminSideBar/>
    </jsp:attribute>
    <jsp:body>
        <page:categoryManagementForm/>
    </jsp:body>
</page:genericPage>

