<%@tag description="Overall side bar template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="blackList" required="true" type="java.util.Collection" %>

<div class="center_text">
    <table>
        <tr>
            <th>User name</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${blackList}" var="user">
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td width="130">
                    <a href="purchase-list/${user.id}">See Purchase List</a>
                </td>
                <td width="10">
                    <form method="POST" action="<c:url value="/admin/setUserBan"/>">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="hidden" name="banValue" value="false">
                        <button type="submit" class="base_button">Unban</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>