<%@tag description="Overall side bar template" pageEncoding="UTF-8" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@attribute name="firstElement" fragment="true" required="true" %>
<%@attribute name="secondElement" fragment="true" required="false" %>
<%@attribute name="thirdElement" fragment="true" required="false" %>

<div class="side_bar">
    <div class="border_box">
        <jsp:invoke fragment="firstElement"/>
        <jsp:invoke fragment="secondElement"/>
        <jsp:invoke fragment="thirdElement"/>
    </div>
</div>