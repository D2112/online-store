<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<div class="creating_form">
    <h4 class="error_message"><span>${resultMessage}</span></h4>

    <form id="creatingProduct" action="<c:url value="creating-product/create"/>" method="POST"
          enctype="multipart/form-data">
        <fieldset>
            <legend>Creating product</legend>
            <page:selectCategoriesMenu formName="creatingProduct"/>
            <page:inputTextField label="Name:" inputName="productName" value="${productName}"
                                 formName="creatingProduct"/>
            <page:inputTextField label="Price:" inputName="price" value="${price}" formName="creatingProduct"/>
            <page:inputTextArea label="Description:" inputName="description" value="${description}"
                                formName="creatingProduct"/>
            <br/>
            <label class="input_field"> Image:
                <input type="file" name="image"/>
            </label>
            <br/>
            <page:attibutesMenu/>
            <div class="center_text" style="padding-top: 20px">
                <button type="submit" form="creatingProduct" class="base_button creating_product_button">
                    Create product
                </button>
            </div>
        </fieldset>
    </form>
</div>


