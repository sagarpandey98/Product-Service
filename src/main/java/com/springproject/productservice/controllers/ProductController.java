package com.springproject.productservice.controllers;

import com.springproject.productservice.Exception.ProductNotExistsException;
import com.springproject.productservice.Service.FakeStoreProductService;
import com.springproject.productservice.Service.ProductService;
import com.springproject.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")

public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(@Qualifier("dbIntegratedProductService") ProductService productService){
        this.productService = productService;
    }
    @GetMapping
    public List<Product> getAllProduct(){
        return productService.getAllProduct();
    }
    @GetMapping("/{id}")
    public Product getSingleProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        return productService.getSingleProduct(id);
    }
    @PostMapping()
    public Product addNewProduct(@RequestBody Product product){
        return null;
    }
    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        return null;
    }
    @DeleteMapping("/{id}")
    public Product deleteProduct(@PathVariable("id") Long id){
        return null;
    }



}
