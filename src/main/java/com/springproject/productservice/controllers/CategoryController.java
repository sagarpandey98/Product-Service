package com.springproject.productservice.controllers;

import com.springproject.productservice.Service.FakeStoreProductService;
import com.springproject.productservice.dtos.FakeStoreProductDto;
import com.springproject.productservice.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products/category")

public class CategoryController {
    FakeStoreProductService fakeStoreProductService;
    @Autowired
    public CategoryController(FakeStoreProductService fakeStoreProductService){
        this.fakeStoreProductService = fakeStoreProductService;
    }
    @GetMapping("/all")
    public List<Category> getAllCategory(){
        return fakeStoreProductService.getAllCategory();
    }
}
