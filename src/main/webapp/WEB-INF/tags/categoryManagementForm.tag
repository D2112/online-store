<%@tag description="displays creating product form" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<div class="creating_form">
    <page:findAndDisplayMessage/>
    <fieldset>
        <legend>Categories</legend>
        <form id="addCategory" action="<c:url value="categories/add"/>" method="POST">
            <page:inputTextField label="Category name:" inputName="categoryName" value="${categoryName}"
                                 formName="addCategory"/>
            <button type="submit" class="base_button">
                Add Category
            </button>
        </form>
        <br/>

        <form id="deleteCategory" action="<c:url value="categories/delete"/>" method="POST">
            <page:selectCategoriesMenu formName="deleteCategory" label="Select category to delete:"/>
            <button type="submit" class="base_button">
                Delete Category
            </button>
        </form>
    </fieldset>
</div>