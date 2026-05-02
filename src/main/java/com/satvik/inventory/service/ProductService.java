package com.satvik.inventory.service;

import com.satvik.inventory.dto.Dtos.*;
import com.satvik.inventory.entity.Category;
import com.satvik.inventory.entity.Product;
import com.satvik.inventory.exception.BadRequestException;
import com.satvik.inventory.exception.ResourceNotFoundException;
import com.satvik.inventory.repository.CategoryRepository;
import com.satvik.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProductResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<ProductResponse> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts()
                .stream().map(this::toResponse).toList();
    }

    public ProductResponse create(ProductRequest request) {
        if (request.sku() != null && productRepository.existsBySku(request.sku())) {
            throw new BadRequestException("SKU '" + request.sku() + "' already exists");
        }
        Product product = buildProduct(new Product(), request);
        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findOrThrow(id);
        buildProduct(product, request);
        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateStock(Long id, StockUpdateRequest request) {
        Product product = findOrThrow(id);
        product.setStockQuantity(request.quantity());
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.delete(findOrThrow(id));
    }

    public BigDecimal getTotalInventoryValue() {
        BigDecimal val = productRepository.getTotalInventoryValue();
        return val == null ? BigDecimal.ZERO : val;
    }

    private Product buildProduct(Product product, ProductRequest req) {
        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setStockQuantity(req.stockQuantity());
        product.setSku(req.sku());
        if (req.lowStockThreshold() != null) product.setLowStockThreshold(req.lowStockThreshold());
        if (req.categoryId() != null) {
            Category category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", req.categoryId()));
            product.setCategory(category);
        }
        return product;
    }

    public Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    ProductResponse toResponse(Product p) {
        String categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
        return new ProductResponse(
                p.getId(), p.getName(), p.getDescription(), p.getPrice(),
                p.getStockQuantity(), p.getStockStatus(), p.getSku(),
                categoryName, p.isLowStock()
        );
    }
}
