package io.haedoang.step1.order.infra;

import io.haedoang.step1.order.domain.Order;
import io.haedoang.step1.order.domain.OrderStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderLockTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("낙관적 락잠금 동시성 테스트")
    void useOptimisticLockConcurrencyTest() throws BrokenBarrierException, InterruptedException {
        // given
        String userId = "haedoang";
        String orderId = UUID.randomUUID().toString();
        Order given = Order.of(orderId, userId);
        orderRepository.save(given);

        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicReference<Exception> exceptionHolder = new AtomicReference<>();


        // when
        // 사용자는 취소할거야
        executor.submit(() -> {
            try {
                barrier.await();
                userOrderCancel(orderId);
            } catch (Exception e) {
                exceptionHolder.set(e);
            }
        });

        // 판매자는 주문 접수할거야
        executor.submit(() -> {
            try {
                barrier.await();
                sellerOrderConfirm(orderId);
            } catch (Exception e) {
                exceptionHolder.set(e);
            }
        });

        barrier.await();

        executor.shutdown();

        while (!executor.isTerminated()) {
            Thread.sleep(5000);
        }


        // then
        assertThat(orderRepository.findById(given.getKey()).get().getVersion()).isEqualTo(1);
        assertThat(exceptionHolder.get()).isInstanceOf(OptimisticLockingFailureException.class);
    }


    public void userOrderCancel(String orderId)  {
        Order order = orderRepository.findOrderByOrderId(orderId).get();
        order.update(OrderStatus.ORDER_CANCEL);
        orderRepository.save(order);
    }

    public void sellerOrderConfirm(String orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId).get();
        order.update(OrderStatus.ORDER_CONFIRM);
        orderRepository.save(order);
    }

}
