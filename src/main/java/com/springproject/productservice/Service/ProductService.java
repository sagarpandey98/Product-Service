package com.springproject.productservice.Service;
import com.springproject.productservice.Exception.ProductNotExistsException;
import com.springproject.productservice.models.Category;
import com.springproject.productservice.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
public interface ProductService {
    public List<Product> getAllProduct();
    public Product getSingleProduct(Long id) throws ProductNotExistsException;
    public Product addNewProduct(Product product);
    public Product updateProduct(Long id,Product product);
    public Product deleteProduct(Long id);
    public List<Category> getAllCategory();
}
