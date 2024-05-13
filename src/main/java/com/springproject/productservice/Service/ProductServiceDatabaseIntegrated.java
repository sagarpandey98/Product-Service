package com.springproject.productservice.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.productservice.Exception.CategoryNotExistException;
import com.springproject.productservice.Exception.CrudOperationException;
import com.springproject.productservice.Exception.ProductNotExistException;
import com.springproject.productservice.dtos.SendEmailEventDto;
import com.springproject.productservice.models.Category;
import com.springproject.productservice.models.Product;
import com.springproject.productservice.repositories.CategoryRepository;
import com.springproject.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service("ProductServiceDatabaseIntegrated")

public class ProductServiceDatabaseIntegrated implements ProductService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductServiceDatabaseIntegrated (ProductRepository productRepository,
                                             CategoryRepository categoryRepository,
                                             RedisTemplate<String, Object> redisTemplate, KafkaTemplate kafkaTemplate, ObjectMapper objectMapper){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection().flushAll();

    }
    @Override
    public List<Product> getAllProduct() throws ProductNotExistException {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
            throw new ProductNotExistException("No product exists");
        }
        else{
            return products;
        }
    }

    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistException {
        if(redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_" + id) != null){
            return ((Product)redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_" + id));
        }

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ProductNotExistException("product with id: " + id + " doesnt exists");
        }
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCTS_"+id, productOptional.get());

        return productOptional.get();
    }

    @Override
    public List<String> getAllCategories() throws CategoryNotExistException {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new CategoryNotExistException("No Category exist");
        }
        else{
            List<String> categoryNames = new ArrayList<>();
            for(Category category: categories){
                categoryNames.add(category.getName());
            }
            return categoryNames;
        }
    }

    @Override
    public List<Product> getInCategory(String category) throws CategoryNotExistException {
        List<Product> products = productRepository.findByCategory_Name(category);
        if(products.isEmpty()){
            throw new CategoryNotExistException("No product in category " + category);
        }
        else{
            return products;
        }
    }


    @Override
    public Product addNewProduct(Product product) throws JsonProcessingException, CrudOperationException {
        Category category = createCategory(product.getCategory());
        product.setCategory(category);

        // Check if product already exists
        Optional<Product> existingProduct = productRepository.findByTitle(product.getTitle());
        if (existingProduct.isPresent()) {
            throw new CrudOperationException("Product with title " + product.getTitle() + " already exists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String to = (String) jwt.getClaims().get("mail");

        product.setCreatedAt(LocalDateTime.now());
        product.setLastUpdatedAt(LocalDateTime.now());

        // Save product and handle any exceptions
        try {
            product = productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }

        SendEmailEventDto sendEmailEventDto = new SendEmailEventDto();
        sendEmailEventDto.setTo(to);
        sendEmailEventDto.setFrom("sagarbvmdelhi@gmail.com");
        sendEmailEventDto.setSubject("Welcome to UserProductNexus");
        sendEmailEventDto.setBody("Welcome to UserProductNexus. Product with title: " + product.getTitle() + " has been added to our catalog");

        // Send email and handle any exceptions
        try {
            kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(sendEmailEventDto));
        } catch (Exception e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }

        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new ProductNotExistException("The product with id " + id + " doesnt exists");
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
                product.getCategory().setCreatedAt(LocalDateTime.now());
                product.getCategory().setLastUpdatedAt(LocalDateTime.now());
                Category newCategory = categoryRepository.save(product.getCategory());
                savedProduct.setCategory(newCategory);
            } else {
                savedProduct.setCategory(product.getCategory());
            }
        }
        savedProduct.setLastUpdatedAt(LocalDateTime.now());
        return productRepository.save(savedProduct);
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new ProductNotExistException("The product with id " + id + " doesnt exists");
        }
        Product savedProduct = optionalProduct.get();
        savedProduct.setTitle(product.getTitle());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setImage(product.getImage());
        if(product.getCategory()!=null) {
            if (categoryRepository.findByName(product.getCategory().getName()).isEmpty()) {
                product.getCategory().setCreatedAt(LocalDateTime.now());
                product.getCategory().setLastUpdatedAt(LocalDateTime.now());
                Category newCategory = categoryRepository.save(product.getCategory());
                savedProduct.setCategory(newCategory);
            } else {
                savedProduct.setCategory(product.getCategory());
            }
        }
        savedProduct.setLastUpdatedAt(LocalDateTime.now());
        return productRepository.save(savedProduct);
    }

    @Override
    public Product deleteProduct(Long id) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new ProductNotExistException("The product with id " + id + " doesnt exists");
        }
        Product product = optionalProduct.get();
        product.setDeleted(true);
        try {
            product = productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error saving product: " + e.getMessage(), e);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String to = (String) jwt.getClaims().get("mail");

        SendEmailEventDto sendEmailEventDto = new SendEmailEventDto();
        sendEmailEventDto.setTo(to);
        sendEmailEventDto.setFrom("sagarbvmdelhi@gmail.com");
        sendEmailEventDto.setSubject("Welcome to UserProductNexus");
        sendEmailEventDto.setBody("Welcome to UserProductNexus. Product with title: " + product.getTitle() + " has been added to our catalog");

        // Send email and handle any exceptions
        try {
            kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(sendEmailEventDto));
        } catch (Exception e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }

        return product;
    }

    private Category createCategory(Category category) {
        Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
        if (optionalCategory.isEmpty()) {
            category.setCreatedAt(LocalDateTime.now());
            category.setLastUpdatedAt(LocalDateTime.now());
            category = categoryRepository.save(category);
        } else {
            category = optionalCategory.get();
        }
        return category;
    }
}
