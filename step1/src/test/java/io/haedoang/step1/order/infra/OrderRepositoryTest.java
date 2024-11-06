package io.haedoang.step1.order.infra;

import io.haedoang.step1.order.domain.Order;
import io.haedoang.step1.order.domain.OrderStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.JoinRowSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("주문 생성하기")
    void create() {
        // given
        String userId = "haedoang";
        String orderId = UUID.randomUUID().toString();
        Order given = Order.of(orderId, userId);

        // when
        Order actual = orderRepository.save(given);

        // then
        assertThat(actual.getKey()).isNotNull();
        assertThat(actual.getVersion()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문 조회하기")
    void search() {
        // given
        String userId = "haedoang";
        String orderId = UUID.randomUUID().toString();
        Order given = Order.of(orderId, userId);
        entityManager.persist(given);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<Order> actual = orderRepository.findById(given.getKey());

        // then
        assertThat(actual.isPresent()).isTrue();
    }

    @Test
    @DisplayName("낙관적 락잠금테스트")
    void useOptimisticLock() {
        // given
        String userId = "haedoang";
        String orderId = UUID.randomUUID().toString();
        Order given = Order.of(orderId, userId);
        entityManager.persist(given);

        entityManager.flush();
        entityManager.clear();

        // when
        Order order = orderRepository.findById(given.getKey()).get();
        order.update(OrderStatus.ORDER_CONFIRM);
        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(order.getVersion()).isGreaterThan(0);
    }
}

