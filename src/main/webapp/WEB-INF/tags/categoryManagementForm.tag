<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<div class="creating_form">
    <fieldset>
        <legend>Categories</legend>
        <form action="<c:url value="creating-product/create"/>" method="POST">
            <page:inputTextField label="Add Category:" inputName="categoryName" value="${categoryName}"/>
            <button type="submit" class="base_button">
                Add Category
            </button>
        </form>
        <form id="deleteCategory" action="<c:url value="creating-product/create"/>" method="POST">
            <page:selectCategoriesMenu formName="deleteCategory"/>
            <button type="submit" class="base_button">
                Delete Category
            </button>
        </form>
    </fieldset>
</div>