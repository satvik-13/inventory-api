package com.satvik.inventory;

import com.satvik.inventory.entity.Category;
import com.satvik.inventory.entity.Product;
import com.satvik.inventory.repository.CategoryRepository;
import com.satvik.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return; // don't seed twice

        // Categories
        Category apparel = new Category(null, "Apparel", "Clothing and fashion items", null);
        Category footwear = new Category(null, "Footwear", "Shoes, sandals, and boots", null);
        Category accessories = new Category(null, "Accessories", "Bags, belts, and jewelry", null);
        Category electronics = new Category(null, "Electronics", "Gadgets and devices", null);

        categoryRepository.save(apparel);
        categoryRepository.save(footwear);
        categoryRepository.save(accessories);
        categoryRepository.save(electronics);

        // Products
        productRepository.save(new Product(null, "Classic White Tee", "100% cotton unisex t-shirt", new BigDecimal("299.00"), 150, 20, "APP-001", apparel));
        productRepository.save(new Product(null, "Slim Fit Jeans", "Dark wash slim fit denim", new BigDecimal("1499.00"), 60, 15, "APP-002", apparel));
        productRepository.save(new Product(null, "Floral Kurti", "Cotton summer kurti", new BigDecimal("799.00"), 8, 10, "APP-003", apparel));   // low stock
        productRepository.save(new Product(null, "Hoodie - Black", "Fleece lined pullover hoodie", new BigDecimal("1299.00"), 0, 10, "APP-004", apparel)); // out of stock

        productRepository.save(new Product(null, "Running Shoes - Air", "Lightweight mesh running shoes", new BigDecimal("2499.00"), 45, 10, "FW-001", footwear));
        productRepository.save(new Product(null, "Leather Loafers", "Premium leather formal loafers", new BigDecimal("3299.00"), 5, 8, "FW-002", footwear));  // low stock
        productRepository.save(new Product(null, "Casual Sneakers", "Canvas lace-up sneakers", new BigDecimal("999.00"), 80, 15, "FW-003", footwear));

        productRepository.save(new Product(null, "Canvas Tote Bag", "Eco-friendly cotton tote", new BigDecimal("499.00"), 120, 20, "ACC-001", accessories));
        productRepository.save(new Product(null, "Leather Wallet", "Slim bifold genuine leather wallet", new BigDecimal("899.00"), 35, 10, "ACC-002", accessories));

        productRepository.save(new Product(null, "Wireless Earbuds", "BT 5.0 noise cancelling earbuds", new BigDecimal("4999.00"), 25, 5, "ELEC-001", electronics));
        productRepository.save(new Product(null, "Smart Watch", "Fitness tracker with heart rate monitor", new BigDecimal("8999.00"), 12, 5, "ELEC-002", electronics));

        System.out.println("\n✅ Sample data seeded — 4 categories, 11 products loaded.");
        System.out.println("📊 Visit http://localhost:8080/swagger-ui.html to explore the API.");
        System.out.println("🗄️  Visit http://localhost:8080/h2-console to browse the database.\n");
    }
}
