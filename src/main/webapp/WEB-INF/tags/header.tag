<%@ tag description="Writes the HTML code for inserting a tab menu." pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="header">
    <div class="language">
        <fmt:message key="main.label.languages"/>:
        <a href="<c:url value="/changeLang?lang=en"/>"><img src="<c:url value="/static/img/en.gif"/>"/></a>
        <a href="<c:url value="/changeLang?lang=ru"/>"><img src="<c:url value="/static/img/rus.jpg"/>"/></a>
    </div>
</div>