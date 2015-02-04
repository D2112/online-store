<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Writes the HTML code for inserting a right side bar." %>

<page:genericSideBar>
    <jsp:attribute name="firstElement">
        <page:managementList/>
    </jsp:attribute>
</page:genericSideBar>