<%@ tag description="Writes the HTML code for inserting a product view." pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="date" type="com.epam.store.model.Date" %>
<jsp:useBean id="dateValue" class="java.util.Date"/>
<jsp:setProperty name="dateValue" property="time" value="${date.time}"/>

<span class="value">
    <fmt:formatDate value="${dateValue}" pattern="dd/MM/yyyy HH:mm:ss"/>
</span>