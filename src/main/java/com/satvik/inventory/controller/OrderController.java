package com.satvik.inventory.controller;

import com.satvik.inventory.dto.Dtos.*;
import com.satvik.inventory.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Manage customer orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)")
    public ResponseEntity<List<OrderResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }

    @GetMapping("/customer")
    @Operation(summary = "Get orders by customer email")
    public ResponseEntity<List<OrderResponse>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(orderService.getByCustomerEmail(email));
    }

    @GetMapping("/revenue")
    @Operation(summary = "Get total revenue from DELIVERED orders")
    public ResponseEntity<Map<String, BigDecimal>> getRevenue() {
        return ResponseEntity.ok(Map.of("totalRevenue", orderService.getTotalRevenue()));
    }

    @PostMapping
    @Operation(summary = "Place a new order (auto-deducts stock)")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                       @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
