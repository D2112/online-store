package com.epam.store.action;

import com.epam.store.model.*;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateProductAction implements Action {
    private static final String EMPTY_STRING = "";
    private static final String CATEGORIES_LIST_NAME = "categories";
    private String categoryName;
    private String productName;
    private String price;
    private String description;
    private List<String> attributeNames;
    private List<String> attributeValues;
    private String resultMessage;

    @Override
    public ActionResult execute(WebContext webContext) {
        ActionResult previousPage = new ActionResult(webContext.getPreviousURI(), true);
        categoryName = webContext.getParameter("categoryName");
        productName = webContext.getParameter("productName");
        price = webContext.getParameter("price");
        description = webContext.getParameter("description");
        attributeNames = new ArrayList<>();
        attributeValues = new ArrayList<>();
        Collections.addAll(attributeNames, webContext.getParameterValues("attributeNames"));
        Collections.addAll(attributeValues, webContext.getParameterValues("attributeValues"));

        setAttributesToFlashScope(webContext); //for displaying on page if error
        if(isFieldsEmpty()) {
            webContext.setAttribute("resultMessage", "Some fields are not filled", Scope.FLASH);
            return previousPage;
        }
        if(!RegexValidator.isIntegerNumber(price) && !RegexValidator.isDecimalNumber(price)) {
            webContext.setAttribute("resultMessage", "Invalid Price", Scope.FLASH);
            return previousPage;
        }
        ProductService productService = webContext.getService(ProductService.class);
        productService.addProduct(createProduct(webContext));
        webContext.setAttribute("resultMessage", "Product has been created", Scope.FLASH);
        return previousPage; //redirect to previous page with message;
    }

    @SuppressWarnings("unchecked")
    private Product createProduct(WebContext webContext) {
        List<Category> categories = (List<Category>) webContext.getAttribute(CATEGORIES_LIST_NAME, Scope.APPLICATION);
        Category productCategory = null;
        for (Category category : categories) {
            if(category.getName().equals(categoryName)) {
                productCategory = category;
                break;
            }
        }
        if(productCategory == null) productCategory = new Category(categoryName);
        Product product = new Product(productName, productCategory, description, new Price(parseStringIntoBigDecimal(price)));
        product.setAttributes(parseAttributes());
        return product;
    }

    private List<Attribute> parseAttributes() {
        List<Attribute> attributeList = new ArrayList<>();
        for (int i = 0; i < attributeNames.size(); i++) {
            Attribute attribute = null;
            String value = attributeValues.get(i);
            if(RegexValidator.isIntegerNumber(value)) {
                attribute = new IntAttribute(Integer.valueOf(value));
            }
            if(RegexValidator.isDecimalNumber(value)) {
                attribute = new DecimalAttribute(parseStringIntoBigDecimal(value));
            }
            if(attribute == null) {
                attribute = new StringAttribute(value);
            }
            attribute.setName(attributeNames.get(i));
            attributeList.add(attribute);
        }
        return attributeList;
    }

    private boolean isFieldsEmpty() {
        return Arrays.asList(categoryName, productName, price, description).contains(EMPTY_STRING)
                || hasAttributesEmptyFields(attributeNames, attributeValues);
    }

    private boolean hasAttributesEmptyFields(List<String> attributeNames, List<String> attributeValues) {
        for (String attributeName : attributeNames) {
            if (attributeName.isEmpty()) return true;
        }
        for (String attributeValue : attributeValues) {
            if (attributeValue.isEmpty()) return true;
        }
        return false;
    }

    private void setAttributesToFlashScope(WebContext webContext) {
        webContext.setAttribute("attributesAmount", webContext.getParameter("attributesAmount"), Scope.FLASH);
        webContext.setAttribute("categoryName", categoryName, Scope.FLASH);
        webContext.setAttribute("productName", productName, Scope.FLASH);
        webContext.setAttribute("price", price, Scope.FLASH);
        webContext.setAttribute("description", description, Scope.FLASH);
        webContext.setAttribute("attributeNames", webContext.getParameterValues("attributeNames"),Scope.FLASH);
        webContext.setAttribute("attributeValues", webContext.getParameterValues("attributeValues"),Scope.FLASH);
    }

    private BigDecimal parseStringIntoBigDecimal(String s) {
        return BigDecimal.valueOf(Double.valueOf(s));
    }
}
