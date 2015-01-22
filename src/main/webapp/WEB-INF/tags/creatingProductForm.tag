<%@ tag description="Writes the HTML code for output managements menu." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="creating_form">
    <h4 class="error_message"><span>${resultMessage}</span></h4>
    <form id="creatingProduct" action="<c:url value="creating-product/create"/>" method="POST">
        <fieldset>
            <legend>Creating product</legend>
            <label class="creating_form_input">Select category:
                <select form="creatingProduct" name="categoryName">
                    <c:if test="${not empty categoryName}">
                        <option selected>
                                ${categoryName}
                        </option>
                    </c:if>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.name}">${category.name}</option>
                    </c:forEach>
                </select>
            </label>
            <label class="creating_form_input">Name:
                <input type="text" name="productName" value="${productName}" form="creatingProduct"
                       class="product_name_input"/>
            </label>
            <label class="creating_form_input">Price:
                <input type="text" name="price" value="${price}" form="creatingProduct"/>
            </label>

            <div class="creating_form_input">
                <label>Description:<br>
                    <textarea form="creatingProduct" name="description" rows="3"
                              cols="87">${description}</textarea>
                </label>
            </div>

            <div class="creating_form_input">
                <label>Attributes:<br></label>
                <table class="attributes_table">
                    <tr>
                        <th>Attribute name</th>
                        <th>Value</th>
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
                                       maxlength="100" value="${attributeName}"/></th>
                            <th><input form="creatingProduct" type="text" name="attributeValues"class="attribute_input"
                                       maxlength="100" value="${attributeValue}"/></th>
                        </tr>
                    </c:forEach>
                    <tr>
                        <th colspan="2">
                            <button form="creatingProduct" formaction="" type="submit" class="baseButton">Set
                                attributes
                                number
                            </button>
                            <input form="creatingProduct" type="number" name="attributesAmount"
                                   value="${attributesAmount}" min="0" max="20"/>
                        </th>
                    </tr>
                </table>
            </div>
            <div class="center_text" style="padding-top: 20px">
                <button type="submit" form="creatingProduct" class="baseButton creating_product_button">
                    Create product
                </button>
            </div>
        </fieldset>
    </form>
</div>


