<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Writes the HTML code for inserting a right side bar." %>

<page:genericSideBar>
    <jsp:attribute name="firstElement">
        <page:cartPreview cart="${sessionScope.cart}"/>
    </jsp:attribute>
    <jsp:attribute name="secondElement">
        <page:welcomeLabel user="${param.user}"/>
    </jsp:attribute>
    <jsp:attribute name="thirdElement">
        <page:categoriesList categories="${applicationScope.categories}"/>
    </jsp:attribute>
</page:genericSideBar>