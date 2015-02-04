<%@ tag description="Writes the HTML code for inserting a tab menu." %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="user" type="com.epam.store.model.User" %>

<h2><fmt:message key="profile.label.yourName"/>: ${user.name}</h2>

<h2><fmt:message key="profile.label.yourEmail"/>: ${user.email}</h2>
<br/>
