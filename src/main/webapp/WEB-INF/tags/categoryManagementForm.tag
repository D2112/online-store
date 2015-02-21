<%@tag description="displays category management form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="creating_form">
    <page:findAndDisplayMessage/>
    <fieldset>
        <legend>Categories</legend>
        <form id="addCategory" action="<c:url value="categories/add"/>" method="POST">
            <fmt:message key="adding-category.label.categoryName" var="categoryNameLabel"/>
            <page:inputTextField label="${categoryNameLabel}:" inputName="categoryName" value="${categoryName}"
                                 formName="addCategory"/>
            <button type="submit" class="base_button">
                <fmt:message key="adding-category.button.add"/>
            </button>
        </form>
        <br/>

        <form id="deleteCategory" action="<c:url value="categories/delete"/>" method="POST">
            <fmt:message key="adding-category.label.selectToDelete" var="selectToDeleteLabel"/>
            <page:selectCategoriesMenu formName="deleteCategory" paramName="categoryName"
                                       label="${selectToDeleteLabel}:"/>
            <button type="submit" class="base_button">
                <fmt:message key="adding-category.button.deleteCategory"/>
            </button>
        </form>
    </fieldset>
</div>