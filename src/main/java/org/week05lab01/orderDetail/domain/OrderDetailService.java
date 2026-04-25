package org.week05lab01.orderDetail.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.week05lab01.inventory.events.InventoryUpdateEvent;
import org.week05lab01.order.domain.Order;
import org.week05lab01.order.infrastructure.OrderRespository;
import org.week05lab01.orderDetail.infrastructure.OrderDetailRepository;
import org.week05lab01.product.domain.Product;
import org.week05lab01.product.infrastructure.ProductRepository;
import org.week05lab01.shared.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;

    private final OrderDetailRepository repository;
    private final OrderRespository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public List<OrderDetail> list() {
        return repository.findAll();
    }

    public OrderDetail save(Long orderId, Long productId, Integer quantity, Double unitPrice) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la orden con id: " + orderId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el producto con id: " + productId));

        OrderDetail orderDetail = new OrderDetail(null, order, product, quantity, unitPrice);
        OrderDetail saved = repository.save(orderDetail);

        publisher.publishEvent(new InventoryUpdateEvent(this, product, quantity));

        return saved;
    }
}
