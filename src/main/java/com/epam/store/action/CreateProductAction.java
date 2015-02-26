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
        ActionResult previousPage = new ActionResult(webContext.getPreviousURI(), true);
        getParametersFromRequest(webContext);
        List<String> validationErrors = validateInputData();
        if (validationErrors.size() > 0) {
            super.setAttributesToFlashScope(webContext); //for displaying on page if error
            webContext.setAttribute("errors", validationErrors, Scope.FLASH);
            return previousPage;
        }
        ProductService productService = webContext.getService(ProductService.class);
        if (productService.isProductExist(productName)) {
            super.setAttributesToFlashScope(webContext); //for displaying on page if error
            webContext.setAttribute
                    ("errorMessage", messagesBundle.getString("creating-product.error.alreadyExist"), Scope.FLASH);
            return previousPage;
        }
        productService.addProduct(createProduct(webContext));
        webContext.setAttribute
                ("successMessage", messagesBundle.getString("creating-product.message.success"), Scope.FLASH);
        return previousPage; //redirect to previous page with message;
    }

    /**
     * Validates input data for empty fields,
     * duplicate attribute names, checks is price a number
     *
     * @return message with validation error or null if there is no validation errors
     */
    private List<String> validateInputData() {
        List<String> errors = new ArrayList<>();
        if (isFieldsEmpty()) {
            errors.add(messagesBundle.getString("creating-product.error.notFilled"));
            return errors; //return to prevent errors when trying validate empty fields
        }
        if (Validator.isDescriptionTooBig(description)) {
            errors.add(messagesBundle.getString("creating-product.error.descriptionTooBig"));
        }
        if (productImage == null) {
            errors.add(messagesBundle.getString("creating-product.error.image"));
        }
        if (hasAttributesDuplicateNames()) {
            errors.add(messagesBundle.getString("creating-product.error.duplicate"));
        }
        if (Validator.notNumber(price)) {
            errors.add(messagesBundle.getString("creating-product.error.price"));
        }
        if (Validator.isNumberTooLarge(price)) {
            errors.add(messagesBundle.getString("creating-product.error.largeNumber"));
        }
        return errors;
    }



    private List<Attribute> parseAttributes() {
        List<Attribute> attributeList = new ArrayList<>();
        for (int i = 0; i < attributeNames.size(); i++) {
            Attribute attribute = null;
            String name = attributeNames.get(i);
            String value = attributeValues.get(i);
            if (Validator.isIntegerNumber(value)) {
                attribute = new IntegerAttribute(name, Integer.valueOf(value));
            }
            if (Validator.isDecimalNumber(value)) {
                attribute = new DecimalAttribute(name, parseStringToBigDecimal(value));
            }
            if (attribute == null) {
                attribute = new StringAttribute(name, value);
            }
            attributeList.add(attribute);
        }
        return attributeList;
    }

    private boolean isFieldsEmpty() {
        if (Arrays.asList(categoryName, productName, price, description).contains(EMPTY_STRING)) return true;
        return (attributeNames.contains(EMPTY_STRING) || attributeValues.contains(EMPTY_STRING));
    }

    private boolean hasAttributesDuplicateNames() {
        Set<String> set = new HashSet<>(attributeNames);
        return set.size() < attributeNames.size();
    }

    private BigDecimal parseStringToBigDecimal(String s) {
        return BigDecimal.valueOf(Double.valueOf(s));
    }

    private void getParametersFromRequest(WebContext webContext) {
        categoryName = webContext.getParameter("categoryName");
        productName = webContext.getParameter("productName");
        price = webContext.getParameter("price");
        description = webContext.getParameter("description");
        try {
            productImage = getImageFromRequest(webContext);
        } catch (IOException | ServletException e) {
            throw new ActionException("exception while getting image from the request", e);
        }
        String[] attributeNamesParameter = webContext.getParameterValues("attributeNames");
        String[] attributeValuesParameter = webContext.getParameterValues("attributeValues");
        attributeNames = new ArrayList<>();
        attributeValues = new ArrayList<>();
        if (attributeNamesParameter != null && attributeValuesParameter != null) {
            Collections.addAll(attributeNames, attributeNamesParameter);
            Collections.addAll(attributeValues, attributeValuesParameter);
        }
    }

    private Image getImageFromRequest(WebContext webContext) throws IOException, ServletException {
        Part part = webContext.getPart("image");
        String imageName = part.getSubmittedFileName();
        String contentType = part.getContentType();
        byte[] imageBytes;
        try (InputStream content = part.getInputStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            if (content.available() == 0) return null; //if nothing to read it means image bytes is empty
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = content.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            imageBytes = output.toByteArray();
        }
        byte[] reducedImage = Images.resize(imageBytes, Image.STANDARD_WIDTH, Image.STANDARD_HEIGHT);
        return new Image(imageName, contentType, reducedImage);
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
        if (productCategory == null) throw new ActionException("Such category is not exist " + categoryName);
        Price productPrice = new Price(parseStringToBigDecimal(price));
        Product product = new Product(productName, productCategory, description, productPrice, productImage);
        product.setAttributes(parseAttributes());
        return product;
    }
}
