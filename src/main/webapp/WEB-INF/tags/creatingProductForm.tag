<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<div class="creating_form">
    <page:findAndDisplayMessage/>
    <form id="creatingProduct" action="<c:url value="creating-product/create"/>" method="POST"
          enctype="multipart/form-data">

        <fmt:message key="creating-product.label.selectCategory" var="selectCategoryLabel"/>
        <fmt:message key="creating-product.label.name" var="nameLabel"/>
        <fmt:message key="creating-product.label.price" var="priceLabel"/>
        <fmt:message key="creating-product.label.description" var="descriptionLabel"/>
        <fieldset>
            <legend><fmt:message key="admin.label.header"/></legend>
            <page:selectCategoriesMenu formName="creatingProduct" paramName="categoryName"
                                       label="${selectCategoryLabel}:"/>
            <page:inputTextField label="${nameLabel}:" inputName="productName" value="${productName}"
                                 formName="creatingProduct"/>
            <page:inputTextField label="${priceLabel}:" inputName="price" value="${price}" formName="creatingProduct"/>
            <page:inputTextArea label="${descriptionLabel}:" inputName="description" value="${description}"
                                formName="creatingProduct"/>
            <br/>
            <label class="input_field"> <fmt:message key="creating-product.label.image"/>:
                <input type="file" name="image"/>
            </label>
            <br/>
            <page:attibutesMenu/>
            <div class="center_text" style="padding-top: 20px">
                <button type="submit" form="creatingProduct" class="base_button creating_product_button">
                    <fmt:message key="creating-product.button.create"/>
                </button>
            </div>
        </fieldset>
    </form>
</div>


