package io.haedoang.step1.order.infra;

import io.haedoang.step1.order.domain.Order;
import io.haedoang.step1.order.domain.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

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
        assertThat(order.getVersion()).isGreaterThan(0).as("Lock 어노테이션을 명시하지 않아도 동작하는 것을 확인");
    }

    @Test
    @DisplayName("낙관적 락 예외 발생 테스트")
    void useOptimisticLockThrowOptimisticLockException() {
        // given
        String userId = "haedoang";
        String orderId = UUID.randomUUID().toString();
        Order given = Order.of(orderId, userId);
        entityManager.persist(given);
        entityManager.flush();
        entityManager.detach(given);

        // when
        Order order = orderRepository.findById(given.getKey()).get();

        // 사용자가 주문을 취소했다
        order.update(OrderStatus.ORDER_CANCEL);
        entityManager.persist(order);
        entityManager.flush();


        // then
        assertThat(order.getVersion()).isGreaterThan(0);
        assertThatThrownBy(() -> {
            // 판매자가 주문을 승인했다
            given.update(OrderStatus.ORDER_CONFIRM);
            entityManager.merge(given);
            entityManager.persist(given);
        }).isInstanceOf(OptimisticLockException.class)
                .as("준영속 상태의 entity를 영속성 컨텍스트에 병합 시 version 충돌로 인해 optimisticLockingFailureException 발생을 검증");
    }
}

