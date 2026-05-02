package com.satvik.inventory.service;

import com.satvik.inventory.dto.Dtos.*;
import com.satvik.inventory.entity.Order;
import com.satvik.inventory.entity.OrderItem;
import com.satvik.inventory.entity.Product;
import com.satvik.inventory.exception.BadRequestException;
import com.satvik.inventory.exception.ResourceNotFoundException;
import com.satvik.inventory.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    public OrderResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<OrderResponse> getByStatus(String status) {
        try {
            Order.OrderStatus s = Order.OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByStatus(s).stream().map(this::toResponse).toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Valid: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
        }
    }

    public List<OrderResponse> getByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailIgnoreCase(email)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.customerEmail());

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productService.findOrThrow(itemReq.productId());

            // Stock validation
            if (product.getStockQuantity() < itemReq.quantity()) {
                throw new BadRequestException(
                    "Insufficient stock for '" + product.getName() +
                    "'. Available: " + product.getStockQuantity() +
                    ", Requested: " + itemReq.quantity()
                );
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(product.getPrice());

            items.add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
        }

        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus(Order.OrderStatus.PENDING);

        return toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, String status) {
        Order order = findOrThrow(id);
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());

            // If cancelling, restore stock
            if (newStatus == Order.OrderStatus.CANCELLED &&
                order.getStatus() != Order.OrderStatus.CANCELLED) {
                for (OrderItem item : order.getItems()) {
                    Product product = item.getProduct();
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                }
            }

            order.setStatus(newStatus);
            return toResponse(orderRepository.save(order));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status);
        }
    }

    public BigDecimal getTotalRevenue() {
        BigDecimal rev = orderRepository.getTotalRevenue();
        return rev == null ? BigDecimal.ZERO : rev;
    }

    private Order findOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> itemResponses = o.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                )).toList();

        return new OrderResponse(
                o.getId(),
                o.getCustomerName(),
                o.getCustomerEmail(),
                o.getStatus().name(),
                o.getTotalAmount(),
                o.getCreatedAt() != null ? o.getCreatedAt().toString() : null,
                itemResponses
        );
    }
}
