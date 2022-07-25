package lt.bit.products.ui.service;

import lt.bit.products.ui.service.domain.OrderEntity;
import lt.bit.products.ui.service.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

  private final OrderRepository repository;

  OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  public void createOrder(OrderEntity order) {
    repository.save(order);
  }
}
