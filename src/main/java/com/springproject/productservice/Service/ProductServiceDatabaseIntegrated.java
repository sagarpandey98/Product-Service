package com.springproject.productservice.Service;

import com.springproject.productservice.Exception.CategoryNotExistException;
import com.springproject.productservice.Exception.CrudOperationException;
import com.springproject.productservice.Exception.ProductNotExistException;
import com.springproject.productservice.Exception.ProductNotSavedException;
import com.springproject.productservice.models.Category;
import com.springproject.productservice.models.Product;
import com.springproject.productservice.repositories.CategoryRepository;
import com.springproject.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("ProductServiceDatabaseIntegrated")

public class ProductServiceDatabaseIntegrated implements ProductService{
    private final ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    @Autowired
    public ProductServiceDatabaseIntegrated (ProductRepository productRepository,
                                             CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Product> getAllProduct() throws ProductNotExistException {
        return null;
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductNotExistException("product with " + id + " doesnt exists");
        }

        return productOptional.get();
    }

    @Override
    public List<String> getAllCategories() throws CategoryNotExistException {
        return null;
    }

    @Override
    public List<Product> getInCategory(String category) throws CategoryNotExistException {
        return null;
    }

    @Override
    public Product addNewProduct(Product product) throws CrudOperationException {
        Category category = product.getCategory();
        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
        if(optionalCategory.isEmpty()){
        }
        else{
            product.setCategory(optionalCategory.get());
        }
        return productRepository.save(product);

    }

    @Override
    public Product updateProduct(Long id, Product product) throws CrudOperationException, ProductNotSavedException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new ProductNotSavedException("The product with id " + id + " doesnt exists");
        }
        Product savedProduct = optionalProduct.get();
        if (product.getTitle()!=null){
            savedProduct.setTitle(product.getTitle());
        }
        if (product.getPrice()!=null){
            savedProduct.setPrice(product.getPrice());
        }
        if (product.getDescription()!=null){
            savedProduct.setDescription(product.getDescription());
        }
        if(product.getImage()!=null){
            savedProduct.setImage(product.getImage());
        }
        if(product.getCategory()!=null) {
            if (categoryRepository.findByName(product.getCategory().getName()).isEmpty()) {
                Category newCategory = categoryRepository.save(product.getCategory());
                savedProduct.setCategory(newCategory);
            } else {
                savedProduct.setCategory(product.getCategory());
            }
        }
        return productRepository.save(savedProduct);
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws CrudOperationException, ProductNotSavedException {
        return null;
    }

    @Override
    public Product deleteProduct(Long id) throws CrudOperationException {
        return null;
    }
}
