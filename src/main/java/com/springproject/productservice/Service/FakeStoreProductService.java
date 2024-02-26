package com.springproject.productservice.Service;

import com.springproject.productservice.Exception.ProductNotExistsException;
import com.springproject.productservice.dtos.FakeStoreProductDto;
import com.springproject.productservice.models.Category;
import com.springproject.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Getter
@Setter
@Service
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;
    @Autowired
    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private Product convertFakeStoreProductToProduct(FakeStoreProductDto fakeStoreProduct){
        Product product = new Product();
        product.setId(fakeStoreProduct.getId());
        product.setTitle(fakeStoreProduct.getTitle());
        product.setDescription(fakeStoreProduct.getDescription());
        product.setPrice(fakeStoreProduct.getPrice());
        product.setImage(fakeStoreProduct.getImage());
        product.setCategory(new Category());
        product.getCategory().setName(fakeStoreProduct.getCategory());

        return product;
    }
    @Override
    public List<Product> getAllProduct() {
        FakeStoreProductDto[] fakeStoreProductsArray = restTemplate.getForObject("https://fakestoreapi.com/products",FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProducts: fakeStoreProductsArray){
            products.add(convertFakeStoreProductToProduct(fakeStoreProducts));
        }
        return products;
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistsException {
        FakeStoreProductDto fakeStoreProduct = restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDto.class);
        if(fakeStoreProduct==null){
            throw new ProductNotExistsException("Product with id: " + id + " does not exists");
        }
        return convertFakeStoreProductToProduct(fakeStoreProduct);

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
        String[] categoriesArray = restTemplate.getForObject("https://fakestoreapi.com/products/categories", String[].class);
        List<Category> categoriesList = new ArrayList<>();
        for(String str: categoriesArray){
            Category temp = new Category();
            temp.setName(str);
            categoriesList.add(temp);
        }
        return categoriesList;
    }
}
