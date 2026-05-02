package com.satvik.inventory.controller;

import com.satvik.inventory.dto.Dtos.DashboardResponse;
import com.satvik.inventory.repository.CategoryRepository;
import com.satvik.inventory.repository.OrderRepository;
import com.satvik.inventory.repository.ProductRepository;
import com.satvik.inventory.service.OrderService;
import com.satvik.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Inventory analytics and summary")
public class DashboardController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get full inventory dashboard summary")
    public ResponseEntity<DashboardResponse> getDashboard() {
        DashboardResponse dashboard = new DashboardResponse(
                productRepository.count(),
                categoryRepository.count(),
                orderRepository.count(),
                productRepository.findLowStockProducts().size(),
                productRepository.findOutOfStockProducts().size(),
                productService.getTotalInventoryValue(),
                orderService.getTotalRevenue()
        );
        return ResponseEntity.ok(dashboard);
    }
}
