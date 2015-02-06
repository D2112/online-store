package com.epam.store.action;

import com.epam.store.Images;
import com.epam.store.model.*;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


@WebAction(path = "POST/admin/creating-product/create")
public class CreateProductAction extends AbstractCreatingProductAction {
    private static final String EMPTY_STRING = "";
    private static final String CATEGORIES_LIST_NAME = "categories";
    private ResourceBundle messagesBundle;
    private String categoryName;
    private String productName;
    private String price;
    private String description;
    private Image productImage;
    private List<String> attributeNames;
    private List<String> attributeValues;

    @Override
    public ActionResult execute(WebContext webContext) {
        messagesBundle = webContext.getMessagesBundle();
        String alreadyExistError = messagesBundle.getString("creating-product.error.alreadyExist");
        String successMessage = messagesBundle.getString("creating-product.message.success");

        ActionResult previousPage = new ActionResult(webContext.getPreviousURI(), true);
        getParametersFromRequest(webContext);
        String validationErrorMessage = validateInputData(webContext);
        if (validationErrorMessage != null) {
            super.setAttributesToFlashScope(webContext); //for displaying on page if error
            webContext.setAttribute("errorMessage", validationErrorMessage, Scope.FLASH);
            return previousPage;
        }
        try {
            productImage = getImageFromRequest(webContext);
        } catch (IOException | ServletException e) {
            throw new ActionException("exception while getting image from the request", e);
        }
        ProductService productService = webContext.getService(ProductService.class);
        //Check if there already exist product with that name
        Product productByName = productService.getProductByName(productName);
        if (productByName != null) {
            super.setAttributesToFlashScope(webContext); //for displaying on page if error
            webContext.setAttribute("errorMessage", alreadyExistError, Scope.FLASH);
            return previousPage;
        }
        productService.addProduct(createProduct(webContext));
        webContext.setAttribute("successMessage", successMessage, Scope.FLASH);
        return previousPage; //redirect to previous page with message;
    }

    private List<Attribute> parseAttributes() {
        List<Attribute> attributeList = new ArrayList<>();
        for (int i = 0; i < attributeNames.size(); i++) {
            Attribute attribute = null;
            String value = attributeValues.get(i);
            if (RegexValidator.isIntegerNumber(value)) {
                attribute = new IntegerAttribute(Integer.valueOf(value));
            }
            if (RegexValidator.isDecimalNumber(value)) {
                attribute = new DecimalAttribute(parseStringToBigDecimal(value));
            }
            if (attribute == null) {
                attribute = new StringAttribute(value);
            }
            attribute.setName(attributeNames.get(i));
            attributeList.add(attribute);
        }
        return attributeList;
    }

    private void validateInputLength() {
        //todo validate input length
    }

    //fixme do this check another way
    private boolean isFieldsEmpty() {
        return Arrays.asList(categoryName, productName, price, description).contains(EMPTY_STRING)
                || hasAttributesEmptyFields(attributeNames, attributeValues);
    }

    private boolean hasAttributesDuplicateNames() {
        Set<String> set = new HashSet<>(attributeNames);
        return set.size() < attributeNames.size();
    }

    private boolean hasAttributesEmptyFields(List<String> attributeNames, List<String> attributeValues) {
        return attributeNames.contains(EMPTY_STRING) || attributeValues.contains(EMPTY_STRING);
    }

    private BigDecimal parseStringToBigDecimal(String s) {
        return BigDecimal.valueOf(Double.valueOf(s));
    }

    @SuppressWarnings("unchecked")
    private Product createProduct(WebContext webContext) {
        List<Category> categories = (List<Category>) webContext.getAttribute(CATEGORIES_LIST_NAME, Scope.APPLICATION);
        Category productCategory = null;
        //Find product category in categories from application context
        //because need category with id from database
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                productCategory = category;
                break;
            }
        }
        if (productCategory == null) productCategory = new Category(categoryName);
        Price productPrice = new Price(parseStringToBigDecimal(price));
        Product product = new Product(productName, productCategory, description, productPrice, productImage);
        product.setAttributes(parseAttributes());
        return product;
    }

    private void getParametersFromRequest(WebContext webContext) {
        categoryName = webContext.getParameter("categoryName");
        productName = webContext.getParameter("productName");
        price = webContext.getParameter("price");
        description = webContext.getParameter("description");
        String[] attributeNamesParameter = webContext.getParameterValues("attributeNames");
        String[] attributeValuesParameter = webContext.getParameterValues("attributeValues");
        attributeNames = new ArrayList<>();
        attributeValues = new ArrayList<>();
        if (attributeNamesParameter != null && attributeValuesParameter != null) {
            Collections.addAll(attributeNames, attributeNamesParameter);
            Collections.addAll(attributeValues, attributeValuesParameter);
        }
    }

    /**
     * Validates input data for empty fields,
     * duplicate attribute names, checks is price a number
     *
     * @return message with validation error or null if there is no validation errors
     */
    private String validateInputData(WebContext webContext) {
        if (isFieldsEmpty()) return messagesBundle.getString("creating-product.error.notFilled");
        if (hasAttributesDuplicateNames()) return messagesBundle.getString("creating-product.error.duplicate");
        //if price is not a number
        if (!RegexValidator.isIntegerNumber(price) && !RegexValidator.isDecimalNumber(price)) {
            return messagesBundle.getString("creating-product.error.price");
        }
        return null;
    }

    private Image getImageFromRequest(WebContext webContext) throws IOException, ServletException {
        Part part = webContext.getPart("image");
        String imageName = part.getSubmittedFileName();
        String contentType = part.getContentType();
        InputStream content = part.getInputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = content.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = output.toByteArray();
        byte[] resizeImage = Images.resize(imageBytes, Image.STANDARD_WIDTH, Image.STANDARD_HEIGHT);
        return new Image(imageName, contentType, resizeImage);
    }
}
