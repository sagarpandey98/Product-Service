package com.springproject.productservice.Service;
import com.springproject.productservice.models.Product;
import java.util.List;

public interface ProductService {
    public List<Product> getAllProduct();
    public Product getSingleProduct(Long id);
    public Product addNewProduct(Product product);
    public Product updateProduct(Long id,Product product);
    public Product deleteProduct(Long id);
}
