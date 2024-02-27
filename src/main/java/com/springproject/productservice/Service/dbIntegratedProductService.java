package com.springproject.productservice.Service;

import com.springproject.productservice.Exception.ProductNotExistsException;
import com.springproject.productservice.models.Category;
import com.springproject.productservice.models.Product;
import com.springproject.productservice.repositories.CategoryRepository;
import com.springproject.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dbIntegratedProductService")
public class dbIntegratedProductService implements ProductService{
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    @Autowired
    public dbIntegratedProductService(ProductRepository productRepository,
                                      CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Product> getAllProduct() {
        return null;
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistsException {
        return null;
    }

    @Override
    public Product addNewProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Product deleteProduct(Long id) {
        return null;
    }

    @Override
    public List<Category> getAllCategory() {
        return null;
    }
}
