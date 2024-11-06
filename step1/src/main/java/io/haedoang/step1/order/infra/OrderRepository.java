package io.haedoang.step1.order.infra;

import io.haedoang.step1.order.domain.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Lock(LockModeType.OPTIMISTIC)
    Optional<Order> findOrderByOrderId(String orderId);
}
