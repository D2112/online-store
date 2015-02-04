<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="input_field">
    <label><fmt:message key="creating-product.label.attributes"/>:<br></label>
    <table class="attributes_table">
        <tr>
            <th><fmt:message key="creating-product.label.attributeName"/></th>
            <th><fmt:message key="creating-product.label.value"/></th>
        </tr>
        <c:forEach begin="1" end="${attributesAmount}" varStatus="index">
            <c:if test="${not empty attributeNames}">
                <c:set var="attributeName" value="${attributeNames[index.count - 1]}"/>
            </c:if>
            <c:if test="${not empty attributeValues}">
                <c:set var="attributeValue" value="${attributeValues[index.count - 1]}"/>
            </c:if>
            <tr>
                <th><input form="creatingProduct" type="text" name="attributeNames" class="attribute_input"
                           value="${attributeName}"/></th>
                <th><input form="creatingProduct" type="text" name="attributeValues" class="attribute_input"
                           value="${attributeValue}"/></th>
            </tr>
        </c:forEach>
        <tr>
            <th colspan="2">
                <button form="creatingProduct" formaction="<c:url value="creating-product/setAttributesAmount"/>"
                        type="submit" class="base_button">
                    <fmt:message key="creating-product.button.setAttributesNumber"/>
                </button>
                <input form="creatingProduct" type="number" name="attributesAmount"
                       value="${attributesAmount}" min="0" max="20"/>
            </th>
        </tr>
    </table>
</div>
