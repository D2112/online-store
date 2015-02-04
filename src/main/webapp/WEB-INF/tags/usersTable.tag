<%@tag description="Overall side bar template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="users" required="true" type="java.util.Collection" %>

<div class="center_text">
    <table>
        <tr>
            <th><fmt:message key="users.label.name"/></th>
            <th><fmt:message key="users.label.email"/></th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td width="130">
                    <a href="purchase-list/${user.id}"><fmt:message key="users.label.seePurchaseList"/></a>
                </td>
                <td width="10">
                    <form method="POST" action="<c:url value="/admin/setUserBan"/>">
                        <input type="hidden" name="id" value="${user.id}">
                        <input type="hidden" name="banValue" value="true">
                        <button type="submit" class="base_button"><fmt:message key="users.button.ban"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>