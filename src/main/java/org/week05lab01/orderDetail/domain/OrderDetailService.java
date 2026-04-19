package org.week05lab01.orderDetail.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.week05lab01.inventory.domain.InventoryService;
import org.week05lab01.inventory.events.InventoryUpdateEvent;
import org.week05lab01.orderDetail.infrastructure.OrderDetailRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;

    private final OrderDetailRepository repository;

    private final InventoryService inventoryService;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public List<OrderDetail> list() {
        return repository.findAll();
    }

    public void save(OrderDetail orderDetail) {
        repository.save(orderDetail);
        publisher.publishEvent(new InventoryUpdateEvent(this, orderDetail.getProduct(), orderDetail.getQuantity()));
    }
}
