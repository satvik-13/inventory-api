package com.satvik.inventory.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

// ─── Category DTOs ────────────────────────────────────────

public class Dtos {

    public record CategoryRequest(
        @NotBlank(message = "Name is required") String name,
        String description
    ) {}

    public record CategoryResponse(Long id, String name, String description, int productCount) {}

    // ─── Product DTOs ────────────────────────────────────────

    public record ProductRequest(
        @NotBlank(message = "Name is required") String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotNull @Min(0) Integer stockQuantity,
        Integer lowStockThreshold,
        String sku,
        Long categoryId
    ) {}

    public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String stockStatus,
        String sku,
        String categoryName,
        boolean lowStock
    ) {}

    // ─── Order DTOs ──────────────────────────────────────────

    public record OrderRequest(
        @NotBlank String customerName,
        @Email String customerEmail,
        @NotNull @Size(min = 1) List<OrderItemRequest> items
    ) {}

    public record OrderItemRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity
    ) {}

    public record OrderResponse(
        Long id,
        String customerName,
        String customerEmail,
        String status,
        BigDecimal totalAmount,
        String createdAt,
        List<OrderItemResponse> items
    ) {}

    public record OrderItemResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
    ) {}

    // ─── Stock DTOs ──────────────────────────────────────────

    public record StockUpdateRequest(
        @NotNull @Min(0) Integer quantity,
        @NotBlank String reason   // e.g. "restock", "adjustment", "damage"
    ) {}

    // ─── Dashboard / Analytics ───────────────────────────────

    public record DashboardResponse(
        long totalProducts,
        long totalCategories,
        long totalOrders,
        int lowStockCount,
        int outOfStockCount,
        BigDecimal totalInventoryValue,
        BigDecimal totalRevenue
    ) {}
}
