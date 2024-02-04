package com.springproject.productservice.controllers;

import com.springproject.productservice.Service.FakeStoreProductService;
import com.springproject.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")

public class ProductController {
    FakeStoreProductService fakeStoreProductService;
    @Autowired
    public ProductController(FakeStoreProductService fakeStoreProductService){
        this.fakeStoreProductService = fakeStoreProductService;
    }
    @GetMapping
    public List<Product> getAllProduct(){
        return fakeStoreProductService.getAllProduct();
    }
    @GetMapping("/{id}")
    public Product getSingleProduct(@PathVariable("id") Long id){
        return fakeStoreProductService.getSingleProduct(id);
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
