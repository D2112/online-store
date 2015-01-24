<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<div class="creating_form">
    <h4 class="error_message"><span>${resultMessage}</span></h4>
    <form id="creatingProduct" action="<c:url value="creating-product/create"/>" method="POST">
        <fieldset>
            <legend>Creating product</legend>
            <page:selectCategoriesMenu/>
            <page:inputTextField label="Name:" inputName="productName" value="${productName}"
                                 formName="creatingProduct"/>
            <page:inputTextField label="Price:" inputName="price" value="${price}" formName="creatingProduct"/>
            <page:inputTextArea label="Description:" inputName="description" value="${description}"
                                formName="creatingProduct"/>
            <page:attibutesMenu/>
            <div class="center_text" style="padding-top: 20px">
                <button type="submit" form="creatingProduct" class="base_button creating_product_button">
                    Create product
                </button>
            </div>
        </fieldset>
    </form>
</div>


