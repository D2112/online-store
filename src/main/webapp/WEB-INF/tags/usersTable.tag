<%@tag description="Overall side bar template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="users" required="true" type="java.util.Collection" %>
<%@ attribute name="user" type="com.epam.store.model.User" %>

<div style="text-align: center;">
    <table>
        <tr>
            <th>User name</th>
            <th>Email</th>
            <th></th>
        </tr>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>banned</td>
            </tr>
        </c:forEach>
    </table>
</div>