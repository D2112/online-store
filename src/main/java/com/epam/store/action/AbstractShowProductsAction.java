package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.WebContext;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public abstract class AbstractShowProductsAction implements Action {
    @Override
    public abstract ActionResult execute(WebContext webContext);

    protected List<Product> getProducts(WebContext webContext, String categoryName) {
        ProductService productService = webContext.getService(ProductService.class);
        return productService.getProductsForCategory(categoryName);
    }

    protected String getCategoryNameFromPath(WebContext webContext) {
        String category = webContext.getFirstParameterFromPath(); //category parameter passes as part of the path
        if (category != null) {
            try {
                category = URLDecoder.decode(category, "UTF-8"); //decode category name from URL decoding
            } catch (UnsupportedEncodingException e) {
                throw new ActionException(e);
            }
        }
        return category;
    }
}
