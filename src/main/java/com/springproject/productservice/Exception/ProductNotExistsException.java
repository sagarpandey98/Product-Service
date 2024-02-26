package com.springproject.productservice.Exception;

public class ProductNotExistsException extends Exception{
    public ProductNotExistsException(String message){
        super(message);
    }
}
