package com.example.webapp.controller;

import com.example.api.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductRepository productRepository;    
    
    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        initSampleProducts();
    }
    
    // Initialize sample products if repository is empty
    private void initSampleProducts() {
        if (productRepository.count() == 0) {
            // First Product
            Product laptop = new Product();
            laptop.setId(UUID.randomUUID().toString());
            laptop.setName("Laptop");
            laptop.setDescription("High-performance laptop");
            laptop.setPrice(new java.math.BigDecimal("999.99"));
            laptop.setStockQuantity(50);
            laptop.setCategory("Electronics");
            laptop.setCreatedAt(LocalDateTime.now());
            
            // Second Product
            Product smartphone = new Product();
            smartphone.setId(UUID.randomUUID().toString());
            smartphone.setName("Smartphone");
            smartphone.setDescription("Latest model smartphone");
            smartphone.setPrice(new java.math.BigDecimal("599.99"));
            smartphone.setStockQuantity(100);
            smartphone.setCategory("Electronics");
            smartphone.setCreatedAt(LocalDateTime.now());
            
            // Save products
            productRepository.save(laptop);
            productRepository.save(smartphone);
        }
    }
    
    // Convert Entity to DTO
    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    
    // Convert DTO to Entity
    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId() != null ? productDto.getId() : UUID.randomUUID().toString());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(productDto.getCategory());
        product.setCreatedAt(productDto.getCreatedAt() != null ? productDto.getCreatedAt() : LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    @Operation(summary = "Get all products", description = "Returns a list of all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductDto.class)))
    })    
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> productEntities = productRepository.findAll();
        List<ProductDto> productDtos = productEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @Operation(summary = "Get products by category", description = "Returns a list of products in a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductDto.class)))
    })    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @Parameter(description = "Category of products to retrieve") @PathVariable String category) {
        List<Product> productEntities = productRepository.findByCategory(category);
        List<ProductDto> productDtos = productEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @Operation(summary = "Get a product by ID", description = "Returns a product based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", 
                    content = @Content)
    })    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @Parameter(description = "ID of the product to retrieve") @PathVariable String id) {
        return productRepository.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new product", description = "Creates a new product and returns the created product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductDto.class)))
    })    
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Parameter(description = "Product to create", required = true) @RequestBody ProductDto productDto) {
        Product product = convertToEntity(productDto);
        product.setId(UUID.randomUUID().toString());
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedProduct));
    }

    @Operation(summary = "Update an existing product", description = "Updates a product based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", 
                    content = @Content)
    })    
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of the product to update") @PathVariable String id,
            @Parameter(description = "Updated product details", required = true) @RequestBody ProductDto productDto) {
        
        Optional<Product> existingProductOpt = productRepository.findById(id);
        
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            
            // Preserve creation timestamp and ID
            LocalDateTime createdAt = existingProduct.getCreatedAt();
            
            // Update product with new values
            Product updatedProduct = convertToEntity(productDto);
            updatedProduct.setId(id);
            updatedProduct.setCreatedAt(createdAt);
            updatedProduct.setUpdatedAt(LocalDateTime.now());
            
            Product savedProduct = productRepository.save(updatedProduct);
            return ResponseEntity.ok(convertToDto(savedProduct));
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a product", description = "Deletes a product based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found", 
                    content = @Content)
    })    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete") @PathVariable String id) {
        
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
